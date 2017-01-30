package controll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.JslRoot;
import model.TianTianJsRoot;

import util.*;

public class GetFundFromNet {

	public static int perPointMoney = 300;
	public static List<StockFund> list = new ArrayList();

	/* 获取当前数据指数 */
	public String getCurrentData(String urlStr) {
		// String urlStr = "http://hq.sinajs.cn/list=sh000016";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		conn.setDoOutput(true); // 可以发送数据
		conn.setDoInput(true); // 可以接收数据
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} // POST方法
			// 必须注意此处需要设置UserAgent，否则google会返回403
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		try {
			conn.connect();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// 写入的POST数据
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			osw.write("q=中国&sl=auto&tl=en&tc=1");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			osw.flush();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			osw.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// 读取响应数据
		BufferedReader in = null;
		try {
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String s = null;
		try {
			while ((s = in.readLine()) != null)
				System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;
	}

	// 从其他网址获取的数据
	// http://jingzhi.funds.hexun.com/502021.shtml
	public Fund createFromHexun(String code, String content) {
		// 针对网址1的解析代码
		/*
		 * JsonFormatToolForHexun tool = new JsonFormatToolForHexun();
		 * HexunPacket packet = tool.formatJson(content);
		 * 
		 * Fund fund = new Fund(); fund.code = code; fund.netEquity =
		 * Double.parseDouble(packet.price); System.err.println("基金代码:" + code +
		 * "|" + "当前交易:" + fund.actualEquity); return fund;
		 */

		// 针对网址2的解析代码
		String listofText[] = content.split(",");

		Fund fund = new Fund();
		fund.code = code;

		if (code.startsWith("150")) // sz打头的代码sz150205
			fund.netEquity = Double.parseDouble(listofText[3]);
		else
			// 502打头的代码 sh的代码 sh502013
			fund.netEquity = Double.parseDouble(listofText[4]);
		System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.netEquity);
		return fund;
	}

	// 从其他网址获取的数据 东方财富
	// http://quote.eastmoney.com/sh502024.html
	public Fund createFromDongcai(String code, String content) {

		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1502052&Reference=xml&rt=0.42024372954164546
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1502062&Reference=xml&rt=0.5008210830067212
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1500942&Reference=xml&rt=0.7448945334961105
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1500952&Reference=xml&rt=0.3139436149418562
		// 股指期货指数
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IF16031

		String listofText[] = content.split(",");

		// System.out.println("netEquity"+getTagValue(str));

		Fund fund = new Fund();
		fund.code = code;
		fund.netEquity = Double.parseDouble(listofText[3]);
		System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.netEquity);
		return fund;
	}

	// 从其他网址获取的数据 天天基金网
	// http://fund.eastmoney.com/502024.html?spm=search
	public Fund createFromTiantian(String code, String content) {
		String strRegex1 = "<div id=\"statuspzgz\" class=\"fundpz\"><span class=\".+?\">(.+?)</span>";
		String strRegex2 = "<p class=\"time\">(.+?)</p>";
		String strRegex3 = "<span class=\"left12\">(.+?)</span><span style=\"margin-left:-2px;\">";

		String netEquity = HtmlUtil.getTagValue(strRegex1, content);
		String nowTime = HtmlUtil.getTagValue(strRegex2, content);

		Fund fund = new Fund();
		fund.code = code;
		fund.actualEquity = Double.parseDouble(netEquity);
		System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.actualEquity);
		return fund;
	}

	// 获取母基金的预估值
	public Fund createMomFundFromTianTian(String code, String content) {

		try {
			TianTianJsRoot root = JsonFormatToolForTianTian.formatJson(content);

			Fund fund = new Fund();
			fund.code = code;
			fund.netEquity = Double.parseDouble(root.getGsz());
			fund.actualEquity = Double.parseDouble(root.getDwjz());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			fund.createTime = sdf.parse(root.getGztime());
			System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.netEquity);
			return fund;
		} catch (Exception ex) {
			System.out.println("createMomFundFromTianTian 字符串出错");
		}
		return null;
	}

	// 从其他网址获取的数据 sina
	// http://hq.sinajs.cn/list=sh502020
	public Fund createFromSina(String code, String content) {

		String listofText[] = content.split(",");

		// System.out.println("netEquity"+getTagValue(str));

		int length = listofText.length;
		Fund fund = new Fund();
		fund.code = code;
		// cms 0310更改
		fund.netEquity = Double.parseDouble(listofText[3]);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			fund.createTime = sdf.parse(listofText[length - 2]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.netEquity);
		return fund;
	}

	public void getQHZS(String readFile, String writepath) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		createQHZSFromSina("IF" + dyzs, writepath);
		createQHZSFromSina("IC" + dyzs, writepath);
		createQHZSFromSina("IH" + dyzs, writepath);
	}

	// 计算对冲
	public Double compHedge(String readFile, String writepath,
			String firstbuyFile) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		Fund iffund = createQHZSFromSina("IF" + dyzs, writepath);
		Fund icfund = createQHZSFromSina("IC" + dyzs, writepath);
		Fund ihfund = createQHZSFromSina("IH" + dyzs, writepath);
		// 计算股指当前的值
		Double value = ihfund.netEquity * 200;
		value += iffund.netEquity * 300;
		value += icfund.netEquity * 300;

		PropertiesUtil.getFirstBuyInfo(firstbuyFile, dyzs);

		/*Double value2 = PropertiesUtil.IHPoint * 200;
		value2 += PropertiesUtil.IFPoint * 300;
		value2 += PropertiesUtil.ICPoint * 300;

		if (list.size() == 0 || list == null) {
			return 0.0;
		}
		// 计算股票市值
		Double value3 = 0.0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).afund.netEquity * list.get(i).bfund.netEquity > 0) {
				value3 += list.get(i).afund.netEquity * list.get(i).afund.num
						+ list.get(i).bfund.netEquity * list.get(i).bfund.num
						+ list.get(i).motherfund.netEquity
						* list.get(i).motherfund.num; // 0318
			}
		}

		Double hedgeValue = value3 - PropertiesUtil.StockValue
				- (value - value2);
		WriteFile
				.writeTxtFile(writepath, "hedgeValue:" + hedgeValue.toString());
*/
		return 0.0;
	}

	// 查询股指期货的值 sina查询股指期货
	public Fund createQHZSFromSina(String code, String path) {
		// http://hq.sinajs.cn/?_=1456983591674/&list=CFF_RE_IF1603
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IH16031&v=0.17222302411725532
		Date d1 = new Date();
		Fund fund = new Fund();
		String strURL = "http://hq.sinajs.cn/?_=" + d1.getTime()
				+ "&list=CFF_RE_" + code;
		try {
			URL url = new URL(strURL);
			// Proxy proxy = HttpProxy.getProxy();//cms 增加了代理
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
			String buf = contentBuf.toString();
			String list[] = buf.split(",");

			fund.code = code;
			fund.netEquity = Double.parseDouble(list[3]);
			System.err.println(code + ":" + fund.netEquity);

			// cms 20160315
			// 计算当前股指值
			WriteFile.writeTxtFile(path, code + ":" + fund.netEquity + ";");

			return fund;

		} catch (Exception ex) {
			System.err.println("createQHZSFromSina" + "网址出错");

		}
		return null;
	}

	// 写入文件 买卖的时候再写入文件
	public void writeFirstBuyFile(String readFile, String firstBuyFile) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		Fund f = createQHZSFromSina("IF" + dyzs);
		if (f != null) {
			PropertiesUtil.updateProperties(f, firstBuyFile);
		}
		f = createQHZSFromSina("IC" + dyzs);
		if (f != null) {
			PropertiesUtil.updateProperties(f, firstBuyFile);
		}
		f = createQHZSFromSina("IH" + dyzs);
		if (f != null) {
			PropertiesUtil.updateProperties(f, firstBuyFile);
		}
		Double stockValue = 0.0;
		if (list.size() != 0 || list != null) {
			// 计算股票市值

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).afund.netEquity * list.get(i).bfund.netEquity > 0) {
					stockValue += list.get(i).afund.netEquity
							* list.get(i).afund.num
							+ list.get(i).bfund.netEquity
							* list.get(i).bfund.num;
				}
			}

		}
		// 计算list总的值
		PropertiesUtil.updateProperties("StockValue", stockValue + "",
				firstBuyFile);

		// /读取文件
		PropertiesUtil.getProperties(firstBuyFile);
	}

	public Fund createQHZSFromSina(String code) {
		// http://hq.sinajs.cn/?_=1456983591674/&list=CFF_RE_IF1603
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IH16031&v=0.17222302411725532
		Date d1 = new Date();
		Fund fund = new Fund();
		String strURL = "http://hq.sinajs.cn/?_=" + d1.getTime()
				+ "&list=CFF_RE_" + code;
		try {
			URL url = new URL(strURL);
			// Proxy proxy = HttpProxy.getProxy();//cms 增加了代理
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
			String buf = contentBuf.toString();
			String list[] = buf.split(",");

			fund.code = code;
			fund.netEquity = Double.parseDouble(list[3]);
			System.err.println(code + ":" + fund.netEquity);

			// cms 20160315

			return fund;

		} catch (Exception ex) {
			System.err.println("createQHZSFromSina" + "网址出错");

		}
		return null;
	}

	// 从其他网址获取的数据 集思录
	// http://fund.eastmoney.com/502024.html?spm=search
	public Fund createFromJisilu(String code, String content) {

		// 将字符串"变成\"
		String tmp = content.replaceAll("\"", "\"");
		JsonFormatToolForjisilu json = new JsonFormatToolForjisilu();
		JslRoot root = json.formatJson(tmp);

		Fund fund = new Fund();
		fund.code = code;
		for (int i = 0; i < root.getRows().size(); i++) {
			if (fund.code.equals(root.getRows().get(i).getId())) {
				fund.netEquity = Double.parseDouble(root.getRows().get(i)
						.getCell().getPrice());
				System.err.println("基金代码:" + code + "|" + "当前交易:"
						+ fund.netEquity);
			}
		}

		return fund;
	}

	/* 计算购买成本，以及最低获利空间 */
	/*
	 * 502020 国金通用上证50502040 长盛上证50502048 易方达上证50
	 */
	/*
	 * 使用http://hq.sinajs.cn/list=of502020 查询基金
	 */
	public Fund getFundCodeDetailByCode(String fundCode, String appendix) {
		/*
		 * String urlStr = "http://fund.eastmoney.com/"+fundCode+".html";
		 * getCurrentData(urlStr);
		 */
		// String strURL = "http://fund.eastmoney.com/" + fundCode + ".html";
		// 天天基金网
		// http://quote.eastmoney.com/sz150094.html
		// http://quote.eastmoney.com/sh502021.html
		// String strURL ="http://quote.eastmoney.com/sz"+fundCode+".html"; 东方财富

		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1502062&Reference=xml&rt=0.4463706939366313

		Date d2 = new Date();// 1456803892615
		Random r = new Random();
		int max = 999;
		int min = 100;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		// 集思录
		String strURL1 = "http://www.jisilu.cn/jisiludata/StockFenJiDetail.php?qtype=dynamic&fund_id="
				+ fundCode + "&___t=" + d2.getTime() + String.valueOf(s);

		// 和讯财富基金
		// String strURL =
		// "http://flashquote.stock.hexun.com/Stock_Combo.ASPX?mc=2_"+ fundCode
		// + "&dt=Q,MI&t=" + d2.getTime();
		// String strURL =
		// "http://quote.stock.hexun.com/stockdata/fund_quote.aspx?stocklist="+
		// fundCode;

		// 东方财富网址 共有5种url
		// Random r = new Random();
		// double d1 = r.nextDouble();
		// String strURL =
		// "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=ZT&jsName=jsfavs&fav="+
		// fundCode+"&Reference=xml&rt=" + d1;

		// sina 网址
		if (fundCode.startsWith("150"))
			appendix = "sz";
		else
			appendix = "sh";
		String strURL = "http://hq.sinajs.cn/list=" + appendix + fundCode;
		try {
			URL url = new URL(strURL);
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
			String buf = contentBuf.toString();
			// System.out.println("captureHtml()的结果：" + buf + "ssssssssss");

			// Pattern pattern =
			// Pattern.compile("<div id=\"statuspzgz\" class=\"fundpz\"><span class=\".+?\">(.+?)</span>");
			/*
			 * 
			 * 
			 * //String netEquity = HtmlUtil.getTagValue(strRegex1, buf); String
			 * nowTime = HtmlUtil.getTagValue(strRegex2, buf); String
			 * actualEquity = HtmlUtil.getTagValue(strRegex3, buf);
			 */

			/*
			 * String[] strarray = getFundCodeABDetailByCode(fundCode); if
			 * (strarray.length == 6) { System.out.println("基金代码:" + fundCode +
			 * "|" + "当前净值:" + strarray[1] + "|" + "估算净值:" + netEquity + "|" +
			 * "A基金:" + strarray[2] + "|" + "B基金:" + strarray[3] + "|" + "当前时间:"
			 * + nowTime); } else { System.out.println("基金代码:" + fundCode + "|"
			 * + "当前净值:" + actualEquity + "|" + "估算净值:" + netEquity + "|" +
			 * "A基金:" + strarray[2] + "|" + "B基金:" + strarray[3] + "|" + "当前时间:"
			 * + nowTime); }
			 */

			// 加入基金
			// Fund fund = createFromHexun(fundCode, buf);
			// Fund fund = createFromJisilu(fundCode, buf);
			// Fund fund = createFromDongcai(fundCode, buf);
			Fund fund = createFromSina(fundCode, buf);

			return fund;
		} catch (Exception ex) {
			System.err.println(fundCode + "网址出错");
		}
		return null;

	}

	public Fund getMotherFundCodeDetailByCode(String fundCode) {

		Date d2 = new Date();// 1456803892615
		Random r = new Random();
		int max = 999;
		int min = 100;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		// "http://fundgz.1234567.com.cn/js/" + code + ".js?rt=" + new
		// Date().getTime(), "utf-8", 天天基金网网址
		String strURL = "http://fundgz.1234567.com.cn/js/" + fundCode
				+ ".js?rt=" + d2.getTime();

		try {
			URL url = new URL(strURL);
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
			String buf = contentBuf.toString();

			Fund fund = createMomFundFromTianTian(fundCode, buf);
			return fund;
		} catch (Exception ex) {
			System.err.println(fundCode + "网址出错");
		}
		return null;

	}

	// 从sina获取A、B 基金
	public String[] getFundCodeABDetailByCode(String fundCode) {
		String strURL = "http://hq.sinajs.cn/list=of" + fundCode;
		URL url;
		HttpURLConnection httpConn = null;
		try {
			url = new URL(strURL);
			httpConn = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		httpConn.setDoInput(true);
		httpConn.setDoOutput(true);
		httpConn.setUseCaches(false);
		httpConn.setConnectTimeout(1000);
		httpConn.setRequestProperty("Content-type",
				"application/x-java-serialized-object");
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(httpConn.getInputStream(), "utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufReader = new BufferedReader(input);
		String line = "";
		StringBuilder contentBuf = new StringBuilder();
		try {
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String buf = contentBuf.toString();

		String[] strarray = buf.split(",");
		return strarray;
	}

	public void getFundCodeByList(String path, String path2) {
		ReadFile.readTxtFile(path);
		if (ReadFile.list.size() > 0) {
			for (int i = 0; i < ReadFile.list.size(); i++) {
				try {
					StockFund tmp = ReadFile.list.get(i);
					StockFund fund = new StockFund();
					fund.afund = getFundCodeDetailByCode(tmp.afund.code,
							tmp.appendix);
					if (fund.afund != null) {
						fund.afund.num = tmp.afund.num;
					}

					fund.bfund = getFundCodeDetailByCode(tmp.bfund.code,
							tmp.appendix);
					if (fund.afund != null) {
						fund.bfund.num = tmp.bfund.num;
					}

					fund.motherfund = getMotherFundCodeDetailByCode(tmp.motherfund.code);
					if (fund.motherfund != null) {
						fund.motherfund.num = tmp.motherfund.num;
					}
					// fund.motherfund =
					// getFundCodeDetailByCode(fund.motherfund.code);

					/*
					 * Trade trade = new Trade(path2); { trade.BuyFund(fund); }
					 */
					// 添加到list
					if (fund.afund != null && fund.bfund != null
							&& fund.motherfund != null)
						list.add(fund);

					else {
						if (fund.afund == null) {
							fund.afund = new Fund();
							fund.afund.num = tmp.afund.num;

						}
						if (fund.bfund == null) {
							fund.bfund = new Fund();
							fund.bfund.num = tmp.bfund.num;
						}
						if (fund.motherfund == null) {

							fund.motherfund = new Fund();
							fund.motherfund.num = tmp.motherfund.num;
							fund.motherfund.code = tmp.motherfund.code;

						}
						list.add(fund);// cms0318
					}
					// cms 0229
					/*
					 * String content = "Mothercode:" + fund.motherfund.code +
					 * "|" + "aFund:" + fund.afund.code + "|" + "AactualEquity:"
					 * + fund.afund.actualEquity + "|" + "bFund:" +
					 * fund.bfund.code + "|" + "BactualEquity:" +
					 * fund.bfund.actualEquity;
					 * 
					 * WriteFile.writeTxtFile(path2, content);
					 */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			WriteExcel.writeExcelFile(path2, list);
		}

	}

}