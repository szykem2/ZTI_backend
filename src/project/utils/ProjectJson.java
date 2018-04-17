package project.utils;

import java.io.Serializable;

import project.models.Project;

public class ProjectJson implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private String title;
	
	public ProjectJson(Project p) {
		this.setId(p.getProjectid());
		this.setDescription(p.getDescription());
		this.setTitle(p.getName());
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
}
