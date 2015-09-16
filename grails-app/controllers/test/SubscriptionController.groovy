package test

import com.constants.ErrorCode
import com.util.ResponseUtil
import grails.plugin.springsecurity.annotation.Secured



//Endpoints will be secured via Oauth
@Secured(['permitAll'])
class SubscriptionController {
    static allowedMethods = [create: ['GET'], change: ['GET'],cancel: ['GET'],status: ['GET']]

    def eventService

    //SUBSCRIPTION_ORDER: fired by AppDirect when a user buys an app from AppDirect.
    //SAMPLE URL for integration: http://59b50285.ngrok.io/test/subscription/create?eventUrl={eventUrl}
    def create(){
        def eventUrl=params?.eventUrl?:""
        def result = eventService.processEvent(eventUrl)
        if(result?.error)//in case of error
            render text: ResponseUtil.generateErrorResponse(result.error)
        else if(result?.accountHolder)//in case of success
            render text: ResponseUtil.generateSuccessResponse(result.accountHolder)
        else //just in case, if all else fails :)
            render text: ResponseUtil.generateErrorResponse(ErrorCode.UNKNOWN_ERROR)
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
}
