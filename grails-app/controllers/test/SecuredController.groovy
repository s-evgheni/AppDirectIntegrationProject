package test

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

class SecuredController {

    static layout = "main"

    @Secured(['ROLE_ADMIN'])
    def admins() {
        render view: 'adminMain'
    }

    @Secured(['ROLE_USER'])
    def users() {
        render view: 'userMain'
    }

    @Secured(['permitAll'])
    def logout(){
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }
}
