package project.models;

import java.io.Serializable;
import javax.persistence.*;

import project.dto.ItemDto;
import project.dto.ItemStatusDto;
import project.dto.ItemTypeDto;
import project.dto.UserDto;

import java.sql.Timestamp;
import java.util.List;


/**
 * Klasa trwa�o�ci dla tabeli ITEM w bazie danych
 */
@Entity
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID elementu
	 */
	private int itemid;

	/**
	 * flaga, m�wi�ca o tym czy stan obiektu to 'zaakceptowany'
	 */
	private Boolean approved;

	/**
	 * data utworzenia elementu
	 */
	private Timestamp creationdate;

	/**
	 * opis elementu
	 */
	private String description;

	/**
	 * data ko�ca pracy nad elementem
	 */
	private Timestamp resolutiondate;

	/**
	 * flaga, m�wi�ca o tym czy stan obiektu to 'rozwi�zany'
	 */
	private Boolean resolved;

	/**
	 * nazwa elementu
	 */
	private String title;
	
	/**
	 * Lista komentarzy przypisanych do danego elementu
	 */
	private List<Comment> comments;

	/**
	 * status elementu
	 * @see project.models.Itemstatus
	 */
	private Itemstatus itemstatus;

	/**
	 * typ elementu
	 * @see project.models.Itemtype
	 */
	private Itemtype itemtype;
	
	/**
	 * Projekt, do kt�rego przypisany jest dany element
	 */
	private Project project;

	/**
	 * osoba odpowiedzialna za rozwi�zanie elementu
	 * @see project.models.User
	 */
	private User owner;

	/**
	 * osoba odpowiedzialna za weryfikacj�/testowanie elementu
	 * @see project.models.User
	 */
	private User approver;

	/**
	 * Konstruktor obiektu
	 */
	public Item() {
	}

	/**
	 * {@link Item#itemid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getItemid() {
		return this.itemid;
	}

	/**
	 * {@link Item#itemid}
	 */
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	/**
	 * {@link Item#approved}
	 */
	public Boolean getApproved() {
		return this.approved;
	}

	/**
	 * {@link Item#approved}
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * {@link Item#creationdate}
	 */
	public Timestamp getCreationdate() {
		return this.creationdate;
	}

	/**
	 * {@link Item#creationdate}
	 */
	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	/**
	 * {@link Item#description}
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * {@link Item#description}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@link Item#resolutiondate}
	 */
	public Timestamp getResolutiondate() {
		return this.resolutiondate;
	}

	/**
	 * {@link Item#resolutiondate}
	 */
	public void setResolutiondate(Timestamp resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	/**
	 * {@link Item#resolved}
	 */
	public Boolean getResolved() {
		return this.resolved;
	}

	/**
	 * {@link Item#resolved}
	 */
	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	/**
	 * {@link Item#title}
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * {@link Item#title}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * {@link Item#comments}
	 */
	@OneToMany(mappedBy="item")
	public List<Comment> getComments() {
		return this.comments;
	}

	/**
	 * {@link Item#comments}
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * Metoda dodaj�ca komentarz do listy komentarzy
	 * @param comment obiekt komentarza, kt�ry ma zosta� dodany
	 * @return zmodyfikowany obiekt komentarza, kt�ry by� przes�any jako argument
	 */
	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setItem(this);

		return comment;
	}

	/**
	 * Metoda usuwaj�ca komentarz z listy komentarzy
	 * @param comment obiekt komentarza, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt komentarza, kt�ry by� przes�any jako argument
	 */
	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setItem(null);

		return comment;
	}

	/**
	 * {@link Item#itemstatus}
	 */
	@ManyToOne
	@JoinColumn(name="STATUSID")
	public Itemstatus getItemstatus() {
		return this.itemstatus;
	}

	/**
	 * {@link Item#itemstatus}
	 */
	public void setItemstatus(Itemstatus itemstatus) {
		this.itemstatus = itemstatus;
	}

	/**
	 * {@link Item#itemtype}
	 */
	@ManyToOne
	@JoinColumn(name="TYPEID")
	public Itemtype getItemtype() {
		return this.itemtype;
	}

	/**
	 * {@link Item#itemtype}
	 */
	public void setItemtype(Itemtype itemtype) {
		this.itemtype = itemtype;
	}

	/**
	 * {@link Item#project}
	 */
	@ManyToOne
	@JoinColumn(name="PROJECTID")
	public Project getProject() {
		return this.project;
	}

	/**
	 * {@link Item#project}
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * {@link Item#owner}
	 */
	@ManyToOne
	@JoinColumn(name="OWNER")
	public User getOwner() {
		return this.owner;
	}

	/**
	 * {@link Item#owner}
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * {@link Item#approver}
	 */
	@ManyToOne
	@JoinColumn(name="APPROVER")
	public User getApprover() {
		return this.approver;
	}

	/**
	 * {@link Item#approver}
	 */
	public void setApprover(User approver) {
		this.approver = approver;
	}

	/**
	 * Metoda s�u�y do por�wnywania ze sob� obiekt�w typu Item
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.itemid == ((Item)obj).getItemid();
	}
}