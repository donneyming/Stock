package util;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GZQH {
public String code;//代码
public double  ifzs;//指数
public double  ihzs;//指数
public double  iczs;//指数

public long  dqsj;//当前时间

public GZQH()
{
	 Date date=new Date();

	 dqsj =date.getTime()/1000;
}
public DBObject GZQH2DbObject(){
	BasicDBObject bo = new BasicDBObject();
	bo.put("code",this.code);
	bo.put("ifzs",this.ifzs);
	bo.put("ihzs",this.ihzs);
	bo.put("iczs",this.iczs);
	bo.put("dqsj", this.dqsj);
	return bo;
	
}
public GZQH DBObj2GZQH(DBObject obj) {
	GZQH  gz= new GZQH();
	if (obj.get("code") != null)
		gz.code = obj.get("code").toString();
	if (obj.get("ifzs") != null)
		gz.ifzs = Double.parseDouble(obj.get("ifzs").toString());
	if (obj.get("ihzs") != null)
		gz.ihzs = Double.parseDouble(obj.get("ihzs").toString());
	if (obj.get("iczs") != null)
		gz.iczs = Double.parseDouble(obj.get("iczs").toString());
	if (obj.get("dqsj") != null)
		gz.dqsj =Long.parseLong( obj.get("dqsj").toString());
	return gz;
}
}
