package su.entity;
/**
 * 商品条目类,对应一本书
 * @author 杨玉杰
 *
 */
public class CartItem {
	/**
	 * 商品
	 */
	private Book book;
	/**
	 * 商品数量
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
	 * 返回条目小计
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
