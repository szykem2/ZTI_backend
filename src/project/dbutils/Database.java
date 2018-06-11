
package project.dbutils;

import project.models.*;

import javax.ejb.Singleton;
import java.util.*;

/**
 * Klasa udostępniająca interfejs bazodanowy
 */
@Singleton
public class Database {
	/**
	 * Obiekt, który udostępnia funkcjonalność JPA
	 */
	private static final JPAConnection connection = new JPAConnection();
	
	/**
	 * Konstruktor obiektu
	 */
	public Database() {
	}
	
	/**
	 * Metoda służy do pobierania wszystkich użytkowników z bazy danych
	 * @return lista użytkowników
	 */
	public List<User> getUsers() {
		return connection.getUserList();
	}
	
	/**
	 * Metoda służy do pobierania użytkownika o zadanym id
	 * @param id identyfikator użytkownika
	 * @return użytkownik, którego id podano w argumencie
	 */
	public User getUser(int id) {
		return connection.getUser(id);
	}

	/**
	 * Metoda służąca to uwierzytelnienia użytkownika
	 * @param login nazwa użytkownika
	 * @param password hasło
	 * @return użytkownik odpowiadający podanym danym identyfikacyjnym
	 */
	public User authorize(String login, String password) {
		return connection.getUser(login, password);
	}

	/**
	 * Metoda służąca do pobierania projektu o zadanym id
	 * @param id identyfikator projektu
	 * @return projekt, którego id zostało podane jako agument
	 */
	public Project getProject(int id) {
		return connection.getProject(id);
	}
	
	/**
	 * Metoda służąca do sprawdzania czy login znajduje się w bazie
	 * @param login nazwa użytkownika, którą należy zwalidować
	 * @return wartość logiczna mówiąca o tym, czy nazwa użytkownika jest dostępna
	 */
	public Boolean validateLogin(String login) {
		return connection.validateLogin(login);
	}
	
	/**
	 * Metoda służąca do sprawdzania czy adres email znajduje się w bazie
	 * @param email adres email, który należy zwalidować
	 * @return wartość logiczna mówiąca o tym, czy adres jest dostępny
	 */
	public Boolean validateEmail(String email) {
		return connection.validateEmail(email);
	}

	/**
	 * Metoda dodająca nowego użytkownika do bazy
	 * @param usr użytkownik, którego należy dodać
	 */
	public void newUser(User usr) {
		connection.newUser(usr);
	}

	/**
	 * Metoda pobierająca z bazy element o zadanym id
	 * @param id identyfikator elementu
	 * @return element, o podanym identyfikatorze
	 */
	public Item getItem(int id) {
		return connection.getItem(id);
	}
	
	/**
	 * Metoda dodająca do bazy danych nowy komentarz
	 * @param cmt obiekt komentarza, który należy dodać do bazy
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
	 * Metoda dodająca nowy element do bazy danych
	 * @param it obiekt elementu, który należy dodać
	 */
	public void newItem(Item it) {
		connection.begin();
		connection.newItem(it);
		it.getProject().getItems().add(it);
		connection.updateProject(it.getProject());
		connection.commit();
	}
	
	/**
	 * Metoda służąca do aktualizacji elementu w bazie
	 * @param it zmodyfikowany obiekt elementu
	 */
	public void updateItem(Item it) {
		connection.updateItem(it);
	}
	
	/**
	 * Metoda usuwająca element z bazy danych
	 * @param id identyfikator elementu, który ma zostać usunięty
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
	 * Metoda usuwająca element z bazy danych
	 * @param it element, który ma zostać usunięty
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
	 * Metoda usuwająca komentarz z bazy danych
	 * @param c komentarz, który ma zostać usunięty
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
	 * Metoda służąca do tworzenia nowego projektu
	 * @param issuer użytkownik tworzący projekt
	 * @param pr nowy projekt, który ma zostać dodany
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
	 * Metoda usuwająca projekt i wszystkie jego zależności z bazy danych
	 * @param id identyfikator projektu, który ma zostać usunięty
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
	 * Metoda usuwająca użytkownika z projektu
	 * @param id identyfikator projektu
	 * @param userid identyfikator użytkownika, którego należy usunąć
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
	 * Metoda dodająca użytkownika do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt użytkownika
	 */
	public void addUserToProject(Project pr, User usr) {
		User u = connection.getUser(usr.getLogin());
		if(pr.getRequestors().contains(usr)) {
			pr.getRequestors().remove(usr);
		}
		pr.getUsers().add(u);
		u.getProjects().add(pr);
		connection.begin();
		connection.updateProject(pr);
		connection.updateUser(u);
		connection.commit();
	}

	/**
	 * Metoda dodająca prośbę użytkownika o dołączenie do projektu
	 * @param usr obiekt użytkownika
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
	 * Metoda zwracająca listę użytkowników, którzy poprosili o dostęp
	 * @param usr użytkownik, który chce uzyskać listę użytkowników, którzy poprosili o dostęp
	 * @param id identyfikator projektu
	 * @return lista użytkowników, którzy poprosili o dostęp do projektu
	 */
	public List<User> getRequested(User usr, int id) {
		Project pr = connection.getProject(id);
		return pr.getRequestors();
	}

	/**
	 * Metoda służąca do odrzucania prośby o dołączenie do projektu
	 * @param usr użytkownik, którego prośba jest odrzucana
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
	 * Metoda służąca do akceptacji prośby o dołączenie do projektu
	 * @param user identyfikator użytkownika, którego prośba jest akceptowana
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
	 * Metoda zwracająca listę dostępnych statusów
	 * @return lista dostępnych statusów
	 */
	public List<Itemstatus> getStatuses() {
		return connection.getStatuses();
	}
	
	/**
	 * Metoda zwracająca listę dostępnych typów
	 * @return lista dostępnych typów
	 */
	public List<Itemtype> getTypes() {
		return connection.getTypes();
	}

	/**
	 * Metoda dodająca administratora do projektu
	 * @param pr obiekt projektu
	 * @param usr obiekt użytkownika, który ma zostać dodany
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
	 * Metoda pobierająca listę projektów z bazy
	 * @return lista projektów z bazy
	 */
	public List<Project> getProjects() {
		return connection.getProjects();
	}

	/**
	 * Metoda pobierająca status o zadanym identyfikatorze
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
	 * Metoda pobierająca typ o zadanym identyfikatorze
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