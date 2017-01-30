package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {
	public static ArrayList<StockFund> list = null;
	public static Map<String, StockFund> map = new HashMap();

	private static double AconvertString2Double(String num) {
		String strList[] = num.toString().split(":");
		double a = Double.parseDouble(strList[0]);
		double b = Double.parseDouble(strList[1]);
		return a / (a + b);
	}
	private static double BconvertString2Double(String num) {
		String strList[] = num.toString().split(":");
		double a = Double.parseDouble(strList[0]);
		double b = Double.parseDouble(strList[1]);
		return b/ (a + b);
	}
	private static StockFund stringToSTockFund(String content) {
		StockFund fund = new StockFund();
		StringBuffer buf = new StringBuffer(content.replaceAll("", "").trim());
		String strList[] = buf.toString().split(";");

		fund.afund.setCode(strList[0].trim());
		fund.bfund.setCode(strList[1].trim());
		fund.motherfund.setCode(strList[2].trim());
		fund.codeName = CodeChange.stringToUnicode(strList[3].trim());
		fund.portion =  strList[4].trim();
		fund.afund.setNum(25000 * AconvertString2Double(strList[4].trim()));
		fund.bfund.setNum(25000 * BconvertString2Double(strList[4].trim()));
		fund.motherfund.setNum(25000);

		fund.appendix = "sz";
		return fund;

	}

	public static void readTxtFile(String filePath) {
		list = new ArrayList<StockFund>();

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
					StockFund fund = stringToSTockFund(lineTxt);
					list.add(fund);
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

	public static void readTxtFile(String filePath, List<StockFund> stockList) {
		if (stockList == null)
			return;

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
					StockFund fund = stringToSTockFund(lineTxt);
					if (!map.containsKey(fund.motherfund.getCode())) {
						stockList.add(fund);
						map.put(fund.motherfund.getCode(), fund);
					}

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
}