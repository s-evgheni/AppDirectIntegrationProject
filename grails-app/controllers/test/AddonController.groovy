package test

import grails.plugin.springsecurity.annotation.Secured

//Endpoints in this controller will be secured via Oauth
//key: appdirectintegration-39910
//secret: JabfzqE8Zt5U
@Secured(['permitAll'])
class AddonController {

    def notification() {
        render 'notification addon endpoint'
    }
}
