package test

import grails.plugin.springsecurity.annotation.Secured


//Endpoints will be secured via Oauth
@Secured(['permitAll'])
class SubscriptionController {
    static allowedMethods = [create: ['POST'], change: ['POST'],cancel: ['POST'],status: ['GET']]

    //SUBSCRIPTION_ORDER: fired by AppDirect when a user buys an app from AppDirect.
    def create(){
        def outcome="create subscription endpoint.\n" +
                       "Header data:\n"
        outcome+=session.authHeaderData
        render outcome
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
