package brz.breeze.file_utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import android.content.Context;
import android.os.Build;

public class BFileUtils {
    
    public static final String TAG = "BFileUtils";
    
	/**
	*@author BREEZE
	*@param filePath 文件路径
	*@param content 文件内容
	*/
    public static void writeFileContent(String filePath,String content) throws FileNotFoundException, IOException{
		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(content.getBytes());
		outputStream.close();
	}
	
	/**
	*@author BREEZE
	*@param filePath 文件路径
	*@param content 追加内容
	*/
	public static void addContentToFile(String filePath,String content) throws IOException{
		FileWriter writer = new FileWriter(filePath,true);
		writer.write(content);
		writer.close();
	}
	
	/**
	*@author BREEZE
	*@param filePath 文件路径
	*/
	public static String readFileContent(String filePath) throws FileNotFoundException, IOException{
		FileInputStream inputStream = new FileInputStream(filePath);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		inputStream.close();
		return new String(bytes);
	}
	
	/**
	*@author BREEZE
	*@param path 文件路径
	*/
	public static void createFile(String path) throws IOException{
		if(path.startsWith("/")){
			path = path.substring(1,path.length());
		}
		String[] dir_path = path.split("/");
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		for(int i = 0;i<dir_path.length;i++){
			if(i != dir_path.length-1){
				String child_path = dir_path[i] + "/";
				String child_dir_path = sb.toString() + child_path;
				System.out.println(child_dir_path);
				File file = new File(child_dir_path);
				if(!file.exists()){
					file.mkdir();
				}
				sb.append(child_path);
			}else{
				sb.append(dir_path[i]);
			}
		}
        File file = new File(sb.toString());
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	/**
	*@author BREEZE
	*@param old 旧文件路径
	*@param newFile 新文件路径
	*/
	public static void copyFile(final String old, final String newfile) throws FileNotFoundException, IOException {
		int bytesum = 0;   
		int byteread = 0;   
		File oldfile = new File(old);   
		if (oldfile.exists()) { //文件存在时   
			InputStream inStream = new FileInputStream(old); //读入原文件   
			FileOutputStream fs = new FileOutputStream(newfile);   
			byte[] buffer = new byte[1444];   
			while ((byteread = inStream.read(buffer)) != -1) {   
				bytesum += byteread; //字节数 文件大小   
				fs.write(buffer, 0, byteread);   
			}   
			inStream.close();   
		}   
    }
	
	/**
	*@author BREEZE
	*@param context 上下文
	*@param fileName 文件夹名称
	*/
	public static String getExternalCacheFile(Context context,String fileName){
		String file = context.getExternalCacheDir() + "/" + fileName + "/";
		File realFile = new File(file);
		if(!realFile.exists()){realFile.mkdir();}
		return file;
	}
	
	/**
	*@author BREEZE
	*@param context 上下文
	*@param fileName 文件名称
	*/
	public static String getExternalFile(Context context,String fileName){
		return context.getExternalFilesDir(fileName).getAbsolutePath();
	}
    
}
