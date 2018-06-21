package su.service;

import java.sql.SQLException;
import java.util.List;

import su.dao.BookDao;
import su.entity.Book;

public class BookService {
	private BookDao bdao=new BookDao();

	public List<Book> selectall() throws SQLException {
		// TODO Auto-generated method stub
		return bdao.selectall();
	}

	public List<Book> selectByCategory(String category) throws SQLException {
		// TODO Auto-generated method stub
		return bdao.selectByCategory(category);
	}

	public Book showbook(String bid) throws SQLException {
		// TODO Auto-generated method stub
		return bdao.selectById(bid);
	}

	public void mod(Book book) throws SQLException {
		// TODO Auto-generated method stub
		bdao.mod(book);
	}

	public void add(Book book) throws SQLException {
		// TODO Auto-generated method stub
		bdao.add(book);
	}

	public void del(Book book) throws SQLException {
		// TODO Auto-generated method stub
		bdao.del(book);
	}
}
