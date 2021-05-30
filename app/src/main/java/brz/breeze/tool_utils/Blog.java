package brz.breeze.tool_utils;
import android.util.Log;
import brz.breeze.file_utils.BFileUtils;
import android.content.Context;
import java.io.IOException;

public class Blog {
	
	public static final int VERBOSE = 1;
	
	public static final int DEBUG = 2;
	
	public static final int INFO = 3;
	
	public static final int WARN = 4;
	
	public static final int ERROR = 5;
	
	public static final int NOTHING = 6;
	
	public static int level = VERBOSE;
	
	private static Context context;
	
	public Blog(Context context){
		this.context = context;
	}
    
    public static void v(String tag,String msg){
		if(level <= VERBOSE){
			write(tag,"V",msg);
			Log.v(tag,msg);
		}
	}
	
	public static void d(String tag,String msg){
		if(level <= DEBUG){
			write(tag,"D",msg);
			Log.v(tag,msg);
		}
	}
	
	public static void i(String tag,String msg){
		if(level <= INFO){
			write(tag,"I",msg);
			Log.v(tag,msg);
		}
	}
	
	public static void w(String tag,String msg){
		if(level <= WARN){
			write(tag,"W",msg);
			Log.v(tag,msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(level <= ERROR){
			write(tag,"E",msg);
			Log.v(tag,msg);
		}
	}
	
	public static void write(String tag,String level,String msg){
		StringBuilder sb = new StringBuilder();
		sb.append(level).append("/").append(tag).append(BToolUtils.getTime("[yyyy-MM-dd HH:mm:ss]"))
			.append("	").append(msg).append("\n");
		String filePath = BFileUtils.getExternalCacheFile(context,"logcat") + BToolUtils.getTime("yyyy-MM-dd") + ".txt";
		try {
			BFileUtils.createFile(filePath);
			BFileUtils.addContentToFile(filePath,sb.toString());
		} catch (IOException e) {}
	}
    
}
