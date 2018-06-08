package project.dto;

import java.io.Serializable;
import project.models.User;

public class UserDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String login;
	private String email;
	
	public UserDto(User u) {
		this.setEmail(u.getEmail());
		this.setId(u.getUserid());
		this.setLogin(u.getLogin());
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
