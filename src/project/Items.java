package project;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import project.dbutils.Database;
import project.models.*;
import project.utils.CommentJson;
import project.utils.ItemJson;

import java.util.*;

@Path("/items")
public class Items {

	@Path("{projectid}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getItems(@PathParam("projectid") String projectid) {
		Database db = new Database();
		List<Item> lst = db.getProject(Integer.parseInt(projectid)).getItems();
		System.out.println("List: " + lst.size());
		if(lst.size() > 0) {
			Item cmt = lst.get(0);
		}
		List<ItemJson> list = new ArrayList<ItemJson>();
		for(Item it: lst) {
			list.add(new ItemJson(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response newItem(Item it) {
		Database db = new Database();
		db.newItem(it);
		return Response.ok().build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateItem(@PathParam("id") String itemid, Item it) {
		it.setItemid(Integer.parseInt(itemid));
		Database db = new Database();
		db.updateItem(it);
		return Response.ok().build();
	}
	
	@Path("{id}")
	@DELETE
	public Response deleteItem(@PathParam("id") String itemid) {
		Database db = new Database();
		db.removeItem(Integer.parseInt(itemid));
		return Response.ok().build();
	}
	
	@Path("{id}/comments")
	@GET
	public Response getComments(@PathParam("id") String id) {
		Database db = new Database();
		Item it = db.getItem(Integer.parseInt(id));
		List<Comment> lst = it.getComments();
		System.out.println("List: " + lst.size());
		if(lst.size() > 0) {
			Comment cmt = lst.get(0);
		}
		List<CommentJson> list = new ArrayList<CommentJson>();
		for(Comment cmt: lst) {
			list.add(new CommentJson(cmt));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
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
