package project.dto;

import java.io.Serializable;
import project.models.Project;

public class ProjectDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String title;
	private boolean isAdmin;
	
	public ProjectDto(Project p) {
		this.setId(p.getProjectid());
		this.setDescription(p.getDescription());
		this.setTitle(p.getName());
		this.isAdmin = false;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
