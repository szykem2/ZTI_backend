package project;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.*;
import project.dbutils.*;
import project.models.*;
import project.utils.Token;

import java.util.*;

@Path("/users")
public class Authentication extends Application {
	
	//localhost:9080/users/login
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(Vector<Map<String,String>> vec) {
		Map<String, String> mp = (Map<String,String>)vec.get(0);
		User usr = new Database().authorize(mp.get("login"), mp.get("password"));
		if(usr == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Invalid credentials").build();
		}
		Token tkn = Token.generateToken(usr);
		Map<String, String> map = new HashMap<String, String>();
		map.put("token", tkn.getToken());
		System.out.println(tkn.getToken());
		return Response.ok(tkn, MediaType.APPLICATION_JSON).build();
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
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> get() {
        Database data = new Database();
		return data.getUsers();
    }
}
