package su.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;
import su.entity.User;

public class UserDao {
	private QueryRunner qr=new TxQueryRunner();
	public void addUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		String sql="insert into tb_user value(?,?,?,?,?,?)";
		Object[] params={user.getUid(),user.getUsername(),user.getPassword(),user.getEmail(),user.getCode(),user.getState()};
		qr.update(sql, params);
	}
	public User select(User user) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from tb_user where username=? and password=?";
		Object[] params={user.getUsername(),user.getPassword()};
		User result=qr.query(sql, new BeanHandler<User>(User.class), params);
		return result;
	}
	
}
