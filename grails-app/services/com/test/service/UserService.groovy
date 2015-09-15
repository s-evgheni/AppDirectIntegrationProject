package com.test.service

import grails.transaction.Transactional

@Transactional
class UserService {

    def getData(String userName) {
        return [userName:userName]
    }
}
