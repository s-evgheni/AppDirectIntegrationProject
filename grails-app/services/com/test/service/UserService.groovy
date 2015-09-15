package com.test.service

import com.test.auth.TestUser
import grails.transaction.Transactional

@Transactional
class UserService {

    //lookup user data from the database
    def lookupUserData(String userName) {
        try{
            def testUser=TestUser.createCriteria().get{eq('username',userName)}
            def userSubscriptions=testUser.subscriptions
            def openIds=testUser.openIds
            return [userName:userName, subscriptions:userSubscriptions, openIds:openIds]
        }
        catch (Exception e){
            return [userName:userName]
        }
    }
}
