<div class="medium-8 medium-push-2 large-6 large-push-3 columns">
    <div class="panel">
        <header class="panel-header"><h3>Welcome to the ADMIN section of our site!</h3></header>
        <div class="row">
            <div class="large-12 columns">
                <p class="text-left">User name: <strong><sec:loggedInUserInfo field="username"/></strong></p>
            </div>
        </div>

        <div class="row">
            <form id="logout-form" name="logoutRequest" autocomplete="off" method="POST" action="logout">
                <div class="row">
                    <div class="medium-12 columns text-right">
                        <button type="submit" class="button" id="logoutButton">Log out</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>