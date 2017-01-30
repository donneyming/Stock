package controll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Fund;
import util.GZQHZS;
import util.HttpUtil;
import util.Log;
import util.StockFund;
import util.UserAgent;
import util.WriteFile;

public class GetGZQH {
	Log log = Log.getLogger();

	private List<GZQHZS> gzList = null;

	private GZQHZS stringToGZQH(String content) {
		GZQHZS gz = new GZQHZS();
		StringBuffer buf = new StringBuffer(content.replaceAll("", "").trim());
		String strList[] = buf.toString().split(";");

		gz.gz = strList[0].trim();
		gz.date = strList[1].trim();

		return gz;

	}

	private void readTxtFile(String filePath) {
		gzList = new ArrayList<GZQHZS>();

		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					GZQHZS gz = stringToGZQH(lineTxt);
					gzList.add(gz);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

	}

	private static int compareDate(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public String getGZFromTxt(String path) {
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "GZQH.txt";
		readTxtFile(readFilePath);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(new Date());
		for (int i = 0; i < gzList.size(); i++) {
			GZQHZS gz = (GZQHZS) gzList.get(i);
			if (compareDate(today, gz.date) == 1) // 今天日期大
				continue;
			else if (compareDate(today, gz.date) == 0 && i < gzList.size() - 1) // 相等
																				// 如果等于了
																				// 需要计算下年的
																				// 暂时未处理
				return gzList.get(i + 1).gz;
			else
				return gzList.get(i).gz;
		}
		return null;
	}

	public void getQHZS(String readFile, String writepath) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		createQHZSFromSina("IF" + dyzs, writepath);
		createQHZSFromSina("IC" + dyzs, writepath);
		createQHZSFromSina("IH" + dyzs, writepath);
	}

	public String[] getQHZS(String readFile) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		String[] array = new String[3];
		array[0] = "IF" + dyzs + ":"
				+ createQHZSFromSina("IF" + dyzs).getNetEquity() + "";
		array[1] = "IC" + dyzs + ":"
				+ createQHZSFromSina("IC" + dyzs).getNetEquity() + "";
		array[2] = "IH" + dyzs + ":"
				+ createQHZSFromSina("IH" + dyzs).getNetEquity() + "";

		return array;
	}
	
	public Double[] getQHZS2(String readFile,String apdix) {
		GetGZQH get = new GetGZQH();
		String dyzs = get.getGZFromTxt(readFile);
		apdix = dyzs;
		Double[] array = new Double[3];
		array[0] = createQHZSFromSina("IF" + dyzs).getNetEquity() ;
		array[1] = createQHZSFromSina("IC" + dyzs).getNetEquity();
		array[2] =  createQHZSFromSina("IH" + dyzs).getNetEquity() ;
		return array;
	}

	// 查询股指期货的值 sina查询股指期货
	private Fund createQHZSFromSina(String code, String path) {
		// http://hq.sinajs.cn/?_=1456983591674/&list=CFF_RE_IF1603
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IH16031&v=0.17222302411725532
		Date d1 = new Date();
		Fund fund = new Fund();
		String strURL = "http://hq.sinajs.cn/?_=" + d1.getTime()
				+ "&list=CFF_RE_" + code;
		try {
			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);
			String list[] = buf.split(",");
			fund.setCode(code);
			fund.setNetEquity(Double.parseDouble(list[3]));
			log.logger.info(code + ":" + fund.getNetEquity());

			WriteFile.writeTxtFile(path, code + ":" + fund.getNetEquity());
			return fund;
		} catch (Exception e) {
			log.logger.error("GetGZQH->createQHZSFromSina:" + e.getMessage());
		}

		return null;
	}

	// 查询股指期货的值 sina查询股指期货
	private Fund createQHZSFromSina(String code) {
		// http://hq.sinajs.cn/?_=1456983591674/&list=CFF_RE_IF1603
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IH16031&v=0.17222302411725532
		Date d1 = new Date();
		Fund fund = new Fund();
		String strURL = "http://hq.sinajs.cn/?_=" + d1.getTime()
				+ "&list=CFF_RE_" + code;
		try {
			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);
			String list[] = buf.split(",");
			fund.setCode(code);
			fund.setNetEquity(Double.parseDouble(list[3]));
			log.logger.info(code + ":" + fund.getNetEquity());

			return fund;
		} catch (Exception e) {
			log.logger.error("GetGZQH->createQHZSFromSina:" + e.getMessage());
		}

		return null;
	}
}
