package test

class SecurityFilters {

    def dependsOn = [RequestLogFilters]
    static def VALID_KEY= 'appdirectintegration-39910'

    def filters = {
        basicAuth(controller:'addon|subscription|user', action:'*') {
            before = {
                session.authHeaderData=[:]//reset auth header data on each request
                def authString = request.getHeader('Authorization')

                if(!authString){
                    log.error("API|Security Filter|basicAuth - Error: Data missing in the authorization header.")
                    return false
                }
                def result=formatAuthStringIntoMap(authString)
                if(!result){
                    log.error("API|Security Filter|basicAuth - Error: Data missing in the authorization header.")
                    return false
                }

                boolean valid=validate(result)
                if(!valid){
                    log.error("API|Security Filter|basicAuth - Error: Data missing in the authorization header.")
                    return false
                }
                //if all valid save data in session
                session.authHeaderData=result

                //OAUTH implementation BEGIN
                //Authorization header sample:
               //OAuth oauth_consumer_key="appdirectintegration-39910", oauth_nonce="-8398153019108983984", oauth_signature="xNUht1XANoFHzQapA2A4KsKcsE8%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1441827672", oauth_version="1.0"
/*                def authType, authTokens = ""

                try{
                    (authType, authTokens) = new String(authString).split(' ')
                }catch(Exception e){
                    log.error("API|Security Filter|basicAuth - Error: authorization token parsing error: " + e)
                    return false
                }
                //check token type
                if(authType!='OAuth'){
                    log.error("API|Security Filter|basicAuth - Error: Invalid token type, must be set to 'OAuth'. Provided value: " + authType?.toUpperCase())
                    return false
                }*/
            }
        }
    }

    //Transform Authorization header data tokens into map for further validation
    private def formatAuthStringIntoMap(tokens){
        //default result is an empty map
        def mapWithTokens=[:]
        if(tokens){
            try{
                tokens=tokens.replace('"','').replace(',','').replace('_','').split(' ')
                tokens.each{
                    def tokenKey, tokenValue
                    if(it.contains('=')){
                        (tokenKey, tokenValue)=new String(it).split("=")
                        mapWithTokens.put("${tokenKey}","${tokenValue}")
                    }
                    else{
                        mapWithTokens<<[authType:it]
                    }
                }
            }catch (Exception e){
                log.error("API|Security Filter|formatAuthStringIntoMap - Error: authorization token parsing error: " + e)
                return mapWithTokens
            }
        }
        return mapWithTokens
    }

    //checks Authorization header data tokens
    private boolean validate(result){
        if(!result||result.size==0) return false
        if(!result.find{it.key=='oauthconsumerkey'}.value||
           !result.find{it.key=='oauthsignature'}.value||
           !result.authType)
                return false
        //TODO: Add other Authorization header validation rules
        return true
    }
}
