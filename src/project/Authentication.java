package project;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import project.dbutils.*;
import project.models.*;

@Path("/users")
public class Authentication extends Application {
	
	//localhost:9080/users/login
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	public String login(User usr) {
		return "Login here";
	}
	
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	public String register(User usr) {
		return "Register here";
	}
	
	@GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public User get(@PathParam("id") String id) {
        //Database data = new Database();
		//return data.readPerson(id);
        return new User();
    }
}
