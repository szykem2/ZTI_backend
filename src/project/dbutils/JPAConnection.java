package project.dbutils;

import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
 
import project.models.*;

@Singleton
class JPAConnection {
	private EntityManagerFactory managerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private boolean isWithinTransaction = false;
 
    public JPAConnection() {
        managerFactory = Persistence.createEntityManagerFactory("PU_DB2");
        entityManager = managerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction(); 
    }
    
    public void begin() {
    	entityTransaction.begin();
    	isWithinTransaction = true;
    }
    
    public void commit() {
    	entityTransaction.commit();
    	isWithinTransaction = false;
    }
    
    public void closeConnection() {
    	//entityManager.close();
    }
 
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
}
