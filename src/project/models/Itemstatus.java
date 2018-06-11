package project.models;
import java.io.Serializable;
import javax.persistence.*;

import project.dto.ItemStatusDto;

import java.util.List;

/**
 * Klasa trwałości dla tabeli ITEMSTATUS w bazie danych
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
	 * wartość statusu
	 */
	private String status;
	
	/**
	 * Lista elementów, które posiadają dany status
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
	 * Metoda dodająca element do listy
	 * @param item obiekt elementu, który ma zostać usunięty
	 * @return zmodyfikowany obiekt elementu przesłany jako argument
	 */
	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemstatus(this);
		return item;
	}

	/**
	 * Metoda usuwająca element z listy
	 * @param item obiekt elementu, który ma zostać usunięty
	 * @return zmodyfikowany obiekt elementu przesłany jako argument
	 */
	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemstatus(null);
		return item;
	}

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu Itemstatus
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.statusid == ((Itemstatus)obj).getStatusid();
	}
}