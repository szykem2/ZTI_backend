package project.dto;

import project.models.Itemtype;

public class ItemTypeDto {
	private int typeid;
	private String type;
	
	public ItemTypeDto(Itemtype itemtype) { 
		this.setType(itemtype.getType());
		this.setTypeid(itemtype.getTypeid());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
}
