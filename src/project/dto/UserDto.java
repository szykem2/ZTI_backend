package project.dto;

import java.io.Serializable;

import project.models.Itemstatus;
import project.models.User;

/**
 * Klasa służąca do przesyłania obiektu użytkownika
 */
public class UserDto implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID użytkownika
	 */
	private int id;

	/**
	 * nazwa użytkownika
	 */
	private String login;

	/**
	 * adres email użytkownika
	 */
	private String email;

	/**
	 * Konstruktor obiektu
	 * @param u obiekt użytkownika, który ma zostać przesłany
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

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu UserDto
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id == ((UserDto)obj).getId();
	}
}