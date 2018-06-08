package project.dto;

import project.models.Itemstatus;

public class ItemStatusDto {
	private int statusid;
	private String status;
	public ItemStatusDto(Itemstatus itemstatus) {
		this.setStatus(itemstatus.getStatus());
		this.setStatusid(itemstatus.getStatusid());
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusid() {
		return statusid;
	}
	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}
	
}
