package project.dbutils;

import java.util.List;
 
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
 
import project.models.*;

class JPAConnection {
	private EntityManagerFactory managerFactory; // = Persistence.createEntityManagerFactory(persistenceUnitName);
    private EntityManager entityManager; // = managerFactory.createEntityManager();
    private EntityTransaction entityTransaction;
 
    public JPAConnection() {
        managerFactory = Persistence.createEntityManagerFactory("PU_DB2");
        entityManager = managerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction(); 
    }
 
    @SuppressWarnings("unchecked")
    public List<User> getUserList() {
        List<User> people = null;
        try {
            people = (List<User>) entityManager.createNamedQuery("User.findAll").getResultList();
            entityManager.close();
        } catch (Exception e) {
            System.out.println("Failed !!! " + e.getMessage());
        }
        return people;
    }
    
    public User getUser(int id) {
    	User usr = null;
    	try {
    		usr = (User) entityManager.find(User.class, id);
    		entityManager.close();
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return usr;
    }

	public User getUser(String login, String password) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findOne").setParameter("login", login).setParameter("pass", password).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    		entityManager.close();
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return usr;
	}

	public Project getProject(int id) {
		Project pr = null;
    	try {
    		pr = (Project) entityManager.find(Project.class, id);
    		entityManager.close();
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return pr;
	}
	
	public Boolean validateEmail(String email) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findEmail").setParameter("email", email).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    		entityManager.close();
    		return false;
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    		return true;
    	}
	}
	
	public Boolean validateLogin(String login) {
		User usr = null;
    	try {
    		List<User> lst = (List<User>) entityManager.createNamedQuery("User.findLogin").setParameter("login", login).getResultList();
    		System.out.println(lst);
    		usr = lst.get(0);
    		entityManager.close();
    		return false;
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    		return true;
    	}
	}

	public void newUser(User usr) {
		entityTransaction.begin();
		entityManager.persist(usr);
	    entityManager.flush();
		entityTransaction.commit();
	}

	public Item getItem(int id) {
		Item it = null;
    	try {
    		it = (Item) entityManager.find(Item.class, id);
    		entityManager.close();
    	}
    	catch (Exception e) {
    		System.out.println("Failed! " + e.getMessage());
    	}
    	return it;
	}
	
	public void newComment(Comment cmt) {
		entityTransaction.begin();
		entityManager.persist(cmt);
	    entityManager.flush();
		entityTransaction.commit();
	}
	
	public void newItem(Item it) {
		entityTransaction.begin();
		entityManager.persist(it);
	    entityManager.flush();
		entityTransaction.commit();
	}
	
	public void updateItem(Item it) {
		entityTransaction.begin();
	    entityManager.merge(it);
	    entityTransaction.commit();
	}
	
	public void removeItem(Item it) {
		System.out.println("Delete item - " + it.getTitle() + " ID: " + it.getItemid());
	    entityTransaction.begin();
	    entityManager.remove(it);
	    entityManager.flush();
	    entityTransaction.commit();
	}
	
	public void newProject(Project pr) {
		entityTransaction.begin();
		entityManager.persist(pr);
	    entityManager.flush();
		entityTransaction.commit();
	}
	
	public void removeProject(Project pr) {
		System.out.println("Delete item - " + pr.getName() + " ID: " + pr.getProjectid());
	    entityTransaction.begin();
	    entityManager.remove(pr);
	    entityManager.flush();
	    entityTransaction.commit();
	}
}
