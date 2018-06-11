package project.exception;
/**
 * Klasa definiująca wyjątek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniającego
 * @see Throwable
 */
public class TokenException extends Throwable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Konstruktor wyjątku
	 * @param msg przekazywana wiadomość
	 */
	public TokenException(String msg) {
		super(msg);
	}
}