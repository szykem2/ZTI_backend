package project.exception;

import javax.ws.rs.core.Response;
/**
 * Klasa definiuj�ca wyj�tek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniaj�cego
 * @see Throwable
 */
public class HeaderException extends Throwable{
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * obiekt typu Response, kt�ry ma zosta� zwr�cony jako odpowied� do klienta
	 */
	
	private Response resp;
	/**
	 * Konstruktor wyj�tku
	 * @param resp obiekt typu Response, kt�ry ma zosta� zwr�cony jako odpowied� do klienta
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
