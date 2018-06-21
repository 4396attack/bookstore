package su.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import cn.itcast.jdbc.JdbcUtils;
import su.dao.BookDao;
import su.dao.OrderDao;
import su.dao.UserDao;
import su.entity.Book;
import su.entity.Order;
import su.entity.OrderItem;
import su.entity.User;
import su.service.OrderService;
import su.web.servlet.PaymentUtil;


public class Mytest {
	@Test
	//test test
	public void fun() throws SQLException{
		Connection con=JdbcUtils.getConnection();
		System.out.println(con);
		
	}
	@Test
	public void fun2() throws AddressException, MessagingException{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp", "smtp.163.com");
		prop.setProperty("mail.smtp.auth", "true");
		Authenticator auth=new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("cn_yangyj", "yang456");
			}
		};
		Session session=Session.getInstance(prop, auth);
		MimeMessage m=new MimeMessage(session);
		m.setSender(new InternetAddress("cn_yangyj@163.com"));
		m.setRecipient(RecipientType.TO, new InternetAddress("1205683178@qq.com"));
		m.setSubject("来自网上书城的注册验证");
		m.setContent("<a href='localhost:8080/userServlet?method=activeStringuid='>点击此处激活</a>", "text/html;charset=utf-8");
		Transport.send(m);
	}
	@Test
	public void fun3(){
		UserDao udao=new UserDao();
		User user=new User();
		user.setPassword("12345");
		user.setUsername("admin");
		try {
			User result=udao.select(user);
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun4(){
		BookDao bdao=new BookDao();
		try {
			List<Book> list=bdao.selectall();
			for(Book book:list){
				System.out.println(book);
				System.out.println(book.getCategory());
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	@Test
	public void fun5(){
		BookDao bdao=new BookDao();
		try {
			List<Book> list=bdao.selectByCategory("3");
			for(Book book:list){
				System.out.println(book);
				System.out.println(book.getCategory());
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	@Test
	public void fun6(){
		BookDao bdao=new BookDao();
		try {
			Book book=bdao.selectById("9");
			System.out.println(book);
			System.out.println(book.getCategory());
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	public void fun7(){
		OrderDao odao=new OrderDao();
		OrderService oservice=new OrderService();
		
		 try {
			List<Order> result=oservice.selectById("184F1E4020D742C09719B43E245A1E14");
			System.out.println(result.get(0).getOid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun8(){
		OrderService oservice=new OrderService();
		try {
			Order order=oservice.load("926BAE1211764150BE543DDA75CD31EB");
			System.out.println(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun9(){
		OrderService oservice=new OrderService();
		try {
			Order order=oservice.load("926BAE1211764150BE543DDA75CD31EB");
			order.setAddress("308");
			oservice.addAddress(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void fun10() {
				String p1_MerId="10001126856";
				String r0_Cmd="Buy";
				String r1_Code="1";
				String r2_TrxId="218685125512392J";
				String r3_Amt="0.01";
				String r4_Cur="RMB";
				String r5_Pid="";
				String r6_Order="EE3001F47DD24BE2A56DC33E50119DB4";
				String r7_Uid="";
				String r8_MP="";
				String r9_BType="1";
				Properties prop=new Properties();
				try {
					prop.load(this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String keyValue=prop.getProperty("keyValue");
				String hmac="2e264a5654d79df67c51ef92a4bede40";
				String hmac_safe="0496851bce67d093e5093ce3b41a25e7";
				boolean result= PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
				System.out.println(result);

	}
}
