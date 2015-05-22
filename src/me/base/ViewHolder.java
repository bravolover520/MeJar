package me.base;

import android.util.SparseArray;
import android.view.View;

/**
 * SparseArray这个类，优化过的存储integer和object键值对的hashmap
 *  只需静态调用get这个方法填入当前Adapter的 convertView 与 子控件的id,就可以实现复用。
 *  
 * <pre>
 * @Override
 * public View getView(int position, View convertView, ViewGroup parent) {
 * 		if (null == convertView)
 * 			convertView = LayoutInflater.from(context).inflate(R.layout.XXX, parent, false);
 * 		ImageView imageView = ViewHolder.get(convertView, R.id.imageView);
 * 		TextView textView = ViewHolder.get(convertView, R.id.textView);
 * 
 * 		Bean bean = getItem(position);
 * 		textView.setText(bean.getText());
 * 		imageView.setImageResource(bean.getImage());
 * 		return convertView;
 * }
 * </pre>
 * @author Jesus{931178805@qq.com}
 * 2014年7月9日
 */
public class ViewHolder {

	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int viewId) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (null == viewHolder) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		} 
		View childView = viewHolder.get(viewId);
		if (null == childView) {
			childView = view.findViewById(viewId);
			viewHolder.put(viewId, childView);
		}
		return (T)childView;
	}
}
