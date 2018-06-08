package project.dto;

import java.sql.Timestamp;
import project.models.Item;

public class ItemDto {
	private int itemid;
	private Boolean approved;
	private Timestamp creationdate;
	private String description;
	private Timestamp resolutiondate;
	private Boolean resolved;
	private String title;
	private ItemStatusDto itemstatus;
	private ItemTypeDto itemtype;
	private UserDto owner;
	private UserDto approver;

	public ItemDto(Item it) {
		this.setItemid(it.getItemid());
		this.setApproved(it.getApproved());
		this.setDescription(it.getDescription());
		this.setResolutiondate(it.getResolutiondate());
		this.setCreationdate(it.getCreationdate());
		this.setResolved(it.getResolved());
		this.setTitle(it.getTitle());
		this.setItemstatus(new ItemStatusDto(it.getItemstatus()));
		this.setItemtype(new ItemTypeDto(it.getItemtype()));
		this.setOwner(new UserDto(it.getOwner()));
		this.setApprover(new UserDto(it.getApprover()));
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

	public ItemStatusDto getItemstatus() {
		return itemstatus;
	}

	public void setItemstatus(ItemStatusDto itemstatus) {
		this.itemstatus = itemstatus;
	}

	public ItemTypeDto getItemtype() {
		return itemtype;
	}

	public void setItemtype(ItemTypeDto itemtype) {
		this.itemtype = itemtype;
	}

	public UserDto getOwner() {
		return owner;
	}

	public void setOwner(UserDto owner) {
		this.owner = owner;
	}

	public UserDto getApprover() {
		return approver;
	}

	public void setApprover(UserDto approver) {
		this.approver = approver;
	}

}
