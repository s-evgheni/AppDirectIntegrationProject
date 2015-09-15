package com.constants

/**
 * API Error Codes
 * for more info see: http://info.appdirect.com/developers/docs/event_references/api_error_codes/
 */
public enum ErrorCode {

    /*This error code is typically used when AppDirect admins try to buy subscriptions for apps they have already purchased directly from the Application Vendor. In this scenario, we'll show users an error message and prompt them to link their accounts.*/
    USER_ALREADY_EXISTS ('USER_ALREADY_EXISTS','This user already exist'),
    /*This error code is typically used when AppDirect admins try to unassign users not found in the Application Vendor's account.*/
    USER_NOT_FOUND ('USER_NOT_FOUND','Invalid user'),
    /*This error code is typically used when AppDirect admins try to add or remove users from an account not found in the Application Vendor's records.*/
    ACCOUNT_NOT_FOUND ('OPERATION_CANCELED','Operation has been cancelled'),
    /*This error code is typically used when AppDirect admins try to assign users beyond the limit of the number of seats available. AppDirect will typically prevent that from happening by monitoring app usage.*/
    MAX_USERS_REACHED ('MAX_USERS_REACHED','Maximum users reached'),
    /*This error code is returned when users try any action that is not authorized for that particular application. For example, if an application does not allow the original creator to be unassigned.*/
    UNAUTHORIZED ('UNAUTHORIZED','Unauthorized'),
    /*This error code is returned when a user manually interrupts the operation (clicking cancel on the account creation page, etc.).*/
    OPERATION_CANCELED ('OPERATION_CANCELED','Operation Canceled'),
    /*This error code is returned when the vendor endpoint is not currently configured.*/
    CONFIGURATION_ERROR ('CONFIGURATION_ERROR','Configuration Error'),
    /*This error code is returned when the vendor was unable to process the event fetched from AppDirect.*/
    INVALID_RESPONSE ('INVALID_RESPONSE','Unable to process event from AppDirect'),
    /*This error code may be used when none of the other error codes apply.*/
    UNKNOWN_ERROR ('UNKNOWN_ERROR','Something went wrong. Please see server logs for details'),
    /*This error code is returned when the vendor was unable to process the event because the service is under provisioning.*/
    PENDING ('PENDING','Service is under provisioning')

    final String originNameCode, message

    ErrorCode(originNameCode, message){
        this.originNameCode=originNameCode
        this.message=message
    }

    String getOriginNameCode() {
        return originNameCode
    }

    String getMessage() {
        return message
    }
}
