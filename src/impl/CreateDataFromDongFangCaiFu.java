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

public class CreateDataFromDongFangCaiFu implements CreateDownDataFromNet {
	Log log = Log.getLogger();

	@Override
	public Fund getFundFromNet(String code) {
		try {
			// 东方财富网址 共有5种url 暂时未全部实现
			Random r = new Random();
			double d1 = r.nextDouble();
			String strURL = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=ZT&jsName=jsfavs&fav="
					+ code + "&Reference=xml&rt=" + d1;
			String buf = HttpUtil.downHtml(strURL);
			log.logger.info(buf);
			Fund fund = createFromDongcai(code, buf);
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromJiSiLu->getFundFromNet:"
					+ e.getMessage());
		}
		return null;
	}

	private Fund createFromDongcai(String code, String content) {

		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1502052&Reference=xml&rt=0.42024372954164546
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1502062&Reference=xml&rt=0.5008210830067212
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1500942&Reference=xml&rt=0.7448945334961105
		// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=fs&jsName=js&stk=1500952&Reference=xml&rt=0.3139436149418562
		// 股指期货指数
		// http://hq2gnqh.eastmoney.com/em_futures2010numericapplication/index.aspx?type=f&id=IF16031
		try {
			String listofText[] = content.split(",");

			// System.out.println("netEquity"+getTagValue(str));

			Fund fund = new Fund();
			fund.setCode(code);
			fund.setNetEquity(Double.parseDouble(listofText[3]));
			System.err.println("基金代码:" + code + "|" + "当前交易:" + fund.getNetEquity());
			return fund;
		} catch (Exception e) {
			log.logger.error("CreateDataFromDongFangCaiFu->createFromDongcai:"
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
			log.logger.error("CreateDataFromDongFangCaiFu->getMotherFundFromNet:"
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
			log.logger.error("CreateDataFromDongFangCaiFu->createMomFundFromTianTian:"
					+ ex.getMessage());
		}
		return null;
	}

}
