import com.test.auth.*

class BootStrap {

    //Define test user accounts on app start
    def init = { servletContext ->
        def roleAdmin = new Role(authority: 'ROLE_ADMIN').save()
        def roleUser = new Role(authority: 'ROLE_USER').save()

        //for testing purposes only. This normally should be fetched from a remote secure DB
        def user = new User(username: 'user', password: 'password', enabled: true).save()
        def admin = new User(username: 'admin', password: 'password', enabled: true).save()

        UserRole.create user, roleUser
        UserRole.create admin, roleUser
        UserRole.create admin, roleAdmin, true
    }
    def destroy = {
    }
}
