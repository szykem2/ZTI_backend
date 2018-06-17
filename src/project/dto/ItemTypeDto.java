package project.dto;
import project.models.Itemtype;

/**
 * Klasa służąca do przesyłania obiektu typu elementu
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
	 * @param itemtype obiekt typu, który ma zostać przesłany
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

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu ItemTypeDto
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.typeid == ((ItemTypeDto)obj).getTypeid();
	}
}