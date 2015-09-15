package test

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

class SecuredController {

    static layout = "main"

    def userService
    def springSecurityService

    @Secured(['ROLE_ADMIN'])
    def admins() {
        render view: 'userMain', model: [userData:getViewModelData(springSecurityService?.currentUser?.username)]
    }

    @Secured(['ROLE_USER'])
    def users() {
        render view: 'userMain', model: [userData:getViewModelData(springSecurityService?.currentUser?.username)]
    }

    @Secured(['permitAll'])
    def logout(){
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }

    private def getViewModelData(String userName){
        def userData=[:]
        if(userName){
            try{
                userData = userService.lookupUserData(userName)
            } catch (Exception e){ return userData }
        }
        return userData
    }
}
