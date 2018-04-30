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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import project.dbutils.Database;
import project.models.*;
import project.utils.CommentJson;
import project.utils.HeaderException;
import project.utils.HeaderValidator;
import project.utils.ItemJson;
import project.utils.ItemstatusJson;
import project.utils.ItemtypeJson;

import java.util.*;

@Path("/items")
public class Items {

	@Path("{projectid}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getItems(@Context  HttpHeaders headers, @PathParam("projectid") String projectid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getProjects().contains(db.getProject(Integer.parseInt(projectid)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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
	
	@Path("itemstatus")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getStatuses() {

		Database db = new Database();

		List<Itemstatus> lst = db.getStatuses();
		System.out.println("List: " + lst.size());
		if(lst.size() > 0) {
			Itemstatus cmt = lst.get(0);
		}
		List<ItemstatusJson> list = new ArrayList<ItemstatusJson>();
		for(Itemstatus it: lst) {
			list.add(new ItemstatusJson(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	@Path("itemtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTypes() {

		Database db = new Database();

		List<Itemtype> lst = db.getTypes();
		System.out.println("List: " + lst.size());
		if(lst.size() > 0) {
			Itemtype cmt = lst.get(0);
		}
		List<ItemtypeJson> list = new ArrayList<ItemtypeJson>();
		for(Itemtype it: lst) {
			list.add(new ItemtypeJson(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response newItem(@Context  HttpHeaders headers, Item it) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getProjects().contains(it.getProject())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.newItem(it);
		return Response.ok().build();
	}
	
	@Path("{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateItem(@Context  HttpHeaders headers, @PathParam("id") String itemid, Item it) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getProjects().contains(it.getProject())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		it.setItemid(Integer.parseInt(itemid));
		db.updateItem(it);
		return Response.ok().build();
	}
	
	@Path("{id}")
	@DELETE
	public Response deleteItem(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getProjects().contains(db.getItem(Integer.parseInt(id)).getProject())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.removeItem(Integer.parseInt(id));
		return Response.ok().build();
	}
	
	@Path("{id}/comments")
	@GET
	public Response getComments(@Context  HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		Item it = db.getItem(Integer.parseInt(id));
		if(!usr.getProjects().contains(it.getProject())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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
