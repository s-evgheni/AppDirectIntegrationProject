package test

import grails.plugin.springsecurity.annotation.Secured
import groovy.xml.XmlUtil
import oauth.signpost.OAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.HttpClient


//Endpoints will be secured via Oauth
@Secured(['permitAll'])
class SubscriptionController {
    static allowedMethods = [create: ['GET'], change: ['GET'],cancel: ['GET'],status: ['GET']]

    private static String CONSUMER_KEY='appdirectintegration-39910'
    private static String CONSUMER_SECRET='JabfzqE8Zt5U'

    //SUBSCRIPTION_ORDER: fired by AppDirect when a user buys an app from AppDirect.
    def create(){
        def outcome=session.authHeaderData
        def eventUrl=params?.eventUrl?:""
        def node = signAnOutgoingRequest(eventUrl)
        def xmlData = XmlUtil.serialize(node)
        def successResponse= XmlUtil.serialize("<result><success>true</success><message>Account creation successful for Fake Co. by Alice</message><accountIdentifier>fakeco123</accountIdentifier></result>")

        render text: successResponse
    }
    //SUBSCRIPTION_CHANGE: fired by AppDirect when a user upgrades/downgrades/modifies an existing subscription.
    def change(){
        render 'change subscription endpoint'
    }

    //SUBSCRIPTION_CANCEL: fired by AppDirect when a user cancels a subscription.
    def cancel(){
        render 'cancel subscription endpoint'
    }
    //SUBSCRIPTION_STATUS: fired by AppDirect when a user request subscription status
    def status(){
        render 'status subscription endpoint'
    }

    private def signAnOutgoingRequest(String eventUrl){
        HttpClient httpClient = new DefaultHttpClient()
        HttpGet request = new HttpGet(eventUrl)

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.sign(request)


        HttpResponse response = httpClient.execute(request)


        System.out.println("\nSending 'GET' request to URL : " + eventUrl)
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode())

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()))

        StringBuffer result = new StringBuffer()
        String line = ""
        while ((line = rd.readLine()) != null) {
            result.append(line)
        }

        String r = getEventData(result.toString())
        System.out.println(r)

        def event = new XmlParser().parseText(r)

        return event
    }

    private String getEventData(String xml){
        def result =xml.substring(++xml.indexOf('>'))
        result=result.substring(++result.indexOf('>'))
        result='<event>'+result
        return result
    }
}
