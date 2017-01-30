package controll;

import impl.CreateJJJZFromSina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;

import model.SysGloableValue;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import util.CodeChange;
import util.Configure;
import util.Fund;
import util.GZQH;
import util.Log;
import util.MongoDBUtil;
import util.StockFund;
import util.ThreadLocalDateUtil;
import util.TimeUtil;
import util.WriteExcel;
import util.WriteFile;

public class CollectJZJob implements Job {
	Log log = Log.getLogger();
	private long currentTime = 0;
	
	private void  fun2(List<StockFund> orginallist,Fund sfnode, String fromdate, String todate) {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		// db.getCollection('BaseData').find({"$and":[{"nowTime":{"$gt":ISODate("2016-03-11")}},{"nowTime":{"$lt":ISODate("2016-03-24")}}]})

		// 查询优化
		
		if (fromdate != null) {
			fromdate = fromdate.replace('-', '/');
			fromdate = fromdate + " 14:57:0";
		}
		
		if (todate != null) {
			todate = todate.replace('-', '/');
			todate = todate + " 15:20:0";
		}
		DBObject obj = null;
		try {
			if (todate!=null && fromdate!=null)
			{
				long l1= ThreadLocalDateUtil.parse2(todate);
				long l2= ThreadLocalDateUtil.parse2(fromdate);

				obj = new BasicDBObject("nowTime", new BasicDBObject("$lte",
						l1).append("$gte",
						l2));
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
					if (sfnode.getCode().equals(
							tmp.motherfund.getCode())) {
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
	private void downloadDataFromJZ() {
		MongoDBUtil dbUtil = MongoDBUtil.instance;
		SysGloableValue sysGV = SysGloableValue.getGloableValue();
		List<StockFund> orginList = new ArrayList();

		for (int i = 0; i < sysGV.fundList.size(); i++) {
			CreateJJJZFromSina cfs = new CreateJJJZFromSina();
			StockFund sf = sysGV.fundList.get(i);
			Fund f = cfs.getFundFromNet(sf.motherfund.getCode());
			if (f != null)
				dbUtil.insert(Configure.DailyDateJZTable,
						f.Fund2DBObj());
	

			log.logger.info("CollectJZJob->downloadDataFromJZ");
				
			//查询2：55-3:20之间的数据并列出来；
			fun2(orginList,f,f.getCreateTime(),f.getCreateTime());
			
			String writeFilePath2 = Class.class.getClass().getResource("/")
					.getPath()
					+ WriteExcel.createSummaryFileName("jzjs");
			WriteExcel.writeExcelFile(writeFilePath2, orginList,true);
				//写入到文件ß
		}
	}


	public CollectJZJob() {
		 Date date=new Date();
		currentTime = date.getTime()/1000;
	}

	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("★★★★★★★★★★★");
		downloadDataFromJZ();
	}

}
