# Read Me
Requirements: Grails v. 2.3.10, Java 7 (set PATH variables for JAVA_HOME and GRAILS_HOME)

To run locally change directory to the root of this app and execute $grails run-app from the command line.

To create 'war' file run $grails war from the command line

##User registration and linking via OpenID option:
1. Navigate to http://localhost:8080/test/ and you should be prompted with the login screen.
2. Leave the Use OpenID checkbox checked and enter a valid OpenID (for Yahoo for example you can use https://me.yahoo.com and for AppDirect you can use https://www.appdirect.com/openid/id/). Don't check the remember-me checkbox yet (it doesn't work with the extended workflows where you create a new user or link an OpenID)
3. Click the "Log in" button.
4. After authenticating at the OpenID provider, you'll be redirected to the registration page. Note that there's a link to just associate the current OpenID with a local account - for now click the "link this OpenID" link.
5. At the next screen enter the username and password for the user with ROLE_ADMIN ('admin'/'P@ssw0rd') or ROLE_USER('user'/'P@ssw0rd')and you will be redirected to http://localhost:8080/test/secured/{admins||users} where your username and opendId data will be displayed </br>

##Test that the OpenID is linked to your account: </br>
Click on a logout button and navigate to http://localhost:8080/test/secured/{admins || users}. Logging out removes the remember-me cookie, so authenticate again with your OpenID (this time check the remember-me checkbox) and it should skip the register/link step and go directly to the secured page. You can also repeat the process and switch to the username/password login and use that.


##To test remember-me:
1. Close the browser and re-open it
2. Navigate to http://localhost:8080/test/
If working correctly and you were logged in as admin it should skip the authentication step entirely and show the secured page.

#API flows:
####Important !
1. In order to use API flows for your account you must change consumer key and secret data in the EventService and rebuild the app 

class EventService {
    private static String CONSUMER_KEY='yourKeyHere'
    private static String CONSUMER_SECRET='yourSecretHere'
...
}

2. The app is running an in memory H2 database instance. Any reboots to the app will cause a complete data loss. 



####Create subscription (non-interactive event. Request endpoint: http://localhost:8080/test/subscription/create):
1. Generate SUBSCRIPTION_ORDER event from AppDirect developer account to the application endpoint (Developer > Products > Edit button > Integration section > Edit Integration > Subscribe to your product)
2. Navigate to http://localhost:8080/test/dbconsole and check new user account created by AppDirect event in the TEST_USER table. (this data will also be available in the Integration > Events section of the developer account)
3. Navigate to http://localhost:8080/test/ and enter username from step 2 with a password='P@ssw0rd'

If working correctly you will be redirected to the secured/users section of the site where information about new user and his subscription will be displayed.

####Change subscription (non-interactive event. Request endpoint: http://localhost:8080/test/subscription/change):
1. Generate SUBSCRIPTION_CHANGE event from AppDirect developer account to the application endpoint (Developer > Products > Edit button > Integration section > Edit Integration > Change(update) the subscription)
2. Navigate to http://localhost:8080/test/ and enter username/password which was assigned to you during SUBSCRIPTION_ORDER event 

If working correctly you will be redirected to the secured/users section of the site where information about user and his updated subscription will be displayed. Please note the changes to the subscription data

####Cancel subscription (non-interactive event. Request endpoint: http://localhost:8080/test/subscription/cancel):
1. Generate SUBSCRIPTION_CANCEL event from AppDirect developer account to the application endpoint (Developer > Products > Edit button > Integration section > Edit Integration > Cancel the subscription)
2. Navigate to http://localhost:8080/test/ and enter username/password which was assigned to you during SUBSCRIPTION_ORDER event

If working correctly you will be redirected to the secured/users section of the site where information about user and his subscription will be displayed. Please note the changes to the subscription state data.
Canceling subscription does not deactivate user's account in the app. This was done intentionally so the outcome of the CANCEL_SUBSCRIPTION event can be observed in the user interface.
At this point SUBSCRIPTION_ORDER event can be executed again. In this case the event will replace cancelled subscription on a given user with a new one from the event.
Please also note that the app do not support SUBSCRIPTION_NOTICE events yet


Technologies used: Grails, Java, Spring Security(with OpenId extension), Zurb's Foundation, HTML5, JavaScript, H2 database, Gradle, Hibernate, SignPost with Apache commons Http library, Oauth 1.0, Tomcat, AppDirect API's;
