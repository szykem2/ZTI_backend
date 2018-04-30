package project;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import project.dbutils.Database;
import project.models.Project;
import project.models.User;
import project.utils.HeaderException;
import project.utils.HeaderValidator;
import project.utils.ProjectJson;
import project.utils.UserJson;

@Path("/requests")
public class Requests {
	
	@Path("{id}")
	@POST
	public Response requestAccess(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			System.out.println("exc");
			return e.getResponse();
		}
		Database db = new Database();
		db.newRequestor(usr, Integer.parseInt(id));
		return Response.ok().build();
	}
	
	@Path("{id}")
	@GET
	public Response getRequestors(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		List<User> lst = db.getRequested(usr, Integer.parseInt(id));
		List<UserJson> l = new ArrayList<UserJson>();
        if(lst.size() > 0) {
        	usr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        System.out.println(lst);
        for(User u : lst) {
        	l.add(new UserJson(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	public Response getProjectsForRequest(@Context  HttpHeaders headers) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		List<Project> lst = db.getProjects();
		List<Project> ulst = usr.getProjects();
		List<ProjectJson> l = new ArrayList<ProjectJson>();
        if(lst.size() > 0) {
        	Project pr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        System.out.println(lst);
        for(Project pr : lst) {
        	if(!ulst.contains(pr)) {
        		l.add(new ProjectJson(pr));
        	}
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}
	
	@Path("{id}/{userid}")
	@POST
	public Response acceptRequest(@Context  HttpHeaders headers, @PathParam("id") String id, @PathParam("userid") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		db.acceptRequest(Integer.parseInt(id), Integer.parseInt(userid));
		return Response.ok().build();
	}
	
	@Path("{id}/{userid}")
	@DELETE
	public Response denyAccess(@Context  HttpHeaders headers, @PathParam("id") String id, @PathParam("id") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		System.out.println("deleteRequest");
		db.denyAccess(db.getUser(Integer.parseInt(userid)), Integer.parseInt(id));
		return Response.ok().build();
	}
	
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}
