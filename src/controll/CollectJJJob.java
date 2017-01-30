package controll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;

import model.SysGloableValue;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import util.CodeChange;
import util.Configure;
import util.Fund;
import util.GZQH;
import util.Log;
import util.MongoDBUtil;
import util.StockFund;
import util.WriteExcel;
import util.WriteFile;

public class CollectJJJob implements Job {
	Log log = Log.getLogger();
	private long currentTime = 0;

	private void downloadDataFromJJ() {
		String collName = Configure.DailyDateTable;
		MongoDBUtil dbUtil = MongoDBUtil.instance;
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

		StringBuilder error = new StringBuilder();

		for (int i = 0; i < sysGV.fundList.size(); i++) {
			StockFund fund = sysGV.fundList.get(i);
			StockFund fundNode = new StockFund();
			fundNode.afund = sysGV.getDownDataWay().getFundFromNet(
					fund.afund.getCode());
			StringBuilder sb = new StringBuilder();

			if (fundNode.afund != null) {
				fundNode.afund.setNum(fund.afund.getNum());
			} else {
				fundNode.afund = new Fund();
				fundNode.afund.setNum(fund.afund.getNum());
				sb.append(fund.motherfund.getCode() + "error:A=0");

			}
			fundNode.bfund = sysGV.getDownDataWay().getFundFromNet(
					fund.bfund.getCode());
			if (fundNode.bfund != null) {
				fundNode.bfund.setNum(fund.bfund.getNum());
			} else {
				fundNode.bfund = new Fund();
				fundNode.bfund.setNum(fund.bfund.getNum());
				sb.append(fund.motherfund.getCode() + "error:B=0");

			}
			fundNode.motherfund = sysGV.getDownDataWay().getMotherFundFromNet(
					fund.motherfund.getCode());

			if (fundNode.motherfund != null) {
				fundNode.motherfund.setNum(fund.motherfund.getNum());
			} else {

				fundNode.motherfund = new Fund();
				fundNode.motherfund.setNum(fund.motherfund.getNum());
				fundNode.motherfund.setCode(fund.motherfund.getCode());
				sb.append(fund.motherfund.getCode() + "error:C=0");

			}
			fundNode.codeName = fund.codeName;
			fundNode.errorInfo = sb.toString();
			fundNode.nowTime = currentTime; // 获取当前时间
			downlist.add(fundNode);// cms0321
		}
		// 插入到数据库
		for (int i = 0; i < downlist.size(); i++)
			try {
				dbUtil.insert(collName, downlist.get(i).StockFund2DbObject());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.logger.error("CollectJob->downloadDataFromJJ:"
						+ e.getMessage());

			}
		WriteExcel.writeExcelFile(writeFilePath2, downlist,false);
		log.logger.info("SysTaskManeger->downloadData");
	}

	public CollectJJJob() {
		Date date = new Date();
		currentTime = date.getTime() / 1000;
	}

	private void downloadDataFromGZ() {
		GetGZQH get = new GetGZQH();
		String path = Configure.getgzqhzsListFilePath();
		GZQH gz = new GZQH();

		Double[] result2 = get.getQHZS2(path, gz.code);
		gz.ifzs = result2[0];
		gz.iczs = result2[1];
		gz.ihzs = result2[2];
		gz.dqsj = currentTime;
		MongoDBUtil dbUtil = MongoDBUtil.instance;

		dbUtil.insert(Configure.GZQHDateTable, gz.GZQH2DbObject());
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("★★★★★★★★★★★");
		downloadDataFromJJ();
		downloadDataFromGZ();
	}

}
