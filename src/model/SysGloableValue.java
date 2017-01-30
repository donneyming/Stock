package model;

import impl.*;

import java.util.ArrayList;
import java.util.List;

import controll.CreateDownDataFromNet;
import controll.MyTask;
import controll.SysTaskManeger;

import util.Configure;
import util.OperSystemUtil;
import util.ReadFile;
import util.StockFund;
import util.SysConstant;

public class SysGloableValue {

	// 将类封装为单例模式
	private static volatile SysGloableValue instance= null;
	/* 链表 */
	public List<StockFund> fundList;

	/* 文件夹路径 */
	public String filePath;

	/* 采集时间间隔 */
	public int timeTap;

	/* 采集方式 */
	private int netCollect;

	/* 配置采集端口 */
	private CreateDownDataFromNet downDataWay;

	
	public CreateDownDataFromNet getDownDataWay() {
		return downDataWay;
	}

	public List<SysTaskManeger> taskList;
	
	public int getNetCollect() {
		return netCollect;
	}

	public void setNetCollect(int netCollect) {
		this.netCollect = netCollect;

		if (netCollect == SysConstant.SinaNet) {
			downDataWay = new CreateDataFromSina();
		} else if (netCollect == SysConstant.JiSiLuNet) {
			downDataWay = new CreateDataFromJiSiLu();
		} else if (netCollect == SysConstant.DongFangCaifuNet) {
			downDataWay = new CreateDataFromDongFangCaiFu();
		} else if (netCollect == SysConstant.HeXunNet) {
			downDataWay = new CreateDataFromHeXun();
		} else {
			downDataWay = new CreateDataFromSina();
		}
	}

	// 构造函数，用于初始化配置需要的属性
	private SysGloableValue() {
		filePath = OperSystemUtil.getFilePath();
		timeTap = SysConstant.TimeTap;
		netCollect = SysConstant.SinaNet;
		fundList = new ArrayList();
		downDataWay = new CreateDataFromSina();
		taskList = new ArrayList();
		
		//初始化基金列表
		initFundList();
	}
	/* 初始化链表 */
	private void initFundList() {
		String readFilePath = Configure.getStockFundListFilePath();
		ReadFile.readTxtFile(readFilePath, fundList);
	} 
	private static class LazyHolder {  
	       private static final SysGloableValue INSTANCE = new SysGloableValue();  
	    }  
	   
	public static SysGloableValue getGloableValue() {
		 if (instance == null) {  
             synchronized (SysGloableValue.class) {  
                if (instance == null) {  
                	instance = new SysGloableValue(); 
                }  
             }  
           } 
           return instance;
	}
	
}
