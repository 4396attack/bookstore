package su.service;

import java.sql.SQLException;

import su.dao.UserDao;
import su.entity.User;

public class UserService {
	private UserDao udao=new UserDao();

	public void addUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		udao.addUser(user);
	}

	public User select(User user) throws SQLException {
		// TODO Auto-generated method stub
		return udao.select(user);
	}
}
