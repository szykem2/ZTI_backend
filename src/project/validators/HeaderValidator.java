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
 * Klasa udostêpnianj¹ca interfejs do walidacji nag³ówków rz¹dania
 */
public class HeaderValidator {
	/*
	 * Metoda waliduj¹ca nag³ówek rz¹dania
	 * @param header nag³ówek rz¹dania
	 * @return obiekt u¿ytkownika, do któremu odpowiada token uwierzytelniaj¹cy przes³any w nag³ówku
	 * @throws HeaderException gdy token uwierzytelniaj¹cy nie zosta³ podany, jest b³êdny lub straci³ wa¿noœæ
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
