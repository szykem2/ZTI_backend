package project.dbutils;

import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
 
import project.models.*;

/**
 * Klasa odpowiadaj¹ca za udostêpnianie interfejsu JPA do komunikacji z baz¹ danych IBM DB2
 */
@Singleton
class JPAConnection {
	
	/**
	 * Obiekt tworz¹cy managera podmiotu
	 */
	private static EntityManagerFactory managerFactory;
	
	/**
	 * Obiekt managera podmiotu
	 */
    private static EntityManager entityManager;
    
    /**
     * Obiekt udostêpniaj¹cy funkcjonalnoœæ transakcji
     */
    private static EntityTransaction entityTransaction;
    
    /**
     * flaga mówi¹ca o tym, czy operacja odbywa siê w ramach transakcji
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
     * Metoda rozpoczynaj¹ca transakcjê
     */
    public void begin() {
    	entityTransaction.begin();
    	isWithinTransaction = true;
    }
    
    /**
     * Metoda koñcz¹ca transakcjê
     */
    public void commit() {
    	entityTransaction.commit();
    	isWithinTransaction = false;
    }
 
    /**
     * Metoda zwracaj¹ca listê zarejestrowanych u¿ytkowników
     * @return lista u¿ytkowników
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
	 * Metoda s³u¿y do pobierania u¿ytkownika o zadanym id
	 * @param id identyfikator u¿ytkownika
	 * @return u¿ytkownik, którego id podano w argumencie
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
	 * Metoda s³u¿¹ca to uwierzytelnienia u¿ytkownika
	 * @param login nazwa u¿ytkownika
	 * @param password has³o
	 * @return u¿ytkownik odpowiadaj¹cy podanym danym identyfikacyjnym
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
	 * Metoda s³u¿¹ca do pobierania projektu o zadanym id
	 * @param id identyfikator projektu
	 * @return projekt, którego id zosta³o podane jako agument
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
	 * Metoda s³u¿¹ca do sprawdzania czy adres email znajduje siê w bazie
	 * @param email adres email, który nale¿y zwalidowaæ
	 * @return wartoœæ logiczna mówi¹ca o tym, czy adres jest dostêpny
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
	 * Metoda s³u¿¹ca do sprawdzania czy login znajduje siê w bazie
	 * @param login nazwa u¿ytkownika, któr¹ nale¿y zwalidowaæ
	 * @return wartoœæ logiczna mówi¹ca o tym, czy nazwa u¿ytkownika jest dostêpna
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
	 * Metoda dodaj¹ca nowego u¿ytkownika do bazy
	 * @param usr u¿ytkownik, którego nale¿y dodaæ
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
	 * Metoda pobieraj¹ca z bazy element o zadanym id
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
	 * Metoda dodaj¹ca do bazy danych nowy komentarz
	 * @param cmt obiekt komentarza, który nale¿y dodaæ do bazy
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
	 * Metoda dodaj¹ca nowy element do bazy danych
	 * @param it obiekt elementu, który nale¿y dodaæ
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
	 * Metoda s³u¿¹ca do aktualizacji elementu w bazie
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
	 * Metoda usuwaj¹ca element z bazy danych
	 * @param id identyfikator elementu, który ma zostaæ usuniêty
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
	 * Metoda s³u¿¹ca do tworzenia nowego projektu
	 * @param issuer u¿ytkownik tworz¹cy projekt
	 * @param pr nowy projekt, który ma zostaæ dodany
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
	 * Metoda usuwaj¹ca projekt i wszystkie jego zale¿noœci z bazy danych
	 * @param pr projekt, który ma zostaæ usuniêty
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
	 * Metoda aktualizuj¹ca u¿ytkownika
	 * @param issuer u¿ytkownik, który ma zostaæ zaktualizowany
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
	 * Metoda aktualizuj¹ca projekt
	 * @param pr projekt, który ma zostaæ zaktualizowany
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
	 * Metoda pobieraj¹ca u¿ytkownika o zadanej nazwie u¿ytkownika
	 * @param login nazwa u¿tykownika
	 * @return u¿ytkownik, do którego pryzpisany jest dany login
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
	 * Metoda pobieraj¹ca dostêpne statusy
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
	 * Metoda pobieraj¹ca dostêpne typy
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
	 * Metoda pobieraj¹ca projekty z bazy danych
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
	 * Metoda aktualizuj¹ca status
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
	 * Metoda aktualizuj¹ca typ
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
	 * Metoda usuwaj¹ca komentarz z bazy danych
	 * @param c komentarz, który ma zostaæ usuniêty
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
