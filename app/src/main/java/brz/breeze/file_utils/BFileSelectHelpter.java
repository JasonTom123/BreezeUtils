package brz.breeze.file_utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BFileSelectHelpter {

    public static final String TAG = "FileSelectHelpter";

	public static OnFileSelectedListener listener;

	private Context mContext;

	private int MODE;

	private String ENDS;

	public static int _MODE_FILE = 0;

	public static int _MODE_DIRECTORY = 1;

	private List<File> path_files = new ArrayList<>();

	private FileAdapter adapter;

	private File baseFile,parentFile;

	private AlertDialog alertDialog;

	private HashMap<File,Boolean> selected_files = new HashMap<>();

	public BFileSelectHelpter(Context context,int mode,String ends,File path) {
		this.mContext = context;
		this.ENDS = ends;
		this.MODE = mode;
		this.baseFile = path;
		alertDialog = new AlertDialog.Builder(mContext).create();
		ListView listview = initListView();
		adapter = new FileAdapter();
		listview.setAdapter(adapter);
		getFiles(baseFile);
		alertDialog.setTitle("选择文件");
		alertDialog.setView(listview);
		alertDialog.setCancelable(false);
		alertDialog.setButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					if (MODE == _MODE_FILE) {
						listener.onFileSelect(new ArrayList<File>(selected_files.keySet()));
					} else {
						listener.onDirectorySeleted(parentFile.getAbsolutePath());
					}
					dismissDialog();
				}
			});
		alertDialog.setButton2("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					dismissDialog();
				}
		});
		alertDialog.setButton3("返回上级", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					preventDismissDialog();
					if (!baseFile.getAbsolutePath().equals(parentFile.getAbsolutePath())) {
						adapter.back();
					}
				}
			});
		alertDialog.create();
		alertDialog.show();
	}

	public static void select(Context context, int mode, String ends,File path, OnFileSelectedListener onSelectListener) {
		if (ends == null) {
			ends = "";
		}
		if (mode == _MODE_DIRECTORY) {
			ends = "";
		}
		if(path == null){
			path = Environment.getExternalStorageDirectory();
		}
		BFileSelectHelpter helper=new BFileSelectHelpter(context,mode,ends,path);
		listener = onSelectListener;
	}
	
	public static interface OnFileSelectedListener {
		void onFileSelect(List<File> files);
		void onDirectorySeleted(String filepath);
	}

    private ListView initListView() {
		ListView listview = new ListView(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																	 LinearLayout.LayoutParams.MATCH_PARENT);
		listview.setDivider(new ColorDrawable(Color.WHITE));
		listview.setLayoutParams(lp);
		return listview;
	}

	private void getFiles(File path) {
		parentFile = path;
		if (path.isDirectory()) {
			File[] files = path.listFiles(new FileFilter(){
					@Override
					public boolean accept(File p1) {
						return !p1.isHidden();
					}
				});
			path_files = Arrays.asList(files);
			Collections.sort(path_files, new Comparator<File>(){
					@Override
					public int compare(File p1, File p2) {
						int i = 2;
						i = p1.compareTo(p2);
						if (p1.isDirectory()) {
							i--;
						}
						return i;
					}
				});
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 关闭对话框
	 */
	private void dismissDialog() {
		try {
			Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(alertDialog, true);
		} catch (Exception e) {
		}
		alertDialog.dismiss();
	}

	/**
	 * 通过反射 阻止关闭对话框
	 */
	private void preventDismissDialog() {
		try {
			Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			//设置mShowing值，欺骗android系统
			field.set(alertDialog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class FileAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return path_files == null ?0: path_files.size();
		}

		@Override
		public Object getItem(int p1) {
			return p1;
		}

		@Override
		public long getItemId(int p1) {
			return p1;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3) {
			final File file = path_files.get(p1);
			final ViewHolder viewHolder;
			if (p2 == null) {
				viewHolder = new ViewHolder();
				p2 = initItemView(viewHolder);
				p2.setTag(viewHolder);
			} else {
				viewHolder = (BFileSelectHelpter.FileAdapter.ViewHolder) p2.getTag();
			}
			if (file.isDirectory()) {
				viewHolder.fileName.setTextColor(Color.parseColor("#009688"));} else {
				viewHolder.fileName.setTextColor(Color.BLACK);
			}
			try {
				viewHolder.icon.setImageBitmap(getFileTypeIcon(mContext, viewHolder.icon, file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			viewHolder.fileName.setText(file.getName());
			if (selected_files.containsKey(file)) {
				viewHolder.checkView.setChecked(true);
			} else {
				viewHolder.checkView.setChecked(false);
			}
			viewHolder.baseView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						if (file.isDirectory()) {
							getFiles(file);
						} else {
							if (!ENDS.equals("") && file.getName().endsWith(ENDS)||ENDS.equals("")) {
								if (selected_files.containsKey(file)) {
									selected_files.remove(file);
									viewHolder.checkView.setChecked(false);
								} else {
									selected_files.put(file, true);
									viewHolder.checkView.setChecked(true);
								}
							}
						}
					}
				});
			if (file.isDirectory() || MODE == _MODE_DIRECTORY || (!ENDS.equals("") && !file.getName().endsWith(ENDS))) {
				viewHolder.checkView.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.checkView.setVisibility(View.VISIBLE);
			}
			return p2;
		}

		public void back() {
			clearSelected();
			getFiles(parentFile.getParentFile());
		}

		public void clearSelected() {
			selected_files.clear();
			notifyDataSetChanged();
		}

		private RelativeLayout initItemView(ViewHolder viewHolder) {
			viewHolder.baseView = new RelativeLayout(mContext);
			viewHolder.baseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 150));

			viewHolder.icon = new ImageView(mContext);
			viewHolder.icon.setLayoutParams(new LayoutParams(100, 100));
			viewHolder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
			LayoutParams icon_lp = (LayoutParams)viewHolder.icon.getLayoutParams();
			icon_lp.addRule(RelativeLayout.ALIGN_PARENT_START);
			icon_lp.addRule(RelativeLayout.CENTER_VERTICAL);
			icon_lp.setMarginStart(20);

			viewHolder.fileName = new TextView(mContext);
			viewHolder.fileName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			viewHolder.fileName.setMaxLines(2);
			LayoutParams tv_lp = (LayoutParams)viewHolder.fileName.getLayoutParams();
			tv_lp.addRule(RelativeLayout.ALIGN_PARENT_START);
			tv_lp.addRule(RelativeLayout.CENTER_VERTICAL);
			tv_lp.setMarginEnd(40);
			tv_lp.setMarginStart(140);

			viewHolder.checkView = new CheckBox(mContext);
			viewHolder.checkView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			LayoutParams ck_lp = (LayoutParams)viewHolder.checkView.getLayoutParams();
			ck_lp.addRule(RelativeLayout.ALIGN_PARENT_END);
			ck_lp.addRule(RelativeLayout.CENTER_VERTICAL);
			ck_lp.setMarginEnd(20);

			viewHolder.baseView.addView(viewHolder.icon);
			viewHolder.baseView.addView(viewHolder.fileName);
			viewHolder.baseView.addView(viewHolder.checkView);
			return viewHolder.baseView;
		}

		class ViewHolder {
			RelativeLayout baseView;
			TextView fileName;
			CheckBox checkView;
			ImageView icon;
		}
	}

	public Bitmap getFileTypeIcon(Context mContext, ImageView imageView, File file) throws IOException {
        AssetManager assetManager = mContext.getAssets();
        String filename = file.getName();
        if (file.isDirectory()) {
            return BitmapFactory.decodeStream(assetManager.open("file_type_icon/directory.png"));
        } else {
            if (filename.endsWith(".java") || filename.endsWith(".xml") || filename.endsWith(".json") || filename.endsWith(".conf") || filename.endsWith(".php")
				|| filename.endsWith(".txt") || filename.endsWith(".log") || filename.endsWith(".ini")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/txt.png"));
            } else if (filename.endsWith(".pdf")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/pdf.png"));
            } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/excel.png"));
            } else if (filename.endsWith(".doc") || filename.endsWith(".docx")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/word.png"));
            } else if (filename.endsWith(".ppt")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/ppt.png"));
            } else if (filename.endsWith(".exe") || filename.endsWith(".sh")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/program.png"));
            } else if (filename.endsWith(".apk")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/apk.png"));
            } else if (filename.endsWith(".zip") || filename.endsWith(".rar")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/zip.png"));
            } else if (filename.endsWith(".html")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/html.png"));
            } else if (filename.endsWith(".png") || filename.endsWith("jpg")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/img.png"));
            } else if (filename.endsWith(".mp3") || filename.endsWith(".wma")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/music.png"));
            } else if (filename.endsWith(".mp4") || filename.endsWith(".avi") || filename.endsWith(".rmvb") || filename.endsWith(".wmv")) {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/video.png"));
            } else {
                return BitmapFactory.decodeStream(assetManager.open("file_type_icon/file.png"));
            }
        }
    }

}
