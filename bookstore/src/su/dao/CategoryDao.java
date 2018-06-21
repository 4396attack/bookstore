package su.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import su.entity.Category;

public class CategoryDao {
	private QueryRunner qr=new TxQueryRunner();
	public List<Category> selectAll() throws SQLException {
		String sql="select * from category";
		List<Category> result=qr.query(sql, new BeanListHandler<Category>(Category.class));
		return result;
		
	}
	public Category selectById(String cid) throws SQLException {
		String sql="select * from category where cid=?";
		Category category=qr.query(sql, new BeanHandler<Category>(Category.class), cid);
		
		return category;
	}
	public void mod(Category category) throws SQLException {
		// TODO Auto-generated method stub
		String sql="update category set cname=? where cid=?";
		Object[] params={category.getCname(),category.getCid()};
		qr.update(sql, params);
	}
	public void add(Category category) throws SQLException {
		// TODO Auto-generated method stub
		String sql="insert into category values(?,?)";
		Object[] params={category.getCid(),category.getCname()};
		qr.update(sql, params);
	}
	public void del(String cid) throws SQLException {
		// TODO Auto-generated method stub
		String sql="delete from category where cid=?";
		qr.update(sql, cid);
	}

}
