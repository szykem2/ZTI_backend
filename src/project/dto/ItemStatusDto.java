package project.dto;

import project.models.Itemstatus;

/**
 * Klasa służąca do przesyłania obiektu statusu elementu
 */
public class ItemStatusDto {
	/**
	 * ID statusu
	 */
	private int statusid;
	
	/**
	 * wartość statusu
	 */
	private String status;
	
	/**
	 * Konstruktor obiektu
	 * @param itemstatus obiekt statusu, który ma zostać przesłany
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

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu ItemstatusDto
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.statusid == ((ItemStatusDto)obj).getStatusid();
	}
}