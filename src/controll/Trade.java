package controll;

import java.text.SimpleDateFormat;
import java.util.Date;

import util.*;

public class Trade {

	public double bit = 0.1;
	public String filePath;

	public Trade(String path) {
		filePath = path;
	}

	// 场内买入基金
	public void BuyFund(StockFund fund) {
		/*double fundCost = fund.aFund + fund.bFund;
		int flag = 0;
		
		if (fundCost < fund.actualEquity) // 市场上的A+B 比场内的低，从市场买，场内卖
		{
			double costFromTrade = TaxFee.cmptBuyFromMaket(fund)
					+ TaxFee.cmptSellToVenue(fund);
			double costFromFund = fundCost * fund.totalNum / 2;

			double bitFromTrade = (fund.actualEquity * fund.totalNum
					- costFromTrade - costFromFund)
					/ fund.actualEquity * fund.totalNum;

			if (bitFromTrade > bit) {
				fund.buyTime = TradeTime.cmptDateNow();// 获取当前时间
				fund.sellTime = TradeTime.cmptTradetoVenue(fund.buyTime);// 计算卖到场内的时间
				System.out.println("买入fund" + fund.motherCode);
				flag = 1;
			} else {
				System.out.println("没有获利空间");
			}

		}
		if (fundCost > fund.actualEquity) // 市场上的A+B 比场内的高，从场内买，市场卖
		{
			double costFromTrade = TaxFee.cmptBuyFromVenue(fund)
					+ TaxFee.cmptSellToMaket(fund);
			double costFromFund = fundCost * fund.totalNum / 2;

			double bitFromTrade = (fund.actualEquity * fund.totalNum
					- costFromTrade - costFromFund)
					/ fund.actualEquity * fund.totalNum;

			if (bitFromTrade > bit) {
				fund.buyTime = TradeTime.cmptDateNow();// 获取当前时间
				fund.sellTime = TradeTime.cmptTradetoMarket(fund.buyTime);// 计算卖到市场的时间
				System.out.println("买入fund" + fund.motherCode);
				flag = 2;
			} else {
				System.out.println("没有获利空间");
			}

		}

		if (flag>0) {
			System.err.println("存在获利");
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
		   
		    
			String content = "code:" + fund.motherCode + "|" + "actualEquity:"
					+ fund.actualEquity + "|" + "aFund:" + fund.aFund + "|"
					+ "bFund:" + fund.bFund + "|" + "buyDate:" + formatter.format(fund.buyTime)
					+ "|" + "sellDate:" +  formatter.format(fund.sellTime);
			if(flag==1)
				content+="|"+"M->V(市场买，场内卖)";
			else if(flag ==2)
				content+="|"+"V->M(场内买，市场卖)";
			else
			{
				
			}
			WriteFile.writeTxtFile(filePath, content);
		}
		flag = 0;
*/
	}

}
