package com.hungsum.framework.ui.fragments;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hungsum.framework.R;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;

public class HsFragment_Tile extends HsFragment
{
	protected Bundle bundle;

	private GridView ucGridView;

	protected List<HashMap<String, Object>> defaultDatas = new ArrayList<HashMap<String,Object>>(); 


	public void SetDatas(List<HashMap<String, Object>> defaultDatas)
	{
		this.defaultDatas = defaultDatas;
	}
	
	public void SetDatas(List<HashMap<String, Object>> defaultDatas,List<String> keys)
	{
		this.defaultDatas = this._setKeys(defaultDatas, keys);
	}

	private List<HashMap<String, Object>> _setKeys(List<HashMap<String, Object>> datas, List<String> funcs)
	{
		List<HashMap<String, Object>> returnDatas = new ArrayList<HashMap<String,Object>>();
		
		for (HashMap<String, Object> data : datas)
		{
			if(data.get("NeedAuth").equals("Y"))
			{

				int i = (Integer)(data.get("Key"));
				
				String key = getString(i);
				for(String func : funcs)
				{
					if(key.equals(func))
					{
						returnDatas.add(data);
						break;
					}
				}
			}else {
				returnDatas.add(data);
			}
		}
		
		return returnDatas;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		//View view = inflater.inflate(R.layout.hsfragment_tile, container,false);
		View view = inflater.inflate(R.layout.hsfragment_tile, null,false);
		
		this.ucGridView = (GridView)view.findViewById(R.id.ucGridView);

		
		
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), 
				defaultDatas, 
				R.layout.control_gridview_item,
				new String[]{"Icon","Label"}, 
				new int[]{R.id.ucImageView,R.id.ucTextView});
		
		this.ucGridView.setAdapter(adapter);
		this.ucGridView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				@SuppressWarnings("unchecked")
				HashMap<String,Object> map = (HashMap<String,Object>)(ucGridView.getAdapter().getItem(arg2));

				dispatchCommEvent(EventCategory.UserFunc, new CommEventObject(HsFragment_Tile.this, map.get("Key")));

			}
		});
		
		return view;
	}
}
