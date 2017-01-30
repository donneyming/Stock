package util;

public class OperSystemUtil {
	
	public static String getFilePath()
	{
    String os = System.getProperty("os.name");  
    if(os.toLowerCase().startsWith("win")){  
      return SysConstant.FilePathForWin;  
    }  
    
    else if(os.toLowerCase().startsWith("mac"))
    {
    	 return SysConstant.FilePathForMac;  
    }
    
    else
    	 return SysConstant.FilePathForMac;  
	}
}
