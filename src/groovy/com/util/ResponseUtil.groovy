package com.util

import com.constants.ErrorCode
import groovy.xml.XmlUtil

/**
 * Handles success and error responses
 */
class ResponseUtil {

    public static def generateErrorResponse(ErrorCode code){
        return XmlUtil.serialize("<result><success>false</success><errorCode>"+code.originNameCode+"</errorCode><message>"+code.message+"</message></result>")
    }

    public static def generateSuccessResponse(companyName, accountIdentifier){
        return XmlUtil.serialize("<result><success>true</success><message>Account creation successful for "+companyName+"</message><accountIdentifier>"+accountIdentifier+"</accountIdentifier></result>")
    }
}