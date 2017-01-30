package util;

import java.io.File;
import java.util.ArrayList;

public class Configure {
	
	
	public static final String DailyDateTable = "CJSJTable";//每天的采集的数据
	public static final String OrignialDateTable = "YSTable";//基础140个数据
	public static final String MoniterDateTable = "JKJJTable";//需要监控的数据

	public static final String ImproveDateTable = "ImproveData";//分析的数据
	public static final String InvestDateTable = "InvestData";//投资的回报数据
	public static final String DailyDateJZTable = "JJJZTable";//基金净值
	
	public static final String GZQHDateTable = "GZQHTable";//股指期货值

	
	public static final String[] TimeList = {"9:00","9:30","10:00","10:30","11:00","11:35","11:38","11:30","13:00","13:30","14:00","14:30","14:45","14:50","14:55","14:58","15:30","21:40"};
	//public static final String[] TimeList = {"16:40:00","16:45:00","16:50:00"};

	public static int ConfirureNet = 1;
	/* 链表 */

	public static String writeFilePath = Class.class.getClass().getResource("/").getPath()
			+ WriteFile.createFileName();
	public static String writeFilePath2 = Class.class.getClass().getResource("/").getPath()
			+ WriteExcel.createFileName();

	public static String  getStockFundListFilePath() {
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "StockCode201603.txt";
		try {
			File f = new File(readFilePath);
			if (f.isFile() && f.exists()) { // 判断文件是否存在
				System.out.print("文件存在");
				return readFilePath;
			} else {
				System.out.print("文件不存在");
				for (int i = 0; i < DefaultConfigure.stocklist.size(); i++) {
					StockFund sf = DefaultConfigure.stocklist.get(i);
					String content = sf.afund.getCode() + ";" + sf.bfund.getCode() + ";"
							+ sf.motherfund.getCode() + ";" + sf.codeName +";" +sf.portion +";" ;
					WriteFile.writeTxtFile(readFilePath, content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return readFilePath;
	}

	public static String getgzqhzsListFilePath() {
		String readGZFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ "GZQH.txt";
		try {
			File file = new File(readGZFilePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				
			} else {
				for (int i = 0; i < DefaultConfigure.gzlist.size(); i++) {
					GZQHZS gz = DefaultConfigure.gzlist.get(i);
					String content = gz.gz + ";" + gz.date + ";";
					WriteFile.writeTxtFile(readGZFilePath, content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return readGZFilePath;
	}
}
