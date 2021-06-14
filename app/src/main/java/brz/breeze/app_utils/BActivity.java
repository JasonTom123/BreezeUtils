package brz.breeze.app_utils;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import brz.breeze.service_utils.BExceptionCatcher;
import java.util.Map;

public abstract class BActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置错误日志抓取
		Thread.setDefaultUncaughtExceptionHandler(BExceptionCatcher.getInstance(this));
	}
	
	/**
	*@author BREEZE
	*@description 提示文本内容
	*/
	public void toast(final String message){
		runOnUiThread(new Runnable(){
			public void run(){
				BToast.toast(getApplicationContext(),message,BToast.LENGTH_SHORT).show();
			}
		});
	}
	
	public abstract void init();
	public abstract void initData();
	
	/**
	*@author BREEZE
	*@description 获取储存的数据
	*/
	public String getStringValue(String key,String def){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(key,def);
	}

	public int getIntValue(String key,int def){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(key,def);
	}

	public boolean getBooleanValue(String key,boolean def){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(key,def);
	}

	public float getFloatValue(String key,float def){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getFloat(key,def);
	}

	public Map<String,?> getValue(){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getAll();
	}

	public void setStringValue(String key,String def){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(key,def).apply();
	}

	public void setIntValue(String key,int def){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt(key,def).apply();
	}

	public void setBooleanValue(String key,boolean def){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(key,def).apply();
	}

	public void setFloatValue(String key,float def){
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putFloat(key,def).apply();
	}
	
	/**
	*@author BREEZE
	*@description 隐藏标题栏
	*/
	public void hideActionBar(){
		if(getActionBar()!=null){
			getActionBar().hide();
		}
	}
	
	/**
	 *@author BREEZE
	 *@description 获取VIEW
	 */
	public <T> T find(int id) {
		return findViewById(id);
	}
	
	public <T> T find(View baseView,int id){
		return baseView.findViewById(id);
	}
    
    /*
	*@author BREEZE
	*@date 2021-06-11 21:13:06
    */

    
}
