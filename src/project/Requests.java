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
 * Klasa endpointu /request dająca funkcjonalność obsługi próśb o dostęp do projektu
 */
@Path("/requests")
public class Requests {
	
	/**
	 * Metoda służąca do dodawania prośby o dołączenie do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, o dostęp do którego składana jest prośba
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
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
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do pobierania prośby o dołączenie do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, o dostęp do którego składana jest prośba
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
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
		Database db = new Database();
		List<User> lst = db.getRequested(usr, Integer.parseInt(id));
		List<UserDto> l = new ArrayList<UserDto>();
        if(lst.size() > 0) {
        	usr = lst.get(0);//just to instantiate list due to jpa lazy binding
        }
        for(User u : lst) {
        	l.add(new UserDto(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda służąca do pobierania projektów, o dostęp do których użytkownik może prosić.
	 * @param headers header rządania pobierany z kontekstu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
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
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Metoda służąca do akceptacji prośby o dołączenie do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, o dostęp do którego składana jest prośba
	 * @param userid ID projektu, o dostęp do którego składana jest prośba
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
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
		Database db = new Database();
		if (usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			db.acceptRequest(Integer.parseInt(id), Integer.parseInt(userid));
		}
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do odrzucania prośby o dołączenie do projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, o dostęp do którego składana prośba jest odrzucana
	 * @param userid ID użytkownika, o którego prośba jest odrzucana
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić lub autoryzować użytkownika
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
		if (usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			db.denyAccess(db.getUser(Integer.parseInt(userid)), Integer.parseInt(id));
		}
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /requests.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /requests/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}