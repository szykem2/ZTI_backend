package project;
import java.util.Set;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;

import project.validators.EmailValidator;
import project.validators.LoginValidator;

/**
 * Klasa konfiguracyjna technologii REST.
 */
@ApplicationPath("")
@Path("")
public class App extends Application {
	/**
	 * Metoda dodaj¹ca klasy definiuj¹ce endpointy komunikacyjne
	 * @return set klas, w których zdefiniowane s¹ œcierzki komunikacyjne z serwerem
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
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
