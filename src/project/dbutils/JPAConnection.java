
package project.dbutils;

import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
 
import project.models.*;

/**
 * Klasa odpowiadająca za udostępnianie interfejsu JPA do komunikacji z bazą danych IBM DB2
 */
@Singleton
public class JPAConnection {
	
	/**
	 * Obiekt tworzący managera podmiotu
	 */
	private static EntityManagerFactory managerFactory;
	
	/**
	 * Obiekt managera podmiotu
	 */
    private static EntityManager entityManager;
    
    /**
     * Obiekt udostępniający funkcjonalność transakcji
     */
    private static EntityTransaction entityTransaction;
    
    /**
     * flaga mówiąca o tym, czy operacja odbywa się w ramach transakcji
     */
    private boolean isWithinTransaction = false;
    
    /**
     * Konstruktor statyczny
     */
    static {
    	managerFactory = Persistence.createEntityManagerFactory("PU_DB2");
        entityManager = managerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction(); 
    }
 
    /**
     * Konstruktor obiektu
     */
    public JPAConnection() {
    }
    
    /**
     * Metoda rozpoczynająca transakcję
     */
    public void begin() {
    	entityTransaction.begin();
    	isWithinTransaction = true;
    }
    
    /**
     * Metoda kończąca transakcję
     */
    public void commit() {
    	entityTransaction.commit();
    	isWithinTransaction = false;
    }
 
    /**
     * Metoda zwracająca listę zarejestrowanych użytkowników
     * @return lista użytkowników
     */
    @SuppressWarnings("unchecked")
    public List<User> getUserList() {
        List<User> people = null;
        try {
            people = (List<User>) entityManager.createNamedQuery("User.findAll").getResultList();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }
        return people;
    }
    
    /**
	 * Metoda służy do pobierania użytkownika o zadanym id
	 * @param id identyfikator użytkownika
	 * @return użytkownik, którego id podano w argumencie
	 */
    public User getUser(int id) {
    	User usr = null;
    	try {
    		usr = (User) entityManager.find(User.class, id);
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return usr;
    }
    
    /**
	 * Metoda służąca to uwierzytelnienia użytkownika
	 * @param login nazwa użytkownika
	 * @param password hasło
	 * @return użytkownik odpowiadający podanym danym identyfikacyjnym
	 */
    @SuppressWarnings("unchecked")
	public User getUser(String login, String password) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findOne").setParameter("login", login).setParameter("pass", password).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return usr;
	}

	/**
	 * Metoda służąca do pobierania projektu o zadanym id
	 * @param id identyfikator projektu
	 * @return projekt, którego id zostało podane jako agument
	 */
	public Project getProject(int id) {
		Project pr = null;
    	try {
    		pr = (Project) entityManager.find(Project.class, id);
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return pr;
	}

	/**
	 * Metoda służąca do sprawdzania czy adres email znajduje się w bazie
	 * @param email adres email, który należy zwalidować
	 * @return wartość logiczna mówiąca o tym, czy adres jest dostępny
	 */
	@SuppressWarnings("unchecked")
	public Boolean validateEmail(String email) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findEmail").setParameter("email", email).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    		return false;
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    		return true;
    	}
	}

	/**
	 * Metoda służąca do sprawdzania czy login znajduje się w bazie
	 * @param login nazwa użytkownika, którą należy zwalidować
	 * @return wartość logiczna mówiąca o tym, czy nazwa użytkownika jest dostępna
	 */
	@SuppressWarnings("unchecked")
	public Boolean validateLogin(String login) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findLogin").setParameter("login", login).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    		return false;
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    		return true;
    	}
	}

	/**
	 * Metoda dodająca nowego użytkownika do bazy
	 * @param usr użytkownik, którego należy dodać
	 */
	public void newUser(User usr) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
			entityManager.persist(usr);
		    entityManager.flush();
			entityTransaction.commit();
		}
		else {
			entityManager.persist(usr);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda pobierająca z bazy element o zadanym id
	 * @param id identyfikator elementu
	 * @return element, o podanym identyfikatorze
	 */
	public Item getItem(int id) {
		Item it = null;
    	try {
    		it = (Item) entityManager.find(Item.class, id);
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return it;
	}

	/**
	 * Metoda dodająca do bazy danych nowy komentarz
	 * @param cmt obiekt komentarza, który należy dodać do bazy
	 */
	public void newComment(Comment cmt) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
			entityManager.persist(cmt);
		    entityManager.flush();
			entityTransaction.commit();
		}
		else {
			entityManager.persist(cmt);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda dodająca nowy element do bazy danych
	 * @param it obiekt elementu, który należy dodać
	 */
	public void newItem(Item it) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
			entityManager.persist(it);
		    entityManager.flush();
			entityTransaction.commit();
		}
		else {
			entityManager.persist(it);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda służąca do aktualizacji elementu w bazie
	 * @param it zmodyfikowany obiekt elementu
	 */
	public void updateItem(Item it) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.merge(it);
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(it);
		}
	}

	/**
	 * Metoda usuwająca element z bazy danych
	 * @param id identyfikator elementu, który ma zostać usunięty
	 */
	public void removeItem(Item it) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.remove(it);
		    entityManager.flush();
		    entityTransaction.commit();
		}
		else {
			entityManager.remove(it);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda służąca do tworzenia nowego projektu
	 * @param issuer użytkownik tworzący projekt
	 * @param pr nowy projekt, który ma zostać dodany
	 */
	public void newProject(Project pr) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
			entityManager.persist(pr);
		    entityManager.flush();
			entityTransaction.commit();
		}
		else {
			entityManager.persist(pr);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda usuwająca projekt i wszystkie jego zależności z bazy danych
	 * @param pr projekt, który ma zostać usunięty
	 */
	public void removeProject(Project pr) {
		pr.getAdmins().clear();
        pr.getItems().clear();
        pr.getUsers().clear();
		if(!isWithinTransaction) {
		    entityTransaction.begin();
		    entityManager.merge(pr);
		    entityManager.remove(pr);
		    entityManager.flush();
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(pr);
		    entityManager.remove(pr);
		    entityManager.flush();
		}
	}

	/**
	 * Metoda aktualizująca użytkownika
	 * @param issuer użytkownik, który ma zostać zaktualizowany
	 */
	public void updateUser(User issuer) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.merge(issuer);
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(issuer);
		}
	}

	/**
	 * Metoda aktualizująca projekt
	 * @param pr projekt, który ma zostać zaktualizowany
	 */
	public void updateProject(Project pr) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.merge(pr);
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(pr);
		}
	}

	/**
	 * Metoda pobierająca użytkownika o zadanej nazwie użytkownika
	 * @param login nazwa użtykownika
	 * @return użytkownik, do którego pryzpisany jest dany login
	 */
	@SuppressWarnings("unchecked")
	public User getUser(String login) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findLogin").setParameter("login", login).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return usr;
	}

	/**
	 * Metoda pobierająca dostępne statusy
	 * @return lista statusów
	 */
	@SuppressWarnings("unchecked")
	public List<Itemstatus> getStatuses() {
		List<Itemstatus> sts = null;
        try {
            sts = (List<Itemstatus>) entityManager.createNamedQuery("Itemstatus.findAll").getResultList();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }
        return sts;
	}
	
	/**
	 * Metoda pobierająca dostępne typy
	 * @return lista typów
	 */
	@SuppressWarnings("unchecked")
	public List<Itemtype> getTypes() {
		List<Itemtype> types = null;
        try {
        	types = (List<Itemtype>) entityManager.createNamedQuery("Itemtype.findAll").getResultList();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }
        return types;
	}

	/**
	 * Metoda pobierająca projekty z bazy danych
	 * @return lista utworzonych projektów
	 */
	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		List<Project> pr = null;
        try {
            pr = (List<Project>) entityManager.createNamedQuery("Project.findAll").getResultList();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }
        return pr;
	}
	
	/**
	 * Metoda aktualizująca status
	 * @param itemstatus status
	 */
	public void updateStatus(Itemstatus itemstatus) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.merge(itemstatus);
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(itemstatus);
		}
	}

	/**
	 * Metoda aktualizująca typ
	 * @param itemtype typ
	 */
	public void updateType(Itemtype itemtype) {
		if(!isWithinTransaction) {
			entityTransaction.begin();
		    entityManager.merge(itemtype);
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(itemtype);
		}
	}

	/**
	 * Metoda usuwająca komentarz z bazy danych
	 * @param c komentarz, który ma zostać usunięty
	 */
	public void removeComment(Comment c) {
		if(!isWithinTransaction) {
		    entityTransaction.begin();
		    entityManager.merge(c);
		    entityManager.remove(c);
		    entityManager.flush();
		    entityTransaction.commit();
		}
		else {
			entityManager.merge(c);
		    entityManager.remove(c);
		    entityManager.flush();
		}
	}
}