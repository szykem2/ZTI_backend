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
	}
	
	public void updateItem(Item it) {
		connection.updateItem(it);
	}
	
	public void removeItem(int id) {
		Item it = connection.getItem(id);
		if(it != null) {
			connection.removeItem(it);
		}
	}
	
	public void newProject(Project pr) {
		connection.newProject(pr);
	}
	
	public void removeProject(int id) {
		Project pr = connection.getProject(id);
		if(pr != null) {
			connection.removeProject(pr);
		}
	}
}
