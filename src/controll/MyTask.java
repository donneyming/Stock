package controll;

import java.util.TimerTask;

import util.PropertiesUtil;
import util.WriteExcel;
import util.WriteFile;

public class MyTask implements Runnable  {

	public void run() {
		// TODO Auto-generated method stub
		String readFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ "StockCode201603.txt";
		String readGZFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ "GZQH.txt";
		String writeFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteFile.createFileName();
		String writeFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createFileName();
		String firstbuyFilePath = Class.class.getClass().getResource("/").getPath()
				+ "firstbuy.properties";
		GetFundFromNet sz = new GetFundFromNet();
		// sz.getCurrentData("http://hq.sinajs.cn/list=sh000016");
		try {
			//获取基金值从网站上,填充list
			sz.getFundCodeByList(readFilePath, writeFilePath2);
			//买卖的时候再写入文件
			//PropertiesUtil.first =false;//用于控制是否要重写配置文件
			sz.writeFirstBuyFile(readGZFilePath, firstbuyFilePath);
			//获取对冲的值
			sz.compHedge(readGZFilePath, writeFilePath,firstbuyFilePath);
			//计算对冲
		
			// sz.calculateFundPromit("502020");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
