package project.models;
import java.io.Serializable;
import javax.persistence.*;

import project.dto.ItemStatusDto;

import java.util.List;

/**
 * Klasa trwa�o�ci dla tabeli ITEMSTATUS w bazie danych
 */
@Entity
@NamedQuery(name="Itemstatus.findAll", query="SELECT i FROM Itemstatus i")
public class Itemstatus implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID statusu
	 */
	private int statusid;
	
	/**
	 * warto�� statusu
	 */
	private String status;
	
	/**
	 * Lista element�w, kt�re posiadaj� dany status
	 */
	private List<Item> items;

	/**
	 * Konstruktor obiektu
	 */
	public Itemstatus() {
	}

	/**
	 * {@link Itemstatus#statusid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getStatusid() {
		return this.statusid;
	}

	/**
	 * {@link Itemstatus#statusid}
	 */
	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}

	/**
	 * {@link Itemstatus#status}
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * {@link Itemstatus#status}
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * {@link Itemstatus#items}
	 */
	@OneToMany(mappedBy="itemstatus")
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * {@link Itemstatus#items}
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * Metoda dodaj�ca element do listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemstatus(this);
		return item;
	}

	/**
	 * Metoda usuwaj�ca element z listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemstatus(null);
		return item;
	}

}