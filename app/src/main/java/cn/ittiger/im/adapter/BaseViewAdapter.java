package cn.ittiger.im.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ListView的基础适配器
 * @author: huylee
 * @time:	2014-11-2下午8:44:03
 * @param <T>
 */
public abstract class BaseViewAdapter<T> extends BaseAdapter {
	private Context context;
	/**
	 * ListView中适配器的数据源对象
	 */
	private List<T> list;
	/**
	 * ListView中自定义Item的layout的id
	 */
	private int viewId;
	/**
	 * 当前选中项的索引
	 */
	private int selectedPosition = -1;

	/**
	 * @param context
	 * @param viewId
	 * @param list
	 */
	public BaseViewAdapter(Context context, int viewId, List<T> list) {
		super();
		this.context = context;
		this.list = list;
		this.viewId = viewId;
	}
	
	/**
	 * 将集合collection中的数据都添加到ListView中
	 * @author: huylee
	 * @time:	2014-11-2下午8:39:20
	 * @param collection
	 */
	public void addAll(Collection<T> collection) {
		this.list.addAll(collection);
		updateListViewUI();
	}
	
	/**
	 * 添加数据Item到Listview中
	 * @author: huylee
	 * @time:	2014-11-2下午8:39:46
	 * @param item
	 */
	public void add(T item) {
		list.add(item);
		updateListViewUI();
	}
	
	/**
	 * 将数据添加到指定位置
	 * Author: hyl
	 * Time: 2015-7-27下午9:42:18
	 * @param position
	 * @param item
	 */
	public void add(int position, T item) {
		list.add(position, item);
		updateListViewUI();
	}
	
	/**
	 * 将指定数据添加到第一个位置
	 * Author: hyl
	 * Time: 2015-7-27下午9:44:09
	 * @param item
	 */
	public void addFirst(T item) {
		list.add(0, item);
		updateListViewUI();
	}
	
	/**
	 * 将指定数据添加到最后一个位置
	 * Author: hyl
	 * Time: 2015-7-27下午9:44:32
	 * @param item
	 */
	public void addLast(T item) {
		add(item);
		updateListViewUI();
	}
	
	/**
	 * 移除ListView中的索引为position的数据
	 * @author: huylee
	 * @time:	2014-11-2下午8:39:58
	 * @param position
	 */
	public void remove(int position) {
		list.remove(position);
		updateListViewUI();
	}
	
	/**
	 * 移除ListView中指定的数据
	 * @author: huylee
	 * @time:	2014-11-2下午8:40:46
	 * @param obj
	 */
	public void remove(T obj) {
		list.remove(obj);
		updateListViewUI();
	}
	
	/**
	 * 重置数据
	 * Author: hyl
	 * Time: 2015-7-21下午2:31:15
	 * @param list
	 */
	public void resetData(List<T> list) {
		this.list = list;
		updateListViewUI();
	}
	
	/**
	 * 移除ListView中所有的数据
	 * @author: huylee
	 * @time:	2014-11-16下午9:41:59
	 */
	public void removeAll() {
		list.clear();
		updateListViewUI();
	}
	
	/**
	 * 重新绘制ListView
	 * @author: huylee
	 * @time:	2014-11-2下午8:40:59
	 */
	public void updateListViewUI() {
		notifyDataSetChanged();
	}
	
	/**
	 * 设置选中项
	 * Author: hyl
	 * Time: 2015-7-21下午2:08:36
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	/**
	 * 获取选中项
	 * Author: hyl
	 * Time: 2015-7-21下午2:18:13
	 * @return
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}
	
	/**
	 * 更新ListView中索引为position的数据为Item
	 * @author: huylee
	 * @time:	2014-11-2下午8:41:52
	 * @param position
	 * @param item
	 */
	public void update(int position, T item) {
		list.set(position, item);
		updateListViewUI();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AbsViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(viewId, parent, false);
			viewHolder = getViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (AbsViewHolder) convertView.getTag();
		}
		
		initListItemView(position, viewHolder, parent, getItem(position));
		
		return convertView;
	}
	
	/**
	 * 获取具体实现的ViewHolder对象，该对象中包括Item中各个自定义元素的初始化过程
	 * @author: huylee
	 * @time:	2015-3-2下午2:36:25
	 * @param convertView	Item的View视图对象
	 * @return
	 */
	public abstract AbsViewHolder getViewHolder(View convertView);
	
	/**
	 * ListView列表中Item的缓存对象，以减少findViewById的操作，提升性能
	 * @author: huylee
	 * 
	 * @time:	2015-3-2下午2:21:42
	 */
	public static class AbsViewHolder {};
	
	/**
	 * 初始化ListView相关信息，包括设置ListView中Item的相关处理，设置Item的具体显示值
	 * @author: huylee
	 * @time:	2014-11-2下午8:42:31
	 * @param position		Item项的索引
	 * @param viewHolder	Item的ViewHolder
	 * @param parent
	 * @param item			当前position位置上的条目数据	
	 */
	protected abstract void initListItemView(int position, AbsViewHolder absViewHolder, ViewGroup parent, T item);

	/**
	 * 获取ListView的List数据源
	 * @author: huylee
	 * @time:	2014-11-2下午8:43:48
	 * @return
	 */
	public List<T> getList() {
		return list;
	}
}
