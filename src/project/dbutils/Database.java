package project.dbutils;

import project.models.*;

import javax.ejb.Singleton;
import java.util.*;

/**
 * Klasa udostêpniaj¹ca interfejs bazodanowy
 */
@Singleton
public class Database {
	/**
	 * Obiekt, który udostêpnia funkcjonalnoœæ JPA
	 */
	private static final JPAConnection connection = new JPAConnection();
	
	/**
	 * Konstruktor obiektu
	 */
	public Database() {
	}
	
	/**
	 * Metoda s³u¿y do pobierania wszystkich u¿ytkowników z bazy danych
	 * @return lista u¿ytkowników
	 */
	public List<User> getUsers() {
		return connection.getUserList();
	}
	
	/**
	 * Metoda s³u¿y do pobierania u¿ytkownika o zadanym id
	 * @param id identyfikator u¿ytkownika
	 * @return u¿ytkownik, którego id podano w argumencie
	 */
	public User getUser(int id) {
		return connection.getUser(id);
	}

	/**
	 * Metoda s³u¿¹ca to uwierzytelnienia u¿ytkownika
	 * @param login nazwa u¿ytkownika
	 * @param password has³o
	 * @return u¿ytkownik odpowiadaj¹cy podanym danym identyfikacyjnym
	 */
	public User authorize(String login, String password) {
		return connection.getUser(login, password);
	}

	/**
	 * Metoda s³u¿¹ca do pobierania projektu o zadanym id
	 * @param id identyfikator projektu
	 * @return projekt, którego id zosta³o podane jako agument
	 */
	public Project getProject(int id) {
		return connection.getProject(id);
	}
	
	/**
	 * Metoda s³u¿¹ca do sprawdzania czy login znajduje siê w bazie
	 * @param login nazwa u¿ytkownika, któr¹ nale¿y zwalidowaæ
	 * @return wartoœæ logiczna mówi¹ca o tym, czy nazwa u¿ytkownika jest dostêpna
	 */
	public Boolean validateLogin(String login) {
		return connection.validateLogin(login);
	}
	
	/**
	 * Metoda s³u¿¹ca do sprawdzania czy adres email znajduje siê w bazie
	 * @param email adres email, który nale¿y zwalidowaæ
	 * @return wartoœæ logiczna mówi¹ca o tym, czy adres jest dostêpny
	 */
	public Boolean validateEmail(String email) {
		return connection.validateEmail(email);
	}

	/**
	 * Metoda dodaj¹ca nowego u¿ytkownika do bazy
	 * @param usr u¿ytkownik, którego nale¿y dodaæ
	 */
	public void newUser(User usr) {
		connection.newUser(usr);
	}

	/**
	 * Metoda pobieraj¹ca z bazy element o zadanym id
	 * @param id identyfikator elementu
	 * @return element, o podanym identyfikatorze
	 */
	public Item getItem(int id) {
		return connection.getItem(id);
	}
	
	/**
	 * Metoda dodaj¹ca do bazy danych nowy komentarz
	 * @param cmt obiekt komentarza, który nale¿y dodaæ do bazy
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
	 * Metoda dodaj¹ca nowy element do bazy danych
	 * @param it obiekt elementu, który nale¿y dodaæ
	 */
	public void newItem(Item it) {
		connection.begin();
		connection.newItem(it);
		it.getProject().getItems().add(it);
		connection.updateProject(it.getProject());
		connection.commit();
	}
	
	/**
	 * Metoda s³u¿¹ca do aktualizacji elementu w bazie
	 * @param it zmodyfikowany obiekt elementu
	 */
	public void updateItem(Item it) {
		connection.updateItem(it);
	}
	
	/**
	 * Metoda usuwaj¹ca element z bazy danych
	 * @param id identyfikator elementu, który ma zostaæ usuniêty
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
	 * Metoda usuwaj¹ca element z bazy danych
	 * @param id element, który ma zostaæ usuniêty
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
	 * Metoda usuwaj¹ca komentarz z bazy danych
	 * @param c komentarz, który ma zostaæ usuniêty
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
	 * Metoda s³u¿¹ca do tworzenia nowego projektu
	 * @param issuer u¿ytkownik tworz¹cy projekt
	 * @param pr nowy projekt, który ma zostaæ dodany
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
	 * Metoda usuwaj¹ca projekt i wszystkie jego zale¿noœci z bazy danych
	 * @param id identyfikator projektu, który ma zostaæ usuniêty
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
	 * Metoda usuwaj¹ca u¿ytkownika z projektu
	 * @param id identyfikator projektu
	 * @param userid identyfikator u¿ytkownika, którego nale¿y usun¹æ
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
	 * Metoda dodaj¹ca u¿ytkownika do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt u¿ytkownika
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
	 * Metoda dodaj¹ca proœbê u¿ytkownika o do³¹czenie do projektu
	 * @param usr obiekt u¿ytkownika
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
	 * Metoda zwracaj¹ca listê u¿ytkowników, którzy poprosili o dostêp
	 * @param usr u¿ytkownik, który chce uzyskaæ listê u¿ytkowników, którzy poprosili o dostêp
	 * @param id identyfikator projektu
	 * @return lista u¿ytkowników, którzy poprosili o dostêp do projektu
	 */
	public List<User> getRequested(User usr, int id) {
		Project pr = connection.getProject(id);
		return pr.getRequestors();
	}

	/**
	 * Metoda s³u¿¹ca do odrzucania proœby o do³¹czenie do projektu
	 * @param usr u¿ytkownik, którego proœba jest odrzucana
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
	 * Metoda s³u¿¹ca do akceptacji proœby o do³¹czenie do projektu
	 * @param user identyfikator u¿ytkownika, którego proœba jest akceptowana
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
	 * Metoda zwracaj¹ca listê dostêpnych statusów
	 * @return lista dostêpnych statusów
	 */
	public List<Itemstatus> getStatuses() {
		return connection.getStatuses();
	}
	
	/**
	 * Metoda zwracaj¹ca listê dostêpnych typów
	 * @return lista dostêpnych typów
	 */
	public List<Itemtype> getTypes() {
		return connection.getTypes();
	}

	/**
	 * Metoda dodaj¹ca administratora do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt u¿ytkownika, który ma zostaæ dodany
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
	 * Metoda pobieraj¹ca listê projektów z bazy
	 * @return lista projektów z bazy
	 */
	public List<Project> getProjects() {
		return connection.getProjects();
	}

	/**
	 * Metoda pobieraj¹ca status o zadanym identyfikatorze
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
	 * Metoda pobieraj¹ca typ o zadanym identyfikatorze
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
