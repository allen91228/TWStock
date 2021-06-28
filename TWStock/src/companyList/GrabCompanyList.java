package companyList;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import internet.HttpMethod;
import internet.HttpsConnect;
public class GrabCompanyList {
	 static public companyListData GetCompanyList() throws Exception{
			String url="https://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
			HttpsConnect conn = new HttpsConnect(url, "",HttpMethod.HTTP_METHOD_GET ,"BIG5");	
			Document doc = conn.getDocument();
			
			Elements startElement=doc.select("tr:contains(股票)");
			
			int start =startElement.get(0).elementSiblingIndex();
			Elements endElement=doc.select("tr:contains(上市認購(售)權證)");
			int end =endElement.get(0).elementSiblingIndex();
			Elements stock = doc.select(String.format("tr:gt(%d):lt(%d) td:nth-child(1)", start,end));
		//找到正確的股票範圍
			int[] ID= new int[stock.size()];
			String[] name= new String[stock.size()];
			
	        for (int i = 0; i < stock.size(); i++) {
	        	String everyInput =stock.get(i).text().replace(" ", "");
//	        	System.out.println(everyInput);
	        	ID[i]=Integer.parseInt(everyInput.split("　")[0]);
	        	name[i]=everyInput.split("　")[1];
	        }
	        companyListData Companylist = new companyListData(ID,name);
	        //將資料加載到companyListData型態
	        
	        return Companylist;
//	        
	        
		}
	    //（P/E *EPS / 預期報酬率） * ROE
	}


