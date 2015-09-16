package com.test.auth

class Subscription {

    String name
    String pricingDuration
    String status
    Date   createdTms
    String marketplace

    static belongsTo = [user: TestUser]

    Subscription(String name, String pricingDuration, String status, String marketplace) {
        this.name = name
        this.pricingDuration = pricingDuration
        this.status = status
        this.createdTms=new Date()
        this.marketplace=marketplace
    }

    static constraints = {
        name blank: false
        pricingDuration blank: false
        status blank: false
    }
}
