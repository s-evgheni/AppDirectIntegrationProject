class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/login/auth" {
            controller = 'openId'
            action = 'auth'
        }
        "/login/openIdCreateAccount" {
            controller = 'openId'
            action = 'createAccount'
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
