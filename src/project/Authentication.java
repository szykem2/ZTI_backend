package project;
import javax.ws.rs.*;

import javax.ws.rs.core.*;
import project.dbutils.*;
import project.dto.UserDto;
import project.exception.HeaderException;
import project.models.*;
import project.utils.*;
import project.validators.HeaderValidator;

import java.util.*;

/**
 * Klasa udostêpniaj¹ca endpointy do obs³ugi rz¹dañ zwi¹zanych z u¿ytkownikami
 */
@Path("/users")
public class Authentication extends Application {

	/**
	 * Metoda s³u¿¹ca do uwierzytelniania u¿ytkowników.
	 * @param usr sparsowany obiekt typu User, w którym zapisane s¹ dane u¿ytkownika
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
	 * @see Response
	 */
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(User usr) {
		Database db = new Database();
		User user = db.authorize(usr.getLogin(), usr.getPassword());
		db.closeConnection();
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
		}
		Token tkn = Token.generateToken(user);
		return Response.ok(tkn, MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Metoda s³u¿¹ca do rejestracji u¿ytkowników.
	 * @param usr sparsowany obiekt typu User, w którym zapisane s¹ dane u¿ytkownika
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 409 CONFLICT je¿eli podane dane logowania nie spe³niaj¹ wymagañ
	 * @see Response
	 */
	@POST
	@Path("/register")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response register(User usr) {
		Database db = new Database();
		db.newUser(usr);
		db.closeConnection();
		return Response.ok().build();
	}
	
	/**
	 * Metoda s³u¿¹ca do pobierania u¿ytkownika o zadanym numerze identyfikacyjnym.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @param id ID u¿ytkownika, którego dane maj¹ zostaæ zwrócone
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
		db.closeConnection();
        return Response.ok(new UserDto(us), MediaType.APPLICATION_JSON).build();
    }

	/**
	 * Metoda s³u¿¹ca do pobierania zarejestrowanych u¿ytkowników.
	 * @param headers header rz¹dania pobierany z kontekstu
	 * @return odpowiedŸ serwera HTML status code 200 OK dla poprawnego rz¹dania lub 401 UNAUTHORIZED je¿eli nie uda³o siê uwierzytelniæ u¿ytkownika
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
        usr = lst.get(0);//just to instantiate the list due to jpa lazy binding
        List<UserDto> l = new ArrayList<UserDto>();
        for(User u : lst) {
        	l.add(new UserDto(u));
        }
        db.closeConnection();
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
    }
	
	/**
	 * Metoda s³u¿¹ca do obs³ugi rz¹dania typu OPTIONS dla endpointu /users.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
