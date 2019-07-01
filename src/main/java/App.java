import com.demooracle.controller.UserCreationController;
import com.demooracle.domain.RoleManagement;
import com.ultraschemer.microweb.controller.AuthorizationFilter;
import com.ultraschemer.microweb.controller.LoginController;
import com.ultraschemer.microweb.controller.LogoffController;
import com.ultraschemer.microweb.domain.JwtSecurityManager;
import com.ultraschemer.microweb.domain.UserManagement;
import com.ultraschemer.microweb.error.StandardException;
import com.ultraschemer.microweb.vertx.WebAppVerticle;
import io.vertx.core.http.HttpMethod;

/*
 * Entry point principal da aplicação:
 */
public class App extends WebAppVerticle {
    @Override
    public void initialization() {
        // Verify the default user and the default role:
        UserManagement.initializeRoot();
        RoleManagement.initializeDefault();

        // Initialize JWT symmetric security keys:
        try {
            JwtSecurityManager.initializeSKey();
        } catch(StandardException e) {
            // No exception is waited here:
            e.printStackTrace();
            System.out.println("Exiting.");
            System.exit(-1);
        }

        // Registra os filtros de inicialização:
        registerFilter(new AuthorizationFilter(this.vertx));

        // Registra os controllers:
        registerController(HttpMethod.POST, "/v0/login", new LoginController(this.vertx));
        registerController(HttpMethod.GET, "/v0/logoff", new LogoffController());

        registerController(HttpMethod.POST, "/v0/user", new UserCreationController());

        // Registra os filtros de finalização:
        // Bem... eles ainda não existem...
    }
}
