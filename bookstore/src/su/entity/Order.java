package su.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * �����࣬�������������Ŀ
 * @author �����
 *
 */
public class Order {
	private String oid;
	private Date ordertime;//�µ�ʱ��
	private double total;//�����ܶ�
	private int state;//����״̬ 0��δ֧���� 1����֧��δ������2���ѷ���δ�ջ���3�����ջ�
	private User user;//���������û�
	private String address;//�û���ַ
	private List<OrderItem> itemlist=new ArrayList<OrderItem>();//������ϸ��Ŀ
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
