package project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PROJECTS database table.
 * 
 */
@Entity
@Table(name="PROJECTS")
@NamedQuery(name="Project.findAll", query="SELECT p FROM Project p")
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;
	private int projectid;
	private String description;
	private String name;
	private List<Item> items;
	private List<User> projects;
	private List<User> isAdminFor;

	public Project() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy="project")
	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setProject(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setProject(null);

		return item;
	}

	@ManyToMany(mappedBy="users")
	public List<User> getProjects() {
		return this.projects;
	}

	public void setProjects(List<User> projects) {
		this.projects = projects;
	}

	@ManyToMany(mappedBy="admins")
	public List<User> getIsAdminFor() {
		return this.isAdminFor;
	}

	public void setIsAdminFor(List<User> isAdminFor) {
		this.isAdminFor = isAdminFor;
	}

}