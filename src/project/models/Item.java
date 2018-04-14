package test;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the ITEM database table.
 * 
 */
@Entity
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	private int itemid;
	private Object approved;
	private Timestamp creationdate;
	private String description;
	private Timestamp resolutiondate;
	private Object resolved;
	private String title;
	private List<Comment> comments;
	private Itemstatus itemstatus;
	private Itemtype itemtype;
	private Project project;
	private User owner;
	private User approver;

	public Item() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getItemid() {
		return this.itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}


	public Object getApproved() {
		return this.approved;
	}

	public void setApproved(Object approved) {
		this.approved = approved;
	}


	public Timestamp getCreationdate() {
		return this.creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Timestamp getResolutiondate() {
		return this.resolutiondate;
	}

	public void setResolutiondate(Timestamp resolutiondate) {
		this.resolutiondate = resolutiondate;
	}


	public Object getResolved() {
		return this.resolved;
	}

	public void setResolved(Object resolved) {
		this.resolved = resolved;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="item")
	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setItem(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setItem(null);

		return comment;
	}


	//bi-directional many-to-one association to Itemstatus
	@ManyToOne
	@JoinColumn(name="STATUSID")
	public Itemstatus getItemstatus() {
		return this.itemstatus;
	}

	public void setItemstatus(Itemstatus itemstatus) {
		this.itemstatus = itemstatus;
	}


	//bi-directional many-to-one association to Itemtype
	@ManyToOne
	@JoinColumn(name="TYPEID")
	public Itemtype getItemtype() {
		return this.itemtype;
	}

	public void setItemtype(Itemtype itemtype) {
		this.itemtype = itemtype;
	}


	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="PROJECTID")
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="OWNER")
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}


	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="APPROVER")
	public User getApprover() {
		return this.approver;
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}

}