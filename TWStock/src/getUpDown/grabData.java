package getUpDown;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import internet.HttpMethod;
import internet.HttpsConnect;

public class grabData {
	String stockNumber;
	int length;
	public String url;
	public eachDayUpDownData result[];
	
	public grabData(String stockNumber) throws Exception {
		this.stockNumber=stockNumber;
		this.url = "https://www.cnyes.com/twstock/ps_historyprice.aspx?code="
				+stockNumber
				+"&ctl00$ContentPlaceHolder1$startText=1980/11/01&ctl00$ContentPlaceHolder1$endText=2099/06/30";
		grabData();
	}
	


	public void grabData() throws Exception {
		
		HttpsConnect conn = new HttpsConnect(url, "",HttpMethod.HTTP_METHOD_GET ,"UTF-8");	
		Document doc = conn.getDocument();
		Elements data = doc.select("tbody tr");
		
		length=data.size()-3;
		this.result=new eachDayUpDownData[length];
		
		for(int i=1;i<data.size()-2;i++) {
//			System.out.println(data.get(i).text());
			this.result[i-1]=new eachDayUpDownData(data.get(i).text());
		}
	}
    public String toString()
    {
         return "There are "+length+" datas, the last data is "+result[0].date;
    }

}
