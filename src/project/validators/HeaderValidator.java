package project.validators;

import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import project.exception.HeaderException;
import project.exception.TokenException;
import project.models.User;
import project.utils.Token;
/*
 * Klasa udostępnianjąca interfejs do walidacji nagłówków rządania
 */
public class HeaderValidator {
	/*
	 * Metoda walidująca nagłówek rządania
	 * @param header nagłówek rządania
	 * @return obiekt użytkownika, do któremu odpowiada token uwierzytelniający przesłany w nagłówku
	 * @throws HeaderException gdy token uwierzytelniający nie został podany, jest błędny lub stracił ważność
	 */
	public static User validate(HttpHeaders header) throws HeaderException{
		MultivaluedMap<String, String> rh = header.getRequestHeaders();
	    List<String> l = rh.get("Authorization");
	    User usr = null;
	    try {
	    	usr = Token.decodeToken(l.get(0));
	    }
	    catch(TokenException e) {
	    	if (e.getMessage().equalsIgnoreCase("Token invalid")) {
	    		throw new HeaderException(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
	    	}
	    	else if(e.getMessage().equalsIgnoreCase("Token expired")) {
	    		throw new HeaderException(Response.status(Response.Status.UNAUTHORIZED).entity("Token expired").build());
	    	}
	    }
	    catch(NullPointerException e) {
	    	throw new HeaderException(Response.status(Response.Status.UNAUTHORIZED).entity("No Token provided").build());
	    }
	    return usr;
	}
}