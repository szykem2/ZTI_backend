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
 * Klasa endpointu /request daj¹ca funkcjonalnoœæ obs³ugi próœb o dostêp do projektu
 */
@Path("/requests")
public class Requests {
	
	/**
	 * Metoda s³u¿¹ca do dodawania proœby o do³¹czenie do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, o dostêp do którego sk³adana jest proœba
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do pobierania próœb o do³¹czenie do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, o dostêp do którego sk³adana jest proœba
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do pobierania projektów, o dostêp do których u¿ytkownik mo¿e prosiæ.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do akceptacji proœby o do³¹czenie do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, o dostêp do którego sk³adana jest proœba
	 * @param userid ID projektu, o dostêp do którego sk³adana jest proœba
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do odrzucania proœby o do³¹czenie do projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, o dostêp do którego sk³adana jest proœba
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ lub autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /requests.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	/**
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /requests/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}
