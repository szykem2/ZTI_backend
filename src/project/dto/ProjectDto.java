package project.dto;

import java.io.Serializable;
import project.models.Project;
/**
 * Klasa służąca do przesyłania projektu
 */
public class ProjectDto implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id projektu
	 */
	private int id;

	/**
	 * opis projektu
	 */
	private String description;

	/**
	 * nazwa projektu
	 */
	private String title;

	/**
	 * flaga mówiąca o tym, czy użytkownik jest administratorem projektu
	 */
	private boolean isAdmin;

	/**
	 * Konstruktor obiektu
	 * @param p obiekt projektu, który ma zostać przesłany
	 * @see project.models.Project
	 */
	public ProjectDto(Project p) {
		this.setId(p.getProjectid());
		this.setDescription(p.getDescription());
		this.setTitle(p.getName());
		this.isAdmin = false;
	}

	/**
	 * {@link ProjectDto#title}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * {@link ProjectDto#title}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * {@link ProjectDto#description}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * {@link ProjectDto#description}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@link ProjectDto#id}
	 */
	public int getId() {
		return id;
	}

	/**
	 * {@link ProjectDto#id}
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * {@link ProjectDto#isAdmin}
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * {@link ProjectDto#isAdmin}
	 */
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu ProjectDto
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id == ((ProjectDto)obj).getId();
	}
}