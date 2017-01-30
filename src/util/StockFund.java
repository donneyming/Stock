package util;

import java.text.ParseException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class StockFund {
	
	public  Fund motherfund; //母基金代码
	public void setMotherfund(Fund motherfund) {
		this.motherfund.setCode(motherfund.getCode());
		this.motherfund.setActualEquity(motherfund.getActualEquity());
		//用于计算生成净值的表 0412 
		this.motherfund.setNetEquity(motherfund.getActualEquity());

		this.motherfund.setCreateTime(motherfund.getCreateTime());
	}
	public  Fund afund; //A基金代码
	public  Fund bfund; //B基金代码
	public  String codeName; //基金名称

	public String appendix;//基金前缀，用于查询sina接口专用
	
	/*public  double netEquity;//网络估计净值
	public  double actualEquity;//实际净值，前一天成交价
	public  double aFund;//A基金
	public  double bFund;//B基金*/
	public 	double  totalNum =100;//买入数量  
	public  long nowTime;//当前时间
	public  Date buyTime;//买入时间
	public  Date sellTime;//卖出时间
	
	public String portion;//a：b的比例字符串
	
	public String errorInfo;//出错信息
	public StockFund()
	{
		motherfund = new Fund();
		afund = new Fund();
		bfund = new Fund();
		 Date date=new Date();
		 errorInfo = new String();
		try {
			this.nowTime =date.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public StockFund(String code1,String code2,String code3,String codeName,String portion)
	{
		motherfund = new Fund(code3);
		this.codeName =codeName;
		afund = new Fund(code1);
		bfund = new Fund(code2);
		this.portion = portion;
	}
	
	public DBObject StockFund2DbObject(){
		BasicDBObject bo = new BasicDBObject();
		bo.put("codeName",this.codeName);
		bo.put("nowTime",this.nowTime);
		bo.put("afund", this.afund.Fund2DBObj());
		bo.put("bfund", this.bfund.Fund2DBObj());
		bo.put("motherfund", this.motherfund.Fund2DBObj());
		bo.put("errorInfo", this.errorInfo);

		return bo;
		
	}
	//fund序列化DBObject
	public StockFund DbObject2StockFund(DBObject obj){
		
		
		StockFund  sf = new StockFund();
		if (obj.get("codeName") != null)
			sf.codeName = obj.get("codeName").toString();
		if (obj.get("nowTime") != null)
			try {
				sf.nowTime = Long.parseLong(obj.get("nowTime").toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (obj.get("afund") != null)
		{
			DBObject aobj = (DBObject) obj.get("afund");
			sf.afund  = new Fund();
			sf.afund =  sf.afund.DBObj2StockCode(aobj);
			
		}
		if (obj.get("bfund") != null)
		{
			DBObject bobj = (DBObject) obj.get("bfund");
			sf.bfund  = new Fund();
			sf.bfund =  sf.bfund.DBObj2StockCode(bobj);
			
		}
		if (obj.get("motherfund") != null)
		{
			DBObject mobj = (DBObject) obj.get("motherfund");
			sf.motherfund  = new Fund();
			sf.motherfund =  sf.motherfund.DBObj2StockCode(mobj);
			
		}
		if (obj.get("errorInfo") != null)
			sf.errorInfo = obj.get("errorInfo").toString();
		return sf;
		
		
	}
	
}
