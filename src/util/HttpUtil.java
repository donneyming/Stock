package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);  
	public static String downHtml(String strUrl)
	{
		String buf = null;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setUseCaches(false);
			httpConn.setConnectTimeout(1000);
			httpConn.setRequestProperty("User-Agent", UserAgent.getUserAgent());
			InputStreamReader input = new InputStreamReader(
					httpConn.getInputStream(), "utf8");
			BufferedReader bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			buf = new String();
			buf = contentBuf.toString();
		} 
		 catch (Exception ex) {
			    Log log = Log.getLogger();  
			    log.logger.error("HttpUtil->downHtml:"+ex.getMessage());  
			}
		return buf;
	}
}
