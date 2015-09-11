# Read Me
Requirements: Grails v. 2.3.10, Java 7

To run locally change directory to the root of this app and execute $grails run-app from the command line.

To create 'war' file run $grails war from the command line

##User registration and linking via OpenID option:
1. Navigate to http://localhost:8080/test/ and you should be prompted with the login screen.
2. Leave the Use OpenID checkbox checked and enter a valid OpenID. Don't check the remember-me checkbox yet (it doesn't work with the extended workflows where you create a new user or link an OpenID)
3. Click the "Log in" button.
4. After authenticating at the OpenID provider, you'll be redirected to the registration page. Note that there's a link to just associate the current OpenID with a local account - for now click the "link this OpenID" link.
5. At the next screen enter the username and password for the user with ROLE_ADMIN ('admin'/'password') and you will be redirected to http://localhost:8080/test/secured/admins </br>

##Test that the OpenID is linked to your account either: </br>
Click on a logout button and navigate to http://localhost:8080/test/secured/admins. Logging out removes the remember-me cookie, so authenticate again with your OpenID (this time check the remember-me checkbox) and it should skip the register/link step and go directly to the secured page. You can also repeat the process and switch to the username/password login and use that.


##To test remember-me:
1. Close the browser and re-open it
2. Navigate to http://localhost:8080/test/
If working correctly and you were logged in as admin it should skip the authentication step entirely and show the secured page.
