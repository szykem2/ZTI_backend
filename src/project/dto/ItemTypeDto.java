package project.dto;

import project.models.Itemtype;

/**
 * Klasa s�u��ca do przesy�ania obiektu typu elementu
 */
public class ItemTypeDto {
	/**
	 * ID typu
	 */
	private int typeid;	
	
	/**
	 * nazwa typu
	 */
	private String type;
	
	/**
	 * Konstruktor obiektu
	 * @param itemtype obiekt typu, kt�ry ma zosta� przes�any
	 * @see project.models.Itemtype
	 */
	public ItemTypeDto(Itemtype itemtype) { 
		this.setType(itemtype.getType());
		this.setTypeid(itemtype.getTypeid());
	}

	/**
	 * {@link ItemTypeDto#type}
	 */
	public String getType() {
		return type;
	}

	/**
	 * {@link ItemTypeDto#type}
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * {@link ItemTypeDto#typeid}
	 */
	public int getTypeid() {
		return typeid;
	}

	/**
	 * {@link ItemTypeDto#typeid}
	 */
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
}
