package project.models;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 * Klasa trwa³oœci dla tabeli USERS w bazie danych
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

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID u¿ytkownika
	 */
	private int userid;

	/**
	 * adres email u¿ytkownika
	 */
	private String email;

	/**
	 * nazwa u¿ytkownika
	 */
	private String login;
	
	/**
	 * has³o do konta u¿ytkownika
	 */
	private String password;
	
	/**
	 * Lista komentarzy napisanych przez danego u¿ytkownika
	 */
	private List<Comment> comments;
	
	/**
	 * Lista elementów, których u¿ytkownik jest odpowiedzialny za rozwi¹zanie
	 */
	private List<Item> isOwner;
	
	/**
	 * Lista elementów, których u¿ytkownik jest odpowiedzialny za akceptacjê rozwi¹zania
	 */
	private List<Item> isApprover;
	
	/**
	 * Lista projektów, do których u¿ytkownik jest autoryzowany
	 */
	private List<Project> projects;
	
	/**
	 * Lista projektów, w których u¿ytkownik jest administratorem
	 */
	private List<Project> isAdmin;
	
	/**
	 * Lista projektów, do których u¿ytkownik poprosi³ o dostêp
	 */
	private List<Project> requests;

	/**
	 * Konstruktor obiektu
	 */
	public User() {
	}

	/**
	 * {@link User#userid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getUserid() {
		return this.userid;
	}

	/**
	 * {@link User#userid}
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}

	/**
	 * {@link User#email}
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * {@link User#email}
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * {@link User#login}
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * {@link User#login}
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * {@link User#password}
	 */
	@Column(name="\"PASSWORD\"")
	public String getPassword() {
		return this.password;
	}

	/**
	 * {@link User#password}
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@link User#comments}
	 */
	@OneToMany(mappedBy="user")
	public List<Comment> getComments() {
		return this.comments;
	}

	/**
	 * {@link User#comments}
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * Metoda dodaj¹ca komentarz do listy
	 * @param comment obiekt komentarza, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt komentarza przes³any jako argument
	 */
	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	/**
	 * Metoda usuwaj¹ca komentarz z listy
	 * @param comment obiekt komentarza, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt komentarza przes³any jako argument
	 */
	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	/**
	 * {@link User#isOwner}
	 */
	@OneToMany(mappedBy="owner")
	public List<Item> getIsOwner() {
		return this.isOwner;
	}

	/**
	 * {@link User#isOwner}
	 */
	public void setIsOwner(List<Item> isOwner) {
		this.isOwner = isOwner;
	}
	
	/**
	 * Metoda dodaj¹ca element do listy
	 * @param item obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item addIsOwner(Item item) {
		getIsOwner().add(item);
		item.setOwner(this);

		return item;
	}

	/**
	 * Metoda usuwaj¹ca element z listy
	 * @param item obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item removeIsOwner(Item item) {
		getIsOwner().remove(item);
		item.setOwner(null);

		return item;
	}

	/**
	 * {@link User#isApprover}
	 */
	@OneToMany(mappedBy="approver")
	public List<Item> getIsApprover() {
		return this.isApprover;
	}

	/**
	 * {@link User#isApprover}
	 */
	public void setIsApprover(List<Item> isApprover) {
		this.isApprover = isApprover;
	}
	
	/**
	 * Metoda dodaj¹ca element do listy
	 * @param isApprover obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item addIsApprover(Item isApprover) {
		getIsOwner().add(isApprover);
		isApprover.setApprover(this);

		return isApprover;
	}

	/**
	 * Metoda usuwaj¹ca element z listy
	 * @param isApprover obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item removeIsApprover(Item isApprover) {
		getIsApprover().remove(isApprover);
		isApprover.setApprover(null);

		return isApprover;
	}

	/**
	 * {@link User#projects}
	 */
	@ManyToMany
	@JoinTable(name="USERS_PROJECTS", joinColumns={@JoinColumn(name="USERID")}, 
									  inverseJoinColumns={@JoinColumn(name="PROJECTID")})
	public List<Project> getProjects() {
		return this.projects;
	}

	/**
	 * {@link User#projects}
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	/**
	 * {@link User#isAdmin}
	 */
	@ManyToMany
	@JoinTable(name="ADMINS", joinColumns={@JoinColumn(name="USERID")}, 
							  inverseJoinColumns={@JoinColumn(name="PROJECTID")})
	public List<Project> getIsAdmin() {
		return this.isAdmin;
	}

	/**
	 * {@link User#isAdmin}
	 */
	public void setIsAdmin(List<Project> isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * {@link User#requests}
	 */
	@ManyToMany
	@JoinTable(name="REQUESTS", joinColumns={@JoinColumn(name="USERID")}, 
								inverseJoinColumns={@JoinColumn(name="PROJECTID")})
	public List<Project> getRequests() {
		return requests;
	}

	/**
	 * {@link User#requests}
	 */
	public void setRequests(List<Project> requests) {
		this.requests = requests;
	}

	/**
	 * Metoda s³u¿y do porównywania ze sob¹ obiektów typu User
	 * @param obj obiekt, który chcemy porównaæ do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.userid == ((User)obj).getUserid();
	}
}