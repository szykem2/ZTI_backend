package project;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import project.dbutils.Database;
import project.dto.ProjectDto;
import project.dto.UserDto;
import project.exception.HeaderException;
import project.models.*;
import project.validators.HeaderValidator;

import java.util.*;

/**
 * Klasa endpointu /projects dająca funkcjonalność obsługi projektów zarówno przez zwykłych użytkowników jak i administratorów
 */
@Path("/projects")
public class Projects {
	
	/**
	 * Metoda służąca do pobierania listy projektów, do których wglądu zalogowany użytkownik jest autoryzowany.
	 * @param headers header rządania pobierany z kontekstu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getProjects(@Context HttpHeaders headers) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			System.out.println(e.getResponse().toString());
			return e.getResponse();
		}
		List<Project> lst = usr.getProjects();
		System.out.println("List: " + lst.size());
		List<ProjectDto> list = new ArrayList<ProjectDto>();
		for(Project p: lst) {
			ProjectDto pr = new ProjectDto(p);
			if(usr.getIsAdmin().contains(p)) {
				pr.setIsAdmin(true);
			}
			list.add(pr);
		}
		return Response.ok(list, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Metoda służąca do dodawania nowego projektu. Użytkownik, który tworzy projekt automatycznie staje się jego administratorem.
	 * @param headers header rządania pobierany z kontekstu
	 * @param pr sparsowany obiekt typu Project przesłany z aplikacji klienta
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addProject(@Context HttpHeaders headers, Project pr) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		List<Project> lst = db.getProjects();
		lst.size();
		for(Project p : lst) {
			if(p.getName().equals(pr.getName())) {
				return Response.status(Response.Status.CONFLICT).build();
			}
		}
		db.newProject(usr, pr);
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do usuwania projektu. Wszyscy użytkownicy są automatycznie usuwani z projektu, elementy i komentarze są usuwane.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, który ma zostać usunięty
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@Path("{id}")
	@DELETE
	public Response deleteProject(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.removeProject(Integer.parseInt(id));
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do pobierania użytkowników autoryzowanych do wyświetlenia projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, dla którego należy zwócić przypisanych użytkowników 
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@Path("{id}/users")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		usr.getProjects().size();
		if(!usr.getProjects().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        List<User> lst = pr.getUsers();
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
	 * Metoda służąca do dodawania autoryzacji dla użytkownika.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, do którego należy dodać użytkownika 
	 * @param usr sparsowany obiekt typu User, który zawiera dane identyfikacyjne użytkownika, którego należy dodać do projektu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@Path("{id}/users")
	@POST
	public Response addUser(@Context HttpHeaders headers, @PathParam("id") String id, User usr) {
		User user = null;
		try {
			user = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!user.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        db.addUserToProject(pr, usr);
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do pobierania administratorów wyświetlanego projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, dla którego należy zwócić przypisanych administratorów 
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@Path("{id}/admins")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getAdmins(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();

        Project pr = db.getProject(Integer.parseInt(id));
        List<User> lst = pr.getAdmins();
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
	 * Metoda służąca do dodawania użytkownika jako administratora.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, do którego należy przypisać nowego administratora 
	 * @param usr sparsowany obiekt typu User, który zawiera dane identyfikacyjne użytkownika, którego należy dodać do projektu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@Path("{id}/admins")
	@POST
	public Response addAdmin(@Context HttpHeaders headers, @PathParam("id") String id, User usr) {
		User user = null;
		try {
			user = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!user.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
        Project pr = db.getProject(Integer.parseInt(id));
        db.addAdminToProject(pr, usr);
		return Response.ok().build();
	}

	/**
	 * Metoda służąca do usuwania użytkownika z projektu.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID projektu, z którego należy usunąć użytkownika 
	 * @param userid ID użytkownika, którego należy usunąć z zadanego projektu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 403 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@Path("{id}/users/{userid}")
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	public Response reomveUser(@Context HttpHeaders headers, @PathParam("id") String id, @PathParam("userid") String userid) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		if(!usr.getIsAdmin().contains(db.getProject(Integer.parseInt(id)))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		db.removeUserFromProject(Integer.parseInt(id), Integer.parseInt(userid));
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /projects.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /projects/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}