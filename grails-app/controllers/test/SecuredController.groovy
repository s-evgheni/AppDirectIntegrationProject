package test

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

class SecuredController {

    static layout = "main"

    def userService
    def springSecurityService

    @Secured(['ROLE_ADMIN'])
    def admins() {
        render view: 'adminMain'
    }

    @Secured(['ROLE_USER'])
    def users() {
        def userData=[:]
        try{
            userData = userService.getData(springSecurityService.currentUser.username)
        }catch (Exception e){
            render view: 'userMain', model: userData
        }
        render view: 'userMain', model: [userName:userData.userName]
    }

    @Secured(['permitAll'])
    def logout(){
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }
}
