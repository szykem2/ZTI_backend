package project.validators;

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
	
	/*
	 * Metoda służąca do sprawdzania poprawności adresu email zarówno poprawności składniowej i dostępności w bazie.
	 * @param login login użytkownika podlegający walidacji
	 * @return odpowiedź serwera HTML status code 200 OK dla poprawnego rządania, 401 UNAUTHORIZED jeżeli nie udało się uwierzytelnić użytkownika lub 409 COINFLICT jeżeli nie udało się  autoryzować użytkownika
	 * @see Response
	 */
	@POST
	@Path("/validateEmail")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response emailValidator(String email) {
		String em = Json.createReader(new ByteArrayInputStream(email.getBytes(StandardCharsets.UTF_8))).readObject().getString("email");
		if(!EmailValidator.validate(em)) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		Database db = new Database();
		if (db.validateEmail(em)) {
			return Response.ok().build();
		}
		else {
			return Response.status(Response.Status.CONFLICT).build();
		}
	}
	
	/*
	 * Metoda służąca do sprawdzania poprawności składniowej adresu email.
	 * @param email email użytkownika podlegający walidacji
	 * @return wartość logiczna mówiąca o poprawności adresu email
	 * @see Response
	 */
	private static boolean validate(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		return email.matches(regex);
	}
}