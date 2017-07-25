package com.hungsum.framework.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsFile;

public class HsFileAdapter extends BaseAdapter implements OnLongClickListener
{
	private LayoutInflater _inflater;
	
	private List<HsFile> _datas;

	public HsFileAdapter(Context context,List<HsFile> datas)
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
	public HsFile getItem(int position)
	{
		return this._datas.get(position);
	}
	
	public List<HsFile> getItems()
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
	
	public void addItem(HsFile item)
	{
		this._datas.add(item);
		notifyDataSetChanged();
	}

	public void sortItem()
	{
		Collections.sort(this._datas);
		notifyDataSetChanged();
	}
	
	public void removeItem(HsFile item)
	{
		this._datas.remove(item);
		notifyDataSetChanged();
	}
	
	public void removeItem(int position)
	{
		this._datas.remove(position);
		notifyDataSetChanged();
	}
	
	public void addItems(List<HsFile> items)
	{
		for (HsFile item : items)
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
			convertView = _inflater.inflate(R.layout.control_list_file_1, null);
			holder.ucImageView = (ImageView) convertView.findViewById(R.id.ucImageView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

//		Bitmap value = getItem(position).getBitmap();
//		if(holder.ucImageView != null)
//		{
//			holder.ucImageView.setImageBitmap(value);
//		}

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
