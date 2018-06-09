package project.dbutils;

import project.models.*;

import javax.ejb.Singleton;
import java.util.*;

/**
 * Klasa udost�pniaj�ca interfejs bazodanowy
 */
@Singleton
public class Database {
	/**
	 * Obiekt, kt�ry udost�pnia funkcjonalno�� JPA
	 */
	private static final JPAConnection connection = new JPAConnection();
	
	/**
	 * Konstruktor obiektu
	 */
	public Database() {
	}
	
	/**
	 * Metoda s�u�y do pobierania wszystkich u�ytkownik�w z bazy danych
	 * @return lista u�ytkownik�w
	 */
	public List<User> getUsers() {
		return connection.getUserList();
	}
	
	/**
	 * Metoda s�u�y do pobierania u�ytkownika o zadanym id
	 * @param id identyfikator u�ytkownika
	 * @return u�ytkownik, kt�rego id podano w argumencie
	 */
	public User getUser(int id) {
		return connection.getUser(id);
	}

	/**
	 * Metoda s�u��ca to uwierzytelnienia u�ytkownika
	 * @param login nazwa u�ytkownika
	 * @param password has�o
	 * @return u�ytkownik odpowiadaj�cy podanym danym identyfikacyjnym
	 */
	public User authorize(String login, String password) {
		return connection.getUser(login, password);
	}

	/**
	 * Metoda s�u��ca do pobierania projektu o zadanym id
	 * @param id identyfikator projektu
	 * @return projekt, kt�rego id zosta�o podane jako agument
	 */
	public Project getProject(int id) {
		return connection.getProject(id);
	}
	
	/**
	 * Metoda s�u��ca do sprawdzania czy login znajduje si� w bazie
	 * @param login nazwa u�ytkownika, kt�r� nale�y zwalidowa�
	 * @return warto�� logiczna m�wi�ca o tym, czy nazwa u�ytkownika jest dost�pna
	 */
	public Boolean validateLogin(String login) {
		return connection.validateLogin(login);
	}
	
	/**
	 * Metoda s�u��ca do sprawdzania czy adres email znajduje si� w bazie
	 * @param email adres email, kt�ry nale�y zwalidowa�
	 * @return warto�� logiczna m�wi�ca o tym, czy adres jest dost�pny
	 */
	public Boolean validateEmail(String email) {
		return connection.validateEmail(email);
	}

	/**
	 * Metoda dodaj�ca nowego u�ytkownika do bazy
	 * @param usr u�ytkownik, kt�rego nale�y doda�
	 */
	public void newUser(User usr) {
		connection.newUser(usr);
	}

	/**
	 * Metoda pobieraj�ca z bazy element o zadanym id
	 * @param id identyfikator elementu
	 * @return element, o podanym identyfikatorze
	 */
	public Item getItem(int id) {
		return connection.getItem(id);
	}
	
	/**
	 * Metoda dodaj�ca do bazy danych nowy komentarz
	 * @param cmt obiekt komentarza, kt�ry nale�y doda� do bazy
	 */
	public void newComment(Comment cmt) {
		cmt.getUser().getComments().add(cmt);
		cmt.getItem().getComments().add(cmt);
		connection.begin();
		connection.newComment(cmt);
		connection.updateUser(cmt.getUser());
		connection.updateItem(cmt.getItem());
		connection.commit();
	}
	
	/**
	 * Metoda dodaj�ca nowy element do bazy danych
	 * @param it obiekt elementu, kt�ry nale�y doda�
	 */
	public void newItem(Item it) {
		connection.begin();
		connection.newItem(it);
		it.getProject().getItems().add(it);
		connection.updateProject(it.getProject());
		connection.commit();
	}
	
	/**
	 * Metoda s�u��ca do aktualizacji elementu w bazie
	 * @param it zmodyfikowany obiekt elementu
	 */
	public void updateItem(Item it) {
		connection.updateItem(it);
	}
	
	/**
	 * Metoda usuwaj�ca element z bazy danych
	 * @param id identyfikator elementu, kt�ry ma zosta� usuni�ty
	 */
	public void removeItem(int id) {
		connection.begin();
		Item it = connection.getItem(id);
		if(it != null) {
			it.getProject().getItems().remove(it);
			it.getItemstatus().getItems().remove(it);
			it.getItemtype().getItems().remove(it);
			if(it.getOwner() != null) {
				it.getOwner().getIsOwner().remove(it);
				connection.updateUser(it.getOwner());
			}
			if(it.getApprover() != null) {
				it.getApprover().getIsApprover().remove(it);
				connection.updateUser(it.getApprover());
			}
			connection.updateProject(it.getProject());
			connection.updateStatus(it.getItemstatus());
			connection.updateType(it.getItemtype());
			connection.removeItem(it);
		}
		connection.commit();
	}
	
	/**
	 * Metoda usuwaj�ca element z bazy danych
	 * @param id element, kt�ry ma zosta� usuni�ty
	 */
	public void removeItem(Item it) {
		if(it != null) {
			it.getProject().getItems().remove(it);
			it.getItemstatus().getItems().remove(it);
			it.getItemtype().getItems().remove(it);
			if(it.getOwner() != null) {
				it.getOwner().getIsOwner().remove(it);
				connection.updateUser(it.getOwner());
			}
			if(it.getApprover() != null) {
				it.getApprover().getIsApprover().remove(it);
				connection.updateUser(it.getApprover());
			}
			for(Comment c : it.getComments()) {
				this.removeComment(c);
			}
			connection.updateProject(it.getProject());
			connection.updateStatus(it.getItemstatus());
			connection.updateType(it.getItemtype());
			connection.removeItem(it);
		}
	}
	
	/**
	 * Metoda usuwaj�ca komentarz z bazy danych
	 * @param c komentarz, kt�ry ma zosta� usuni�ty
	 */
	public void removeComment(Comment c) {
		if(c != null) {
			c.getItem().getComments().remove(c);
			c.getUser().getComments().remove(c);
			connection.updateItem(c.getItem());
			connection.updateUser(c.getUser());
			connection.removeComment(c);
		}
	}
	
	/**
	 * Metoda s�u��ca do tworzenia nowego projektu
	 * @param issuer u�ytkownik tworz�cy projekt
	 * @param pr nowy projekt, kt�ry ma zosta� dodany
	 */
	public void newProject(User issuer, Project pr) {
		issuer.getIsAdmin().add(pr);
		issuer.getProjects().add(pr);
		List<User> lst = new ArrayList<User>();
		lst.add(issuer);
		pr.setAdmins(lst);
		pr.setUsers(lst);
		connection.begin();
		connection.updateUser(issuer);
		connection.newProject(pr);
		connection.commit();
	}
	
	/**
	 * Metoda usuwaj�ca projekt i wszystkie jego zale�no�ci z bazy danych
	 * @param id identyfikator projektu, kt�ry ma zosta� usuni�ty
	 */
	public void removeProject(int id) {
		Project pr = connection.getProject(id);
		
		if(pr != null) {
			connection.begin();
			for(User a : pr.getAdmins()) {
				a.getIsAdmin().remove(pr);
				connection.updateUser(a);
			}
			for(User a : pr.getUsers()) {
				a.getProjects().remove(pr);
				connection.updateUser(a);
			}
			for(Item it : pr.getItems()) {
				this.removeItem(it);
			}
			connection.removeProject(pr);
			connection.commit();
		}
	}

	/**
	 * Metoda usuwaj�ca u�ytkownika z projektu
	 * @param id identyfikator projektu
	 * @param userid identyfikator u�ytkownika, kt�rego nale�y usun��
	 */
	public void removeUserFromProject(int id, int userid) {
        Project pr = connection.getProject(id);
        User usr = connection.getUser(userid);
        pr.getUsers().remove(usr);
        if(pr.getAdmins().contains(usr)) {
        	pr.getAdmins().remove(usr);
        }
        if(usr.getIsAdmin().contains(pr)) {
        	usr.getIsAdmin().remove(pr);
        }
        usr.getProjects().remove(pr);
        connection.begin();
        connection.updateUser(usr);
        connection.updateProject(pr);
        connection.commit();
        if(pr.getUsers().isEmpty()) {
        	this.removeProject(id);
        }
	}

	/**
	 * Metoda dodaj�ca u�ytkownika do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt u�ytkownika
	 */
	public void addUserToProject(Project pr, User usr) {
		User u = connection.getUser(usr.getLogin());
		pr.getUsers().add(u);
		u.getProjects().add(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(u);
		connection.commit();
	}

	/**
	 * Metoda dodaj�ca pro�b� u�ytkownika o do��czenie do projektu
	 * @param usr obiekt u�ytkownika
	 * @param id identyfikator projektu
	 */
	public void newRequestor(User usr, int id) {
		Project pr = connection.getProject(id);
		pr.getRequestors().add(usr);
		usr.getRequests().add(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(usr);
		connection.commit();
	}

	/**
	 * Metoda zwracaj�ca list� u�ytkownik�w, kt�rzy poprosili o dost�p
	 * @param usr u�ytkownik, kt�ry chce uzyska� list� u�ytkownik�w, kt�rzy poprosili o dost�p
	 * @param id identyfikator projektu
	 * @return lista u�ytkownik�w, kt�rzy poprosili o dost�p do projektu
	 */
	public List<User> getRequested(User usr, int id) {
		Project pr = connection.getProject(id);
		return pr.getRequestors();
	}

	/**
	 * Metoda s�u��ca do odrzucania pro�by o do��czenie do projektu
	 * @param usr u�ytkownik, kt�rego pro�ba jest odrzucana
	 * @param project identyfikator projektu
	 */
	public void denyAccess(User usr, int project) {
		Project pr = connection.getProject(project);
		pr.getRequestors().remove(usr);
		usr.getRequests().remove(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(usr);
		connection.commit();
	}

	/**
	 * Metoda s�u��ca do akceptacji pro�by o do��czenie do projektu
	 * @param user identyfikator u�ytkownika, kt�rego pro�ba jest akceptowana
	 * @param project identyfikator projektu
	 */
	public void acceptRequest(int project, int user) {
		Project pr = connection.getProject(project);
		User usr = connection.getUser(user);
		pr.getRequestors().remove(usr);
		pr.getUsers().add(usr);
		usr.getRequests().remove(pr);
		usr.getProjects().add(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(usr);
		connection.commit();
	}

	/**
	 * Metoda zwracaj�ca list� dost�pnych status�w
	 * @return lista dost�pnych status�w
	 */
	public List<Itemstatus> getStatuses() {
		return connection.getStatuses();
	}
	
	/**
	 * Metoda zwracaj�ca list� dost�pnych typ�w
	 * @return lista dost�pnych typ�w
	 */
	public List<Itemtype> getTypes() {
		return connection.getTypes();
	}

	/**
	 * Metoda dodaj�ca administratora do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt u�ytkownika, kt�ry ma zosta� dodany
	 */
	public void addAdminToProject(Project pr, User usr) {
		User u = connection.getUser(usr.getLogin());
		if(pr.getAdmins().contains(u)) {
			return;
		}
		pr.getAdmins().add(u);
		u.getIsAdmin().add(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(u);
		connection.commit();
	}

	/**
	 * Metoda pobieraj�ca list� projekt�w z bazy
	 * @return lista projekt�w z bazy
	 */
	public List<Project> getProjects() {
		return connection.getProjects();
	}

	/**
	 * Metoda pobieraj�ca status o zadanym identyfikatorze
	 * @param id identyfikator statusu
	 * @return status o zadym identyfikatorze
	 */
	public Itemstatus getStatus(int id) {
		for(Itemstatus t : connection.getStatuses()) {
			if (t.getStatusid() == id) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Metoda pobieraj�ca typ o zadanym identyfikatorze
	 * @param id identyfikator typu
	 * @return typ o zadym identyfikatorze
	 */
	public Itemtype getType(int id) {
		for(Itemtype t : connection.getTypes()) {
			if (t.getTypeid() == id) {
				return t;
			}
		}
		return null;
	}
}
