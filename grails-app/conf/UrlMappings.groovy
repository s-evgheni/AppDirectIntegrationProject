class UrlMappings {

	static mappings = {

        "/"{
            controller= 'public'
        }

        "/login/auth" {
            controller = 'public'
            action = 'auth'
        }

        "/login/openIdCreateAccount" {
            controller = 'public'
            action = 'createAccount'
        }

        "/subscription/create" {
            controller = 'subscription'
            action = 'create'
        }

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "500"(view:'/error')
	}
}
