package com.hungsum.framework.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsBitmap;

public class HsBitmapAdapter extends BaseAdapter implements OnLongClickListener
{
	private LayoutInflater _inflater;
	
	private List<HsBitmap> _datas;

	public HsBitmapAdapter(Context context,List<HsBitmap> datas)
	{
		_datas = datas;
		
		_inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return this._datas.size();
	}

	@Override
	public HsBitmap getItem(int position)
	{
		return this._datas.get(position);
	}
	
	public List<HsBitmap> getItems()
	{
		return this._datas;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public void clear()
	{
		this._datas.clear();
		notifyDataSetChanged();
	}
	
	public void addItem(HsBitmap item)
	{
		this._datas.add(item);
		notifyDataSetChanged();
	}

	public void sortItem()
	{
		Collections.sort(this._datas);
		notifyDataSetChanged();
	}
	
	public void removeItem(HsBitmap item)
	{
		this._datas.remove(item);
		notifyDataSetChanged();
	}
	
	public void removeItem(int position)
	{
		this._datas.remove(position);
		notifyDataSetChanged();
	}
	
	public void addItems(List<HsBitmap> items)
	{
		for (HsBitmap item : items)
		{
			this._datas.add(item);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = _inflater.inflate(R.layout.control_gridview_bitmap, null);
			holder.ucImageView = (ImageView) convertView.findViewById(R.id.ucImageView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		Bitmap value = getItem(position).getBitmap();
		if(holder.ucImageView != null)
		{
			holder.ucImageView.setImageBitmap(value);
		}

		return convertView;
	}

	public class ViewHolder
	{
		public ImageView ucImageView;
	}

	@Override
	public boolean onLongClick(View v)
	{
		return false;
	}

}
