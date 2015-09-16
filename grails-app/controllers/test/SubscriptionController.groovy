package test

import com.constants.ErrorCode
import com.util.ResponseUtil
import grails.plugin.springsecurity.annotation.Secured



//Endpoints are secured via Security Filter which performs basic validation of the Oauth authorization header
@Secured(['permitAll'])
class SubscriptionController {
    static allowedMethods = [create: ['GET'], change: ['GET'],cancel: ['GET'],status: ['GET']]

    def eventService

    //SUBSCRIPTION_ORDER: fired by AppDirect when a user buys an app from AppDirect.
    //SAMPLE URL for integration: http://59b50285.ngrok.io/test/subscription/create?eventUrl={eventUrl}
    def create(){
        def eventUrl=params?.eventUrl?:""
        if(eventUrl){
            def result = eventService.processEvent(eventUrl)
            if(result?.error){//in case of error
                render text: ResponseUtil.generateErrorResponse(result.error)
                return
            }
            else if(result?.accountId&&result?.message){//in case of success
                render text: ResponseUtil.generateSuccessResponse(result.accountId, result.message)
                return
            }
        }
        //in any other case
        render text: ResponseUtil.generateErrorResponse(ErrorCode.UNKNOWN_ERROR)
    }
    //SUBSCRIPTION_CHANGE: fired by AppDirect when a user upgrades/downgrades/modifies an existing subscription.
    def change(){
        def eventUrl=params?.eventUrl?:""
        if(eventUrl){
            def result = eventService.processEvent(eventUrl)
            if(result?.error){//in case of error
                render text: ResponseUtil.generateErrorResponse(result.error)
                return
            }
            else if(result?.accountId&&result?.message){//in case of success
                render text: ResponseUtil.generateSuccessResponse(result.accountId, result.message)
                return
            }
        }
        render text: ResponseUtil.generateErrorResponse(ErrorCode.UNKNOWN_ERROR)
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
