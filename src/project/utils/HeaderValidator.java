package project.utils;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import project.models.User;

public class HeaderValidator {
	public static User validate(HttpHeaders header) throws HeaderException{
		MultivaluedMap<String, String> rh = header.getRequestHeaders();
	    List<String> l = rh.get("Authorization");
	    User usr = null;
	    try {
	    	usr = Token.decodeToken(l.get(0));
	    }
	    catch(TokenException e) {
	    	System.out.println(e.getMessage());
	    	if (e.getMessage().equalsIgnoreCase("Token invalid")) {
	    		System.out.println("dupsko");
	    		throw new HeaderException(Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build());
	    	}
	    	else if(e.getMessage().equalsIgnoreCase("Token expired")) {
	    		throw new HeaderException(Response.status(Response.Status.FORBIDDEN).entity("Token expired").build());
	    	}
	    }
	    catch(NullPointerException e) {
	    	System.out.println(e.getMessage());
	    	throw new HeaderException(Response.status(Response.Status.FORBIDDEN).entity("No Token provided").build());
	    }
	    return usr;
	}
}
