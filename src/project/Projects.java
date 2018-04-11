package project;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import project.models.*;

import java.util.*;

@Path("/projects")
public class Projects {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Project> getProjects() {
		//Implement
		return new ArrayList<Project>();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void addProject(Project pr) {
		
	}
	
	@Path("{id}")
	@DELETE
	public void deleteProject(@PathParam("id") String id) {
		
	}
	
	@Path("{id}/users")
	@GET
	public List<User> getUsers(@PathParam("id") String id) {
		return new ArrayList<User>();
	}
}
