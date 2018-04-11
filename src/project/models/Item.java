package project.models;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="item")
public class Item {
	
	public enum ItemType {
		defect, task, feature
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="itemid")
	private int itemID;
	
	@ManyToOne
	@JoinColumn(name = "itemid")
	private Project projectID;
	
	@ManyToOne
	@JoinColumn(name = "owner")
	private User owner;
	
	@ManyToOne
	@JoinColumn(name = "approver")
	private User approver;
	
	@Column(name="typeid")
	private int itemType;
	
	private String description;
	private String title;
	private Date creationDate;
	private Date resolutionDate;
	private Boolean resolved;
	private Boolean approved;
	
	public void setItemID(int id) {
		this.itemID = id;
	}
	public int getItemID() {
		return itemID;
	}
	
	public void setProjectID(Project id) {
		this.projectID = id;
	}
	public Project getProjectID() {
		return projectID;
	}
	
	public void setOwner(User id) {
		this.owner = id;
	}
	public User getOwner() {
		return owner;
	}
	
	public void setApprover(User id) {
		this.approver = id;
	}
	public User getApprover() {
		return approver;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	public String getDescription() {
		return description;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public void setCreationDate(Date cDate) {
		this.creationDate = cDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setResolutionDate(Date cDate) {
		this.resolutionDate = cDate;
	}
	public Date getResolutionDate() {
		return resolutionDate;
	}
	
	public void setResolved(boolean res) {
		this.resolved = res;
	}
	public boolean getResolved() {
		return resolved;
	}
	
	public void setApproved(boolean appr) {
		this.approved = appr;
	}
	public boolean getApproved() {
		return approved;
	}
}
