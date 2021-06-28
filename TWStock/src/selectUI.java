import java.awt.EventQueue;

import javax.swing.JFrame;

import companyList.GrabCompanyList;
import companyList.companyListData;
import getUpDown.grabData;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.DefaultHighLowDataset;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;

public class selectUI {

	private JFrame frame;
	private JTextField textID;
	private JTextField textDays;
	static companyListData compancyList;
	private JTextField textMA;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					selectUI window = new selectUI();
					window.frame.setVisible(true);
					System.setProperty("file.encoding", "UTF-8");//設定系統語系
					compancyList = GrabCompanyList.GetCompanyList();//爬股票列表(很慢)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public selectUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("台股瀏覽程式");
		frame.setBounds(100, 100, 1081, 525);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		
		JLabel lblID = new JLabel("股票代碼");
		lblID.setBounds(33, 71, 87, 18);
		frame.getContentPane().add(lblID);
		
		JLabel lblDays = new JLabel("欲查詢的天數");
		lblDays.setBounds(33, 127, 87, 18);
		frame.getContentPane().add(lblDays);

		JLabel lblname = new JLabel("");
		lblname.setBounds(189, 101, 86, 15);
		frame.getContentPane().add(lblname);
		
		textID = new JTextField();
		textID.setBounds(179, 70, 96, 21);
		frame.getContentPane().add(textID);
		textID.setColumns(10);
		textID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblname.setText(getUpdateTextID());
			}
		});
		
		textDays = new JTextField();
		textDays.setColumns(10);
		textDays.setBounds(179, 126, 96, 21);
		frame.getContentPane().add(textDays);

		
		JCheckBox chckbx1MA = new JCheckBox("顯示1MA");
		chckbx1MA.setBounds(33, 165, 97, 23);
		frame.getContentPane().add(chckbx1MA);
		
		JCheckBox chckbx5MA = new JCheckBox("顯示5MA");
		chckbx5MA.setBounds(178, 165, 97, 23);
		frame.getContentPane().add(chckbx5MA);
		
		JCheckBox chckbx20MA = new JCheckBox("顯示20MA");
		chckbx20MA.setBounds(33, 205, 97, 23);
		frame.getContentPane().add(chckbx20MA);
		
		JCheckBox chckbx120MA = new JCheckBox("顯示120MA");
		chckbx120MA.setBounds(179, 205, 97, 23);
		frame.getContentPane().add(chckbx120MA);
		
		textMA = new JTextField();
		textMA.setBounds(167, 241, 96, 21);
		frame.getContentPane().add(textMA);
		textMA.setColumns(10);
		
		JCheckBox chckbxUserMA = new JCheckBox("顯示自定義MA");
		chckbxUserMA.setBounds(33, 240, 125, 23);
		frame.getContentPane().add(chckbxUserMA);
		
		
		JCheckBox chckbxOpenNewWindow = new JCheckBox("在新視窗中開啟");
		chckbxOpenNewWindow.setBounds(33, 267, 125, 23);
		frame.getContentPane().add(chckbxOpenNewWindow);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(300, 10, 755, 466);
		frame.getContentPane().add(tabbedPane);

		
		JButton btnGo = new JButton("查詢");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblname.setText(getUpdateTextID());
				String ID=textID.getText();
				//使用者輸入股票ID
				//透過股票列表找出對應的名子
				grabData connect;
				try {

					String name=lblname.getText();
					connect = new grabData(ID);

					System.out.println(name+"，已成功獲取資料"+connect);
					//連線到cnyes.com爬下所有歷史資料，開盤收盤漲幅資料交易量
					
					makeChart ch = new makeChart(connect.result);
					//將爬到的資料匯入至圖表類別
					int date=Integer.parseInt(textDays.getText());
					DefaultHighLowDataset OHLCDataset=(DefaultHighLowDataset) ch.getOHLCDataset("price",date);
					TimeSeries[] MAs=new TimeSeries[5];
					if (chckbx1MA.isSelected())MAs[0]=ch.getMA(date,1);
					if (chckbx5MA.isSelected())MAs[1]=ch.getMA(date,5);
					if (chckbx20MA.isSelected())MAs[2]=ch.getMA(date,20);
					if (chckbx120MA.isSelected())MAs[3]=ch.getMA(date,120);
					if (chckbxUserMA.isSelected())MAs[4]=ch.getMA(date,Integer.parseInt(textMA.getText()));
					//加載特定日期的資料
			        makeChart.createCandlestickChart(name,"日期","價格",OHLCDataset,MAs,true);
			        JFreeChart chart= makeChart.createCandlestickChart(name,"日期","價格",OHLCDataset,MAs,true);
			        if (chckbxOpenNewWindow.isSelected()) {
						ChartFrame frame = new ChartFrame(name, chart);
						frame.pack();
						frame.setVisible(true);
			        }
			        else {
			        	ChartPanel chartPanel= new ChartPanel(chart);
			        	tabbedPane.add(name,chartPanel);
			        	tabbedPane.validate();
			        }

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnGo.setBounds(33, 296, 87, 23);
		frame.getContentPane().add(btnGo);
		
		JButton btnNewButton = new JButton("關閉目前分頁");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabbedPane.getTabCount()!=0)	tabbedPane.remove(tabbedPane.getSelectedIndex());
			}
		});
		btnNewButton.setBounds(167, 296, 109, 23);
		frame.getContentPane().add(btnNewButton);
		



	}
	 String getUpdateTextID(){
		String ID=textID.getText();
		//使用者輸入股票ID
		//透過股票列表找出對應的名子

		String name=compancyList.getName(Integer.parseInt(ID));
		return (ID+name);
		
	}
}

