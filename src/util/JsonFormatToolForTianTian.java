package util;

//用于获取天天财经网母基金的值

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.util.JSON;

import model.HexunPacket;
import model.TianTianJsRoot;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;



//data  frame
//refreshData('150221','2','Q',['20160301144203','1.185','1.170','1.174','1.161','1.173','1.172','16075432','1.171','2265000','1.170','15000','1.169','1030000','1.168','1679011','1.173','77723','1.174','780078','1.175','1622000','1.176','1055000','1.178','1030000','137112797','160072350','21500','0.00','0.00','0.0000','75543011','60749186']);refreshData('150221','2','MI',[['1.169','2262844','2647433'],['1.172','2128013','2489977']]);
/* 该类提供格式化JSON字符串的方法。
* 该类的方法formatJson将JSON字符串格式化，方便查看JSON数据。
* <p>例如：
* <p>JSON字符串：["yht","xzj","zwy"]
* <p>格式化为：
* <p>[
* <p>     "yht",
* <p>     "xzj",
* <p>     "zwy"
* <p>]
* 
* <p>使用算法如下：
* <p>对输入字符串，追个字符的遍历
* <p>1、获取当前字符。
* <p>2、如果当前字符是前方括号、前花括号做如下处理：
* <p>（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
* <p>（2）打印：当前字符。
* <p>（3）前方括号、前花括号，的后面必须换行。打印：换行。
* <p>（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
* <p>（5）进行下一次循环。
* <p>3、如果当前字符是后方括号、后花括号做如下处理：
* <p>（1）后方括号、后花括号，的前面必须换行。打印：换行。
* <p>（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
* <p>（3）打印：当前字符。
* <p>（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
* <p>（5）继续下一次循环。
* <p>4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
* <p>5、打印：当前字符。
* 
* @author  yanghaitao
* @version  [版本号, 2014年9月29日]
*/
public class JsonFormatToolForTianTian
{
	
   
   /**
    * 返回格式化JSON字符串。
    * 
    * @param json 未格式化的JSON字符串。
    * @return 格式化的JSON字符串。
    */
   public static TianTianJsRoot formatJson(String html)
   {
	   TianTianJsRoot root  =new TianTianJsRoot();
	   
	   String temp1 = html.replace("jsonpgz(", "");
	   String temp2 =temp1.replace(");", "");
			try {
				
				JSONObject obj = JSONObject.fromObject(temp2);  
				root.setFundcode(obj.getString("fundcode"));
				root.setGsz(obj.getString("gsz"));
				root.setGztime(obj.getString("gztime"));;
				root.setDwjz(obj.getString("dwjz"));

			
			} catch (JSONException ex) {
				// 异常处理代码
				System.out.print(ex.toString());
			}
			return root;
	 
   }
  
   public static void main(String[] args)
   {
	   JsonFormatToolForTianTian tool = new JsonFormatToolForTianTian();
       
       /*int age = 23;
       
       Name name = new Name();
       name.setFirstName("zhang");
       name.setLastName("san");
       
       List<String> aihao = new ArrayList<String>();
       aihao.add("pashan");
       aihao.add("movies");
       
       Person person = new Person();
       person.setName(name);
       person.setAge(age);
       person.setAihao(aihao);*/
     String buf = "refreshData('150221','2','Q',['20160301144203','1.185','1.170','1.174','1.161','1.173','1.172','16075432','1.171','2265000','1.170','15000','1.169','1030000','1.168','1679011','1.173','77723','1.174','780078','1.175','1622000','1.176','1055000','1.178','1030000','137112797','160072350','21500','0.00','0.00','0.0000','75543011','60749186']);refreshData('150221','2','MI',[['1.169','2262844','2647433'],['1.172','2128013','2489977']]);";
       HexunPacket packet = new HexunPacket();
       
       //JSONObject jo = JSONObject.fromObject(packet);
       //System.out.println(jo.toString());
       System.out.println(tool.formatJson(buf));
   }
}