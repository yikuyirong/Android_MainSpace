package com.hungsum.framework.chart;

import java.util.List;

import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.utils.HsGZip;

public abstract class HsChartWithLabelValue extends HsAbstractChart
{
	protected List<IHsLabelValue> items;
	
	public HsChartWithLabelValue(List<IHsLabelValue> items)
	{
		this.items = items;
	}
	
	public HsChartWithLabelValue(String zippedItems)
	{
		try
		{
			this.items = new ModelHsLabelValues().Create(HsGZip.DecompressString(zippedItems));
		}
		catch(Exception e){}
	}
}
