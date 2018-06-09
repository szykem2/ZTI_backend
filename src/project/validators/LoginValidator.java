package project.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

/*
 * Klasa definiuj�ca endpoint s�u��cy do walidacji loginu
 */
@Path("/")
public class LoginValidator extends Application {

	/*
	 * Metoda s�u��ca do sprawdzania poprawno�ci loginu zar�wno zgodno�ci z wymaganiami jak i dost�pno�ci w bazie.
	 * @param login login u�ytkownika podlegaj�cy walidacji
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania, 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika lub 409 FORBIDDEN je�eli nie uda�o si�  autoryzowa� u�ytkownika
	 * @see Response
	 */
	@POST
	@Path("/validateLogin")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response loginValidator(String login) {
		String lgn = Json.createReader(new ByteArrayInputStream(login.getBytes(StandardCharsets.UTF_8))).readObject().getString("login");
		if(!LoginValidator.validate(lgn)) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		Database db = new Database();
		if (db.validateLogin(lgn)) {
			return Response.ok().build();
		}
		else {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
	
	/*
	 * Metoda s�u��ca do sprawdzania zgodno�ci loginu z przyj�tymi wymaganiami.
	 * @param login login u�ytkownika podlegaj�cy walidacji
	 * @return warto�� logiczna m�wi�ca o poprawno�ci nazwy u�ytkownika
	 * @see Response
	 */
	private static boolean validate(String login) {
		return login.length() > 30 || login.length() < 5;
	}
}
