package project;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.*;
import project.dbutils.*;
import project.models.*;
import project.utils.Token;
import project.utils.UserJson;

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
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
		}
		Token tkn = Token.generateToken(usr);
		return Response.ok(tkn, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response register(User usr) {
		Database db = new Database();
		db.newUser(usr);
		return Response.ok().build();
	}
	
	@GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@PathParam("id") String id) {
		Database db = new Database();
		User us = db.getUser(Integer.parseInt(id));
        return Response.ok(new UserJson(us), MediaType.APPLICATION_JSON).build();
    }
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        Database data = new Database();
        List<User> lst = data.getUsers();
        System.out.println(lst.size());
        User usr = lst.get(0);//just to instantiate the list due to jpa lazy binding
        List<UserJson> l = new ArrayList<UserJson>();
        for(User u : lst) {
        	l.add(new UserJson(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
    }
	
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
