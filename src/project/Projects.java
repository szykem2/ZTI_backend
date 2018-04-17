package project;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.dbutils.Database;
import project.models.*;
import project.utils.HeaderException;
import project.utils.HeaderValidator;
import project.utils.ProjectJson;
import project.utils.Token;
import project.utils.TokenException;
import project.utils.UserJson;

import java.util.*;
import java.util.stream.Collectors;

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
			return e.getResponse();
		}
		List<Project> lst = usr.getProjects();
		System.out.println("List: " + lst.size());
		System.out.println(lst);
		List<ProjectJson> list = new ArrayList<ProjectJson>();
		for(Project p: lst) {
			list.add(new ProjectJson(p));
		}

		return Response.ok(list, MediaType.APPLICATION_JSON).build();

	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addProject(Project pr) {
		Database db = new Database();
		db.newProject(pr);
		return Response.ok().build();
	}
	
	@Path("{id}")
	@DELETE
	public Response deleteProject(@PathParam("id") String id) {
		Database db = new Database();
		db.removeProject(Integer.parseInt(id));
		return Response.ok().build();
	}
	
	@Path("{id}/users")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers(@PathParam("id") String id) {
		Database data = new Database();
        Project pr = data.getProject(Integer.parseInt(id));
        List<User> lst = pr.getUsers();
        List<UserJson> l = new ArrayList<UserJson>();
        if(lst.size() > 0) {
        	User usr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        System.out.println(lst);
        for(User u : lst) {
        	l.add(new UserJson(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}
	
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
