# Read Me
Requirements: Grails v. 2.3.10, Java 7 (set PATH variables for JAVA_HOME and GRAILS_HOME)

To run locally change directory to the root of this app and execute $grails run-app from the command line.

To create 'war' file run $grails war from the command line

##User registration and linking via OpenID option:
1. Navigate to http://localhost:8080/test/ and you should be prompted with the login screen.
2. Leave the Use OpenID checkbox checked and enter a valid OpenID. Don't check the remember-me checkbox yet (it doesn't work with the extended workflows where you create a new user or link an OpenID)
3. Click the "Log in" button.
4. After authenticating at the OpenID provider, you'll be redirected to the registration page. Note that there's a link to just associate the current OpenID with a local account - for now click the "link this OpenID" link.
5. At the next screen enter the username and password for the user with ROLE_ADMIN ('admin'/'P@ssw0rd') and you will be redirected to http://localhost:8080/test/secured/admins where your username and opendId data will be displayed </br>

##Test that the OpenID is linked to your account either: </br>
Click on a logout button and navigate to http://localhost:8080/test/secured/admins. Logging out removes the remember-me cookie, so authenticate again with your OpenID (this time check the remember-me checkbox) and it should skip the register/link step and go directly to the secured page. You can also repeat the process and switch to the username/password login and use that.


##To test remember-me:
1. Close the browser and re-open it
2. Navigate to http://localhost:8080/test/
If working correctly and you were logged in as admin it should skip the authentication step entirely and show the secured page.

##To create subscription (non-interactive event):
1. Generate CREATE_SUBSCRIPTION event from AppDirect developer account (Developer > Products > Edit button > Integration section > Edit Integration > Subscribe to your product)
2. Navigate to http://localhost:8080/test/dbconsole and check new user account created by AppDirect event in the TEST_USER table. (this data will also be available in the Integration > Events section of the developer account)
3. Navigate to http://localhost:8080/test/secured/users and enter username from step 2 with a password='P@ssw0rd'
If working correctly you will be redirected to the secured/users section of the site where information about new user and his active subscriptions will be displayed.
