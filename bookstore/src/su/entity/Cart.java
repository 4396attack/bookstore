package su.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * ���ﳵ��
 * @author �����
 *
 */
public class Cart {
	/**
	 * ���ﳵ�е���Ʒ��Ŀ��һ�����ﳵ�п����ж����Ʒ��Ŀ
	 */
	private Map<String,CartItem> citems=new HashMap<String,CartItem>();
	/**
	 * ���ع��ﳵ�ܼ�
	 * @return
	 */
	public double getTotal() {
		BigDecimal total=new BigDecimal("0");
		for(CartItem c:citems.values()){
			BigDecimal subtotal=new BigDecimal(""+c.getSubtotal());
			total=total.add(subtotal);
		}
		return total.doubleValue();
	}
	@Override
	public String toString() {
		return "Cart [citesms=" + citems +  "]";
	}
	/**
	 * ����������Ŀ
	 * @return
	 */
	public Collection<CartItem> getItems(){
		return citems.values();
	}
	/**
	 * �����ﳵ�������Ʒ
	 * @param c
	 */
	public void addItem(CartItem c){
		/**
		 * ���������������˸��鱾���ٴ����ʱ
		 * ���ᴴ��һ���µ���Ʒ��Ŀ�������޸�ԭ����Ŀ����Ʒ����
		 */
		if(citems.containsKey(c.getBook().getBid())){
			CartItem item=citems.get(c.getBook().getBid());
			item.setCount(item.getCount()+c.getCount());
			citems.put(c.getBook().getBid(), item);
		}
		else{
		/**
		 * �������ڣ��򴴽�һ���µ���Ʒ��Ŀ
		 */
		citems.put(c.getBook().getBid(), c);
		}
	}
	/**
	 * ɾ��ĳ����Ʒ��Ŀ
	 * @param bid
	 */
	public void delete(String bid){
		citems.remove(bid);
	}
	/**
	 * ��չ��ﳵ����������е���Ʒ��Ŀ
	 */
	public void clear(){
		citems.clear();
	}
}
