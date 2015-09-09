package com.test.auth



class OpenID {

	String url

	static belongsTo = [user: TestUser]

	static constraints = {
		url unique: true
	}
}
