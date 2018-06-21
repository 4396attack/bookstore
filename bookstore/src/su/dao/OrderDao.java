package su.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import su.entity.Book;
import su.entity.Order;
import su.entity.OrderItem;

public class OrderDao {
	private QueryRunner qr=new TxQueryRunner();
	public void addOrder(Order order) throws SQLException {
		// TODO Auto-generated method stub
		String sql="insert into orders values(?,?,?,?,?,?)";
		/**
		 * 将util的date转化为sql的date
		 */
		Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
		Object[] params={order.getOid(),timestamp,order.getTotal(),order.getState(),order.getUser().getUid(),""};
		qr.update(sql, params);
	}
	public void addItems(List<OrderItem> itemlist) throws SQLException {
		// TODO Auto-generated method stub
		/**
		 * 采用批处理，将一个订单中的多个订单条目存入数据库
		 */
		String sql="insert into orderitem values(?,?,?,?)";
		/**
		 *订单条目用一个二维数组表示
		 */
		Object[][] params=new Object[itemlist.size()][];
		for(int i=0;i<itemlist.size();i++){
			OrderItem item=itemlist.get(i);
			params[i]=new Object[]{item.getIid(),item.getCount(),item.getOrder().getOid(),item.getBook().getBid()};
		}
		qr.batch(sql, params);
	}
	/**
	 * 查询某一用户的所有订单
	 * @param uid 用户id
	 * @return
	 * @throws SQLException
	 */
	public List<Order> seletById(String uid) throws SQLException {
		// TODO Auto-generated method stub
		String sql="select * from orders where uid=?";
		List<Order> result=qr.query(sql, new BeanListHandler<Order>(Order.class), uid);		
		return result;
	}
	/**
	 * 加载指定订单的所有订单条目
	 * @throws SQLException 
	 */
	public void selectItems(Order order) throws SQLException{
		String sql="select * from orderitem,book where orderitem.bid=book.bid and orderitem.oid=?";
		List<Map<String,Object>> result=qr.query(sql, new MapListHandler(), order.getOid());
		List<OrderItem> items=new ArrayList<OrderItem>();
		for(Map<String,Object> map:result){
			OrderItem item=new OrderItem();
			item=CommonUtils.toBean(map, OrderItem.class);
			Book book=CommonUtils.toBean(map, Book.class);
			item.setBook(book);
			item.setOrder(order);
			items.add(item);
			
		}
		order.setItemlist(items);
	}
	public Order seletByOid(String oid) throws SQLException {
		String sql="select * from orders where oid=?";
		Order order=qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		return order;
	}
	public void changeState(String oid) throws SQLException {
		String sql="update orders set state=3 where oid=?";
		qr.update(sql, oid);
		
	}
	public void update(Order order) throws SQLException {
		String sql="update orders set address=? where oid=?";
		Object[] params={order.getAddress(),order.getOid()};
		qr.update(sql, params);
		
	}
	public void pay(String oid) throws SQLException {
		String sql="update orders set state=1 where oid=?";
		qr.update(sql, oid);
		
	}
	public List<Order> all() throws SQLException {
		String sql="select * from orders";
		List<Order> orders=qr.query(sql, new BeanListHandler<Order>(Order.class));
		return orders;
		
	}
	public List<Order> selectByState(String state) throws SQLException {
		String sql="select * from orders where state=?";
		List<Order> result=qr.query(sql, new BeanListHandler<Order>(Order.class), state);
		return result;
	}
	public void send(String oid) throws SQLException {
		// TODO Auto-generated method stub
		String sql="update orders set state=2 where oid=?";
		qr.update(sql, oid);
	}
}
