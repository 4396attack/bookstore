package su.web.servlet;

import cn.itcast.servlet.BaseServlet;
import su.entity.Book;
import su.entity.Cart;
import su.entity.CartItem;
import su.service.BookService;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class cartServlet
 */
@WebServlet("/cartServlet")
public class cartServlet extends BaseServlet {
	private BookService bservice=new BookService();
	public String addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		String bid=request.getParameter("bid");
		int count=Integer.valueOf(request.getParameter("count"));
		try {
			/**
			 * 如果session未得到购物车，证明还未登入；
			 * 将请求打回登入界面
			 */
			if(cart==null){
				request.setAttribute("msg", "请先登入!");
				return "f:/jsps/order/msg.jsp";
			}
			Book book=bservice.showbook(bid);
			CartItem cartitem=new CartItem();
			cartitem.setBook(book);
			cartitem.setCount(count);
			cart.addItem(cartitem);
			return "f:/jsps/cart/list.jsp";

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid=request.getParameter("bid");
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
}
