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
import project.dto.ProjectDto;
import project.dto.UserDto;
import project.exception.HeaderException;
import project.models.Project;
import project.models.User;
import project.validators.HeaderValidator;

/**
 * Klasa endpointu /request daj�ca funkcjonalno�� obs�ugi pr�b o dost�p do projektu
 */
@Path("/requests")
public class Requests {
	
	/**
	 * Metoda s�u��ca do dodawania pro�by o do��czenie do projektu.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @param id ID projektu, o dost�p do kt�rego sk�adana jest pro�ba
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
	 * @see Response
	 */
	@Path("{id}")
	@POST
	public Response requestAccess(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		db.newRequestor(usr, Integer.parseInt(id));
		db.closeConnection();
		return Response.ok().build();
	}
	
	/**
	 * Metoda s�u��ca do pobierania pr�b o do��czenie do projektu.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @param id ID projektu, o dost�p do kt�rego sk�adana jest pro�ba
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
	 * @see Response
	 */
	@Path("{id}")
	@GET
	public Response getRequestors(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		//TODO: check if user is admin of the project
		Database db = new Database();
		List<User> lst = db.getRequested(usr, Integer.parseInt(id));
		List<UserDto> l = new ArrayList<UserDto>();
        if(lst.size() > 0) {
        	usr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        for(User u : lst) {
        	l.add(new UserDto(u));
        }
        db.closeConnection();
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda s�u��ca do pobierania projekt�w, o dost�p do kt�rych u�ytkownik mo�e prosi�.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
	 * @see Response
	 */
	@GET
	public Response getProjectsForRequest(@Context HttpHeaders headers) {
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
		List<ProjectDto> l = new ArrayList<ProjectDto>();
        if(lst.size() > 0) {
        	Project pr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        for(Project pr : lst) {
        	if(!ulst.contains(pr)) {
        		l.add(new ProjectDto(pr));
        	}
        }
        db.closeConnection();
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda s�u��ca do akceptacji pro�by o do��czenie do projektu.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @param id ID projektu, o dost�p do kt�rego sk�adana jest pro�ba
	 * @param userid ID projektu, o dost�p do kt�rego sk�adana jest pro�ba
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
	 * @see Response
	 */
	@Path("{id}/{userid}")
	@POST
	public Response acceptRequest(@Context HttpHeaders headers, @PathParam("id") String id, @PathParam("userid") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		//TODO: check if user is admin
		Database db = new Database();
		db.acceptRequest(Integer.parseInt(id), Integer.parseInt(userid));
		db.closeConnection();
		return Response.ok().build();
	}

	/**
	 * Metoda s�u��ca do odrzucania pro�by o do��czenie do projektu.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @param id ID projektu, o dost�p do kt�rego sk�adana jest pro�ba
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� lub autoryzowa� u�ytkownika
	 * @see Response
	 */
	@Path("{id}/{userid}")
	@DELETE
	public Response denyAccess(@Context HttpHeaders headers, @PathParam("id") String id, @PathParam("id") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		//TODO: check if user is admin
		db.denyAccess(db.getUser(Integer.parseInt(userid)), Integer.parseInt(id));
		db.closeConnection();
		return Response.ok().build();
	}
	
	/**
	 * Metoda s�u��ca do obs�ugi rz�dania typu OPTIONS dla endpointu /requests.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	/**
	 * Metoda s�u��ca do obs�ugi rz�dania typu OPTIONS dla endpointu /requests/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}
