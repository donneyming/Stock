package controll;

/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import util.HtmlUtil;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

/**
 * WebCollector 2.x版本的tutorial(version>=2.20) 2.x版本特性：
 * 1）自定义遍历策略，可完成更为复杂的遍历业务，例如分页、AJAX
 * 2）可以为每个URL设置附加信息(MetaData)，利用附加信息可以完成很多复杂业务，例如深度获取
 * 、锚文本获取、引用页面获取、POST参数传递、增量更新等。 3）使用插件机制，WebCollector内置两套插件。
 * 4）内置一套基于内存的插件（RamCrawler)，不依赖文件系统或数据库，适合一次性爬取，例如实时爬取搜索引擎。 5）内置一套基于Berkeley
 * DB（BreadthCrawler)的插件：适合处理长期和大量级的任务，并具有断点爬取功能，不会因为宕机、关闭导致数据丢失。
 * 6）集成selenium，可以对javascript生成信息进行抽取 7）可轻松自定义http请求，并内置多代理随机切换功能。
 * 可通过定义http请求实现模拟登录。 8）使用slf4j作为日志门面，可对接多种日志
 * 
 * 可在cn.edu.hfut.dmic.webcollector.example包中找到例子(Demo)
 * 
 * @author hu
 */
public class TutorialCrawler extends BreadthCrawler {

	public TutorialCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	/*
	 * 可以往next中添加希望后续爬取的任务，任务可以是URL或者CrawlDatum
	 * 爬虫不会重复爬取任务，从2.20版之后，爬虫根据CrawlDatum的key去重，而不是URL
	 * 因此如果希望重复爬取某个URL，只要将CrawlDatum的key设置为一个历史中不存在的值即可 例如增量爬取，可以使用
	 * 爬取时间+URL作为key。
	 * 
	 * 新版本中，可以直接通过 page.select(css选择器)方法来抽取网页中的信息，等价于
	 * page.getDoc().select(css选择器)方法，page.getDoc()获取到的是Jsoup中的
	 * Document对象，细节请参考Jsoup教程
	 */
	@Override
	public void visit(Page page, CrawlDatums next) {
		if (page.matchUrl("http://jingzhi.funds.hexun.com/[0-9]+.shtml")) {
			String netEquity = page.select("font[id=value1]").first().data();
			//String nowTime = page.select("div[id=tradetime]").first().text();

			
			/* String strRegex1 =
			  "<div id=\"statuspzgz\" class=\"fundpz\"><span class=\".+?\">(.+?)</span>"
			  ; String strRegex2 = "<p class=\"time\">(.+?)</p>"; String
			 strRegex3 =
			 "<span class='left12'><span class=\"green bold\">(.+?)</span>";
			 
			 String netEquity = HtmlUtil.getTagValue(strRegex1,
			 page.getHtml()); String nowTime = HtmlUtil.getTagValue(strRegex2,
			 page.getHtml());
			 */

			System.err.println("title:" + netEquity );

		}
	}

	public static void main(String[] args) throws Exception {
		/*TutorialCrawler crawler = new TutorialCrawler("crawler", true);
		/*crawler.addSeed("http://fund.eastmoney.com/502020.html");
		crawler.addSeed("http://fund.eastmoney.com/213008.html");
		crawler.addSeed("http://fund.eastmoney.com/000173.html");
		crawler.addSeed("http://fund.eastmoney.com/000477.html");
		*/
		//crawler.addRegex("http://fund.eastmoney.com/.*");

		/* 可以设置每个线程visit的间隔，这里是毫秒 */
		// crawler.setVisitInterval(1000);
		/* 可以设置http请求重试的间隔，这里是毫秒 */
		// crawler.setRetryInterval(1000);
		/* 设置每层爬取爬取的最大URL数量 */
		
		
		//crawler.addSeed("http://jingzhi.funds.hexun.com/150095.shtml");

		//crawler.start(1);
		String[][]   cellData =  new   String[10][4]; 
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<4;j++)
			{
				cellData[i][j] ="00";
			}
		}
		
	}

}
