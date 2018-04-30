package project;
import java.util.Set;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import project.utils.EmailValidator;
import project.utils.LoginValidator;

@ApplicationPath("")
@Path("")
public class App extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(Authentication.class);
        resources.add(Items.class);
        resources.add(Projects.class);
        resources.add(Requests.class);
        resources.add(EmailValidator.class);
        resources.add(LoginValidator.class);
        return resources;
    }
}
