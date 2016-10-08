package cn.ittiger.im.activity.interfaces;

/**
 * 吐司消息显示接口
 * @author: huylee
 * @time:	2015-7-12下午9:39:13
 */
public interface IShowToastMessage {
	/**
	 * 显示一个长时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:31:46
	 * @param message	提示内容
	 */
	public void showLongToast(String message);
	
	/**
	 * 显示一个长时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:31:46
	 * @param msgId		提示内容的资源ID
	 */
	public void showLongToast(int msgId);
	
	/**
	 * 显示一个短时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param message	提示内容
	 */
	public void showShortToast(String message);
	
	/**
	 * 显示一个短时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param msgId		提示内容的资源ID
	 */
	public void showShortToast(int msgId);
	
	/**
	 * 显示一个自定义时长的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param message	提示内容
	 * @param duration	吐司提示显示的时间
	 */
	public void showToast(String message, int duration);

	/**
	 * 显示一个自定义时长的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:31:46
	 * @param msgId		提示内容的资源ID
	 * @param duration	吐司提示显示的时间
	 */
	public void showToast(int msgId, int duration);
}
