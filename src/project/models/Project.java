package project.models;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="projects")
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="projectid")
	private int projectID;
	private String name;
	private String description;
	
	/*@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "document_inbox", joinColumns = @JoinColumn(name = "userinbox_id"),
               inverseJoinColumns = @JoinColumn(name = "inbox_id"))
    private List<User> userinbox  = new ArrayList<User>();
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "document_outbox", joinColumns = @JoinColumn(name = "useroutbox_id"),
               inverseJoinColumns = @JoinColumn(name = "outbox_id"))
    private List<User> useroutbox  = new ArrayList<User>();*/
	
	@ManyToMany(targetEntity=User.class)
	@JoinTable(name = "admins", joinColumns = @JoinColumn(name = "userID"))
	private List<User> admins = new ArrayList<>();
	
	@ManyToMany(targetEntity=User.class)
	@JoinTable(name = "users_projects", joinColumns = @JoinColumn(name = "userID"))
	private List<User> authorizedUsers = new ArrayList<>();
	
	public int getProjectId() {
	      return projectID;
	}

	public void setProjectId(int id) {
		this.projectID = id;
	}
	
	public String getName( ){
		return name;
	}

	public void setName( String name ){
		this.name = name;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setAdmins(List<User> admins) {
		this.admins = admins;
	}
	
	public List<User> getAdmins() {
		return admins;
	}
	
	public void setAuthorizedUsers(List<User> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}
	
	public List<User> getAuthorizedUsers() {
		return authorizedUsers;
	}
}
