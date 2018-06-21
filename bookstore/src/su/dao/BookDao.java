package su.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import su.entity.Book;
import su.entity.Category;

public class BookDao {
	private QueryRunner qr=new TxQueryRunner();
	public List<Book> selectall() throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from book,category where book.cid=category.cid and book.del=0";
		List<Book> books=new ArrayList<Book>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=qr.query(sql, new MapListHandler());
		for(Map<String,Object> map:list){
			Book book=CommonUtils.toBean(map, Book.class);
			Category category=CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			books.add(book);
		}
		return books;
	}
	public List<Book> selectByCategory(String c) throws SQLException {
		String sql="select * from book,category where book.cid=category.cid and book.cid=? and book.del=0";
		List<Book> books=new ArrayList<Book>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=qr.query(sql, new MapListHandler(),c);
		for(Map<String,Object> map:list){
			Book book=CommonUtils.toBean(map, Book.class);
			Category category=CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			books.add(book);
		}
		return books;
	}
	public Book selectById(String bid) throws SQLException {
		String sql="select * from book,category where book.cid=category.cid and book.bid=? and book.del=0";
		Map<String,Object> map=new HashMap<String,Object>();
		map=qr.query(sql, new MapHandler(), bid);
		Book book=CommonUtils.toBean(map, Book.class);
		Category category=CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		return book;
	}
	public void mod(Book book) throws SQLException {
		// TODO Auto-generated method stub
		String sql="update book set bname=?,price=?,author=?,cid=? where bid=? and book.del=0";
		Object[] params={book.getBname(),book.getPrice(),book.getAuthor(),book.getCategory().getCid(),book.getBid()};
		qr.update(sql, params);
	}
	public void add(Book book) throws SQLException {
		String sql="insert into book values(?,?,?,?,?,?,?)";
		Object[] params={book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),book.getImage(),book.getCategory().getCid(),book.isDel()};
		qr.update(sql, params);
		
	}
	public void del(Book book) throws SQLException {
		String sql="update book set del=1 where bid=?";
		qr.update(sql, book.getBid());
		
	}

}
