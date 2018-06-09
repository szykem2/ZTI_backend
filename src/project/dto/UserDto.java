package project.dto;

import java.io.Serializable;
import project.models.User;

/**
 * Klasa s³u¿¹ca do przesy³ania obiektu u¿ytkownika
 */
public class UserDto implements Serializable{

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID u¿ytkownika
	 */
	private int id;

	/**
	 * nazwa u¿ytkownika
	 */
	private String login;

	/**
	 * adres email u¿ytkownika
	 */
	private String email;

	/**
	 * Konstruktor obiektu
	 * @param u obiekt u¿ytkownika, który ma zostaæ przes³any
	 * @see project.models.User
	 */
	public UserDto(User u) {
		if(u == null) {
			this.setEmail("");
			this.setId(-1);
			this.setLogin("unassigned");
			return;
		}
		this.setEmail(u.getEmail());
		this.setId(u.getUserid());
		this.setLogin(u.getLogin());
	}

	/**
	 * {@link UserDto#email}
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * {@link UserDto#email}
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * {@link UserDto#login}
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * {@link UserDto#login}
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * {@link UserDto#id}
	 */
	public int getId() {
		return id;
	}

	/**
	 * {@link UserDto#id}
	 */
	public void setId(int id) {
		this.id = id;
	}
}
