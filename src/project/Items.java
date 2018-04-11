package project;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import project.models.*;

import java.util.*;

@Path("/items")
public class Items {

	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public List<Item> getItems(int projectID) {
		return new ArrayList<Item>();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void newItem(Item it) {
		
	}
	
	@Path("{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public void updateItem(@PathParam("id") String itemid, Item it) {
		
	}
	
	@Path("{id}")
	@DELETE
	public void deleteItem(@PathParam("id") String itemid) {
		
	}
	
	@Path("comments")
	@GET
	public List<Comment> getComments(@PathParam("id") String id) {
		return new ArrayList<Comment>();
	}
}
