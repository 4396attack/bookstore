package su.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import su.entity.Book;
import su.entity.Cart;
import su.entity.CartItem;
import su.service.BookService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class bookServlet
 */
@WebServlet("/bookServlet")
public class bookServlet extends BaseServlet {
	private BookService bservice=new BookService();
	
	public String selectBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String category=request.getParameter("category");
		if(category==null||category.trim().isEmpty()){
			try {
				List<Book> list=bservice.selectall();
				request.setAttribute("list", list);
				return "/jsps/book/list.jsp";
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			List<Book> list=bservice.selectByCategory(category);
			request.setAttribute("list", list);
			return "/jsps/book/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String showBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		if(bid!=null && !bid.trim().isEmpty()){
			Book book;
			try {
				book = bservice.showbook(bid);
				request.setAttribute("book",book);
				return "/jsps/book/desc.jsp";
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
