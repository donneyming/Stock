package controll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import model.SysGloableValue;

import util.CodeChange;
import util.HttpUtil;
import util.Log;
import util.ReadFile;
import util.StockFund;
import util.ThreadLocalDateUtil;
import util.TimeUtil;
import util.WriteExcel;
import util.WriteFile;

public class SysTaskManeger implements Runnable {
	Log log = Log.getLogger();
	private DelayQueue<SysTask> queue = new DelayQueue<SysTask>();
	public boolean flag = true;

	private void downloadData() {
		List<StockFund> downlist = new ArrayList();
		// TODO Auto-generated method stub
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "StockCode.txt";
		String readGZFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ "GZQH.txt";
		String writeFilePath = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteFile.createFileName();
		String writeFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createFileName();

		SysGloableValue sysGV = SysGloableValue.getGloableValue();

		for (int i = 0; i < sysGV.fundList.size(); i++) {
			StockFund fund = sysGV.fundList.get(i);
			StockFund fundNode = new StockFund();
			
			// 增加数量
			fundNode.afund = sysGV.getDownDataWay().getFundFromNet(
					fund.afund.getCode());
			if (fundNode.afund != null) {
				fundNode.afund.setNum(fund.afund.getNum());
			}
			
			
			fundNode.bfund = sysGV.getDownDataWay().getFundFromNet(
					fund.bfund.getCode());
			if (fundNode.bfund != null) {
				fundNode.bfund.setNum(fund.bfund.getNum());
			}

			fundNode.motherfund = sysGV.getDownDataWay().getMotherFundFromNet(
					fund.motherfund.getCode());
			
			if (fundNode.motherfund != null) {
				fundNode.motherfund.setNum(fund.motherfund.getNum());
			}
			fundNode.codeName = CodeChange.unicodeToString(fund.codeName);
			
			fundNode.nowTime =System.currentTimeMillis();//获取当前时间   
			// 添加到list
			if (fundNode.afund != null && fundNode.bfund != null)
				downlist.add(fundNode);
		}
		WriteExcel.writeExcelFile(writeFilePath2, downlist);
		log.logger.info("SysTaskManeger->downloadData");
	}

	public void addTask(String time) {
		SysTask task = new SysTask(time);
		this.queue.add(task);
	}

	public void run() {
		while (flag) {
			try {
				SysTask task = queue.take();
				if(task !=null)
				{
					downloadData();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.logger.error("SysTaskManeger->run:"
						+ e.getMessage());
			}

		}
		/*
		 * GetFundFromNet sz = new GetFundFromNet(); //
		 * sz.getCurrentData("http://hq.sinajs.cn/list=sh000016"); try {
		 * //获取基金值从网站上 sz.getFundCodeByList(readFilePath, writeFilePath2);
		 * 
		 * //获取股指期货的值 sz.getQHZS(readFilePath, writeFilePath);
		 * 
		 * // sz.calculateFundPromit("502020"); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * }
		 */

	}

}
