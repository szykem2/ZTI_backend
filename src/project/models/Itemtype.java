package project.models;

import java.io.Serializable;
import javax.persistence.*;

import project.dto.ItemStatusDto;

import java.util.List;

/**
 * Klasa trwa�o�ci dla tabeli ITEMTYPE w bazie danych
 */
@Entity
@NamedQuery(name="Itemtype.findAll", query="SELECT i FROM Itemtype i")
public class Itemtype implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID typu
	 */
	private int typeid;
	
	/**
	 * nazwa typu
	 */
	private String type;
	
	/**
	 * Lista element�w danego typu
	 */
	private List<Item> items;

	/**
	 * Konstruktor obiektu
	 */
	public Itemtype() {
	}

	/**
	 * {@link Itemtype#typeid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getTypeid() {
		return this.typeid;
	}

	/**
	 * {@link Itemtype#typeid}
	 */
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	/**
	 * {@link Itemtype#type}
	 */
	@Column(name="\"TYPE\"")
	public String getType() {
		return this.type;
	}

	/**
	 * {@link Itemtype#type}
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * {@link Itemtype#items}
	 */
	@OneToMany(mappedBy="itemtype")
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * {@link Itemtype#items}
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * Metoda usuwaj�ca element z listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemtype(this);

		return item;
	}

	/**
	 * Metoda usuwaj�ca element z listy
	 * @param item obiekt elementu, kt�ry ma zosta� usuni�ty
	 * @return zmodyfikowany obiekt elementu przes�any jako argument
	 */
	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemtype(null);

		return item;
	}

}