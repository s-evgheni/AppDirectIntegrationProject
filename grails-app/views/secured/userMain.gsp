<div class="medium-8 medium-push-2 large-6 large-push-3 columns">
    <div class="panel">
        <h3>Welcome to the secure section of our site!</h3>
        <header class="panel-header"><h3>User Data</h3></header>
        <div class="row">
            <div class="large-12 columns">
                <p class="text-left">User name: <strong>${userData.userName}</strong></p>
                <g:each in="${userData.openIds}" var="openId">
                    <p class="text-left">OpenID : <strong>${openId?.url}</strong></p>
                </g:each>
            </div>
        </div>
        <g:if test="${userData?.subscriptions}">
            <header class="panel-header"><h3>Subscription Data</h3></header>
            <div class="row">
                <div class="large-12 columns">
                    <g:each in="${userData.subscriptions}" var="subscription">
                        <p class="text-left">Subscription Name: <strong>${subscription?.name}</strong></p>
                        <p class="text-left">Marketplace: <strong>${subscription?.marketplace}</strong></p>
                        <p class="text-left">Billing period: <strong>${subscription?.pricingDuration}</strong></p>
                        <p class="text-left">Status: <strong>${subscription?.status}</strong></p>
                        <p class="text-left">Date created: <strong>${subscription?.createdTms}</strong></p>
                    </g:each>
                </div>
            </div>
        </g:if>

        <div class="row">
            <form id="logout-form" name="logoutRequest" autocomplete="off" method="POST" action="logout">
                <div class="row">
                    <div class="medium-12 columns text-center">
                        <button type="submit" class="button" id="logoutButton">Log out</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>