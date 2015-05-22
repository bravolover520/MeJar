package me.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * BaseAdapter 引擎类，必须实现getView()，其他的都是BaseAdapter的方法
 * @author Jesus{931178805@qq.com}
 * 2014年7月9日
 */
public abstract class EngineAdapter<T> extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<T> mDatas = new ArrayList<T>();
	
	public EngineAdapter(Context ctx, List<T> datas) {
		mContext = ctx;
		mInflater = LayoutInflater.from(ctx);
		if (null != datas) 
			mDatas = datas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
