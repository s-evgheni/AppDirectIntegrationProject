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

    //processes an event and return map back with results
    def processEvent(String eventUrl) {
        def event = getEventData(eventUrl)

        if(event?.errorCode){
            return [outcome:'fail', error:ErrorCode.UNKNOWN_ERROR]
        }
        else{
            switch (event?.data?.type?.text()){
                case EventType.SUBSCRIPTION_ORDER:
                    processCreateSubscriptionEvent(event.data)
                    return [outcome:'success', error: null]
                    break;
                default:
                    return [outcome:'fail', error:ErrorCode.UNKNOWN_ERROR]
            }

        }
    }

    //Attempts to create a new user with given subscription data
    def private processCreateSubscriptionEvent(eventData){
        //all new users will be assigned a username=email, role=USER_ROLE,
        //and password='password'
        String username=EventDataParserUtil.getCreator(eventData).email
        String password="password"
        if(username){
            try{
                String openId=EventDataParserUtil.getCreator(eventData).openId?:""
                //check if user exist
                boolean exist = TestUser.createCriteria().get{eq('username',username)}

                if(!exist){
                    boolean created=TestUser.withTransaction {
                        def newUser = TestUser.newInstance(username:username,password:password,enabled:true)
                        if(openId)
                            newUser.addToOpenIds(url: openId)

                        def order = EventDataParserUtil.getOrder(eventData)
                        //add subscription
                        if(order?.editionCode&&order?.pricingDuration)
                            newUser.addToSubscriptions(name:order?.editionCode,
                                                       pricingDuration:order.pricingDuration,
                                                       status: SubscriptionStatus.ACTIVE)
                        else return false

                        if(!newUser.save()) return false
                        //add user role
                        def defaultRole = TestRole.findWhere('authority': 'ROLE_USER')
                        UserRole.create newUser, defaultRole
                    }
                    return created
                }
                return false

            }
            catch (Exception e){
                log.error("EventService|processCreateSubscriptionEvent > Failed to create user. Cause:"+e.toString())
                return false
            }
        }
        return false




    }

    private def getEventData(String eventUrl){
        HttpClient httpClient = new DefaultHttpClient()
        HttpGet request = new HttpGet(eventUrl)

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.sign(request)

        HttpResponse response = httpClient.execute(request)


        log.info("\nSending 'GET' request to URL : " + eventUrl)
        log.info("Response Code : " + response.getStatusLine().getStatusCode())

        if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
            return [data:null, errorCode: response.getStatusLine().getStatusCode()]
        }
        else{
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()))

            StringBuffer result = new StringBuffer()
            String line = ""
            while ((line = rd.readLine()) != null) {
                result.append(line)
            }

            String xml = extractEventNode(result.toString())
            log.info('Event XML: '+xml)
            def eventNode = new XmlParser().parseText(xml)
            return [data:eventNode, errorCode: null]
        }
    }

    private String extractEventNode(String xml){
        def result =xml.substring(++xml.indexOf('>'))
        result=result.substring(++result.indexOf('>'))
        result='<event>'+result
        return result
    }
}
