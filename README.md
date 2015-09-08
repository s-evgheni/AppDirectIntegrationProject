# Read Me
to run locally execute $grails run-app from the command line
to create war file run $grails war from the command line

#User registration and linking (local testing):
1. Navigate to http://localhost:8080/test/secured/admins and you should be prompted with the login screen.
2. Leave the Use OpenID checkbox checked and enter a valid OpenID. Don't check the remember-me checkbox yet (it doesn't work with the extended workflows where you create a new user or link an OpenID)
3. Click the "Log in" button.
4. After authenticating at the OpenID provider, you'll be redirected to the registration page. Note that there's a link to just associate the current OpenID with a local account - for now click the "link this OpenID" link.
5. At the next screen enter the username and password for the user with ROLE_ADMIN ('admin'/'password') and you will be redirected to http://localhost:8080/test/secured/admins

To test that the OpenID is linked to your account either:
1. logout by navigating to http://localhost:8080/test/logout/ (must be POST request) and navigate to http://localhost:8080/test/secured/admins. 
Logging out removes the remember-me cookie, so authenticate again with your OpenID (this time check the remember-me checkbox) and it should skip the register/link step and go directly to the secured page. You can also repeat the process and switch to the username/password login and use that.
or
2. Login to the http://localhost:8080/test/dbconsole (username:'sa'/nopassword) and check openId table

To test remember-me:
1. close the browser and re-open it
2. navigate to http://localhost:8080/test/secured/admins. 
If working correctly it should skip the authentication step entirely and show the secured page.
