package project.models;
import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ITEMSTATUS database table.
 * 
 */
@Entity
@NamedQuery(name="Itemstatus.findAll", query="SELECT i FROM Itemstatus i")
public class Itemstatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private int statusid;
	private String status;
	private List<Item> items;

	public Itemstatus() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getStatusid() {
		return this.statusid;
	}

	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}


	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(mappedBy="itemstatus")
	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setItemstatus(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setItemstatus(null);

		return item;
	}

}