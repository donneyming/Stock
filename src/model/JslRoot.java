package model;
import java.util.List;


public class JslRoot {
private int page;

private List<JslRows> rows ;

private int total;

public void setPage(int page){
this.page = page;
}
public int getPage(){
return this.page;
}
public void setRows(List<JslRows> rows){
this.rows = rows;
}
public List<JslRows> getRows(){
return this.rows;
}
public void setTotal(int total){
this.total = total;
}
public int getTotal(){
return this.total;
}

}