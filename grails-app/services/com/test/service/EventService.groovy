package com.test.service

import com.constants.SubscriptionStatus
import com.test.auth.Subscription
import com.test.auth.TestRole
import com.test.auth.TestUser
import com.test.auth.UserRole
import com.constants.ErrorCode
import com.util.EventDataParserUtil
import com.constants.EventType
import grails.transaction.Transactional
import oauth.signpost.OAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.HttpClient

@Transactional
class EventService {

    private static String CONSUMER_KEY='appdirectintegration-39910'
    private static String CONSUMER_SECRET='JabfzqE8Zt5U'
    private static String DEFAULT_USER_PASSWORD='P@ssw0rd'

    //attempts to processes an event and return back a Map with positive or negative outcome
    def processEvent(String eventUrl) {
        def event = getEventData(eventUrl)

        if(event?.errorCode)
            return [error: event.errorCode]

        String eventType = EventDataParserUtil.getEventType(event.data)
        def eventResult
        switch (eventType){
            case EventType.SUBSCRIPTION_ORDER:
                eventResult = processCreateSubscriptionEvent(event.data)
                break
            case EventType.SUBSCRIPTION_CHANGE:
                eventResult = processChangeSubscriptionEvent(event.data)
                break
            case EventType.SUBSCRIPTION_CANCEL:
                eventResult = processCancelSubscriptionEvent(event.data)
                break
            default:
                eventResult=[error:ErrorCode.INVALID_RESPONSE]
                break
        }
        return eventResult
    }

    //Behaviour:
    //1. If user do not exist: Creates a new user with given subscription data
    //2. If user exist: Replace user's old subscription with given subscription data. Set subscription state to ACTIVE
    //in case of success returns [message, accountId]
    //in case of failure returns [error: com.constants.ErrorCode]
    def private processCreateSubscriptionEvent(eventData){
        //all new users will be assigned a username=creator.email, role=USER_ROLE,
        //and default password='P@ssw0rd'
        String accountIdentifier=EventDataParserUtil.getCreator(eventData).email
        log.info("EventService|processCreateSubscriptionEvent > Processing CREATE_SUBSCRIPTION event for user:"+accountIdentifier)
        if(accountIdentifier){
            try{
                String password=DEFAULT_USER_PASSWORD
                String openId=EventDataParserUtil.getCreator(eventData).openId?:""
                String marketplace= EventDataParserUtil.getMarketPlace(eventData).partner?:"Unknown"
                def order = EventDataParserUtil.getOrder(eventData)
                log.info("EventService|processCreateSubscriptionEvent > User's openID:"+openId)

                def existingUser = TestUser.findByUsername(accountIdentifier)//check if user exist in the database
                if(!existingUser){//if not exist we need to create a new one and add a subscription to his/her account
                    def transactionResult=TestUser.withTransaction {
                        def newUser = TestUser.newInstance(username:accountIdentifier,password:password,enabled:true)

                        //add subscription data to user
                        log.info("EventService|processCreateSubscriptionEvent > User's order data:"+order.toString())
                        if(order?.editionCode&&order?.pricingDuration)
                            newUser.addToSubscriptions(name:order?.editionCode,
                                    pricingDuration:order.pricingDuration,
                                    status: SubscriptionStatus.ACTIVE,
                                    createdTms: new Date(),
                                    marketplace: marketplace)
                        else{
                            log.error("EventService|processCreateSubscriptionEvent > User's order data can not be found!")
                            return [error: ErrorCode.INVALID_RESPONSE]
                        }
                        //add open id login option to this user account
                        if(openId)
                            newUser.addToOpenIds(url: openId)

                        if(!newUser.save()){
                            log.error("EventService|processCreateSubscriptionEvent > Failed to persist user's data!")
                            return [error: ErrorCode.UNKNOWN_ERROR]
                        }
                        //add role to user to enable authentication flow
                        def defaultRole = TestRole.findWhere('authority': 'ROLE_USER')
                        UserRole.create newUser, defaultRole
                        //if all good return message and accountID data to build successful response
                        String accountHolderFullName= EventDataParserUtil.getCreator(eventData).firstName+" "+EventDataParserUtil.getCreator(eventData).lastName
                        return [message:"New subscription has been created for:"+accountHolderFullName, accountId:accountIdentifier]
                    }
                    log.info("EventService|processChangeSubscriptionEvent > CREATE_SUBSCRIPTION event for user:"+accountIdentifier+"has been successfully processed!")
                    return transactionResult
                }
                else{//for existing users check that a user have no active subscription
                    Subscription currentSubscription = existingUser.subscriptions[0]//a user can have only one active subscription at any given time
                    if(currentSubscription.status.equals(SubscriptionStatus.ACTIVE)){
                        log.error("EventService|processCreateSubscriptionEvent > Account with id: "+accountIdentifier+" can only have one active subscription at any given time")
                        return [error: ErrorCode.OPERATION_CANCELED]
                    }
                    log.info("EventService|processChangeSubscriptionEvent > Current subscription data: "+currentSubscription.toString()+" New subscription data:"+order.toString())
                    if(order?.editionCode&&order?.pricingDuration){
                        currentSubscription.name=order.editionCode
                        currentSubscription.pricingDuration=order.pricingDuration
                        currentSubscription.status=SubscriptionStatus.ACTIVE
                        currentSubscription.createdTms=new Date()
                        currentSubscription.marketplace=marketplace
                    }
                    //save changes to the database
                    if(!existingUser.save()){
                        log.error("EventService|processChangeSubscriptionEvent > Failed to create subscription for user ID: "+accountIdentifier)
                        return [error: ErrorCode.UNKNOWN_ERROR]
                    }
                    //if all good return message and accountID data to build successful response
                    log.info("EventService|processChangeSubscriptionEvent > CREATE_SUBSCRIPTION event for user:"+accountIdentifier+"has been successfully processed!")
                    return [message:"New subscription has been added to:"+accountIdentifier, accountId:accountIdentifier]
                }
            }
            catch (Exception e){
                log.error("EventService|processCreateSubscriptionEvent > Failed to create user. Cause:"+e.toString())
                return [error: ErrorCode.UNKNOWN_ERROR]
            }
        }
        return [error: ErrorCode.INVALID_RESPONSE]
    }

    //Behaviour:
    //Searches for the user based on accountIdentifier and modifies his current subscription to a new one provided in the CHANGE_SUBSCRIPTION event XML
    //in case of success returns [message, accountId]
    //in case of failure returns [error: com.constants.ErrorCode]
    private def processChangeSubscriptionEvent(eventData){
        String accountIdentifier=EventDataParserUtil.getAccount(eventData).accountIdentifier
        boolean accountIsActive=EventDataParserUtil.getAccount(eventData).status.equals("ACTIVE")
        if(!(accountIsActive||accountIdentifier)){
            log.error("EventService|processChangeSubscriptionEvent > User account is not active or accountIdentifier is not present in the event XML")
            return [error: ErrorCode.USER_NOT_FOUND]
        }
        def subscription = EventDataParserUtil.getOrder(eventData)
        log.info("EventService|processChangeSubscriptionEvent > Processing CHANGE_SUBSCRIPTION event for user:"+accountIdentifier)
        if(subscription){
            try{
                def user = TestUser.findByUsername(accountIdentifier)//check if user exist in the database
                if(user){
                    Subscription currentSubscription = user.subscriptions[0]
                    def order = EventDataParserUtil.getOrder(eventData)
                    String marketplace= EventDataParserUtil.getMarketPlace(eventData).partner?:"Unknown"
                    log.info("EventService|processChangeSubscriptionEvent > Current subscription data: "+currentSubscription.toString()+" New subscription data:"+order.toString())
                    if(order?.editionCode&&order?.pricingDuration){
                        currentSubscription.name=order.editionCode
                        currentSubscription.pricingDuration=order.pricingDuration
                        currentSubscription.status=SubscriptionStatus.ACTIVE
                        currentSubscription.createdTms=new Date()
                        currentSubscription.marketplace=marketplace
                    }
                    //save changes to the database
                    if(!user.save()){
                        log.error("EventService|processChangeSubscriptionEvent > Failed to update subscription's data!")
                        return [error: ErrorCode.UNKNOWN_ERROR]
                    }
                    //if all good return accountHolder data for successful response
                    log.info("EventService|processChangeSubscriptionEvent > CHANGE_SUBSCRIPTION event for user:"+accountIdentifier+"has been successfully processed!")
                    return [message:"Subscription has been changed for user ID: "+accountIdentifier, accountId:accountIdentifier]
                }
                log.error("EventService|processChangeSubscriptionEvent > User with given identifier do not match any records in the database")
                return [error: ErrorCode.USER_NOT_FOUND]
            }
            catch (Exception e){
                log.error("EventService|processChangeSubscriptionEvent > Failed to update subscription for current user. Cause:"+e.toString())
                return [error: ErrorCode.UNKNOWN_ERROR]
            }
        }
        log.error("EventService|processChangeSubscriptionEvent > Can not extract subscription data from the even XML")
        return [error: ErrorCode.INVALID_RESPONSE]
    }

    //Behaviour:
    //Searches for the user based on accountIdentifier and changes the status of his current subscription to CANCELLED
    //in case of success returns [message, accountId]
    //in case of failure returns [error: com.constants.ErrorCode]
    private def processCancelSubscriptionEvent(eventData){
        String accountIdentifier=EventDataParserUtil.getAccount(eventData).accountIdentifier
        boolean accountIsActive=EventDataParserUtil.getAccount(eventData).status.equals("ACTIVE")
        if(!(accountIsActive||accountIdentifier)){
            log.error("EventService|processCancelSubscriptionEvent > User account is not in canceled state or accountIdentifier is not present in the event XML")
            return [error: ErrorCode.USER_NOT_FOUND]
        }

        log.info("EventService|processCancelSubscriptionEvent > Processing CANCEL_SUBSCRIPTION event for user: "+accountIdentifier)
        try{
            def user = TestUser.findByUsername(accountIdentifier)//check if user exist in the database
            if(user){
                Subscription currentSubscription = user.subscriptions[0]
                log.info("EventService|processCancelSubscriptionEvent > Current subscription data: "+currentSubscription.toString())
                if(currentSubscription){
                    currentSubscription.status=SubscriptionStatus.CANCELLED
                }
                //save changes to the database
                if(!user.save()){
                    log.error("EventService|processCancelSubscriptionEvent > Failed to update subscription's data!")
                    return [error: ErrorCode.UNKNOWN_ERROR]
                }
                //if all good return accountHolder data for successful response
                log.info("EventService|processCancelSubscriptionEvent > CANCEL_SUBSCRIPTION event for user:"+accountIdentifier+"has been successfully processed!")
                return [message:"Subscription has been cancelled for user ID: "+accountIdentifier, accountId:accountIdentifier]
            }
            log.error("EventService|processCancelSubscriptionEvent > User with given identifier do not match any records in the database")
            return [error: ErrorCode.USER_NOT_FOUND]
        }
        catch (Exception e){
            log.error("EventService|processCancelSubscriptionEvent > Failed to update subscription for current user. Cause:"+e.toString())
            return [error: ErrorCode.UNKNOWN_ERROR]
        }
    }

    //Attempts to create a get even XML data from a given AppDirect URL
    //in case of success returns [data: eventXML] as an instance of groovy.util.Node
    //in case of failure returns [error: com.constants.ErrorCode]
    private def getEventData(String eventUrl){
        try{
            HttpClient httpClient = new DefaultHttpClient()
            HttpGet request = new HttpGet(eventUrl)

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            consumer.sign(request)

            log.info("\nEventService|getEventData > Sending 'GET' request to URL : " + eventUrl)
            HttpResponse response = httpClient.execute(request)
            log.info("EventService|getEventData > Response Code : " + response.getStatusLine().getStatusCode())

            if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
                return [error: ErrorCode.UNKNOWN_ERROR]

            BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent()))

            StringBuffer result = new StringBuffer()
            String line = ""
            while ((line = rd.readLine()) != null) {
                result.append(line)
            }

            log.info("\nEventService|getEventData > Event XML: " + result.toString())
            def dataNode=new XmlParser().parseText(result.toString())
            return [data:dataNode]

        }catch (Exception e){
            log.error("EventService|getEventData > Failed to retrieve event data from:"+eventUrl+" Cause:"+e.toString())
            return [error: ErrorCode.UNKNOWN_ERROR]
        }
    }
}
