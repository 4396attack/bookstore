package su.entity;
/**
 * ������Ŀ�࣬��ʾ�����е�һ����Ʒ��Ŀ
 * @author �����
 *
 */
public class OrderItem {
	private String iid;
	private int count;//��Ʒ����
	private Order order;//�����������
	private Book book;//��Ʒ
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
