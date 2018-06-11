package project.validators;

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
 * Klasa definiująca endpoint służący do walidacji loginu
 */
@Path("/")
public class LoginValidator extends Application {

	/*
	 * Metoda służąca do sprawdzania poprawności loginu zarówno zgodności z wymaganiami jak i dostępności w bazie.
	 * @param login login użytkownika podlegający walidacji
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania lub 409 FORBIDDEN jeżeli nie udało się  autoryzować użytkownika
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
	 * Metoda służąca do sprawdzania zgodności loginu z przyjętymi wymaganiami.
	 * @param login login użytkownika podlegający walidacji
	 * @return wartość logiczna mówiąca o poprawności nazwy użytkownika
	 * @see Response
	 */
	private static boolean validate(String login) {
		return login.length() < 30 && login.length() > 5;
	}
}