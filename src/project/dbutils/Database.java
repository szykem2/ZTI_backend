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
}
