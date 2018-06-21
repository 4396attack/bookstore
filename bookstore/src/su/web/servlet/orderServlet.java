package su.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import su.entity.Book;
import su.entity.Cart;
import su.entity.CartItem;
import su.entity.Order;
import su.entity.OrderItem;
import su.entity.User;
import su.exception.OrderException;
import su.service.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class orderServlet
 */
@WebServlet("/orderServlet")
public class orderServlet extends BaseServlet {
	private OrderService oservice=new OrderService();
	/**
	 * 生成订单
	 * 1.根据传来的购物车商品条目，生产订单对象；
	 * 2.转发到订单显示页面
	 * 3.清空购物车信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String order(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 得到购物车信息
		 */
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		Collection<CartItem> list=(Collection<CartItem>) cart.getItems();
		List<OrderItem> orderitems=new ArrayList<OrderItem>();
		/**
		 * 创建订单，并为订单设置信息
		 */
		Order order=new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(0);
		order.setUser((User) request.getSession().getAttribute("user"));
		order.setTotal(cart.getTotal());
		/**
		 * 遍历购物车条目，将商品条目添加到订单中
		 */
		for(CartItem c:list){
			OrderItem oitem=new OrderItem();
			Book book=c.getBook();
			int count=c.getCount();
			oitem.setBook(book);
			oitem.setIid(CommonUtils.uuid());
			oitem.setOrder(order);
			oitem.setCount(count);
			orderitems.add(oitem);
		}
		order.setItemlist(orderitems);
		/**
		 * 清空购物车
		 */
		cart.clear();
		/**
		 * 调用service中的方法，将订单信息保存到数据库中
		 */
		oservice.addOrder(order);
		/**
		 * 转发到订单显示页面
		 */
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	public String myorder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid=request.getParameter("uid");
		try {
			List<Order> orders=oservice.selectById(uid);
			request.setAttribute("orders", orders);
			return "f:/jsps/order/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 1加载指定订单信息,存储到request域中
	 * 2转发到订单显示页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		try {
			Order order=oservice.load(oid);
			request.setAttribute("order", order);
			return "f:/jsps/order/desc.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 订单在线支付功能
	 * 1.根据表单收货地址，修改订单的address
	 * 2.配置易宝支付的13+1参数
	 * 3.跳往易宝支付网关
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		String address=request.getParameter("address");
		try {
			Order order=oservice.load(oid);
			order.setAddress(address);
			oservice.addAddress(order);
			Properties prop=new Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties"));
			/**
			 * 设置13+1参数
			 */
			String p0_Cmd="Buy";
			String p1_MerId=prop.getProperty("p1_MerId");
			String p2_Order=oid;
			String p3_Amt="0.01";
			String p4_Cur="CNY";
			String p5_Pid="";
			String p6_Pcat="";
			String p7_Pdesc="";
			String p8_Url=prop.getProperty("p8_Url");
			String p9_SAF="";
			String pa_MP="";
			String pd_FrpId=request.getParameter("pd_FrpId");
			String pr_NeedResponse="1";
			String keyValue=prop.getProperty("keyValue");
			/**
			 * 根据13参数计算hmac值
			 */
			String hmac=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
												p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
			System.out.println(hmac);
			/**
			 * 生成支付网关url（携带13参数）
			 */
			StringBuilder url=new StringBuilder(prop.getProperty("url"));
			url.append("?p0_Cmd="+p0_Cmd);
			url.append("&p1_MerId="+p1_MerId);
			url.append("&p2_Order="+p2_Order);
			url.append("&p3_Amt="+p3_Amt);
			url.append("&p4_Cur="+p4_Cur);
			url.append("&p5_Pid="+p5_Pid);
			url.append("&p6_Pcat="+p6_Pcat);
			url.append("&p7_Pdesc="+p7_Pdesc);
			url.append("&p8_Url="+p8_Url);
			url.append("&p9_SAF="+p9_SAF);
			url.append("&pa_MP="+pa_MP);
			url.append("&pd_FrpId="+pd_FrpId);
			url.append("&pr_NeedResponse="+pr_NeedResponse);
			url.append("&hmac="+hmac);
			response.sendRedirect(url.toString());
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 易宝支付的回调函数，当用户支付成功后将会重定向到指定url(返回11+1参数)
	 * 1.修改订单状态
	 * 2.显示成功信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 获取易宝支付网关发来的11+1参数，做后台校验，检验请求的合法性
		 */
		Properties prop=new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties"));
		String keyValue=prop.getProperty("keyValue");
		String p1_MerId=request.getParameter("p1_MerId");
		String r0_Cmd=request.getParameter("r0_Cmd");
		String r1_Code=request.getParameter("r1_Code");
		String r2_TrxId=request.getParameter("r2_TrxId");
		String r3_Amt=request.getParameter("r3_Amt");
		String r4_Cur=request.getParameter("r4_Cur");
		String r5_Pid=request.getParameter("r5_Pid");
		String r6_Order=request.getParameter("r6_Order");
		String r7_Uid=request.getParameter("r7_Uid");
		String r8_MP=request.getParameter("r8_MP");
		String r9_BType=request.getParameter("r9_BType");
		String hmac=request.getParameter("hmac");
		/**
		 * 如果检验为非法请求，则保存错误信息，显示到msg.jsp
		 */
		boolean result=PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, 
				r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,keyValue );
		System.out.println(result);
		if(!result){
			request.setAttribute("msg", "非法请求！");
			System.out.println("true1");
			return "f:/jsps/msg.jsp";
		}
		/**
		 * 检验通过，修改订单信息
		 * 保存成功信息，转发到msg.jsp
		 */
		System.out.println(false);
		try {
			oservice.pay(r6_Order);
			/**
			 * 如果交易结果返回类型为2，为点对点通讯，需回馈一个success；
			 */
			if(r9_BType.equals("2")){
				response.getWriter().println("success");
			}
			request.setAttribute("msg", "支付成功，等待卖家发货！");
			return "f:/jsps/msg.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 确认收货
	 * 当订单状态为未收货时才可以进行确认收货
	 * 完成收货后返回到msg.jsp显示信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		try {
			Order order=oservice.load(oid);
			/**
			 * 若订单状态不是已发货状态则抛出异常
			 * 将错误信息返回到msg.jsp
			 */
			if(order.getState()!=2){
				throw new OrderException("订单信息异常，确认收货失败！");
			}
			/**
			 * 若订单状态为已发货则修改成 订单完成状态
			 * 将结果信息返回到msg.jsp
			 */
			oservice.confirm(oid);
			
			request.setAttribute("msg", "收货成功！");
			return "f:/jsps/order/msg.jsp";
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}catch(OrderException e){
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/order/msg.jsp";
		}
	}
}
