package com.test.service

import com.constants.SubscriptionStatus
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
        switch (eventType){
            case EventType.SUBSCRIPTION_ORDER:
                def createSubscriptionResult = processCreateSubscriptionEvent(event.data)
                return createSubscriptionResult
                break
            default:
                return [error:ErrorCode.INVALID_RESPONSE]
        }
    }

    //Attempts to create a new user with given subscription data
    //in case of success returns [accountHolder.fullName, accountHolder.identifier]
    //in case of failure returns [error: com.constants.ErrorCode]
    def private processCreateSubscriptionEvent(eventData){
        //all new users will be assigned a username=creator.email, role=USER_ROLE,
        //and default password='P@ssw0rd'
        String userName=EventDataParserUtil.getCreator(eventData).email
        log.info("EventService|processCreateSubscriptionEvent > Processing CREATE_SUBSCRIPTION event for user:"+userName)
        if(userName){
            try{
                String password=DEFAULT_USER_PASSWORD
                String openId=EventDataParserUtil.getCreator(eventData).openId?:""
                log.info("EventService|processCreateSubscriptionEvent > User's openID:"+openId)
                //check if user exist
                boolean exist = TestUser.createCriteria().get{eq('username',userName)}

                if(!exist){
                    def transactionResult=TestUser.withTransaction {
                        def newUser = TestUser.newInstance(username:userName,password:password,enabled:true)

                        //add subscription data to user
                        def order = EventDataParserUtil.getOrder(eventData)
                        log.info("EventService|processCreateSubscriptionEvent > User's order data:"+order.toString())
                        if(order?.editionCode&&order?.pricingDuration)
                            newUser.addToSubscriptions(name:order?.editionCode,
                                    pricingDuration:order.pricingDuration,
                                    status: SubscriptionStatus.ACTIVE)
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
                        //if all good return accountHolder data for successful response
                        String accountHolderFullName= EventDataParserUtil.getCreator(eventData).firstName+" "+EventDataParserUtil.getCreator(eventData).lastName
                        return [accountHolder:[fullName:accountHolderFullName, identifier:userName]]
                    }
                    return transactionResult
                }
                log.error("EventService|processCreateSubscriptionEvent > "+userName+" already exist in a system!")
                return [error: ErrorCode.USER_ALREADY_EXISTS]
            }
            catch (Exception e){
                log.error("EventService|processCreateSubscriptionEvent > Failed to create user. Cause:"+e.toString())
                return [error: ErrorCode.UNKNOWN_ERROR]
            }
        }
        return [error: ErrorCode.INVALID_RESPONSE]
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
