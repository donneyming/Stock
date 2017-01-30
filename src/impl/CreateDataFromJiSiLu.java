package impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import controll.CreateDownDataFromNet;

import model.JslRoot;
import model.TianTianJsRoot;


import util.Fund;
import util.HttpUtil;
import util.JsonFormatToolForTianTian;
import util.JsonFormatToolForjisilu;
import util.Log;

public class CreateDataFromJiSiLu implements CreateDownDataFromNet {
	Log log = Log.getLogger();

	@Override
	public Fund getFundFromNet(String code) {
		
		try
		{
		Date d2 = new Date();// 1456803892615
		int max = 999;
		int min = 100;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		// 集思录
		String strURL = "http://www.jisilu.cn/jisiludata/StockFenJiDetail.php?qtype=dynamic&fund_id="
				+ code + "&___t=" + d2.getTime() + String.valueOf(s);
		String buf = HttpUtil.downHtml(strURL);
		log.logger.info(buf);

		Fund fund = createFromJisilu(code, buf);
		return fund;
		}
		catch (Exception e) {
			  log.logger.error("CreateDataFromJiSiLu->getFundFromNet:"+e.getMessage());  
		}
		return null;
	}

	private Fund createFromJisilu(String code, String content) {
		try {
		// 将字符串"变成\"
		String tmp = content.replaceAll("\"", "\"");
		JsonFormatToolForjisilu json = new JsonFormatToolForjisilu();
		JslRoot root = json.formatJson(tmp);
		Fund fund = new Fund();
		fund.setCode(code);
		for (int i = 0; i < root.getRows().size(); i++) {
			if (fund.getCode().equals(root.getRows().get(i).getId())) {
				fund.setNetEquity(Double.parseDouble(root.getRows().get(i)
						.getCell().getPrice()));
				System.err.println("基金代码:" + code + "|" + "当前交易:"
						+ fund.getNetEquity());
				log.logger.info("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			}
		}

		return fund;
		}
		catch (Exception e) {
			  log.logger.error("CreateDataFromJiSiLu->createFromJisilu:"+e.getMessage());  
		}
		return null;
	}
	@Override
	public Fund getMotherFundFromNet(String code) {
		try {
			Date d2 = new Date();// 1456803892615
			Random r = new Random();
			int max = 999;
			int min = 100;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;
			// "http://fundgz.1234567.com.cn/js/" + code + ".js?rt=" + new
			// Date().getTime(), "utf-8", 天天基金网网址
			String strURL = "http://fundgz.1234567.com.cn/js/" + code
					+ ".js?rt=" + d2.getTime();
			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);

			Fund fund = createMomFundFromTianTian(code, buf);
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromJiSiLu->getMotherFundFromNet:"
					+ e.getMessage());
		}
		return null;

	}

	// 获取母基金的预估值
	private Fund createMomFundFromTianTian(String code, String content) {

		try {
			TianTianJsRoot root = JsonFormatToolForTianTian.formatJson(content);

			Fund fund = new Fund();
			fund.setCode(code);
			fund.setNetEquity(Double.parseDouble(root.getGsz()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			fund.setCreateTime(root.getGztime());
			System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			log.logger.info("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			return fund;
		} catch (Exception ex) {
			log.logger.error("CreateDataFromJiSiLu->createMomFundFromTianTian:"
					+ ex.getMessage());
		}
		return null;
	}

}
