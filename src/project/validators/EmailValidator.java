package project.validators;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * Metoda s�u��ca do sprawdzania poprawno�ci adresu email zar�wno poprawno�ci sk�adniowej i dost�pno�ci w bazie.
	 * @param login login u�ytkownika podlegaj�cy walidacji
	 * @return odpowied� serwera HTML status code 200 OK dla poprawnego rz�dania, 401 UNAUTHORIZED je�eli nie uda�o si� uwierzytelni� u�ytkownika lub 409 COINFLICT je�eli nie uda�o si�  autoryzowa� u�ytkownika
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
	 * Metoda s�u��ca do sprawdzania poprawno�ci sk�adniowej adresu email.
	 * @param email email u�ytkownika podlegaj�cy walidacji
	 * @return warto�� logiczna m�wi�ca o poprawno�ci adresu email
	 * @see Response
	 */
	private static boolean validate(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		return email.matches(regex);
	}
}
