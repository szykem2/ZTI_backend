package project;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import project.dbutils.*;
import project.models.*;
import project.utils.*;
import java.util.*;

@Path("/users")
public class Authentication extends Application {
	
	//localhost:9080/users/login
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(User usr) {
		Database db = new Database();
		User user = db.authorize(usr.getLogin(), usr.getPassword());
		db.closeConnection();
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
		}
		Token tkn = Token.generateToken(user);
		return Response.ok(tkn, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response register(User usr) {
		Database db = new Database();
		db.newUser(usr);
		db.closeConnection();
		return Response.ok().build();
	}
	
	@GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		User us = db.getUser(Integer.parseInt(id));
		db.closeConnection();
        return Response.ok(new UserJson(us), MediaType.APPLICATION_JSON).build();
    }
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@Context  HttpHeaders headers) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
        Database data = new Database();
        List<User> lst = data.getUsers();
        System.out.println(lst.size());
        usr = lst.get(0);//just to instantiate the list due to jpa lazy binding
        List<UserJson> l = new ArrayList<UserJson>();
        for(User u : lst) {
        	l.add(new UserJson(u));
        }
        data.closeConnection();
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
    }
	
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
