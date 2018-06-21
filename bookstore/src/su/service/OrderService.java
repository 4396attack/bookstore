package su.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import su.dao.OrderDao;
import su.entity.Order;

public class OrderService {
	private OrderDao odao=new OrderDao();
	/**
	 * 将订单信息添加到数据库中，
	 * 将订单条目信息添加到数据库中
	 * 需在事务中进行
	 * @param order
	 * @throws SQLException 
	 */
	public void addOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			JdbcUtils.beginTransaction();
			odao.addOrder(order);
			odao.addItems(order.getItemlist());
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}
	public List<Order> selectById(String uid) throws SQLException {
		// TODO Auto-generated method stub
		List<Order> orders=odao.seletById(uid);
		for(Order o:orders){
			odao.selectItems(o);
		}
		return orders;
	}
	public Order load(String oid) throws SQLException {
		Order order=odao.seletByOid(oid);
		odao.selectItems(order);
		return order;
	}
	public void confirm(String oid) throws SQLException {
		odao.changeState(oid);
		
	}
	public void addAddress(Order order) throws SQLException {
		// TODO Auto-generated method stub
		odao.update(order);
	}
	public void pay(String oid) throws SQLException {
		// TODO Auto-generated method stub
		odao.pay(oid);
		
	}
	public List<Order> all() throws SQLException {
		List<Order> orders=odao.all();
		for(Order o:orders){
			odao.selectItems(o);
		}
		return orders;
	}
	public List<Order> selectByState(String state) throws SQLException {
		// TODO Auto-generated method stub
		List<Order> orders= odao.selectByState(state);
		for(Order o:orders){
			odao.selectItems(o);
		}
		return orders;
	}
	public void send(String oid) throws SQLException {
		// TODO Auto-generated method stub
		odao.send(oid);
	}
}
