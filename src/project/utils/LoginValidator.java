package project.utils;

import javax.ws.rs.Consumes;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.json.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import project.dbutils.Database;

@Path("/")
public class LoginValidator extends Application {
	@POST
	@Path("/validateLogin")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response loginValidator(String login) {
		String lgn = Json.createReader(new ByteArrayInputStream(login.getBytes(StandardCharsets.UTF_8))).readObject().getString("login");
		Database db = new Database();
		if (db.validateLogin(lgn)) {
			db.closeConnection();
			return Response.ok().build();
		}
		else {
			db.closeConnection();
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
}
