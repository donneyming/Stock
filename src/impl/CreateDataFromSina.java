package impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import model.TianTianJsRoot;

import controll.CreateDownDataFromNet;

import util.Fund;
import util.HttpUtil;
import util.JsonFormatToolForTianTian;
import util.Log;
import util.UserAgent;

public class CreateDataFromSina implements CreateDownDataFromNet {
	Log log = Log.getLogger();

	@Override
	public Fund getFundFromNet(String code) {
		try {
			String appendix = new String();
			if (code.startsWith("150"))
				appendix = "sz";
			else
				appendix = "sh";
			String strURL = "http://hq.sinajs.cn/list=" + appendix + code;

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

	private Fund createFromSina(String code, String content) {
		try {
			String listofText[] = content.split(",");
			log.logger.info(listofText);
			int length = listofText.length;
			Fund fund = new Fund();
			fund.setCode(code);
			// cms 0310更改
			fund.setNetEquity(Double.parseDouble(listofText[3]));
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			try {
				fund.setCreateTime(listofText[length - 2]);
			} catch (Exception e) {
				log.logger.error("CreateDataFromSina->createFromSina:"
						+ e.getMessage());
			}
			log.logger.info("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			// System.err.println("基金代码:" + code + "|" + "当前交易:" +
			// fund.netEquity);
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromSina->createFromSina:"
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
			log.logger.error("CreateDataFromSina->getMotherFundFromNet:"
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
			log.logger.error("CreateDataFromSina->createMomFundFromTianTian:"
					+ ex.getMessage());
		}
		return null;
	}

}
