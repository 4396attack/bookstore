package su.entity;
/**
 * ��Ʒ��Ŀ��,��Ӧһ����
 * @author �����
 *
 */
public class CartItem {
	/**
	 * ��Ʒ
	 */
	private Book book;
	/**
	 * ��Ʒ����
	 */
	private int count;
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * ������ĿС��
	 * @return
	 */
	public double getSubtotal() {
		return book.getPrice()*count;
	}
	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + ", subtotal="  + "]";
	}
	
	
}
