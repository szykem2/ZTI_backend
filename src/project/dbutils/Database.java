package project.dbutils;

import project.models.*;
import java.util.*;

public class Database {
	private JPAConnection connection;
	
	public Database() {
		connection = new JPAConnection();
	}
	
	public List<User> getUsers() {
		return connection.getUserList();
	}
	
	public User getUser(int id) {
		return connection.getUser(id);
	}

	public User authorize(String login, String password) {
		return connection.getUser(login, password);
	}

	public Project getProject(int id) {
		return connection.getProject(id);
	}
	
	public Boolean validateLogin(String login) {
		return connection.validateLogin(login);
	}
	
	public Boolean validateEmail(String email) {
		return connection.validateEmail(email);
	}

	public void newUser(User usr) {
		connection.newUser(usr);
	}

	public Item getItem(int id) {
		return connection.getItem(id);
	}
	
	public void newComment(Comment cmt) {
		connection.newComment(cmt);
	}
	
	public void newItem(Item it) {
		connection.newItem(it);
		it.getProject().getItems().add(it);
		connection.updateProject(it.getProject());
	}
	
	public void updateItem(Item it) {
		connection.updateItem(it);
	}
	
	public void removeItem(int id) {
		Item it = connection.getItem(id);
		if(it != null) {
			it.getApprover().getIsApprover().remove(it);
			it.getOwner().getIsOwner().remove(it);
			connection.removeItem(it);
		}
	}
	
	public void newProject(User issuer, Project pr) {
		issuer.getIsAdmin().add(pr);
		issuer.getProjects().add(pr);
		List<User> lst = new ArrayList<User>();
		lst.add(issuer);
		pr.setAdmins(lst);
		pr.setUsers(lst);
		connection.updateUser(issuer);
		connection.newProject(pr);
	}
	
	public void removeProject(int id) {
		Project pr = connection.getProject(id);
		
		if(pr != null) {
			for(User a : pr.getAdmins()) {
				a.getIsAdmin().remove(pr);
				connection.updateUser(a);
			}
			for(User a : pr.getUsers()) {
				a.getProjects().remove(pr);
				connection.updateUser(a);
			}
			connection.removeProject(pr);
		}
	}

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
        connection.updateUser(usr);
        connection.updateProject(pr);
	}

	public void addUserToProject(Project pr, User usr) {
		User u = connection.getUser(usr.getLogin());
		pr.getUsers().add(u);
		u.getProjects().add(pr);
		connection.updateProject(pr);
		connection.updateUser(u);
	}

	public void newRequestor(User usr, int id) {
		Project pr = connection.getProject(id);
		pr.getRequestors().add(usr);
		usr.getRequests().add(pr);
		connection.updateProject(pr);
		connection.updateUser(usr);
	}

	public List<User> getRequested(User usr, int id) {
		Project pr = connection.getProject(id);
		return pr.getRequestors();
	}

	public void denyAccess(User usr, int project) {
		Project pr = connection.getProject(project);
		pr.getRequestors().remove(usr);
		usr.getRequests().remove(pr);
		System.out.println("denying");
		connection.updateProject(pr);
		connection.updateUser(usr);
	}

	public void acceptRequest(int project, int user) {
		Project pr = connection.getProject(project);
		User usr = connection.getUser(user);
		pr.getRequestors().remove(usr);
		pr.getUsers().add(usr);
		usr.getRequests().remove(pr);
		usr.getProjects().add(pr);
		connection.updateProject(pr);
		connection.updateUser(usr);
	}

	public List<Itemstatus> getStatuses() {
		return connection.getStatuses();
	}

	public List<Itemtype> getTypes() {
		return connection.getTypes();
	}

	public void addAdminToProject(Project pr, User usr) {
		User u = connection.getUser(usr.getLogin());
		pr.getUsers().add(u);
		u.getProjects().add(pr);
		connection.updateProject(pr);
		connection.updateUser(u);
	}

	public List<Project> getProjects() {
		return connection.getProjects();
	}

	public Itemstatus getStatus(int id) {
		for(Itemstatus t : connection.getStatuses()) {
			if (t.getStatusid() == id) {
				return t;
			}
		}
		return null;
	}

	public Itemtype getType(int id) {
		for(Itemtype t : connection.getTypes()) {
			if (t.getTypeid() == id) {
				return t;
			}
		}
		return null;
	}
}
