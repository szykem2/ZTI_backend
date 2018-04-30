package project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name="USERS")
@NamedQueries({
@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
@NamedQuery(name="User.findOne", query="SELECT u FROM User u WHERE u.login=:login AND u.password=:pass"),
@NamedQuery(name="User.findEmail", query="SELECT u FROM User u WHERE u.email=:email"),
@NamedQuery(name="User.findLogin", query="SELECT u FROM User u WHERE u.login=:login")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userid;
	private String email;
	private String login;
	private String password;
	private List<Comment> comments;
	private List<Item> isOwner;
	private List<Item> isApprover;
	private List<Project> projects;
	private List<Project> isAdmin;
	private List<Project> requests;

	public User() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}


	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	@Column(name="\"PASSWORD\"")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(mappedBy="user")
	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	@OneToMany(mappedBy="owner")
	public List<Item> getIsOwner() {
		return this.isOwner;
	}

	public void setIsOwner(List<Item> isOwner) {
		this.isOwner = isOwner;
	}

	public Item addIsOwner(Item item) {
		getIsOwner().add(item);
		item.setOwner(this);

		return item;
	}

	public Item removeIsOwner(Item item) {
		getIsOwner().remove(item);
		item.setOwner(null);

		return item;
	}

	@OneToMany(mappedBy="approver")
	public List<Item> getIsApprover() {
		return this.isApprover;
	}

	public void setIsApprover(List<Item> isApprover) {
		this.isApprover = isApprover;
	}

	public Item addIsApprover(Item isApprover) {
		getIsOwner().add(isApprover);
		isApprover.setApprover(this);

		return isApprover;
	}

	public Item removeIsApprover(Item isApprover) {
		getIsApprover().remove(isApprover);
		isApprover.setApprover(null);

		return isApprover;
	}

	@ManyToMany
	@JoinTable(
		name="USERS_PROJECTS"
		, joinColumns={
			@JoinColumn(name="USERID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="PROJECTID")
			}
		)
	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@ManyToMany
	@JoinTable(
		name="ADMINS"
		, joinColumns={
			@JoinColumn(name="USERID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="PROJECTID")
			}
		)
	public List<Project> getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(List<Project> isAdmin) {
		this.isAdmin = isAdmin;
	}

	@ManyToMany
	@JoinTable(
		name="REQUESTS"
		, joinColumns={
			@JoinColumn(name="USERID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="PROJECTID")
			}
		)
	public List<Project> getRequests() {
		return requests;
	}


	public void setRequests(List<Project> requests) {
		this.requests = requests;
	}

	@Override
	public boolean equals(Object obj) {
		return this.userid == ((User)obj).getUserid();
	}
}