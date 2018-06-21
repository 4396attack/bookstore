package su.web.servlet.admin;

import cn.itcast.servlet.BaseServlet;
import su.entity.Order;
import su.service.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class adminorderServlet
 */
@WebServlet("/admin/adminorderServlet")
public class adminorderServlet extends BaseServlet {
	private OrderService oservice=new OrderService();
	public String all(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Order> list=oservice.all();
			request.setAttribute("list", list);
			return "f:/adminjsps/admin/order/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String selectByState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String state=request.getParameter("state");
		try {
			List<Order> list = oservice.selectByState(state);
			request.setAttribute("list", list);
			return "f:/adminjsps/admin/order/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		try {
			Order order=oservice.load(oid);
			if(order.getState()==1){
				
				oservice.send(oid);
				request.setAttribute("msg", "发货成功！");
				return "f:/adminjsps/admin/msg.jsp";
			}else{
				request.setAttribute("msg", "订单信息异常，发货失败！");
				return "f:/adminjsps/admin/msg.jsp";
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	

}
