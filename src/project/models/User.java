package project.models;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int userID;
	private String login;
	private String email;
	private String password;
	
	@ManyToMany(targetEntity=User.class)
	@JoinTable(name = "admins", joinColumns = @JoinColumn(name = "userID"))
	private List<Project> isAdminFor;
	
	@ManyToMany(targetEntity=User.class)
	@JoinTable(name = "users_projects", joinColumns = @JoinColumn(name = "userID"))
	private List<Project> isAuthorized;
	
	public void setLogin(String login) {
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void setIsAdminFor(List<Project> admins) {
		this.isAdminFor = admins;
	}
	
	public List<Project> getISAdminFor() {
		return isAdminFor;
	}
	
	public void setIsAuthorized(List<Project> authorized) {
		this.isAuthorized = authorized;
	}
	
	public List<Project> getIsAuthorized() {
		return isAuthorized;
	}
}
