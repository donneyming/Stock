package controll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.quartz.SchedulerException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sun.media.jfxmedia.logging.Logger;

import util.CodeChange;
import util.Configure;
import util.Fund;
import util.GZQH;
import util.Log;
import util.MongoDBUtil;
import util.ReadFile;
import util.StockCode;
import util.StockFund;
import util.ThreadLocalDateUtil;
import util.TimeUtil;
import util.WriteExcel;
import model.SysGloableValue;

public class UserInterface {

	Log log = Log.getLogger();
	private boolean flag2 = false;
	private boolean flag1 = false;
	private SysGloableValue sysGV = null;

	/* 配置采集方式 */
	public void configure(int netCollect) {
		if (sysGV != null)
			sysGV.setNetCollect(netCollect);
		else {
			log.logger
					.error("UserInterface->configure:SysGloableValue sysGV =null");
		}
	}

	/* 系统启动 */
	public UserInterface(SysGloableValue sysGV) {
		this.sysGV = sysGV;
		// initFundList(sysGV);
		// initTaskList(sysGV);
	}

	//

	public String[][] readFromDB() {

		List<StockCode> dbList = new ArrayList();
		String readFilePath = Class.class.getClass().getResource("/").getPath()
				+ "StockCode201603.txt";
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		dbUtil.dropCollection(Configure.OrignialDateTable);
		StockCode sc = new StockCode();
		sc.readTxtFile(readFilePath);

		List<DBObject> list = dbUtil.findAll(Configure.OrignialDateTable);
		for (int i = 0; i < list.size(); i++) {

			StockCode insc = sc.DBObj2StockCode(list.get(i));
			dbList.add(insc);
		}

		String[][] cellData = new String[dbList.size()][6];

		try {

			// Object[] cellData = new Object[downlist.size()][4];
			for (int i = 0; i < dbList.size(); i++) {
				if (dbList.get(i).getMotherCode() != null) {
					cellData[i][0] = dbList.get(i).getMotherCode();
				}
				if (dbList.get(i).getCodeName() != null) {
					cellData[i][1] = new String(dbList.get(i).getCodeName()
							.getBytes("utf8"), "gb2312");
				}
				cellData[i][2] = "" + dbList.get(i).getACode();
				cellData[i][3] = "" + dbList.get(i).getBCode();
				cellData[i][4] = "" + dbList.get(i).getPortion();

			}

			return cellData;

		} catch (Exception ex) {
			log.logger.error("UserInterface->collectNow:" + ex.getMessage());
		}
		return null;
	}

	// 从网络上采集数据
	private void fun(List<StockFund> destList, List<StockFund> orginallist) {

		for (int i = 0; i < orginallist.size(); i++) {
			StockFund fund = orginallist.get(i);
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
			Date date = new Date();
			fundNode.nowTime = date.getTime() / 1000; // 获取当前时间
			destList.add(fundNode);// cms0321

		}

	}

	// 将数据转换成表格数据
	public DefaultTableModel creat(List<StockFund> downlist,
			String[] columnNames, String[][] cellData) {
		try {

			// Object[] cellData = new Object[downlist.size()][4];
			for (int i = 0; i < downlist.size(); i++) {
				if (downlist.get(i).motherfund.getCode() != null) {
					cellData[i][0] = downlist.get(i).motherfund.getCode();
				}
				if (downlist.get(i).codeName != null) {
					cellData[i][1] = CodeChange
							.unicodeToString(downlist.get(i).codeName);
				}

				cellData[i][2] = "" + downlist.get(i).afund.getNetEquity();
				cellData[i][3] = "" + downlist.get(i).bfund.getNetEquity();
				cellData[i][4] = "" + downlist.get(i).motherfund.getNetEquity();
				double yijia = downlist.get(i).motherfund.getNetEquity()
						* downlist.get(i).motherfund.getNum()
						- downlist.get(i).afund.getNetEquity()
						* downlist.get(i).afund.getNum()
						- downlist.get(i).bfund.getNetEquity()
						* downlist.get(i).bfund.getNum();
				cellData[i][5] = yijia + "";
			}
			DefaultTableModel model = new DefaultTableModel(cellData,
					columnNames);
			return model;
		} catch (Exception ex) {
			log.logger.error("UserInterface->creat:" + ex.getMessage());
		}
		return null;
	}

	/* 立刻从网上采集数据 */
	public DefaultTableModel collect() {
		List<StockFund> downlist = new ArrayList();
		String collName = Configure.DailyDateTable;
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		fun(downlist, sysGV.fundList);
		for (int i = 0; i < downlist.size(); i++)
			try {
				// 插入到数据库
				dbUtil.insert(collName, downlist.get(i).StockFund2DbObject());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.logger.error("UserInterface->collectNow:" + e.getMessage());

			}
		// 插入到文件
		String writeFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createFileName();
		WriteExcel.writeExcelFile(writeFilePath2, downlist,false);

		String[] columnNames = { "基金代码", "基金名称", "A价格", "B价格", "C价格", "溢价" };
		String[][] cellData = new String[downlist.size()][6];

		DefaultTableModel model = creat(downlist, columnNames, cellData);
		return model;
	}

	/* 初始化链表 */
	public void initTaskList() {

		/*
		 * SysTaskManeger r1 = new SysTaskManeger(); for (int i = 0; i <
		 * Configure.TimeList.length; i++) { r1.addTask(Configure.TimeList[i]);
		 * } Thread t = new Thread(r1); t.start();
		 */
		for (int i = 0; i < Configure.TimeList.length; i++) {
			CollectJJJob job = new CollectJJJob();
			String time = Configure.TimeList[i];
			String hour = time.split(":")[0];
			String miniute = time.split(":")[1];

			// 0 15 10 ? * * 每天10点15分触发

			String timeConfig = "0 " + miniute + " " + hour + " ? * *";
			String job_name = "time" + time;
			try {
				System.out.println("【系统启动】");
				QuartzManager.removeJob(job_name);
				QuartzManager.addJob(job_name, job, timeConfig); // 每2秒钟执行一次
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CollectJZJob jzjob = new CollectJZJob();

		try {
			QuartzManager.addJob("jzJob", jzjob, " 0 10 10 ? * *");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double AconvertString2Double(String num) {
		try {
			String strList[] = num.toString().split(":");
			double a = Double.parseDouble(strList[0]);
			double b = Double.parseDouble(strList[1]);
			return a / (a + b);
		} catch (Exception ex) {
			log.logger.error("UserInterface->AconvertString2Double " + num);
		}
		return 0.0;
	}

	private double BconvertString2Double(String num) {
		try {
			String strList[] = num.toString().split(":");
			double a = Double.parseDouble(strList[0]);
			double b = Double.parseDouble(strList[1]);
			return b / (a + b);
		} catch (Exception ex) {
			log.logger.error("UserInterface->AconvertString2Double " + num);
		}
		return 0.0;
	}

	// 计算选中的节点的利润
	public DefaultTableModel cmptProfit() {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		List<DBObject> list = dbUtil.find(Configure.MoniterDateTable, null,
				null);
		List<StockFund> orginList = new ArrayList();
		List<StockFund> downList = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			StockCode sc = new StockCode();
			StockCode tmp = sc.DBObj2StockCode(list.get(i));
			// 根据母基金代码填充fundNode；
			StockFund fund = new StockFund();

			fund.afund.setCode(tmp.getACode());
			fund.bfund.setCode(tmp.getBCode());
			fund.motherfund.setCode(tmp.getMotherCode());
			fund.codeName = tmp.getCodeName();
			fund.portion = tmp.getPortion();
			fund.afund.setNum(25000 * AconvertString2Double(fund.portion));
			fund.bfund.setNum(25000 * BconvertString2Double(fund.portion));
			fund.motherfund.setNum(25000);
			orginList.add(fund);

		}

		fun(downList, orginList);

		String[] columnNames = { "基金代码", "基金A代码", "A价格", "B价格", "C价格", "溢价" };
		String[][] cellData = new String[downList.size()][6];
		DefaultTableModel model = creat(downList, columnNames, cellData);
		return model;
	}

	// 从数据库采集数据
	private void fun2(HashMap<String, List<StockFund>> downList,
			List<StockFund> orginallist, String fromdate, String todate) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		// db.getCollection('BaseData').find({"$and":[{"nowTime":{"$gt":ISODate("2016-03-11")}},{"nowTime":{"$lt":ISODate("2016-03-24")}}]})

		String str = "{\"$and\":[{\"nowTime\":{\"$gt\":ISODate(" + fromdate
				+ ")}},{\"nowTime\":{\"$lt\":ISODate(" + todate + ")}}]} ";

		// 查询优化
		if (todate != null) {
			todate = todate.replace('-', '/');
			todate = todate + " 23:59:0";
		}
		if (fromdate != null) {
			fromdate = fromdate.replace('-', '/');
			fromdate = fromdate + " 0:0:0";
		}

		DBObject obj = null;
		try {
			if (todate != null && fromdate != null) {
				long l1 = ThreadLocalDateUtil.parse2(todate);
				long l2 = ThreadLocalDateUtil.parse2(fromdate);

				obj = new BasicDBObject("nowTime",
						new BasicDBObject("$lte", l1).append("$gte", l2));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DBObject> list = dbUtil.find(Configure.DailyDateTable, obj, null);
		// 可以优化
		for (int i = 0; i < list.size(); i++) {
			try {
				StockFund sf = new StockFund();
				StockFund tmp = sf.DbObject2StockFund(list.get(i));

				for (int j = 0; j < orginallist.size(); j++) {

					// 同样的母基金
					if (orginallist.get(j).motherfund.getCode().equals(
							tmp.motherfund.getCode())) {
						// 如果tmp.nowTime存在
						// 找到 destList 里面的链表直接添加
						// 如果不存在
						// destList 创建相关时间链表再

						if (downList.containsKey(TimeUtil.getDate(tmp.nowTime))) {
							List<StockFund> tmpList = downList.get(TimeUtil
									.getDate(tmp.nowTime));
							tmpList.add(tmp);
						} else {
							List<StockFund> tmpList = new ArrayList<StockFund>();
							tmpList.add(tmp);

							downList.put(TimeUtil.getDate(tmp.nowTime), tmpList);
						}

					}
				}
			} catch (Exception ex) {
				log.logger.error("UserInterface->fun2:" + ex.getMessage());
			}
		}

	}

	// 从数据库采集基金净值数据
	private void fun3(HashMap<String, List<StockFund>> orginalList) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		// db.getCollection('BaseData').find({"$and":[{"nowTime":{"$gt":ISODate("2016-03-11")}},{"nowTime":{"$lt":ISODate("2016-03-24")}}]})

		DBObject obj = null;
		List<DBObject> list = dbUtil
				.find(Configure.DailyDateJZTable, obj, null);
		// 可以优化
		for (int i = 0; i < list.size(); i++) {
			try {
				Fund tmp = new Fund();
				Fund fund = tmp.DBObj2StockCode(list.get(i));

				Iterator iter = orginalList.entrySet().iterator();
				while (iter.hasNext()) {
					// 能获得map中的每一个键值对了
					Entry entry = (Entry) iter.next();
					String key = entry.getKey().toString();
					String time = fund.getCreateTime().replace('/', '-');
					if (key.contains(time)) {
						// 替换掉里面的净值
						List<StockFund> sfList = orginalList.get(key);
						for (int j = 0; j < sfList.size(); j++) {
							if (sfList.get(j).motherfund.getCode().equals(
									fund.getCode()))// 说明代码一样
							{
								sfList.get(j).motherfund.setActualEquity(fund
										.getActualEquity());
							}

						}
					}
				}
			} catch (Exception ex) {
				log.logger.error("UserInterface->fun2:" + ex.getMessage());
			}
		}

	}

	public GZQH getGzqh(long nowtime) {

		GZQH gz = new GZQH();
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		DBObject obj = null;
		try {

			obj = new BasicDBObject("dqsj", nowtime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DBObject> list = dbUtil.find(Configure.GZQHDateTable, obj, null);
		return gz.DBObj2GZQH(list.get(0));

	}

	// 将数据转换成表格数据
	public DefaultTableModel creat2(HashMap<String, List<StockFund>> downList,
			String[] columnNames, String[][] cellData, int type) {
		try {

			int i = 0;

			Iterator iter = downList.entrySet().iterator();

			//
			while (iter.hasNext()) {
				// 能获得map中的每一个键值对了
				Entry entry = (Entry) iter.next();
				Object key = entry.getKey();
				cellData[i][0] = "" + key;
				List<StockFund> list = (List<StockFund>) entry.getValue();
				Double jjzz = 0.0;
				Double zyl = 0.0;
				Double gzqh = 0.0;
				Double test = 0.0;

				StringBuilder error = new StringBuilder();
				for (int j = 0; j < list.size(); j++) {

					if (type == 1) {
						jjzz += list.get(j).afund.getNetEquity()
								* list.get(j).afund.getNum()
								+ list.get(j).bfund.getNetEquity()
								* list.get(j).bfund.getNum()
								+ list.get(j).motherfund.getNetEquity()
								* list.get(j).motherfund.getNum();
						zyl += Math.abs(list.get(j).afund.getNetEquity()
								* list.get(j).afund.getNum()
								+ list.get(j).bfund.getNetEquity()
								* list.get(j).bfund.getNum()
								- list.get(j).motherfund.getNetEquity()
								* list.get(j).motherfund.getNum());
					} else {
						jjzz += list.get(j).afund.getNetEquity()
								* list.get(j).afund.getNum()
								+ list.get(j).bfund.getNetEquity()
								* list.get(j).bfund.getNum()
								+ list.get(j).motherfund.getActualEquity()
								* list.get(j).motherfund.getNum();
						zyl += Math.abs(list.get(j).afund.getNetEquity()
								* list.get(j).afund.getNum()
								+ list.get(j).bfund.getNetEquity()
								* list.get(j).bfund.getNum()
								- list.get(j).motherfund.getActualEquity()
								* list.get(j).motherfund.getNum());
					}

					test += list.get(j).motherfund.getActualEquity()
							* list.get(j).motherfund.getNum();
					error.append(list.get(i).errorInfo);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

				long millionSeconds = sdf.parse(key.toString()).getTime() / 1000;// 毫秒

				try {
					GZQH gz = getGzqh(millionSeconds);
					if (gz != null) {
						gzqh = gz.ifzs * 300 + gz.ihzs * 200 + gz.iczs * 300;
					}
				} catch (Exception ex) {

				}
				cellData[i][2] = "" + gzqh;

				cellData[i][1] = "" + jjzz;
				cellData[i][3] = "" + zyl;
				cellData[i][4] = "" + test;

				cellData[i][5] = error.toString();
				i++;
			}

			DefaultTableModel model = new DefaultTableModel(cellData,
					columnNames);
			return model;
		} catch (Exception ex) {
			log.logger.error("UserInterface->creat2:" + ex.getMessage());
		}
		return null;
	}

	public DefaultTableModel creat3(HashMap<String, List<StockFund>> downList,
			String[] columnNames, String[][] cellData) {
		try {

			int i = 0;

			Iterator iter = downList.entrySet().iterator();

			//
			while (iter.hasNext()) {
				// 能获得map中的每一个键值对了
				Entry entry = (Entry) iter.next();
				Object key = entry.getKey();
				cellData[i][0] = "" + key;
				List<StockFund> list = (List<StockFund>) entry.getValue();
				Double jjzz = 0.0;
				Double zyl = 0.0;
				Double gzqh = 0.0;
				Double test = 0.0;

				StringBuilder error = new StringBuilder();
				for (int j = 0; j < list.size(); j++) {
					jjzz += list.get(j).afund.getNetEquity()
							* list.get(j).afund.getNum()
							+ list.get(j).bfund.getNetEquity()
							* list.get(j).bfund.getNum()
							+ list.get(j).motherfund.getActualEquity()
							* list.get(j).motherfund.getNum();
					zyl += Math.abs(list.get(j).afund.getNetEquity()
							* list.get(j).afund.getNum()
							+ list.get(j).bfund.getNetEquity()
							* list.get(j).bfund.getNum()
							- list.get(j).motherfund.getActualEquity()
							* list.get(j).motherfund.getNum());

					test += list.get(j).motherfund.getActualEquity()
							* list.get(j).motherfund.getNum();
					error.append(list.get(i).errorInfo);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

				long millionSeconds = sdf.parse(key.toString()).getTime() / 1000;// 毫秒

				try {
					GZQH gz = getGzqh(millionSeconds);
					if (gz != null) {
						gzqh = gz.ifzs * 300 + gz.ihzs * 200 + gz.iczs * 300;
					}
				} catch (Exception ex) {

				}
				cellData[i][2] = "" + gzqh;

				cellData[i][1] = "" + jjzz;
				cellData[i][3] = "" + zyl;
				cellData[i][4] = "" + test;

				cellData[i][5] = error.toString();
				i++;
			}

			DefaultTableModel model = new DefaultTableModel(cellData,
					columnNames);
			return model;
		} catch (Exception ex) {
			log.logger.error("UserInterface->creat2:" + ex.getMessage());
		}
		return null;
	}

	// 计算选中的节点的汇总统计
	public DefaultTableModel summary(String fromdate, String todate) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		List<DBObject> list = dbUtil.find(Configure.MoniterDateTable, null,
				null);
		List<StockFund> orginList = new ArrayList();
		HashMap<String, List<StockFund>> downList = new HashMap<String, List<StockFund>>();
		for (int i = 0; i < list.size(); i++) {
			StockCode sc = new StockCode();
			StockCode tmp = sc.DBObj2StockCode(list.get(i));
			// 根据母基金代码填充fundNode；
			StockFund fund = new StockFund();

			fund.afund.setCode(tmp.getACode());
			fund.bfund.setCode(tmp.getBCode());
			fund.motherfund.setCode(tmp.getMotherCode());
			fund.codeName = tmp.getCodeName();
			fund.portion = tmp.getPortion();
			fund.afund.setNum(25000 * AconvertString2Double(fund.portion));
			fund.bfund.setNum(25000 * BconvertString2Double(fund.portion));
			fund.motherfund.setNum(25000);
			orginList.add(fund);

		}

		fun2(downList, orginList, fromdate, todate);

		fun3(downList);// 获取净值数据

		String[] columnNames = { "采集时间", "基金总市值", "股指期货总市值", "基金盈利", "母基金净值",
				"基金出错统计" };
		String[][] cellData = new String[downList.size()][6];

		DefaultTableModel model = creat2(downList, columnNames, cellData, 1);// 算网络

		String writeFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createSummaryFileName("Net");
		// 写excel
		WriteExcel.writeExcelFile2(writeFilePath2, columnNames, cellData);

		creat2(downList, columnNames, cellData, 2);// 算净值

		String writeFilePath3 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createSummaryFileName("Actual");
		// 写excel
		WriteExcel.writeExcelFile2(writeFilePath3, columnNames, cellData);

		return model;

	}

	private void fun(List<StockFund> orginallist, Fund sfnode, String fromdate,
			String todate) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		// db.getCollection('BaseData').find({"$and":[{"nowTime":{"$gt":ISODate("2016-03-11")}},{"nowTime":{"$lt":ISODate("2016-03-24")}}]})
		// 查询优化

		if (fromdate != null) {
			fromdate = fromdate.replace('-', '/');
			fromdate = fromdate + " 14:57:0";
		}

		if (todate != null) {
			todate = todate.replace('-', '/');
			todate = todate + " 22:20:0";
		}
		DBObject obj = null;
		try {
			if (todate != null && fromdate != null) {
				long l1 = ThreadLocalDateUtil.parse2(todate);
				long l2 = ThreadLocalDateUtil.parse2(fromdate);

				obj = new BasicDBObject("nowTime",
						new BasicDBObject("$lte", l1).append("$gte", l2));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DBObject> list = dbUtil.find(Configure.DailyDateTable, obj, null);
		// 可以优化
		for (int i = 0; i < list.size(); i++) {
			try {
				StockFund sf = new StockFund();
				StockFund tmp = sf.DbObject2StockFund(list.get(i));
				// 同样的母基金
				if (sfnode.getCode().equals(tmp.motherfund.getCode())) {
					// 如果tmp.nowTime存在
					// 找到 destList 里面的链表直接添加
					// 如果不存在
					// destList 创建相关时间链表再

					tmp.setMotherfund(sfnode);
					orginallist.add(tmp);
				}

			} catch (Exception ex) {
				log.logger.error("CollectJZJob->fun2:" + ex.getMessage());
			}
		}

	}

	// 计算选中的节点的汇总统计
	public void summary2(String date) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		DBObject obj = null;
		try {
			if (date != null) {
				
				date = date.replace('-', '/');
				obj = new BasicDBObject("createTime", date);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DBObject> list = dbUtil
				.find(Configure.DailyDateJZTable, obj, null);
		List<StockFund> orginList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			try {
				Fund sf = new Fund();
				Fund tmp = sf.DBObj2StockCode(list.get(i));

				fun(orginList, tmp, date, date);

			} catch (Exception ex) {
				log.logger.error("CollectJZJob->summary2:" + ex.getMessage());
			}
		}
		String writeFilePath2 = Class.class.getClass().getResource("/")
				.getPath()
				+ WriteExcel.createSummaryFileName("jzjs");
		WriteExcel.writeExcelFile(writeFilePath2, orginList,true);

	}
}
