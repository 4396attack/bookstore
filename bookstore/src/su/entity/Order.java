package su.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 订单类，包含多个订单条目
 * @author 杨玉杰
 *
 */
public class Order {
	private String oid;
	private Date ordertime;//下单时间
	private double total;//订单总额
	private int state;//订单状态 0：未支付， 1：已支付未发货；2：已发货未收货；3：已收货
	private User user;//订单所属用户
	private String address;//用户地址
	private List<OrderItem> itemlist=new ArrayList<OrderItem>();//订单详细条目
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<OrderItem> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<OrderItem> itemlist) {
		this.itemlist = itemlist;
	}
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state + ", user="
				+ user + ", address=" + address + ", itemlist=" + itemlist + "]";
	}
	
}
