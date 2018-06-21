package su.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import su.entity.Cart;
import su.entity.User;
import su.service.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class userServlet
 */
@WebServlet("/userServlet")
public class userServlet extends BaseServlet {
	private UserService uservice=new UserService();
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);
		Map<String,String> errs=new HashMap<String,String>();
		if(user.getUsername()==null||user.getUsername().trim().isEmpty()){
			errs.put("nameErr", "�û�������Ϊ��");
		}else{
			if(user.getUsername().length()<2||user.getUsername().length()>10){
				errs.put("nameErr", "�û�����������3-10λ֮��");
			}
		}
		if(user.getPassword()==null||user.getPassword().trim().isEmpty()){
			errs.put("pswErr", "���벻��Ϊ��");
		}else{
			if(user.getPassword().length()<6||user.getPassword().length()>13){
				errs.put("pswErr", "���볤������6-13λ֮��");
			}
		}
		if(user.getEmail()==null||user.getEmail().trim().isEmpty()){
			errs.put("emailErr", "���䲻��Ϊ��");
		}else{
			if(!user.getEmail().matches("\\w+@\\w+.\\w+")){
				errs.put("emailErr", "�����ʽ����ȷ");
			}
		}
		if(!errs.isEmpty()){
			request.setAttribute("user", user);
			request.setAttribute("errs", errs);
			return "/jsps/user/regist.jsp";
		}
		user.setUid(CommonUtils.uuid());
		user.setState(1);
		user.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		//System.out.println(user);
		try {
			uservice.addUser(user);
			request.setAttribute("msg", "��ϲע��ɹ�");
			return "jsps/msg.jsp";
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);
		try {
			User result=uservice.select(user);
			if(result==null){
				request.setAttribute("msg", "�û������������");
				return "/jsps/user/login.jsp";
			}
			HttpSession session=request.getSession();
			Cart cart=new Cart();
			session.setAttribute("user", result);
			session.setAttribute("cart", cart);
			
			return "/index.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	public String exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.invalidate();
		return "/index.jsp";
	}
	
}
