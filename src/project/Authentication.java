package project;
import javax.ws.rs.*;

import javax.ws.rs.core.*;
import project.dbutils.*;
import project.dto.UserDto;
import project.exception.HeaderException;
import project.models.*;
import project.utils.*;
import project.validators.*;

import java.util.*;

/**
 * Klasa udostępniająca endpointy do obsługi rządań związanych z użytkownikami
 */
@Path("/users")
public class Authentication extends Application {

	/**
	 * Metoda służąca do uwierzytelniania użytkowników.
	 * @param usr sparsowany obiekt typu User, w którym zapisane są dane użytkownika
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(User usr) {
		Database db = new Database();
		User user = db.authorize(usr.getLogin(), usr.getPassword());
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
		}
		Token tkn = Token.generateToken(user);
		return Response.ok(tkn, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Metoda służąca do rejestracji użytkowników.
	 * @param usr sparsowany obiekt typu User, w którym zapisane są dane użytkownika
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 409 CONFLICT jeżeli podane dane logowania nie spełniają wymagań
	 * @see Response
	 */
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response register(User usr) {
		LoginValidator v = new LoginValidator();
		Response r = v.loginValidator(usr.getLogin());
		if(r.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
			return Response.status(Response.Status.CONFLICT).entity("Invalid login").build();
		}
		
		EmailValidator vd = new EmailValidator();
		r = vd.emailValidator(usr.getEmail());
		if(r.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
			return Response.status(Response.Status.CONFLICT).entity("Invalid email").build();
		}
		
		Database db = new Database();
		db.newUser(usr);
		return Response.ok().build();
	}
	
	/**
	 * Metoda służąca do pobierania użytkownika o zadanym numerze identyfikacyjnym.
	 * @param headers header rządania pobierany z kontekstu
	 * @param id ID użytkownika, którego dane mają zostać zwrócone
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@Context HttpHeaders headers, @PathParam("id") String id) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
		Database db = new Database();
		User us = db.getUser(Integer.parseInt(id));
        return Response.ok(new UserDto(us), MediaType.APPLICATION_JSON).build();
    }

	/**
	 * Metoda służąca do pobierania zarejestrowanych użytkowników.
	 * @param headers header rządania pobierany z kontekstu
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika
	 * @see Response
	 */
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@Context HttpHeaders headers) {
		User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
			return e.getResponse();
		}
        Database db = new Database();
        List<User> lst = db.getUsers();
        lst.size();
        usr = lst.get(0);
        List<UserDto> l = new ArrayList<UserDto>();
        for(User u : lst) {
        	l.add(new UserDto(u));
        }
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
    }
	
	/**
	 * Metoda służąca do obsługi rządania typu OPTIONS dla endpointu /users.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}