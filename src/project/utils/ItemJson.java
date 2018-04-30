package project.utils;

import java.sql.Timestamp;
import project.models.Item;

public class ItemJson {
	private int itemid;
	private Boolean approved;
	private Timestamp creationdate;
	private String description;
	private Timestamp resolutiondate;
	private Boolean resolved;
	private String title;
	private ItemstatusJson itemstatus;
	private ItemtypeJson itemtype;
	private UserJson owner;
	private UserJson approver;

	public ItemJson(Item it) {
		this.setItemid(it.getItemid());
		this.setApproved(it.getApproved());
		this.setDescription(it.getDescription());
		this.setResolutiondate(it.getResolutiondate());
		this.setCreationdate(it.getCreationdate());
		this.setResolved(it.getResolved());
		this.setTitle(it.getTitle());
		this.setItemstatus(new ItemstatusJson(it.getItemstatus()));
		this.setItemtype(new ItemtypeJson(it.getItemtype()));
		this.setOwner(new UserJson(it.getOwner()));
		this.setApprover(new UserJson(it.getApprover()));
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Timestamp getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public Timestamp getResolutiondate() {
		return resolutiondate;
	}

	public void setResolutiondate(Timestamp resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ItemstatusJson getItemstatus() {
		return itemstatus;
	}

	public void setItemstatus(ItemstatusJson itemstatus) {
		this.itemstatus = itemstatus;
	}

	public ItemtypeJson getItemtype() {
		return itemtype;
	}

	public void setItemtype(ItemtypeJson itemtype) {
		this.itemtype = itemtype;
	}

	public UserJson getOwner() {
		return owner;
	}

	public void setOwner(UserJson owner) {
		this.owner = owner;
	}

	public UserJson getApprover() {
		return approver;
	}

	public void setApprover(UserJson approver) {
		this.approver = approver;
	}

}
