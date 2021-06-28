package internet;

import java.io.BufferedReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpsConnect {
	private Document doc;


	public HttpsConnect(String URL, String cookies, boolean isPost,String coding) throws Exception {
		HttpsReader https = new HttpsReader();
		https.root = URL;
		https.charSet = coding;
		BufferedReader buf = https.readyBuffer(cookies, isPost);
		String line = null;
		String allLine = "";
		while ((line = buf.readLine()) != null) {
			allLine += line;
		}
		System.setProperty("file.encoding", "UTF-8");
		String UTF_8_str = new String(allLine.getBytes("UTF-8"), "UTF-8");

		this.doc = Jsoup.parse(UTF_8_str);
	}
	public Document getDocument(){
		return this.doc;
	}
}
//https://blog.csdn.net/xiaozhendong123/article/details/50107889
//（P/E *EPS / 預期報酬率） * ROE