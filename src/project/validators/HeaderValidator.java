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
 * Klasa udost�pnianj�ca interfejs do walidacji nag��wk�w rz�dania
 */
public class HeaderValidator {
	/*
	 * Metoda waliduj�ca nag��wek rz�dania
	 * @param header nag��wek rz�dania
	 * @return obiekt u�ytkownika, do kt�remu odpowiada token uwierzytelniaj�cy przes�any w nag��wku
	 * @throws HeaderException gdy token uwierzytelniaj�cy nie zosta� podany, jest b��dny lub straci� wa�no��
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
