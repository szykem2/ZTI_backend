package project.utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

@Path("/")
public class EmailValidator extends Application {
	@POST
	@Path("/validateEmail")
	@Consumes({MediaType.APPLICATION_JSON})
	public void emailValidator() {
		
	}
}
