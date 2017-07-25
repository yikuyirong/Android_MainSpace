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

public class BmxsthtjbChart extends HsChartWithLabelValue
{
	public BmxsthtjbChart(List<IHsLabelValue> items)
	{
		super(items);
	}
	
	public BmxsthtjbChart(String zippedItems)
	{
		super(zippedItems);
	}

	@Override
	public Intent execute(Context context)
	{
		//序列标题
		String[] titles_left = new String[] { "数量"};
		String[] titles_right = new String[] { "提货金额","实际金额"};


		//左序列集
	    List<HsAbstractChart.XYValues> sValues_left = new ArrayList<HsAbstractChart.XYValues>();
		//右序列集
	    List<HsAbstractChart.XYValues> sValues_right = new ArrayList<HsAbstractChart.XYValues>();
	    
	    //提货数量序列
	    HsAbstractChart.XYValues thsls = new HsAbstractChart.XYValues();

	    //提货金额序列
	    HsAbstractChart.XYValues thjes = new HsAbstractChart.XYValues();
	    //实际提货金额序列
	    HsAbstractChart.XYValues sjjes = new HsAbstractChart.XYValues();
	    
	    String bmbh = "",bmmc = "";

    	for (IHsLabelValue item : this.items)
		{
    		XYValue thsl = new XYValue();
    		XYValue thje = new XYValue();
    		XYValue sjje = new XYValue();
    		for (IHsLabelValue itemDetail : item.getDetails())
			{
    			String label = itemDetail.getLabel();
    			String value = itemDetail.getValue().toString();
    			if(label.equals("Date"))
    			{
    				thsl.XValue = thje.XValue = sjje.XValue = Double.parseDouble(value.substring(8));
    			}else if(label.equals("Thsl"))
    			{
    				thsl.YValue = Double.parseDouble(value) / 10000;
    			}else if(label.equals("Thje"))
    			{
    				thje.YValue = Double.parseDouble(value) / 10000;
    			}else if(label.equals("Sjje"))
    			{
    				sjje.YValue = Double.parseDouble(value) / 10000;
    			}else if(label.equals("Bmmc"))
    			{
    				bmmc = value;
    			}else if(label.equals("Bmbh"))
    			{
    				bmbh = value;
    			}
			}
    		
			thsls.add(thsl);
			thjes.add(thje);
			sjjes.add(sjje);
		}
    	
    	//计算最大最小值
    	thsls.CalMM();
    	thjes.CalMM();
    	sjjes.CalMM();

    	sValues_left.add(thsls);
    	
    	sValues_right.add(thjes);
    	sValues_right.add(sjjes);

    	//序列颜色
    	int[] colors = new int[]{Color.GREEN,Color.YELLOW,Color.RED};

	    //序列端点样式
	    PointStyle[] styles = new PointStyle[] { PointStyle.POINT,PointStyle.POINT,PointStyle.POINT};
	    
    	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(3);
    	
        setRenderer(renderer, colors, styles);
        for (int i = 0; i < renderer.getSeriesRendererCount(); i++) 
        {
          XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
          r.setLineWidth(3f);
        }

	    setChartSettings(renderer, "", "日期", "数量(万)", 
	    		thsls.GetXMin(),
	    		thsls.GetXMax(),
	    		thsls.GetYMin(),
	    		thsls.GetYMax(),
	        Color.BLACK, Color.WHITE);
	    renderer.setXLabels(thsls.size());
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    
	    //设置背景色
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setApplyBackgroundColor(true);

	    renderer.setYAxisAlign(Align.RIGHT, 1);
	    renderer.setYLabelsAlign(Align.LEFT, 1);

	    //设置图例字体
	    renderer.setAxisTitleTextSize(20);
	    renderer.setLegendTextSize(20);
	    
	    //应用这句后，图表就不能拖动了
	    //renderer.setClickEnabled(true);

	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    
	    renderer.setYTitle("金额(万元)", 1);
	    
	    
	    renderer.setZoomButtonsVisible(true);
	    
//	    renderer.setPanLimits(new double[] {thsls.GetXMin(),thsls.GetXMax(),thsls.GetYMin(),thsls.GetYMax()});
//	    renderer.setZoomLimits(new double[] {thsls.GetXMin(),thsls.GetXMax(),thsls.GetYMin(),thsls.GetYMax()});

	    
	    
	    XYMultipleSeriesDataset dataset = buildDataset(titles_left,sValues_left);
	    
	    addXYSeries(dataset, titles_right, sValues_right, 1);

	    Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
	        "部门销售提货统计表(" + bmmc + bmbh +  ")");
	    return intent;
	}
}
