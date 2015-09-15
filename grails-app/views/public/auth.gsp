<div class="medium-8 medium-push-2 large-6 large-push-3 columns" xmlns="http://www.w3.org/1999/html">
    <g:if test='${flash.message}'>
        <div class='alert-box error'>
            ${flash.message}
        </div>
    </g:if>

	<div class="panel">
		<h3>Welcome to the public section of our site</h3>

        <div class="row text-right">
            <label for='toggle'>
                <input type='checkbox' checked="checked" id='toggle' onclick='toggleForms()'/> Use OpenID<br/>
            </label>
        </div>

        <header class="panel-header" id="openidLoginHeader"><h3>Login with OpenID</h3></header>
		<div class="row" id="openidLogin">
			<div class="large-12 columns">
                <form action='${openIdPostUrl}' method='POST' autocomplete='off' name='openIdLoginForm'>
                    <div class="input-wrapper">
                        <label>
                            <g:textField id="openId-url-input" name="${openidIdentifier}" type="text"
                                         placeholder="e.g. https://me.yahoo.com/"/>
                        </label>
                    </div>
                    <g:if test='${persistentRememberMe}'>
                        <div class="input-wrapper">
                            <label for='remember_me_openId'>
                                <input type='checkbox' name='${rememberMeParameter}' id='remember_me_openId'/> Remember me<br/>
                            </label>
                            <br/>
                        </div>
                    </g:if>
                    <footer class="panel-footer">
                        <div class="row text-center">
                            <button type="submit" class="button" id="openId-login-button"><strong>Log In</strong></button>
                        </div>
                    </footer>
                </form>
			</div>
		</div>

        <header class="panel-header" id="formLoginHeader" style="display: none"><h3>Login with your credentials</h3></header>
        <div class="row" id="formLogin" style="display: none">
            <div class="large-12 columns">
                <form action='${daoPostUrl}' method='POST' autocomplete='off' name='openIdLoginForm'>
                    <div class="input-wrapper">

                        <label for="username-input">
                            <g:textField id="username-input" name="j_username" type="text"
                                         placeholder="User Name"/>
                        </label>
                    </div>
                    <div class="input-wrapper">
                        <label for="password-input">
                            <g:passwordField id="password-input" name="j_password"
                                         placeholder="Password"/>
                        </label>
                    </div>

                    <div class="input-wrapper">
                        <label for='remember_me_user'>
                                <input type='checkbox' name='${rememberMeParameter}' id='remember_me_user'/> Remember me<br/>
                        </label>
                        <br/>
                    </div>
                    <footer class="panel-footer">
                        <div class="row text-center">
                            <button type="submit" class="button" id="login-button"><strong>Log In</strong></button>
                        </div>
                    </footer>
                </form>
            </div>
        </div>
	</div>
</div>
<script>

    var openid = true;

    (function() {
        document.forms['openIdLoginForm'].elements['openid_identifier'].focus();
    })();

    function toggleForms() {
        if (openid) {
            document.getElementById('openidLoginHeader').style.display = 'none';
            document.getElementById('openidLogin').style.display = 'none';

            document.getElementById('formLoginHeader').style.display = '';
            document.getElementById('formLogin').style.display = '';
        }
        else {
            document.getElementById('openidLoginHeader').style.display = '';
            document.getElementById('openidLogin').style.display = '';

            document.getElementById('formLoginHeader').style.display = 'none';
            document.getElementById('formLogin').style.display = 'none';
        }
        openid = !openid;
    }
</script>