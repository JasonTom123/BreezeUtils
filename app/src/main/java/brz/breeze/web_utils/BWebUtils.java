package brz.breeze.web_utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;

public class BWebUtils {
    
    public static final String TAG = "BWebUtils";
	
    /**
	*@author BREEZE
	*@param picUri 图片直链
	*/
	public static Bitmap getWebPicture(String picUri) throws IOException{
		URL u = new URL(picUri);
		HttpURLConnection con = (HttpURLConnection)u.openConnection();
		con.setRequestMethod("GET");
		InputStream input = con.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		input.close();
		return bitmap;
	}
	
	/**
	*@author BREEZE
	*@param uri 网页链接
	*/
	public static String getWebContent(String uri) throws MalformedURLException, IOException{
		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		InputStream input = con.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len;
		while ((len = input.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		out.close();
		input.close();
		return new String(out.toByteArray(), "utf-8");
	}
	
	/**
	*@author BREEZE
	*@param uri 网页链接
	*@param post POST内容
	*/
	public static String postWebData(String uri,String post) throws MalformedURLException, IOException{
		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		OutputStream outputStream = con.getOutputStream();
		outputStream.write(post.getBytes());
		InputStream input = con.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len;
		while ((len = input.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		out.close();
		input.close();
		outputStream.close();
		return new String(out.toByteArray(), "utf-8");
	}
	
}
