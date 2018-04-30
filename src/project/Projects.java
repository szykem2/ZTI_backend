package project;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import project.dbutils.Database;
import project.models.*;
import project.utils.*;
import java.util.*;

@Path("/projects")
public class Projects {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProjects(@Context  HttpHeaders headers) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			System.out.println(e.getResponse().toString());
			return e.getResponse();
		}
		List<Project> lst = usr.getProjects();
		System.out.println("List: " + lst.size());
		System.out.println(lst);
		List<ProjectJson> list = new ArrayList<ProjectJson>();
		for(Project p: lst) {
			ProjectJson pr = new ProjectJson(p);
			if(usr.getIsAdmin().contains(p)) {
				pr.setIsAdmin(true);
			}
			list.add(pr);
		}

		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addProject(@Context  HttpHeaders headers, Project pr) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		db.newProject(usr, pr);
		return Response.ok().build();
	}
	
	
	
	@Path("{id}")
	@DELETE
	public Response deleteProject(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.removeProject(Integer.parseInt(id));
		return Response.ok().build();
	}
	
	@Path("{id}/users")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		System.out.println("db");
		System.out.println(usr.getProjects().size());
		System.out.println(db.getProject(Integer.parseInt(id)).getProjectid());
		if(!usr.getProjects().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        List<User> lst = pr.getUsers();
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
	
	@Path("{id}/users")
	@POST
	public Response addUser(@Context  HttpHeaders headers, @PathParam("id") String id, User usr) {
		User user = null;
		try {
			user = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!user.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        db.addUserToProject(pr, usr);
		return Response.ok().build();
	}
	
	@Path("{id}/admins")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAdmins(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();

        Project pr = db.getProject(Integer.parseInt(id));
        List<User> lst = pr.getAdmins();
        List<UserJson> l = new ArrayList<UserJson>();
        if(lst.size() > 0) {
        	usr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        for(User u : lst) {
        	l.add(new UserJson(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}
	
	@Path("{id}/admins")
	@POST
	public Response addAdmin(@Context  HttpHeaders headers, @PathParam("id") String id, User usr) {
		User user = null;
		try {
			user = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!user.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        db.addAdminToProject(pr, usr);
		return Response.ok().build();
	}
	
	@Path("{id}/users/{userid}")
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	public Response reomveUser(@Context  HttpHeaders headers, @PathParam("id") String id, @PathParam("userid") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.removeUserFromProject(Integer.parseInt(id), Integer.parseInt(userid));
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
