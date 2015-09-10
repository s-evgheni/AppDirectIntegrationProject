modules = {
    application {
        resource url:'js/application.js'
    }

    common {
        resource url: '/css/test.css'
        resource url: '/css/errors.css'
        resource url: '/js/libs/modernizr.js', disposition: 'head'
        resource url: "/js/libs/jquery.js"
        resource url: "/js/Test.js"
        resource url: "/js/appDirect.js"
    }
}