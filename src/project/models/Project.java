package project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * Klasa trwa�o�ci dla tabeli PROJECTS w bazie danych
 */
@Entity
@Table(name="PROJECTS")
@NamedQuery(name="Project.findAll", query="SELECT p FROM Project p")
public class Project implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id projektu
	 */
	private int projectid;

	/**
	 * opis projektu
	 */
	private String description;

	/**
	 * nazwa projektu
	 */
	private String name;
	
	/**
	 * Lista element�w przpisanych do danego projektu
	 */
	private List<Item> items;

	/**
	 * lista autoryzowanych u�ytkownik�w
	 */
	private List<User> users;

	/**
	 * lista administrator�w projektu
	 */
	private List<User> admins;

	/**
	 * lista u�ytkownik�w, kt�rzy chc� uzyska� dost�p do projektu
	 */
	private List<User> requestors;

	/**
	 * konstruktor obiektu
	 */
	public Project() {
	}

	/**
	 * {@link Project#projectid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getProjectid() {
		return this.projectid;
	}

	/**
	 * {@link Project#projectid}
	 */
	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	/**
	 * {@link Project#description}
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * {@link Project#description}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@link Project#name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * {@link Project#name}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@link Project#items}
	 */
	@OneToMany(mappedBy="project")
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * {@link Project#items}
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * Metoda dodaj�ca element do listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item addItem(Item item) {
		getItems().add(item);
		item.setProject(this);

		return item;
	}

	/**
	 * Metoda usuwaj�ca element z listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setProject(null);

		return item;
	}

	/**
	 * {@link Project#users}
	 */
	@ManyToMany(mappedBy="projects")
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * {@link Project#users}
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * {@link Project#admins}
	 */
	@ManyToMany(mappedBy="isAdmin")
	public List<User> getAdmins() {
		return this.admins;
	}

	/**
	 * {@link Project#admins}
	 */
	public void setAdmins(List<User> admins) {
		this.admins = admins;
	}

	/**
	 * {@link Project#requestors}
	 */
	@ManyToMany(mappedBy="requests")
	public List<User> getRequestors() {
		return requestors;
	}

	/**
	 * {@link Project#requestors}
	 */
	public void setRequestors(List<User> requestors) {
		this.requestors = requestors;
	}

	/**
	 * Metoda s�u�y do por�wnywania ze sob� obiekt�w typu Project
	 * @param obj obiekt, kt�ry chcemy por�wna� do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.projectid == ((Project)obj).getProjectid();
	}

}