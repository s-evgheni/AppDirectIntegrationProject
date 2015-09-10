package test

import grails.plugin.springsecurity.annotation.Secured

//Endpoints will be secured via Oauth
//key: appdirectintegration-39910
//secret: JabfzqE8Zt5U
@Secured(['permitAll'])
class UserController {
    static allowedMethods = [assign: ['POST'], unassign: ['POST']]

    //USER_ASSIGNMENT: fired by AppDirect when a user assigns a user to an app.
    def assign() {
        /*FLOW:
        1. Admin assigns a seat to user. This creates an event resource identified by URL https://www.acme-marketplace.com/api/integration/v1/events/12345.
        2. AppDirect calls http://example.com/assign?url=https://www.acme-marketplace.com/api/integration/v1/events/12345
        3. test app issues a signed fetch to: https://www.acme-marketplace.com/api/integration/v1/events/12345
        AppDirect returns a user assignment event:*/
        def signatureIsValid = validateOauthSignature()

        render "User assigned endpoint"

    }
    //USER_UNASSIGNMENT: fired by AppDirect when a user unassigns a user from an app.
    def unassign() {
        render "User unassigned endpoint"
    }



    /*Authorization: OAuth realm="", oauth_nonce="72250409", oauth_timestamp="1294966759",oauth_consumer_key="Dummy",
                     oauth_signature_method="HMAC-SHA1", oauth_version="1.0",oauth_signature="IBlWhOm3PuDwaSdxE/Qu4RKPtVE="*/
    def private validateOauthSignature(){
        boolean result = false

    }
}
