package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import controll.GetFundFromNet;
import controll.GetGZQH;

public class PropertiesUtil {
	
	// public  static Double  IFPoint;
	 //public  static Double  IHPoint;
	 //public  static Double  ICPoint;
	 //public  static Double  StockValue;

	 //public static boolean first =false;
	
	 
	/**
	 * 增加属性文件值
	 * 
	 * @param key
	 * @param value
	 */
	public static void addProperties(String key[], String value[], String file) {
		Properties iniFile = getProperties(file);
		FileOutputStream oFile = null;
		try {
			iniFile.put(key, value);
			oFile = new FileOutputStream(file, true);
			iniFile.store(oFile, "modify properties file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取配置文件
	 * 
	 * @return
	 */
	public static void getFirstBuyInfo(String file,String dyzs) {
		Properties pro = null;
		// 从文件mdxbu.properties中读取网元ID和模块ID信息
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			pro = new Properties();
			pro.load(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//IFPoint = Double.valueOf(pro.get("IF"+dyzs).toString());
		//IHPoint = Double.valueOf(pro.get("IH"+dyzs).toString());
		//ICPoint = Double.valueOf(pro.get("IC"+dyzs).toString());

		//StockValue= Double.valueOf(pro.get("StockValue").toString());
	}
	public static Properties getProperties(String file) {
		Properties pro = null;
		// 从文件mdxbu.properties中读取网元ID和模块ID信息
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			pro = new Properties();
			pro.load(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return pro;
		
	}
	/**
	 * 保存属性到文件中
	 * 
	 * @param pro
	 * @param file
	 */
	public static void saveProperties(Properties pro, String file) {
		if (pro == null) {
			return;
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(file, false);
			pro.store(oFile, "modify properties file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改属性文件
	 * 
	 * @param key
	 * @param value
	 */
	public static void updateProperties(String key, String value, String file) {
		try {
			File f = new File(file);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
		} catch (Exception ex) {

		}
		
	
			
		// key为空则返回
		//if (key == null || "".equalsIgnoreCase(key)) {
			//return;
		//}
		Properties pro = getProperties(file);
		if (pro == null) {
			pro = new Properties();
		}
		//if(first)
		{
		pro.put(key, value);

		// 保存属性到文件中
		saveProperties(pro, file);
		}
	}

	public static void updateProperties(Fund f, String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				file.createNewFile();// 不存在则创建
			}
		} catch (Exception ex) {

		}
		// key为空则返回
		//if (f.code == null || "".equalsIgnoreCase(f.code)) {
		//	return;
		//}
		Properties pro = getProperties(fileName);
		if (pro == null) {
			pro = new Properties();
		}
		
	//	if(first)
		{
			pro.put(f.getCode(), String.valueOf(f.getNetEquity()));

			// 保存属性到文件中
			saveProperties(pro, fileName);
		}
		
	}
	public static void main(String[] args) {
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "firstbuy.properties";
		GetGZQH get = new GetGZQH();
		String readFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ "StockCode.txt";
		String dyzs = get.getGZFromTxt(readFilePath2);
		GetFundFromNet gn = new GetFundFromNet();
		Fund f= gn.createQHZSFromSina("IF" + dyzs) ;
		if(f !=null)
		{
			updateProperties("IF" + dyzs, f.getNetEquity()+"",
				readFilePath);
		}
		f= gn.createQHZSFromSina("IC" + dyzs) ;
		if(f !=null)
		{
			updateProperties("IC" + dyzs, f.getNetEquity()+"",
				readFilePath);
		}
		f= gn.createQHZSFromSina("IH" + dyzs) ;
		if(f !=null)
		{
			updateProperties("IH" + dyzs, f.getNetEquity()+"",
				readFilePath);
		}

		
		System.out.println(getProperties(readFilePath).get("IC"+ dyzs));
	}
}
