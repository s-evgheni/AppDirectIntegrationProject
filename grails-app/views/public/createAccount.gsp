<div class="medium-8 medium-push-2 large-6 large-push-3 columns" xmlns="http://www.w3.org/1999/html">

    <g:hasErrors bean="${command}">
        <div data-alert class="alert-box error">
            <g:renderErrors bean="${command}" as="list"/>
        </div>
    </g:hasErrors>
    <g:if test='${flash.error}'>
        <div data-alert class="alert-box error">
            ${flash.error}
        </div>
    </g:if>

    <div class="panel">
        <header class="panel-header" id="openidLoginHeader"><h3>User registration form</h3></header>
        <div class="row" id="registration-form">
            <div class="large-12 columns">
                <g:form action='createAccount'>
                    <div class="input-wrapper">
                        <div class="medium-3 columns">
                            <strong>Your Open ID:</strong>
                        </div>
                        <div class="medium-9 columns text-left">
                            <p>${openId}</p>
                        </div>
                    </div>

                    <div class="input-wrapper">
                        <div class="medium-3 columns">
                            <strong>Username:</strong>
                        </div>
                        <div class="medium-9 columns text-left">
                            <g:textField name='username' value='${command.username}'/>
                        </div>
                    </div>

                    <div class="input-wrapper">
                        <div class="medium-3 columns">
                            <strong>Password:</strong>
                        </div>
                        <div class="medium-9 columns text-left">
                            <g:passwordField name='password' value='${command.password}'/>
                        </div>
                    </div>

                    <div class="input-wrapper">
                        <div class="medium-3 columns text-left">
                            <strong>Password (again):</strong>
                        </div>
                        <div class="medium-9 columns">
                            <g:passwordField name='password2' value='${command.password2}'/>
                        </div>
                    </div>

                    <g:if test='${openId}'>
                        <div class="input-wrapper">
                            <div class="medium-10 columns text-left">
                                If you're already a user you can <g:link action='linkAccount'>link this OpenID</g:link> to your account<br/>
                            </div>
                        </div>
                    </g:if>
                    <br/>
                    <footer class="panel-footer">
                        <div class="row text-center">
                            <button type="submit" class="button" id="login-button"><strong>Register</strong></button>
                        </div>
                    </footer>
                </g:form>
            </div>
        </div>
    </div>
</div>