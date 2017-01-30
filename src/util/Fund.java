package util;

import java.util.Calendar;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

public class Fund  extends ReflectionDBObject
{
	private  String code;
	private  double netEquity;//网络估计净值
	private  double actualEquity;//实际净值，前一天成交价
	private  double num;//当前数量
	private  String createTime;//当前时间
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getNetEquity() {
		return netEquity;
	}

	public void setNetEquity(double netEquity) {
		this.netEquity = netEquity;
	}

	public double getActualEquity() {
		return actualEquity;
	}

	public void setActualEquity(double actualEquity) {
		this.actualEquity = actualEquity;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Fund(String code)
	{   super();
		this.code = code;
		netEquity = 0;
		actualEquity = 0;
		num =0;		
		//createTime = Calendar.getInstance().getTime();
	}
	
	public Fund()
	{   super();
		code = new String();
		netEquity = 0;
		actualEquity = 0;
		num =0;
		//createTime = new Date();
	}
	
	public DBObject Fund2DBObj()
	{
		BasicDBObject bo = new BasicDBObject();
		bo.put("code", this.code);
		bo.put("netEquity", this.netEquity);

		bo.put("actualEquity", this.actualEquity);
		bo.put("num", this.num);
		bo.put("createTime", this.createTime);

		return bo;

		
	}
	public Fund DBObj2StockCode(DBObject obj) {
		Fund  fund = new Fund();
		if (obj.get("code") != null)
			fund.code = obj.get("code").toString();
		if (obj.get("netEquity") != null)
			fund.netEquity = Double.parseDouble(obj.get("netEquity").toString());
		if (obj.get("actualEquity") != null)
			fund.actualEquity = Double.parseDouble(obj.get("actualEquity").toString());
		if (obj.get("num") != null)
			fund.num = Double.parseDouble(obj.get("num").toString());
		if (obj.get("createTime") != null)
			fund.createTime = obj.get("createTime").toString();;//new Date(obj.get("createTime").toString());

		return fund;
	}
}