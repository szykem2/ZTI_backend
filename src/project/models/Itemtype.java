package test;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ITEMTYPE database table.
 * 
 */
@Entity
@NamedQuery(name="Itemtype.findAll", query="SELECT i FROM Itemtype i")
public class Itemtype implements Serializable {
	private static final long serialVersionUID = 1L;
	private int typeid;
	private String type;
	private List<Item> items;

	public Itemtype() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getTypeid() {
		return this.typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}


	@Column(name="\"TYPE\"")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	//bi-directional many-to-one association to Item
	@OneToMany(mappedBy="itemtype")
	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemtype(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemtype(null);

		return item;
	}

}