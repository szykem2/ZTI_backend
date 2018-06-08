package project.exception;
/**
 * Klasa definiuj¹ca wyj¹tek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniaj¹cego
 * @see Throwable
 */
public class TokenException extends Throwable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Konstruktor wyj¹tku
	 * @param msg przekazywana wiadomoœæ
	 */
	public TokenException(String msg) {
		super(msg);
	}
}
