package com.hungsum.framework.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.graphics.Color;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsLabelValues extends ModelBase<ArrayList<IHsLabelValue>>
{
	private static final long serialVersionUID = -1098438663520103691L;

	private String mRootName;

	public ModelHsLabelValues()
	{
		this("Item");
	}
	
	public ModelHsLabelValues(String rootName)
	{
		mRootName = rootName;
	}
	
	@Override
	protected HsSaxHandler<ArrayList<IHsLabelValue>> getHandler()
	{
		return new HsKeyLabelsDataHandler();
	}

	protected class HsKeyLabelsDataHandler extends HsSaxHandler<ArrayList<IHsLabelValue>>
	{
		private HsLabelValue tmpValue;

		@Override
		protected ArrayList<IHsLabelValue> getNewInstance()
		{
			return new ArrayList<IHsLabelValue>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);
			
			if (localName.equals(mRootName))
			{
				tmpValue = new HsLabelValue(null);
				tmpValue.SetColor(Color.BLACK);
				tmpValue.SetIsShowDetailImage(true);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(localName.equals("Items"))
			{
				//
			}
			else if(localName.equals(mRootName))
			{
				this.returnObject.add(tmpValue);
			}else if(localName.equals("Value"))
			{
				tmpValue.setValue(sb.toString());
			}else if(localName.equals("Label"))
			{
				tmpValue.SetLabel(sb.toString());
			}else if(localName.equals("ItemColor"))
			{
				tmpValue.SetColor(Integer.valueOf(sb.toString()));
			}else if(localName.equals("ItemShowDetailImage"))
			{
				tmpValue.SetIsShowDetailImage(Boolean.valueOf(sb.toString()));
			}else {
				tmpValue.addDetail(new HsLabelValue(localName, sb.toString()));
			}
		}
	}
}
