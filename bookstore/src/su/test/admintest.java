package su.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import cn.itcast.commons.CommonUtils;
import su.entity.Book;
import su.entity.Category;
import su.service.BookService;
import su.service.CategoryService;

public class admintest {
	@Test
	public void fun(){
		CategoryService service=new CategoryService();
		try {
			List<Category> list=service.selectAll();
			System.out.println(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun2(){
		CategoryService service=new CategoryService();
		try {
			service.del("0FC7DCF631C44FCA815758890F3B38EE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun3(){
		BookService service=new BookService();
		try {
			List<Book> list=service.selectall();
			System.out.println(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun4(){
		BookService service=new BookService();
		Category category=new Category();
		category.setCid("1");
		Book book=new Book();
		book.setBid(CommonUtils.uuid());
		book.setBname("book");
		book.setPrice(19.9);
		book.setAuthor("mac");
		book.setCategory(category);
		
		try {
			System.out.println(book);
			service.add(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
