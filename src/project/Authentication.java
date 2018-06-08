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
 * Klasa udost�pniaj�ca endpointy do obs�ugi rz�da� zwi�zanych z u�ytkownikami
 */
@Path("/users")
public class Authentication extends Application {

	/**
	 * Metoda s�u��ca do uwierzytelniania u�ytkownik�w.
	 * @param usr sparsowany obiekt typu User, w kt�rym zapisane s� dane u�ytkownika
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
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
	 * Metoda s�u��ca do rejestracji u�ytkownik�w.
	 * @param usr sparsowany obiekt typu User, w kt�rym zapisane s� dane u�ytkownika
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 409 CONFLICT je�eli podane dane logowania nie spe�niaj� wymaga�
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
	 * Metoda s�u��ca do pobierania u�ytkownika o zadanym numerze identyfikacyjnym.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @param id ID u�ytkownika, kt�rego dane maj� zosta� zwr�cone
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
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
	 * Metoda s�u��ca do pobierania zarejestrowanych u�ytkownik�w.
	 * @param headers header rz�dania pobierany z kontekstu
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania lub 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika
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
	 * Metoda s�u��ca do obs�ugi rz�dania typu OPTIONS dla endpointu /users.
	 */
	@OPTIONS
	public Response getOptions() {
    	return Response.ok().header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS")
    			.header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build(); 
    	}
}
