import java.text.SimpleDateFormat;//时间格式
import java.util.Date;
import java.awt.Color;
import org.jfree.data.time.*;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.*;

import getUpDown.eachDayUpDownData;

public class makeChart {
	eachDayUpDownData data[];
	int length;
	OHLCSeries series = new OHLCSeries("");// 每段時間的K線圖成交量數據
	TimeSeries series2 = new TimeSeries("");// 每段時間的成交量數據
	TimeSeries seriesClose = new TimeSeries("");


	makeChart(eachDayUpDownData data[]) {
		updateData(data);
	}

	void updateData(eachDayUpDownData data[]) {
		this.length = data.length;
		this.data = new eachDayUpDownData[length];
		for (int i = 0; i < this.length; i++) {
			this.data[i] = data[i];
		}
	}


	
	@SuppressWarnings("deprecation") OHLCDataset getOHLCDataset(String name,int DaysToShow) {
		int size=DaysToShow;
		if (DaysToShow >= data.length-1) 
			size = data.length;
        
        Date[] date = new Date[size];
        double[] high = new double[size];
        double[] low = new double[size];
        double[] open = new double[size];
        double[] close = new double[size];
        double[] volume = new double[size];

        for(int i=0;i<size;i++) {
            date[i] = new Date(data[i].y-1900, data[i].m-1, data[i].d);//我懂為什麼這函數會deprecation了
            eachDayUpDownData chartData = data[i];
            high[i] = chartData.high;
            low[i] = chartData.low;
            open[i] = chartData.open;
            close[i] = chartData.close;
            volume[i] = chartData.amount;
        }
        return new DefaultHighLowDataset(name, date, high, low, open, close, volume);
    }
	TimeSeries getMA(int DaysToShow,int MA) {
		int size=DaysToShow;
		if (DaysToShow >= data.length-MA-1) 
			size = data.length-MA;
        
        TimeSeries ts = new TimeSeries(MA+"MA");

        for(int i=0;i<size;i++) {
        	double average=0;
            for(int u=0;u<MA;u++ ) {
            	
            	eachDayUpDownData chartData = data[i+u];
            	average+= chartData.close;
            	
            }
            average /=MA;
            ts.add(new Day(data[i].d, data[i].m, data[i].y), average);

        }
        return ts;
    }


	public static JFreeChart createCandlestickChart(String title,
            String timeAxisLabel,
            String valueAxisLabel,
            OHLCDataset dataset,
            TimeSeries[] MA,
            boolean legend) {

		DateAxis timeAxis = new DateAxis(timeAxisLabel);
		timeAxis.setAutoTickUnitSelection(legend);
		timeAxis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());// 設置時間線顯示的規則，用這個方法就摒除掉了周六和周日這些沒有交易的日期(很多人都不知道有此方法)，使圖形看上去連續
		timeAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));// 設置顯示時間的格式

		NumberAxis valueAxis = new NumberAxis(valueAxisLabel);
		XYPlot plot = new XYPlot(null, timeAxis, valueAxis, null);//載入座標軸
		
		CandlestickRenderer candlestickRender = new CandlestickRenderer();
		candlestickRender.setUseOutlinePaint(true); // 設置是否使用自定義的邊框線，程序自帶的邊框線的顏色不符合中國股票市場的習慣
		candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);// 設置如何對K線圖的寬度進行設定
		candlestickRender.setAutoWidthGap(0);// 設置各個K線圖之間的間隔
		candlestickRender.setUpPaint(Color.RED);// 設置股票上漲的K線圖顏色
		candlestickRender.setDownPaint(Color.GREEN);// 設置股票下跌的K線圖顏色
		plot.setDataset(0, dataset);
		plot.setRenderer(0,candlestickRender);
		
			TimeSeriesCollection otherDataSet = new TimeSeriesCollection();
			for (int i =0;i<MA.length;i++) {
				if (MA[i]!=null) {
					otherDataSet.addSeries(MA[i]);
				}
			}

			plot.setDataset(1, otherDataSet);
			plot.mapDatasetToRangeAxis(1, 0);

			XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
			//(true, false)可以隱藏數據點
			plot.setRenderer(1, renderer2);
			plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
			
			
			JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
			return chart;

}


	
	
}
