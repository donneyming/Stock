package model;

//用于查询天天财经网母基金的值
public class TianTianJsRoot {
private String fundcode;

private String name;

private String jzrq;

private String dwjz;

private String gsz;

private String gszzl;

private String gztime;

public void setFundcode(String fundcode){
this.fundcode = fundcode;
}
public String getFundcode(){
return this.fundcode;
}
public void setName(String name){
this.name = name;
}
public String getName(){
return this.name;
}
public void setJzrq(String jzrq){
this.jzrq = jzrq;
}
public String getJzrq(){
return this.jzrq;
}
public void setDwjz(String dwjz){
this.dwjz = dwjz;
}
public String getDwjz(){
return this.dwjz;
}
public void setGsz(String gsz){
this.gsz = gsz;
}
public String getGsz(){
return this.gsz;
}
public void setGszzl(String gszzl){
this.gszzl = gszzl;
}
public String getGszzl(){
return this.gszzl;
}
public void setGztime(String gztime){
this.gztime = gztime;
}
public String getGztime(){
return this.gztime;
}

}