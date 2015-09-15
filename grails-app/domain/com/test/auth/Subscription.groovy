package com.test.auth

class Subscription {

    String name
    String pricingDuration
    String status

    static belongsTo = [user: TestUser]

    Subscription(String name, String pricingDuration, String status) {
        this.name = name
        this.pricingDuration = pricingDuration
        this.status = status
    }

    static constraints = {
        name blank: false
        pricingDuration blank: false
        status blank: false
    }
}
