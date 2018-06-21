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
	 * ���ɶ���
	 * 1.���ݴ����Ĺ��ﳵ��Ʒ��Ŀ��������������
	 * 2.ת����������ʾҳ��
	 * 3.��չ��ﳵ��Ϣ
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String order(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * �õ����ﳵ��Ϣ
		 */
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		Collection<CartItem> list=(Collection<CartItem>) cart.getItems();
		List<OrderItem> orderitems=new ArrayList<OrderItem>();
		/**
		 * ������������Ϊ����������Ϣ
		 */
		Order order=new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(0);
		order.setUser((User) request.getSession().getAttribute("user"));
		order.setTotal(cart.getTotal());
		/**
		 * �������ﳵ��Ŀ������Ʒ��Ŀ��ӵ�������
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
		 * ��չ��ﳵ
		 */
		cart.clear();
		/**
		 * ����service�еķ�������������Ϣ���浽���ݿ���
		 */
		oservice.addOrder(order);
		/**
		 * ת����������ʾҳ��
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
	 * 1����ָ��������Ϣ,�洢��request����
	 * 2ת����������ʾҳ��
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
	 * ��������֧������
	 * 1.���ݱ��ջ���ַ���޸Ķ�����address
	 * 2.�����ױ�֧����13+1����
	 * 3.�����ױ�֧������
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
			 * ����13+1����
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
			 * ����13��������hmacֵ
			 */
			String hmac=PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
												p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
			System.out.println(hmac);
			/**
			 * ����֧������url��Я��13������
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
	 * �ױ�֧���Ļص����������û�֧���ɹ��󽫻��ض���ָ��url(����11+1����)
	 * 1.�޸Ķ���״̬
	 * 2.��ʾ�ɹ���Ϣ
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * ��ȡ�ױ�֧�����ط�����11+1����������̨У�飬��������ĺϷ���
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
		 * �������Ϊ�Ƿ������򱣴������Ϣ����ʾ��msg.jsp
		 */
		boolean result=PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, 
				r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,keyValue );
		System.out.println(result);
		if(!result){
			request.setAttribute("msg", "�Ƿ�����");
			System.out.println("true1");
			return "f:/jsps/msg.jsp";
		}
		/**
		 * ����ͨ�����޸Ķ�����Ϣ
		 * ����ɹ���Ϣ��ת����msg.jsp
		 */
		System.out.println(false);
		try {
			oservice.pay(r6_Order);
			/**
			 * ������׽����������Ϊ2��Ϊ��Ե�ͨѶ�������һ��success��
			 */
			if(r9_BType.equals("2")){
				response.getWriter().println("success");
			}
			request.setAttribute("msg", "֧���ɹ����ȴ����ҷ�����");
			return "f:/jsps/msg.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * ȷ���ջ�
	 * ������״̬Ϊδ�ջ�ʱ�ſ��Խ���ȷ���ջ�
	 * ����ջ��󷵻ص�msg.jsp��ʾ��Ϣ
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
			 * ������״̬�����ѷ���״̬���׳��쳣
			 * ��������Ϣ���ص�msg.jsp
			 */
			if(order.getState()!=2){
				throw new OrderException("������Ϣ�쳣��ȷ���ջ�ʧ�ܣ�");
			}
			/**
			 * ������״̬Ϊ�ѷ������޸ĳ� �������״̬
			 * �������Ϣ���ص�msg.jsp
			 */
			oservice.confirm(oid);
			
			request.setAttribute("msg", "�ջ��ɹ���");
			return "f:/jsps/order/msg.jsp";
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}catch(OrderException e){
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/order/msg.jsp";
		}
	}
}
