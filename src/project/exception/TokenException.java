package project.exception;
/**
 * Klasa definiuj�ca wyj�tek rzucany w przypadku problemu z parsowaniem tokena uwierzytelniaj�cego
 * @see Throwable
 */
public class TokenException extends Throwable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Konstruktor wyj�tku
	 * @param msg przekazywana wiadomo��
	 */
	public TokenException(String msg) {
		super(msg);
	}
}
