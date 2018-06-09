package project.models;
import java.io.Serializable;
import javax.persistence.*;

import project.dto.ItemStatusDto;

import java.util.List;

/**
 * Klasa trwa³oœci dla tabeli ITEMSTATUS w bazie danych
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
	 * wartoœæ statusu
	 */
	private String status;
	
	/**
	 * Lista elementów, które posiadaj¹ dany status
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
	 * Metoda dodaj¹ca element do listy
	 * @param item obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemstatus(this);
		return item;
	}

	/**
	 * Metoda usuwaj¹ca element z listy
	 * @param item obiekt elementu, który ma zostaæ usuniêty
	 * @return zmodyfikowany obiekt elementu przes³any jako argument
	 */
	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemstatus(null);
		return item;
	}

}