package project.utils;

import project.models.Itemtype;

public class ItemtypeJson {
	private int typeid;
	private String type;
	
	public ItemtypeJson(Itemtype itemtype) { 
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
