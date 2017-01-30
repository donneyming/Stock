package util;

public class TaxFee {

	// 场内买入基金的费用
	public static double cmptBuyFromVenue(Fund fund) {
		return fund.getNetEquity() * fund.getNum() * 0.0005;
	}

	// 场内卖出基金的费用
	public static double cmptSellToVenue(Fund fund) {
		return fund.getNetEquity() * fund.getNum() * 0.0005;
	}

	// 二级市场买入基金的费用（申购）1.5%
	public static double cmptBuyFromMaket(Fund fund) {
		return fund.getNetEquity() * fund.getNum() * 0.0015;
	}

	// 二级市场卖出基金的费用（赎回）0.5%
	public static double cmptSellToMaket(Fund fund) {
		return fund.getNetEquity() * fund.getNum() * 0.005;
	}

	// 场内 拆分费用
	public static double cmptSplit(Fund fund) {
		return 0;
	}

	// 场内 合并费用
	public static double cmptCombile(Fund fund) {
		return 0;
	}
}
