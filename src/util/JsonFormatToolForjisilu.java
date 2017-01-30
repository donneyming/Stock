package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.util.JSON;

import model.JslRoot;
import model.JslRows;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

/*
 {
 "page": 1,
 "rows": [
 {
 "id": "502021",
 "cell": {
 "type_cd": "A\\u57fa",
 "fund_id": "502021",
 "fund_nm": "\\u56fd\\u91d150A",
 "ratio": "5",
 "profit_rt": "5.102%",
 "est_val": "1.010",
 "discount_rt": "-1.98%",
 "price": "0.990",
 "volume": "7.85",
 "increase_rt": "0.20%",
 "fund_nav": "1.0100",
 "nav_dt": "2016-02-29"
 }
 },
 {
 "id": "502022",
 "cell": {
 "type_cd": "B\\u57fa",
 "fund_id": "502022",
 "fund_nm": "\\u56fd\\u91d150B",
 "ratio": "5",
 "profit_rt": "7.367%",
 "est_val": "1.014",
 "discount_rt": "1.492%",
 "price": "1.029",
 "volume": "10.36",
 "increase_rt": "1.38%",
 "fund_nav": "0.9860",
 "nav_dt": "2016-02-29"
 }
 },
 {
 "id": "502020",
 "cell": {
 "type_cd": "\\u6bcd\\u57fa",
 "fund_id": "502020",
 "fund_nm": "\\u56fd\\u91d150 (\\u4e0a\\u8bc150)",
 "ratio": " ",
 "profit_rt": " ",
 "est_val": "1.0119",
 "discount_rt": "-0.241%",
 "fund_nav": "0.9980",
 "nav_dt": "2016-02-29",
 "price": "1.0095 (\\u5408\\u5e76\\u4ef7\\u683c)",
 "volume": " ",
 "increase_rt": "1.47% (\\u6307\\u6570\\u6da8\\u5e45)"
 }
 }
 ],
 "total": 3
 }
 */
public class JsonFormatToolForjisilu {

	@SuppressWarnings("unchecked")
	public JslRoot formatJson(String html) {
		JslRoot root = new JslRoot();
		// root= (JslRoot)JsonPluginsUtil.jsonToBean(html,root.getClass());
		try {
			
			JSONObject obj = JSONObject.fromObject(html);  
			root.setPage(obj.getInt("page"));
			if(obj.has("rows"))
			{
			root.setRows(obj.getJSONArray("rows"));
			 JSONArray rows = obj.getJSONArray("rows");  
			 for (int i = 0; i < rows.size(); i++) {  
	                System.out.println("rows:" + rows.getString(i) + " ");  
	            }  
             root.setRows(JSONArray.toList(rows, JslRows.class));

			}
			root.setTotal(obj.getInt("total"));
		
		} catch (JSONException ex) {
			// 异常处理代码
			System.out.print(ex.toString());
		}
		return root;
	}

	public static void main(String[] args) {
		JsonFormatToolForjisilu json = new JsonFormatToolForjisilu();

		//String str = "{page:1,rows:[{id:502021,cell:{type_cd:A\u57fa,fund_id:502021,fund_nm:\u56fd\u91d150A,ratio:5,profit_rt:5.102%,est_val:1.010,discount_rt:-1.98%,price:0.990,volume:7.85,increase_rt:0.20%,fund_nav:1.0100,nav_dt:2016-02-29}},{id:502022,cell:{type_cd:B\u57fa,fund_id:502022,fund_nm:\u56fd\u91d150B,ratio:5,profit_rt:7.367%,est_val:1.014,discount_rt:1.492%,price:1.029,volume:10.36,increase_rt:1.38%,fund_nav:0.9860,nav_dt:2016-02-29}},{id:502020,cell:{type_cd:\u6bcd\u57fa,fund_id:502020,fund_nm:\u56fd\u91d150 (\u4e0a\u8bc150),ratio: ,profit_rt: ,est_val:1.0119,discount_rt:-0.241%,fund_nav:0.9980,nav_dt:2016-02-29,price:1.0095 (\u5408\u5e76\u4ef7\u683c),volume: ,increase_rt:1.47% (\u6307\u6570\u6da8\u5e45)}}],total:3}";
		//String str ="{page:1,rows:[{id:502021,cell:{type_cd:A57fa,fund_id:502021,fund_nm:56fd91d150A,ratio:5,profit_rt:5.102%,est_val:1.010,discount_rt:-1.98%,price:0.990,volume:7.85,increase_rt:0.20%,fund_nav:1.0100,nav_dt:2016-02-29}},{id:502022,cell:{type_cd:B57fa,fund_id:502022,fund_nm:56fd91d150B,ratio:5,profit_rt:7.367%,est_val:1.014,discount_rt:1.492%,price:1.029,volume:10.36,increase_rt:1.38%,fund_nav:0.9860,nav_dt:2016-02-29}},{id:502020,cell:{type_cd:6bcd57fa,fund_id:502020,fund_nm:56fd91d150 (4e0a8bc150),ratio: ,profit_rt: ,est_val:1.0119,discount_rt:-0.241%,fund_nav:0.9980,nav_dt:2016-02-29,price:1.0095 (54085e764ef7683c),volume: ,increase_rt:1.47% (630765706da85e45)}}],total:3}" ;
		String str ="{\"page\":1,\"rows\":[{\"id\":\"502021\",\"cell\":{\"type_cd\":\"A\u57fa\",\"fund_id\":\"502021\",\"fund_nm\":\"\u56fd\u91d150A\",\"ratio\":\"5\",\"profit_rt\":\"5.107%\",\"est_val\":\"1.010\",\"discount_rt\":\"-2.08%\",\"price\":\"0.990\",\"volume\":\"7.85\",\"increase_rt\":\"0.20%\",\"fund_nav\":\"1.0110\",\"nav_dt\":\"2016-03-01\"}},{\"id\":\"502022\",\"cell\":{\"type_cd\":\"B\u57fa\",\"fund_id\":\"502022\",\"fund_nm\":\"\u56fd\u91d150B\",\"ratio\":\"5\",\"profit_rt\":\"7.342%\",\"est_val\":\"1.014\",\"discount_rt\":\"1.081%\",\"price\":\"1.029\",\"volume\":\"10.36\",\"increase_rt\":\"1.38%\",\"fund_nav\":\"1.0180\",\"nav_dt\":\"2016-03-01\"}},{\"id\":\"502020\",\"cell\":{\"type_cd\":\"\u6bcd\u57fa\",\"fund_id\":\"502020\",\"fund_nm\":\"\u56fd\u91d150 (\u4e0a\u8bc150)\",\"ratio\":\" \",\"profit_rt\":\" \",\"est_val\":\"1.0119\",\"discount_rt\":\"-0.444%\",\"fund_nav\":\"1.0140\",\"nav_dt\":\"2016-03-01\",\"price\":\"1.0095 (\u5408\u5e76\u4ef7\u683c)\",\"volume\":\" \",\"increase_rt\":\"1.47% (\u6307\u6570\u6da8\u5e45)\"}}],\"total\":3}";

		// String str
		// ="{page:1,rows:[{id:502021,cell:{type_cd:Au57fa,fund_id:502021,fund_nm:u56fdu91d150A,ratio:5,profit_rt:5.102%,est_val:1.010,discount_rt:-1.98%,price:0.990,volume:7.85,increase_rt:0.20%,fund_nav:1.0100,nav_dt:2016-02-29}},{id:502022,cell:{type_cd:Bu57fa,fund_id:502022,fund_nm:u56fdu91d150B,ratio:5,profit_rt:7.367%,est_val:1.014,discount_rt:1.492%,price:1.029,volume:10.36,increase_rt:1.38%,fund_nav:0.9860,nav_dt:2016-02-29}},{id:502020,cell:{type_cd:u6bcdu57fa,fund_id:502020,fund_nm:u56fdu91d150 (u4e0au8bc150),ratio: ,profit_rt: ,est_val:1.0119,discount_rt:-0.241%,fund_nav:0.9980,nav_dt:2016-02-29,price:1.0095 (u5408u5e76u4ef7u683c),volume: ,increase_rt:1.47% (u6307u6570u6da8u5e45)}}],total:3} ";
	
		JslRoot root  = json.formatJson(str.replaceAll("\"", "\""));
		System.out.println(root.getRows().get(0).getId()+"|"+root.getRows().get(0).getCell().getPrice());
	}
}
