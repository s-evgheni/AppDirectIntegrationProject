import com.test.auth.*

class BootStrap {

    //Define test user accounts on app start to test login
    def init = { servletContext ->
        def roleAdmin = new TestRole(authority: 'ROLE_ADMIN').save()
        def roleUser = new TestRole(authority: 'ROLE_USER').save()

        def user = new TestUser(username: 'user', password: 'password', enabled: true).save()
        def admin = new TestUser(username: 'admin', password: 'password', enabled: true).save()

        UserRole.create user, roleUser
        UserRole.create admin, roleAdmin, true
    }
    def destroy = {
    }
}
