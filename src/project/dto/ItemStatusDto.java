package project.dto;

import project.models.Itemstatus;

/**
 * Klasa s�u��ca do przesy�ania obiektu statusu elementu
 */
public class ItemStatusDto {
	/**
	 * ID statusu
	 */
	private int statusid;
	
	/**
	 * warto�� statusu
	 */
	private String status;
	
	/**
	 * Konstruktor obiektu
	 * @param itemstatus obiekt statusu, kt�ry ma zosta� przes�any
	 * @see project.models.Itemstatus
	 */
	public ItemStatusDto(Itemstatus itemstatus) {
		this.setStatus(itemstatus.getStatus());
		this.setStatusid(itemstatus.getStatusid());
	}
	
	/**
	 * {@link ItemStatusDto#status}
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * {@link ItemStatusDto#status}
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * {@link ItemStatusDto#statusid}
	 */
	public int getStatusid() {
		return statusid;
	}
	
	/**
	 * {@link ItemStatusDto#statusid}
	 */
	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}
	
}
