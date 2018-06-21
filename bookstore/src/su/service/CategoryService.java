package su.service;

import java.sql.SQLException;
import java.util.List;

import su.dao.CategoryDao;
import su.entity.Category;

public class CategoryService {
	private CategoryDao cdao=new CategoryDao();

	public List<Category> selectAll() throws SQLException {
		// TODO Auto-generated method stub
		return cdao.selectAll();
	}

	public Category selectById(String cid) throws SQLException {
		// TODO Auto-generated method stub
		return cdao.selectById(cid);
	}

	public void mod(Category category) throws SQLException {
		// TODO Auto-generated method stub
		cdao.mod(category);
	}

	public void add(Category category) throws SQLException {
		// TODO Auto-generated method stub
		cdao.add(category);
	}

	public void del(String cid) throws SQLException {
		// TODO Auto-generated method stub
		cdao.del(cid);
	}
}
