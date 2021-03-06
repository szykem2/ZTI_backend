﻿package project;

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
import java.util.*;

/**
 * Klasa udostępniająca endpointy do obsługi elementów projektów
 */
@Path("/items")
public class Items {

	/**
	 * Metoda służąca do pobierania elementów przypisanych do projektu o zadanym identyfikatorze.
	 * @param headers header rządania pobierany z kontekstu
	 * @param projectid ID projektu, którego elementy mają zostać przesłane
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
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
		lst.size();
		if(lst.size() > 0) {
			Item cmt = lst.get(0);
		}
		List<ItemDto> list = new ArrayList<ItemDto>();
		for(Item it: lst) {
			list.add(new ItemDto(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda służąca do pobierania statusów, które można przypisać elementom.
	 * @return odpowiedź serwera HTML status code 200 OK
	 * @see Response
	 */
	@Path("itemstatus")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getStatuses() {
		Database db = new Database();
		List<Itemstatus> lst = db.getStatuses();
		lst.size();
		if(lst.size() > 0) {
			Itemstatus cmt = lst.get(0);
		}
		List<ItemStatusDto> list = new ArrayList<ItemStatusDto>();
		for(Itemstatus it: lst) {
			list.add(new ItemStatusDto(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda służąca do pobierania typów, które można przypisać elementom.
	 * @return odpowiedź serwera HTML status code 200 OK
	 * @see Response
	 */
	@Path("itemtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getTypes() {
		Database db = new Database();
		List<Itemtype> lst = db.getTypes();
		lst.size();
		if(lst.size() > 0) {
			Itemtype cmt = lst.get(0);
		}
		List<ItemTypeDto> list = new ArrayList<ItemTypeDto>();
		for(Itemtype it: lst) {
			list.add(new ItemTypeDto(it));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda służąca do dodawania nowego elementu do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param it obiekt JSON, który przechowuje składowe elementu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli użytkownik nie jest autoryzowany do zadanego projektu
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
		if(obj.getString("approver").equals("ua")) {
			item.setOwner(null);
		}
		else {
			item.setOwner(db.getUser(obj.getInt("approver")));
		}
		item.setCreationdate(Timestamp.valueOf(obj.getString("creationDate")));
		item.setDescription(obj.getString("description"));
		item.setItemstatus(db.getStatus(1));
		item.setItemtype(db.getType(obj.getInt("type")));
		if(obj.getString("owner").equals("ua")) {
			item.setOwner(null);
		}
		else {
			item.setOwner(db.getUser(obj.getInt("owner")));
		}
		item.setProject(pr);
		item.setResolutiondate(null);
		item.setResolved(false);
		item.setTitle(obj.getString("title"));
		db.newItem(item);
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do aktualizowania elementu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param itemid ID elementu, który ma zostać zaktualizowany
	 * @param it obiekt JSON, który przechowuje składowe elementu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli użytkownik nie jest autoryzowany do zadanego projektu
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
		String msg = usr.getLogin() + " made some changes:\n";
		if((item.getApprover() == null && !obj.getString("approver").equals("ua")) || (!obj.getString("approver").equals("ua") && (item.getApprover().getUserid() != obj.getInt("approver")))) {
			String lgn = "unassigned";
			if(item.getApprover() != null) {
				lgn = item.getApprover().getLogin();
			}
			msg += "Changed approver: " + lgn + " -> " + db.getUser(obj.getInt("approver")).getLogin() + "\n";
			item.setApprover(db.getUser(obj.getInt("approver")));
		}
		item.setCreationdate(Timestamp.valueOf(obj.getString("creationdate")));
		
		if(!item.getDescription().equals(obj.getString("description"))) {
			msg += "Changed description: " + item.getDescription() + " -> " + obj.getString("description") + "\n";
		}
		item.setDescription(obj.getString("description"));
		
		if(item.getItemtype().getTypeid() != obj.getInt("itemtype")) {
			msg += "Changed type: " + item.getItemtype().getType() + " -> " + db.getType(obj.getInt("itemtype")).getType() + "\n";
		}
		item.setItemtype(db.getType(obj.getInt("itemtype")));
		
		if(item.getItemstatus().getStatusid() != obj.getInt("itemstatus")) {
			msg += "Changed type: " + item.getItemstatus().getStatus() + " -> " + db.getStatus(obj.getInt("itemstatus")).getStatus() + "\n";
		}
		item.setItemstatus(db.getStatus(obj.getInt("itemstatus")));
		
		if((item.getOwner() == null && !obj.getString("owner").equals("ua")) || (!obj.getString("owner").equals("ua") && (item.getOwner().getUserid() != obj.getInt("owner")))) {
			String lgn = "unassigned";
			if(item.getOwner() != null) {
				lgn = item.getOwner().getLogin();
			}
			msg += "Changed owner: " + lgn + " -> " + db.getUser(obj.getInt("owner")).getLogin() + "\n";
			item.setOwner(db.getUser(obj.getInt("owner")));
		}
		
		String date = obj.optString("resolutiondate");
		if (date.equals("")) {
			item.setResolutiondate(null);
		}
		else {
			item.setResolutiondate(Timestamp.valueOf(date));
		}
		item.setTitle(obj.getString("title"));
		db.updateItem(item);
		Comment cmt = new Comment();
		cmt.setContent(msg);
		cmt.setCreated(new Timestamp(System.currentTimeMillis()));
		cmt.setItem(item);
		cmt.setUser(usr);
		db.newComment(cmt);
		return Response.ok().build();
	}
	/**
	 * Metoda służąca do dodawania nowego elementu do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID elementu, który ma zostać usunięty
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli użytkownik nie jest autoryzowany do zadanego projektu
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
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do pobierania komentarzy przypisanych do elementu o zadanym identyfikatorze.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID elementu, którego komentarze mają zostać przesłane
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
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
		if(lst.size() > 0) {
			Comment cmt = lst.get(0);
		}
		List<CommentDto> list = new ArrayList<CommentDto>();
		for(Comment cmt: lst) {
			list.add(new CommentDto(cmt));
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Metoda służąca do dodawania komentarza do elementu o zadanym identyfikatorze.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID elementu, do którego ma zostać przypisany komentarz
	 * @param it treść komentarza
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
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
		Timestamp date = null;
		date = Timestamp.valueOf(obj.getString("created"));

		Comment cmt = new Comment();
		cmt.setContent(obj.getString("content"));
		cmt.setCreated(date);
		cmt.setUser(usr);
		cmt.setItem(db.getItem(Integer.parseInt(id)));
		db.newComment(cmt);
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /items.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /items/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}