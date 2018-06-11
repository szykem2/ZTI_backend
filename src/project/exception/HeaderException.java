package project.exception;

import javax.ws.rs.core.Response;
/**
 * Klasa definiująca wyjątek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniającego
 * @see Throwable
 */
public class HeaderException extends Throwable{
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * obiekt typu Response, który ma zostać zwrócony jako odpowiedź do klienta
	 */
	
	private Response resp;
	/**
	 * Konstruktor wyjątku
	 * @param resp obiekt typu Response, który ma zostać zwrócony jako odpowiedź do klienta
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