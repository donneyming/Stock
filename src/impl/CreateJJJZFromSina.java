package impl;

import java.util.Date;
import java.util.Random;

import util.Fund;
import util.HttpUtil;
import util.Log;
import util.ThreadLocalDateUtil;


//获取基金净值
public class CreateJJJZFromSina {
	Log log = Log.getLogger();

	//http://hq.sinajs.cn/?_=1458877171697/&list=f_502020
	//var hq_str_f_502020="国金上证50分级,1.098,0.637,1.117,2016-03-24,0.253564";
	public Fund getFundFromNet(String code) {
		try {
			Date d2 = new Date();// 1456803892615
			int max = 999;
			int min = 100;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;

			String strURL = "http://hq.sinajs.cn/?_=" +d2.getTime()+s+"&list=f_"+ code;

			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);

			Fund fund = createFromSina(code, buf);
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromSina->getFundFromNet:"
					+ e.getMessage());
		}
		return null;

	}
	public String getFundFromNet(String code,String datefrom,String dateto) {
		try {
			Date d2 = new Date();// 1456803892615
			int max = 999;
			int min = 100;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;
			// 东方财富网址 共有5种url 暂时未全部实现
			Random r = new Random();
			double d1 = r.nextDouble();

			return null;
		} catch (Exception e) {
			log.logger.error("CreateDataFromJiSiLu->getFundFromNet:"
					+ e.getMessage());
		}
		return null;
	}
	//var hq_str_f_502020="国金上证50分级,1.098,0.637,1.117,2016-03-24,0.253564";

	private Fund createFromSina(String code, String content) {

		try {
			String listofText[] = content.split(",");

			// System.out.println("netEquity"+getTagValue(str));

			Fund fund = new Fund();
			fund.setCode(code);
			fund.setActualEquity(Double.parseDouble(listofText[1]));
			String date =listofText[4].replace('-', '/');
			//date+=" 22:0:0";
			fund.setCreateTime(date);

			System.err.println("基金代码:" + code + "|" + "净值基金:" + fund.getActualEquity());
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateJJJZFromSina->createFromSina:"
					+ e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		CreateJJJZFromSina cs  = new CreateJJJZFromSina();
		cs.getFundFromNet("502020");
	}
}
