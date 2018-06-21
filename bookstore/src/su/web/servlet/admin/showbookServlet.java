package su.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import su.entity.Book;
import su.entity.Category;
import su.service.BookService;
import su.service.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class showbookServlet
 */
@WebServlet("/admin/showbookServlet")
public class showbookServlet extends BaseServlet {
	private CategoryService cservice=new CategoryService();
	private BookService bservice=new BookService();
	public String show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Book> list=bservice.selectall();
			request.setAttribute("list", list);
			return "f:/adminjsps/admin/book/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String desc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		try {
			Book book=bservice.showbook(bid);
			List<Category> category=cservice.selectAll();
			request.setAttribute("category", category);
			request.setAttribute("book", book);
			return "f:/adminjsps/admin/book/desc.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String mod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Book book=CommonUtils.toBean(request.getParameterMap(), Book.class);
		String bid=request.getParameter("bid");
		String cid=request.getParameter("cid");
		try {
			Category category=cservice.selectById(cid);
			book.setBid(bid);
			book.setCategory(category);
			bservice.mod(book);
			return "f:/admin/showbookServlet?method=show";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 读取所有分类
	 * 跳转到add.jsp
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Category> category=cservice.selectAll();
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/book/add.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 删除指定图书
	 * 其实是假删除操作，即book中的del改为true 
	 * 显示图书室只显示del为false的图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		try {
			Book book=bservice.showbook(bid);
			bservice.del(book);
			return "f:/admin/showbookServlet?method=show";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
