package project.utils;

import project.models.Itemstatus;

public class ItemstatusJson {
	private int statusid;
	private String status;
	public ItemstatusJson(Itemstatus itemstatus) {
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
