package project;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;

import project.models.Project;

@ApplicationPath("/")
public class App extends Application {
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(Authentication.class);
        resources.add(Items.class);
        resources.add(Projects.class);
        return resources;
    }
}
