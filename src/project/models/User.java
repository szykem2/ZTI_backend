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
@NamedQuery(name="User.findOne", query="SELECT u FROM User u WHERE u.login=:login AND u.password=:pass")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userid;
	private String email;
	private String login;
	private String password;
	private List<Comment> comments;
	private List<Item> items;
	private List<Item> isAdminFor;
	private List<Project> users;
	private List<Project> admins;

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
	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setOwner(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setOwner(null);

		return item;
	}

	@OneToMany(mappedBy="approver")
	public List<Item> getIsAdminFor() {
		return this.isAdminFor;
	}

	public void setIsAdminFor(List<Item> isAdminFor) {
		this.isAdminFor = isAdminFor;
	}

	public Item addIsAdminFor(Item isAdminFor) {
		getIsAdminFor().add(isAdminFor);
		isAdminFor.setApprover(this);

		return isAdminFor;
	}

	public Item removeIsAdminFor(Item isAdminFor) {
		getIsAdminFor().remove(isAdminFor);
		isAdminFor.setApprover(null);

		return isAdminFor;
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
	public List<Project> getUsers() {
		return this.users;
	}

	public void setUsers(List<Project> users) {
		this.users = users;
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
	public List<Project> getAdmins() {
		return this.admins;
	}

	public void setAdmins(List<Project> admins) {
		this.admins = admins;
	}

}