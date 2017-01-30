package impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import model.TianTianJsRoot;
import controll.CreateDownDataFromNet;
import util.Fund;
import util.HttpUtil;
import util.JsonFormatToolForTianTian;
import util.Log;

public class CreateDataFromHeXun implements CreateDownDataFromNet {
	Log log = Log.getLogger();

	@Override
	public Fund getFundFromNet(String code) {
		try {
			String strURL = "http://quote.stock.hexun.com/stockdata/fund_quote.aspx?stocklist="
					+ code;
			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);

			Fund fund = createFromHexun(code, buf);
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromHeXun->createFromHexun:"
					+ e.getMessage());
		}
		return null;
	}

	private Fund createFromHexun(String code, String content) {
		// 针对网址1的解析代码
		/*
		 * JsonFormatToolForHexun tool = new JsonFormatToolForHexun();
		 * HexunPacket packet = tool.formatJson(content);
		 * 
		 * Fund fund = new Fund(); fund.code = code; fund.netEquity =
		 * Double.parseDouble(packet.price); System.err.println("基金代码:" + code +
		 * "|" + "当前交易:" + fund.actualEquity); return fund;
		 */
		// http://jingzhi.funds.hexun.com/502021.shtml

		// 针对网址2的解析代码
		try {
			String listofText[] = content.split(",");

			
			Fund fund = new Fund();
			fund.setCode(code);

			if (code.startsWith("150")) // sz打头的代码sz150205
				fund.setNetEquity(Double.parseDouble(listofText[3]));
			else
				// 502打头的代码 sh的代码 sh502013
				fund.setNetEquity(Double.parseDouble(listofText[4]));
			System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			log.logger.info("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromHeXun->createFromHexun:"
					+ e.getMessage());
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
			log.logger.error("CreateDataFromHeXun->getMotherFundFromNet:"
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
			log.logger.error("CreateDataFromHeXun->createMomFundFromTianTian:"
					+ ex.getMessage());
		}
		return null;
	}

}