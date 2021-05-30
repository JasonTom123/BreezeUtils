package brz.breeze.tool_utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BToolUtils {
    
    public static final String TAG = "BToolUtils";
    
	
    public static String md5(String content) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("md5");
		byte[] bytes = digest.digest(content.getBytes());
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<bytes.length;i++){
			int var = bytes[i] & 0xff;
			if(var < 16){
				sb.append("0");
			}
			sb.append(Integer.toHexString(var));
		}
		return sb.toString();
	}
	
	/**
	*@author BREEZE
	*@param format 类型
	*/
	public static String getTime(String format){
		return new SimpleDateFormat(format).format(System.currentTimeMillis());
	}
	
	private static String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	public static String getDayInWeek(){
		Calendar c=Calendar. getInstance();
		int week_day=c. get(Calendar. DAY_OF_WEEK)-1;
		if (week_day<0) {
			week_day=0;
		}
		return weeks[week_day];
	}
    
}
