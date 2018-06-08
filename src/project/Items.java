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

import org.json.JSONObject;

import project.dbutils.Database;
import project.dto.CommentDto;
import project.dto.ItemDto;
import project.dto.ItemStatusDto;
import project.dto.ItemTypeDto;
import project.exception.HeaderException;
import project.models.*;
import project.validators.HeaderValidator;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Klasa udostêpniaj¹ca endpointy do obs³ugi elementów projektów
 */
@Path("/items")
public class Items {

	/**
	 * Metoda s³u¿¹ca do pobierania elementów przypisanych do projektu o zadanym identyfikatorze.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param projectid ID projektu, którego elementy maj¹ zostaæ przes³ane
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
	 * @see Response
	 */
	@Path("{projectid}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getItems(@Context HttpHeaders headers, @PathParam("projectid") String projectid) {
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
		List<ItemDto> list = new ArrayList<ItemDto>();
		for(Item it: lst) {
			list.add(new ItemDto(it));
		}
		db.closeConnection();
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda s³u¿¹ca do pobierania statusów, które mo¿na przypisaæ elementom.
	 * @return odpowiedŸ serwera HTML status code 200 OK
	 * @see Response
	 */
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
		List<ItemStatusDto> list = new ArrayList<ItemStatusDto>();
		for(Itemstatus it: lst) {
			list.add(new ItemStatusDto(it));
		}
		db.closeConnection();
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda s³u¿¹ca do pobierania typów, które mo¿na przypisaæ elementom.
	 * @return odpowiedŸ serwera HTML status code 200 OK
	 * @see Response
	 */
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
		List<ItemTypeDto> list = new ArrayList<ItemTypeDto>();
		for(Itemtype it: lst) {
			list.add(new ItemTypeDto(it));
		}
		db.closeConnection();
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda s³u¿¹ca do dodawania nowego elementu do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param it obiekt JSON, który przechowuje sk³adowe elementu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli u¿ytkownik nie jest autoryzowany do zadanego projektu
	 * @see Response
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response newItem(@Context HttpHeaders headers, String it) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		JSONObject obj = new JSONObject(it);
		Project pr = db.getProject(obj.getInt("projectid"));
		if(!usr.getProjects().contains(pr)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Item item = new Item();
		item.setApproved(false);
		item.setComments(null);
		item.setApprover(db.getUser(obj.getInt("approver")));
		item.setCreationdate(Timestamp.valueOf(obj.getString("creationDate")));
		item.setDescription(obj.getString("description"));
		item.setItemstatus(db.getStatus(1));
		item.setItemtype(db.getType(obj.getInt("type")));
		item.setOwner(db.getUser(obj.getInt("owner")));
		item.setProject(pr);
		item.setResolutiondate(null);
		item.setResolved(false);
		item.setTitle(obj.getString("title"));
		db.newItem(item);
		db.closeConnection();
		return Response.ok().build();
	}

	/**
	 * Metoda s³u¿¹ca do aktualizowania elementu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param itemid ID elementu, który ma zostaæ zaktualizowany
	 * @param it obiekt JSON, który przechowuje sk³adowe elementu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli u¿ytkownik nie jest autoryzowany do zadanego projektu
	 * @see Response
	 */
	@Path("{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateItem(@Context HttpHeaders headers, @PathParam("id") String itemid, String it) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		JSONObject obj = new JSONObject(it);
		if(!usr.getProjects().contains(db.getProject(obj.getInt("projectid")))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Item item = db.getItem(Integer.parseInt(itemid));
		item.setApprover(db.getUser(obj.getInt("approver")));
		item.setCreationdate(Timestamp.valueOf(obj.getString("creationdate")));
		item.setDescription(obj.getString("description"));
		item.setItemstatus(db.getStatus(1));
		item.setItemtype(db.getType(obj.getInt("itemtype")));
		item.setItemstatus(db.getStatus(obj.getInt("itemstatus")));
		item.setOwner(db.getUser(obj.getInt("owner")));
		String date = obj.optString("resolutiondate");
		if (date.equals("")) {
			item.setResolutiondate(null);
		}
		else {
			item.setResolutiondate(Timestamp.valueOf(date));
		}
		item.setTitle(obj.getString("title"));
		db.updateItem(item);
		db.closeConnection();
		return Response.ok().build();
	}
	/**
	 * Metoda s³u¿¹ca do dodawania nowego elementu do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID elementu, który ma zostaæ usuniêty
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli u¿ytkownik nie jest autoryzowany do zadanego projektu
	 * @see Response
	 */
	@Path("{id}")
	@DELETE
	public Response deleteItem(@Context HttpHeaders headers, @PathParam("id") String id) {
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
		db.closeConnection();
		return Response.ok().build();
	}
	
	/**
	 * Metoda s³u¿¹ca do pobierania komentarzy przypisanych do elementu o zadanym identyfikatorze.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param projectid ID elementu, którego komentarze maj¹ zostaæ przes³ane
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
	 * @see Response
	 */
	@Path("{id}/comments")
	@GET
	public Response getComments(@Context HttpHeaders headers, @PathParam("id") String id) {
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
		List<CommentDto> list = new ArrayList<CommentDto>();
		for(Comment cmt: lst) {
			list.add(new CommentDto(cmt));
		}
		db.closeConnection();
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Metoda s³u¿¹ca do dodawania komentarza do elementu o zadanym identyfikatorze.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param projectid ID projektu, do którego ma zostaæ przypisany komentarz
	 * @param it treœæ komentarza
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
	 * @see Response
	 */
	@Path("{id}/comments")
	@POST
	public Response newComment(@Context HttpHeaders headers, @PathParam("id") String id, String it) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		JSONObject obj = new JSONObject(it);
		Comment cmt = new Comment();
		cmt.setContent(obj.getString("content"));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date date = null;
		try {
			date = format.parse(obj.getString("created"));
		}
		catch (ParseException e) {
			
		}
		cmt.setCreated(date);
		cmt.setUser(usr);
		cmt.setItem(db.getItem(Integer.parseInt(id)));
		db.newComment(cmt);
		db.closeConnection();
		return Response.ok().build();
	}
	
	/**
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /items.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
	
	/**
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /items/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
