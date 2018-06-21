package su.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/**
 * 购物车类
 * @author 杨玉杰
 *
 */
public class Cart {
	/**
	 * 购物车中的商品条目，一个购物车中可以有多个商品条目
	 */
	private Map<String,CartItem> citems=new HashMap<String,CartItem>();
	/**
	 * 返回购物车总价
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
	 * 返回所有条目
	 * @return
	 */
	public Collection<CartItem> getItems(){
		return citems.values();
	}
	/**
	 * 往购物车中添加商品
	 * @param c
	 */
	public void addItem(CartItem c){
		/**
		 * 如果购物中已添加了改书本，再次添加时
		 * 不会创建一个新的商品条目，而是修改原有条目的商品数量
		 */
		if(citems.containsKey(c.getBook().getBid())){
			CartItem item=citems.get(c.getBook().getBid());
			item.setCount(item.getCount()+c.getCount());
			citems.put(c.getBook().getBid(), item);
		}
		else{
		/**
		 * 若不存在，则创建一个新的商品条目
		 */
		citems.put(c.getBook().getBid(), c);
		}
	}
	/**
	 * 删除某个商品条目
	 * @param bid
	 */
	public void delete(String bid){
		citems.remove(bid);
	}
	/**
	 * 清空购物车，即清除所有的商品条目
	 */
	public void clear(){
		citems.clear();
	}
}
