package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.HexunPacket;

import org.json.JSONObject;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class JsonFormatToolForHexun
{
	public List<HexunPacket> listPacket;
	public JsonFormatToolForHexun()
	{
		 listPacket = new ArrayList<HexunPacket>();
	}
	private String ReadFromPacket(String buf)
	{
		String list[] = buf.split(";");
		int start = list[1].indexOf("[[");
		int end = list[1].indexOf("]]");
		String temp = list[1].substring(start+1, end+1);
		
		return temp;

	} 
	private HexunPacket StringtoPacket(String buf)
	{
		HexunPacket packet = new HexunPacket();
		String str1= HtmlUtil.delHTMLTag2(buf.replace('[', ' ').replace(']',' ').replace('\'', ' '));
		String list[] = str1.split(",");
		packet.price =list[0];
		packet.num1 =list[1];
		packet.num2 = list[2];
		return packet;

	} 
   private static final String HexunPacket = null;
/**
    * 单位缩进字符串。
    */
   private static String SPACE = "   ";
   
   /**
    * 返回格式化JSON字符串。
    * 
    * @param json 未格式化的JSON字符串。
    * @return 格式化的JSON字符串。
    */
   public HexunPacket formatJson(String html)
   {
	   
	   String json = ReadFromPacket(html);
       StringBuffer result = new StringBuffer();
       
       int length = json.length();
       int number = 0;
       char key = 0;

       //遍历输入字符串。
       for (int i = 0; i < length; i++)
       {
           //1、获取当前字符。
           key = json.charAt(i);
           
           //2、如果当前字符是前方括号、前花括号做如下处理：
           if((key == '[') || (key == '{') )
           {
               //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
               if((i - 1 > 0) && (json.charAt(i - 1) == ':'))
               {
                   result.append('\n');
                   result.append(indent(number));
               }
               //（2）打印：当前字符。
               result.append(key);
               //（3）前方括号、前花括号，的后面必须换行。打印：换行。
               result.append('\n');
               
               //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
               number++;
               result.append(indent(number));
               
               //（5）进行下一次循环。
               continue;
           }
           
           //3、如果当前字符是后方括号、后花括号做如下处理：
           if((key == ']') || (key == '}') )
           {
               //（1）后方括号、后花括号，的前面必须换行。打印：换行。
               result.append('\n');
               
               //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
               number--;
               result.append(indent(number));
               
               //（3）打印：当前字符。
               result.append(key);
               
               //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
               if(((i + 1) < length) && (json.charAt(i + 1) != ','))
               {
                   result.append('\n');
               }
               
               //将数据转换成数据结构；
               listPacket.add(StringtoPacket(result.toString()));
               //（5）继续下一次循环。
               continue;
           }
           
           //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
           if((key == ','))
           {
               result.append(key);
               result.append('\n');
               result.append(indent(number));
               continue;
           }
           
           //5、打印：当前字符。
           result.append(key);
       }
       if(listPacket.size() >0)
       return listPacket.get(listPacket.size()-1);
       else
    	   return null;
      // return result.toString();
   }
   
   /**
    * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
    * 
    * @param number 缩进次数。
    * @return 指定缩进次数的字符串。
    */
   private String indent(int number)
   {
       StringBuffer result = new StringBuffer();
       for(int i = 0; i < number; i++)
       {
           result.append(SPACE);
       }
       return result.toString();
   }
   
   public static void main(String[] args)
   {
	   JsonFormatToolForHexun tool = new JsonFormatToolForHexun();
       
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