package com.util

import com.constants.ErrorCode
import groovy.xml.XmlUtil

/**
 * Generate success and error XML responses
 */
class ResponseUtil {


    public static def generateErrorResponse(ErrorCode code){
        return XmlUtil.serialize("<result><success>false</success><errorCode>"+code.originNameCode+"</errorCode><message>"+code.message+"</message></result>")
    }

    public static def generateSuccessResponse(accountHolder){
        return XmlUtil.serialize("<result><success>true</success><message>Account creation successful for "+accountHolder.fullName+"</message><accountIdentifier>"+accountHolder.identifier+"</accountIdentifier></result>")
    }
}