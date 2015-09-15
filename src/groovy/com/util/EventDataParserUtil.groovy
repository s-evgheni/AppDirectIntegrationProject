package com.util

/**
 * Extracts data pieces from event XML and returns them as a map
 */
class EventDataParserUtil {

    public static getEventType(eventXml){
        return eventXml?.type?.text()?:""
    }

    public static getMarketPlace(eventXml){
        return [baseUrl:eventXml?.marketplace?.baseUrl?.text()?:"",
                partner:eventXml?.marketplace?.partner?.text()?:""]
    }

    public static getFlag(eventXml){
        return eventXml?.flag?.text()?:""
    }

    public static getCreator(eventXml){
        if(!eventXml) return [:]
        return [email:eventXml?.creator?.email?.text()?:"",
                firstName:eventXml?.creator?.firstName?.text()?:"",
                lastName:eventXml?.creator?.lastName?.text()?:"",
                language:eventXml?.creator?.language?.text()?:"",
                openId:eventXml?.creator?.openId?.text()?:"",
                uuid:eventXml?.creator?.uuid?.text()?:""]
    }

    public static getCompany(eventXml){
        return [country:eventXml?.payload?.company?.country?.text()?:"",
                name:eventXml?.payload?.company?.name?.text()?:"",
                uuid:eventXml?.payload?.company?.uuid?.text()?:"",
                website:eventXml?.payload?.company?.website?.text()?:"",]
    }

    public static getOrder(eventXml){
        return [editionCode:eventXml?.payload?.order?.editionCode?.text()?:"",
                pricingDuration:eventXml?.payload?.order?.pricingDuration?.text()?:""]
    }

    public static getPayload(eventXml){
        return [company:getCompany(eventXml),
                order:getOrder(eventXml)]
    }
}
