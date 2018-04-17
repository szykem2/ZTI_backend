package project.utils;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import project.dbutils.Database;

@Path("/")
public class EmailValidator extends Application {
	@POST
	@Path("/validateEmail")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response emailValidator(String email) {
		System.out.println(email);
		String em = Json.createReader(new ByteArrayInputStream(email.getBytes(StandardCharsets.UTF_8))).readObject().getString("email");
		System.out.println(em);
		Database db = new Database();
		if (db.validateEmail(em)) {
			return Response.ok().build();
		}
		else {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
}
