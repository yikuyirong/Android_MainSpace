package com.hungsum.jbboss.charts;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.hungsum.framework.chart.HsAbstractChart;
import com.hungsum.framework.chart.HsChartWithLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;

public class DwsnltjbChart extends HsChartWithLabelValue
{
	public DwsnltjbChart(List<IHsLabelValue> items)
	{
		super(items);
	}
	
	public DwsnltjbChart(String zippedString)
	{
		super(zippedString);
	}

	@Override
	public Intent execute(Context context)
	{
		//序列标题
	    String[] titles = new String[] { "重量","平均重量"};


	    List<HsAbstractChart.XYValues> sValues = new ArrayList<HsAbstractChart.XYValues>();
	    //重量序列值
	    HsAbstractChart.XYValues zhongliangValues = new HsAbstractChart.XYValues();
	    //平均重量序列值
	    HsAbstractChart.XYValues avgZhongliangValues = new HsAbstractChart.XYValues();
	    

    	for (IHsLabelValue item : this.items)
		{
    		XYValue zhongliangValue = new XYValue();
    		XYValue avgZhongliangValue = new XYValue();
    		for (IHsLabelValue itemDetail : item.getDetails())
			{
    			String label = itemDetail.getLabel();
    			String value = itemDetail.getValue().toString();
    			if(label.equals("Gbrq"))
    			{
    				zhongliangValue.XValue = avgZhongliangValue.XValue = Double.parseDouble(value.substring(6));
    			}else if(label.equals("Zhongliang"))
    			{
    				zhongliangValue.YValue = Double.parseDouble(value) / 1000;
    			}else if(label.equals("AvgZhongliang"))
    			{
    				avgZhongliangValue.YValue = Double.parseDouble(value) / 1000;
    			}
			}
    		
			zhongliangValues.add(zhongliangValue);
			avgZhongliangValues.add(avgZhongliangValue);
		}
    	
    	//计算最大最小值
    	zhongliangValues.CalMM();
    	
    	sValues.add(zhongliangValues);
    	sValues.add(avgZhongliangValues);

    	//序列颜色
	    int[] colors = new int[]{Color.GREEN,Color.YELLOW};

	    //序列端点样式
	    PointStyle[] styles = new PointStyle[] { PointStyle.POINT,PointStyle.POINT};
	    
    	
    	XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    for (int i = 0; i < renderer.getSeriesRendererCount(); i++) {
	      XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
	      r.setFillPoints(true);
          r.setLineWidth(3f);
	    }
	    
	    setChartSettings(renderer, "", "日期", "重量(吨)", 
	    		zhongliangValues.GetXMin(),
	    		zhongliangValues.GetXMax(),
	    		zhongliangValues.GetYMin(),
	    		zhongliangValues.GetYMax(),
	        Color.BLACK, Color.WHITE);
	    renderer.setXLabels(zhongliangValues.size());
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    
	    //设置背景色
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setApplyBackgroundColor(true);

	    //设置图例字体
	    renderer.setAxisTitleTextSize(20);
	    renderer.setLegendTextSize(20);
	    
	    //应用这句后，图表就不能拖动了
	    //renderer.setClickEnabled(true);

	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    
	    renderer.setZoomButtonsVisible(true);
	    
//	    renderer.setPanLimits(new double[] {zhongliangValues.GetXMin(),zhongliangValues.GetXMax(),zhongliangValues.GetYMin(),zhongliangValues.GetYMax()});
//	    renderer.setZoomLimits(new double[] {zhongliangValues.GetXMin(),zhongliangValues.GetXMax(),zhongliangValues.GetYMin(),zhongliangValues.GetYMax()});

	    XYMultipleSeriesDataset dataset = buildDataset(titles, sValues);
	    //XYSeries series = dataset.getSeriesAt(0);
	    //series.addAnnotation("Vacation", 6, 30);
	    Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
	        "单位送奶量统计表");
	    return intent;
	}
}
