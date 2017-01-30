package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

//用于基本库构建
public class StockCode {

	private String ACode;// A基金代码
	private String BCode;// B基金代码
	private String CodeName;// 名称
	private String Portion;// 比例
	private String MotherCode;// 母基金代码

	public StockCode() {
		ACode = new String();
		BCode = new String();
		CodeName = new String();
		Portion = new String();
		MotherCode = new String();

	}

	public String getMotherCode() {
		return MotherCode;
	}

	public void setMotherCode(String motherCode) {
		MotherCode = motherCode;
	}

	public String getACode() {
		return ACode;
	}

	public void setACode(String aCode) {
		ACode = aCode;
	}

	public String getBCode() {
		return BCode;
	}

	public void setBCode(String bCode) {
		BCode = bCode;
	}

	public String getCodeName() {
		return CodeName;
	}

	public void setCodeName(String name) {
		CodeName = name;
	}

	public String getPortion() {
		return Portion;
	}

	public void setPortion(String portion) {
		Portion = portion;
	}

	public StockCode(String MotherCode, String ACode, String BCode,
			String Name, String Portion) {
		this.MotherCode = MotherCode;
		this.ACode = ACode;
		this.BCode = BCode;
		this.CodeName = Name;
		this.Portion = Portion;
	}

	public StockCode stringToStockCode(String content) {
		StockCode fund = new StockCode();
		StringBuffer buf = new StringBuffer(content.replaceAll("", "").trim());
		String strList[] = buf.toString().split(";");

		fund.setACode(strList[0].trim());
		fund.setBCode(strList[1].trim());
		fund.setMotherCode(strList[2].trim());

		fund.setCodeName(CodeChange.stringToUnicode(strList[3].trim()));
		fund.setPortion(strList[4].trim());

		return fund;
	}

	public void readTxtFile(String filePath) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		String collName = Configure.OrignialDateTable;
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
					StockCode fund = stringToStockCode(lineTxt);
					dbUtil.insert(collName, dbUtil.bean2DBObject(fund));
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
	public DBObject StockFund2DbObject(){
		BasicDBObject bo = new BasicDBObject();
		bo.put("MotherCode",this.MotherCode);
		bo.put("CodeName",this.CodeName);
		bo.put("ACode", this.ACode);
		bo.put("BCode", this.BCode);
		bo.put("Portion", this.Portion);
		return bo;
		
	}
	public StockCode DBObj2StockCode(DBObject obj) {
		StockCode  sc = new StockCode();
		if (obj.get("ACode") != null)
			sc.ACode = obj.get("ACode").toString();
		if (obj.get("BCode") != null)
			sc.BCode = obj.get("BCode").toString();
		if (obj.get("CodeName") != null)
			sc.CodeName = obj.get("CodeName").toString();
		if (obj.get("MotherCode") != null)
			sc.MotherCode = obj.get("MotherCode").toString();
		if (obj.get("Portion") != null)
			sc.Portion = obj.get("Portion").toString();

		return sc;
	}

	public static void main(String[] args) {
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "StockCode201603.txt";
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		dbUtil.dropCollection(Configure.DailyDateTable);
		StockCode sc = new StockCode();
		sc.readTxtFile(readFilePath);

		List<DBObject> list = dbUtil.findAll(Configure.DailyDateTable);
		for (int i = 0; i < list.size(); i++) {
			sc.DBObj2StockCode(list.get(i));

		//	System.out.println(sc.getACode());
			//System.out.println(sc.getBCode());
			//System.out.println(sc.getMotherCode());
			System.out.println(CodeChange.unicodeToString(sc.getCodeName()));
			//System.out.println(sc.getPortion());
			System.out.println();
		}

	}

}
