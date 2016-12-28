package cn.ittiger.util;

import java.util.Collection;

public class ValueUtil {
	/**
	 * 判断字符串是否为空，Null或空白字符串均返回true
	 * Author: hyl
	 * Time: 2015-8-13下午1:18:26
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if(value == null) {
			return true;
		}
		if(value.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断集合是否为空，Null或集合没有元素均返回true
	 * Author: hyl
	 * Time: 2015-8-17下午9:30:24
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		if(collection == null || collection.size() <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断Object是否为空
	 * Author: hyl
	 * Time: 2015-8-17下午9:42:29
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		if(object == null) {
			return true;
		}
		return false;
	}
}
