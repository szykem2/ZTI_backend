package project;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ibm.db2.jcc.t2zos.db;

import project.dbutils.Database;
import project.dto.ProjectDto;
import project.dto.UserDto;
import project.exception.HeaderException;
import project.models.*;
import project.utils.*;
import project.validators.HeaderValidator;

import java.util.*;

/**
 * Klasa endpointu /projects daj¹ca funkcjonalnoœæ obs³ugi projektów zarówno przez zwyk³ych u¿ytkowników jak i administratorów
 */
@Path("/projects")
public class Projects {
	
	/**
	 * Metoda s³u¿¹ca do pobierania listy projektów, do których wgl¹du zalogowany u¿ytkownik jest autoryzowany.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do dodawania nowego projektu. U¿ytkownik, który tworzy projekt automatycznie staje siê jego administratorem.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param pr sparsowany obiekt typu Project przes³any z aplikacji klienta
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do usuwania projektu. Wszyscy u¿ytkownicy s¹ automatycznie usuwani z projektu, elementy i komentarze s¹ usuwane.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, który ma zostaæ usuniêty
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do pobierania u¿ytkowników autoryzowanych do wyœwietlenia projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, dla którego nale¿y zwóciæ przypisanych u¿ytkowników 
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do dodawania autoryzacji dla u¿ytkownika.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, do którego nale¿y dodaæ u¿ytkownika 
	 * @param usr sparsowany obiekt typu User, który zawiera dane identyfikacyjne u¿ytkownika, którego nale¿y dodaæ do projektu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do pobierania administratorów wyœwietlanego projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, dla którego nale¿y zwóciæ przypisanych administratorów 
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do dodawania u¿ytkownika jako administratora.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, do którego nale¿y przypisaæ nowego administratora 
	 * @param usr sparsowany obiekt typu User, który zawiera dane identyfikacyjne u¿ytkownika, którego nale¿y dodaæ do projektu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do usuwania u¿ytkownika z projektu.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID projektu, z którego nale¿y usun¹æ u¿ytkownika 
	 * @param userid ID u¿ytkownika, którego nale¿y usun¹æ z zadanego projektu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania, 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika lub 403 FORBIDDEN je¿eli nie uda³o siê  autoryzowaæ u¿ytkownika
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
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /projects.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
	
	/**
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /projects/{id}.
	 */
	@Path("{id}")
	@OPTIONS
	public Response getOptionsl() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
	}
}
