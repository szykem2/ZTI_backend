package project.dto;

import java.sql.Timestamp;
import project.models.Item;

/**
 * Klasa s³u¿¹ca do przesy³ania obiektu typu Item
 */
public class ItemDto {

	/**
	 * ID elementu
	 */
	private int itemid;

	/**
	 * flaga, mówi¹ca o tym czy stan obiektu to 'zaakceptowany'
	 */
	private Boolean approved;

	/**
	 * data utworzenia elementu
	 */
	private Timestamp creationdate;

	/**
	 * opis elementu
	 */
	private String description;

	/**
	 * data koñca pracy nad elementem
	 */
	private Timestamp resolutiondate;

	/**
	 * flaga, mówi¹ca o tym czy stan obiektu to 'rozwi¹zany'
	 */
	private Boolean resolved;

	/**
	 * nazwa elementu
	 */
	private String title;

	/**
	 * status elementu
	 * @see ItemStatusDto
	 */
	private ItemStatusDto itemstatus;

	/**
	 * typ elementu
	 * @see ItemTypeDto
	 */
	private ItemTypeDto itemtype;

	/**
	 * osoba odpowiedzialna za rozwi¹zanie elementu
	 * @see UserDto
	 */
	private UserDto owner;

	/**
	 * osoba odpowiedzialna za weryfikacjê/testowanie elementu
	 * @see User
	 */
	private UserDto approver;

	/**
	 * Konstruktor obiektu
	 * @param it obiektu typu Item, który ma zostaæ przes³any
	 * @see project.models.Item
	 */
	public ItemDto(Item it) {
		this.setItemid(it.getItemid());
		this.setApproved(it.getApproved());
		this.setDescription(it.getDescription());
		this.setResolutiondate(it.getResolutiondate());
		this.setCreationdate(it.getCreationdate());
		this.setResolved(it.getResolved());
		this.setTitle(it.getTitle());
		this.setItemstatus(new ItemStatusDto(it.getItemstatus()));
		this.setItemtype(new ItemTypeDto(it.getItemtype()));
		this.setOwner(new UserDto(it.getOwner()));
		this.setApprover(new UserDto(it.getApprover()));
	}

	/**
	 * {@link ItemDto#itemid}
	 */
	public int getItemid() {
		return itemid;
	}

	/**
	 * {@link ItemDto#itemid}
	 */
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	/**
	 * {@link ItemDto#approved}
	 */
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * {@link ItemDto#approved}
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * {@link ItemDto#creationdate}
	 */
	public Timestamp getCreationdate() {
		return creationdate;
	}

	/**
	 * {@link ItemDto#creationdate}
	 */
	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	/**
	 * {@link ItemDto#resolutiondate}
	 */
	public Timestamp getResolutiondate() {
		return resolutiondate;
	}

	/**
	 * {@link ItemDto#resolutiondate}
	 */
	public void setResolutiondate(Timestamp resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	/**
	 * {@link ItemDto#resolved}
	 */
	public Boolean getResolved() {
		return resolved;
	}

	/**
	 * {@link ItemDto#resolved}
	 */
	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	/**
	 * {@link ItemDto#description}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * {@link ItemDto#description}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@link ItemDto#title}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * {@link ItemDto#title}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * {@link ItemDto#itemstatus}
	 */
	public ItemStatusDto getItemstatus() {
		return itemstatus;
	}

	/**
	 * {@link ItemDto#itemstatus}
	 */
	public void setItemstatus(ItemStatusDto itemstatus) {
		this.itemstatus = itemstatus;
	}

	/**
	 * {@link ItemDto#itemtype}
	 */
	public ItemTypeDto getItemtype() {
		return itemtype;
	}

	/**
	 * {@link ItemDto#itemtype}
	 */
	public void setItemtype(ItemTypeDto itemtype) {
		this.itemtype = itemtype;
	}

	/**
	 * {@link ItemDto#owner}
	 */
	public UserDto getOwner() {
		return owner;
	}

	/**
	 * {@link ItemDto#owner}
	 */
	public void setOwner(UserDto owner) {
		this.owner = owner;
	}

	/**
	 * {@link ItemDto#approver}
	 */
	public UserDto getApprover() {
		return approver;
	}

	/**
	 * {@link ItemDto#approver}
	 */
	public void setApprover(UserDto approver) {
		this.approver = approver;
	}

}
