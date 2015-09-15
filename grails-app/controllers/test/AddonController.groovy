package test

import grails.plugin.springsecurity.annotation.Secured

//Endpoints in this controller will be secured via Oauth
//key: appdirectintegration-39910
//secret: JabfzqE8Zt5U
@Secured(['permitAll'])
class AddonController {
    //Limits access to controller actions based on the HTTP request method, sending a 405 (Method Not Allowed) error code when an incorrect HTTP method is used.
    static allowedMethods = [notification: ['GET']]

    def notification() {
        render 'notification addon endpoint'
    }
}
