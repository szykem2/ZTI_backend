package project.exception;

import javax.ws.rs.core.Response;
/**
 * Klasa definiuj¹ca wyj¹tek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniaj¹cego
 * @see Throwable
 */
public class HeaderException extends Throwable{
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * obiekt typu Response, który ma zostaæ zwrócony jako odpowiedŸ do klienta
	 */
	
	private Response resp;
	/**
	 * Konstruktor wyj¹tku
	 * @param resp obiekt typu Response, który ma zostaæ zwrócony jako odpowiedŸ do klienta
	 */
	
	public HeaderException(Response resp ) {
		this.resp = resp;
	}
	/**
	 * {@link HeaderException#resp}
	 */
	public Response getResponse() {
		return resp;
	}
}
