package brz.breeze.app_utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.io.File;
import android.app.Application;
import android.graphics.Typeface;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.os.Looper;

public class BAppUtils {
    
    public static final String TAG = "BAppUtils";

	public static void setMApplication(Application mApplication) {
		BAppUtils.mApplication = mApplication;
	}

	public static Application getMApplication() {
		return mApplication;
	}
    
	/**
	*@author BREEZE
	*@param context 上下文，获取版本号
	*/
    public static int getVersionCode(Context context){
		try{
			PackageManager pm=context.getPackageManager();
			PackageInfo info=pm.getPackageInfo(context.getPackageName(),0);
			return info.versionCode;
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	*@author BREEZE
	*/
	public static String getAndroidVersion()
    {
        return android.os.Build.VERSION.RELEASE;
    }
	
	/**
	*@author BREEZE
	*@param Context 上下文
	*/
	public static String getAppVersionName(Context Context) {
        String strVersion = null;
        try {
            PackageInfo pi = null;
            pi = Context.getPackageManager().getPackageInfo(Context.getPackageName(), 0);
            if (pi != null) {
                strVersion = pi.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return strVersion;
    }
	
	/**
	*@author BREEZE
	*@param Context 上下文
	*/
	public static String getPackageName(Context Context) {
        return Context.getPackageName();
    }

	/**
	*@author BREEZE
	*/
    public static String PhoneName() {
        return android.os.Build.BRAND;
    }

	/**
	*@author BREEZE
	*/
    public static boolean getIsRoot() {
        boolean isRoot = false;
        try {
            isRoot = (new File("/system/bin/su").exists())
                || (new File("/system/xbin/su").exists());
        } catch (Exception e) {
            return false;
        }
        return isRoot;
	}
	
	/**
	*@author BREEZE
	*@description 获取中文字体
	*/
	public static Typeface getChineseTypeface(Context context){
		try{
			AssetManager am = context.getAssets();
			return Typeface.createFromAsset(am,"font/chi_1.ttf");
		}catch(Exception e){
			BToast.toast(context,e.toString(),1).show();
		}
		return Typeface.DEFAULT;
	}
	
	/**
	*@author BREEZE
	*@description 获取英文字体
	*/
	public static Typeface getEnglishTypeface(Context context){
		try{
			AssetManager am = context.getAssets();
			return Typeface.createFromAsset(am,"font/eng_1.ttf");
		}catch(Exception e){
			BToast.toast(context,e.toString(),1).show();
		}
		return Typeface.DEFAULT;
	}
	
	public static Application mApplication;
	
	/**
	*@author BREEZE
	*@description 从Assets中取出文件
	*/
	public static InputStream getResFromAssets(Context context,String path){
		try{
			AssetManager as = context.getAssets();
			return as.open(path);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	*@author BREEZE
	*@description 获取线程
	*/
	private static ExecutorService executorService;
	private static Handler uiHandler;
	public static void execute(Runnable runnable){
		if(executorService==null){
			executorService = Executors.newFixedThreadPool(5);
		}
		executorService.execute(runnable);
	}
	
	public static void uiExecute(Runnable runnable){
		if(uiHandler == null){
			uiHandler = new Handler(Looper.getMainLooper());
		}
		uiHandler.post(runnable);
	}
    
}
