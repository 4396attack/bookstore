package su.entity;
/**
 * 订单条目类，表示订单中的一个商品条目
 * @author 杨玉杰
 *
 */
public class OrderItem {
	private String iid;
	private int count;//商品总数
	private Order order;//所属订单编号
	private Book book;//商品
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public double getSubtotal(){
		return book.getPrice()*count;
	}
	@Override
	public String toString() {
		return "OrderItem [iid=" + iid + ", count=" + count + ", book=" + book + "]";
	}
	
	
}
