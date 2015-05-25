package com.guu.file.adapter;

import java.util.ArrayList;
import java.util.List;

import com.guu.file.R;
import com.guu.file.Global.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<Menu> data = new ArrayList<Menu>();

	public GridViewAdapter(Context context, List<Menu> data) {
		super();
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}
	
	

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		View view = convertView;
		Menu menu = data.get(position);
		if(view == null){
			view = mInflater.inflate(R.layout.view_grid_item, null);
			vh = new ViewHolder();
			
			vh.icon = (ImageView)view.findViewById(R.id.icon);
			vh.name = (TextView)view.findViewById(R.id.name);
			
			vh.icon.setBackgroundResource(menu.icon);
			vh.name.setText(menu.menuname);
			
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		
		return view;
	}

	private class ViewHolder {
		ImageView icon;
		TextView name;
	}

}
