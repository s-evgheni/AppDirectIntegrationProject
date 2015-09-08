package test

class RequestLogFilters {

    def PSEUDO_SESSION_ID = 'pseudo_session_id'

    def filters = {
        requestLogFilter(uri: '/**') {
            def startTime
            before = {
                startTime = System.currentTimeMillis()
                log.info """
################### Begin Request ${request.requestURL} #############################"""

                log.info """
##################################################################
Requested URL: ${request.requestURL}
Method:        ${request.method}
Remote Addr:   ${request.remoteAddr}
Valid Session: ${request.isRequestedSessionIdValid()}
Secure:        ${request.isSecure()}
Session:       ${getPseudoSessionId(session)}
Headers:       ${getHeadersString(request)}
PSEUDOSESSIONID ${getPseudoSessionId(session)} JSESSIONID ${getJSessionID(request)}"""

                log.debug """
Cookies:       ${getCookieString(request)}"""

                log.info """
##################################################################"""
            }

            after = {
                def during = System.currentTimeMillis() - startTime

                log.info """
PSEUDOSESSIONID ${getPseudoSessionId(session)} JSESSIONID ${getJSessionID(request)}
################### End Request ${request.requestURL} in ${during} ms #######################"""
            }
        }
    }

    private def getPseudoSessionId(session) {
        if (!session) return '-1'

        if (!session.getAttribute(PSEUDO_SESSION_ID)) {
            session.setAttribute(PSEUDO_SESSION_ID, UUID.randomUUID().toString())
        }
        return session.getAttribute(PSEUDO_SESSION_ID)
    }

    private def getHeadersString(request) {
        def headers = '\n'
        request.headerNames.each { name ->
            if (name == 'cookie') return
            headers += "Header [Name: ${name.toString()}  Value: ${request.getHeader(name.toString())}]\n"
        }
        headers
    }

    private def getCookieString(request) {
        def cst = '\n'
        request.cookies.each { cookie ->
            def cv = cookie.name == 'JSESSIONID' ? 'XXXXXXXXXXXXXXXXX' : cookie.value
            cst += "Cookie [${cookie.name}  \tValue: ${cv}  \tPath: ${cookie.path}  \tDomain: ${cookie.domain}  \tVersion: ${cookie.version}  \tSecure: ${cookie.secure}]\n"
        }
        cst
    }

    private def getJSessionID(request) {
        def sessionCookie = request.cookies.find { it -> it.name == 'JSESSIONID' }
        if (sessionCookie)
            return sessionCookie.value
    }
}