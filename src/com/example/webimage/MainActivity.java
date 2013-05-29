package com.example.webimage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.webimage.image.ImageCache;
import com.example.webimage.image.ImageWorker;
import com.example.webimage.util.ConnectivityUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView imgPic;
	private Button btn;
	private ProgressDialog pdDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgPic = (ImageView) findViewById(R.id.imageView1);
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ConnectivityUtil.hasconnected(MainActivity.this)) {
					getWebImage();
				} else {
					//
				}
			}
		});	
	}

	private void getWebImage() {
		String strUrl = "http://c.hiphotos.baidu.com/image/w%3D230/sign=cc0194d0f4246b607b0eb577dbf91a35/d1a20cf431adcbef0401b485aeaf2edda3cc9f78.jpg";
		new WebImageTask().execute(strUrl);
	}
	class WebImageTask extends AsyncTask<String, Void, Bitmap>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pdDialog = ProgressDialog.show(MainActivity.this, "���Ժ�", "���ڼ���...");
			super.onPreExecute();
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imgUrl=params[0];
			//1 �ȴ��ڴ滺���ж�ȡ
			Bitmap bm=ImageCache.getInstance().get(imgUrl);
			if(bm==null){
				//2 �ڴ滺�治���ڣ��ڴ���洢��ȡ��
				bm=new ImageWorker().getBitmapByFilePath(imgUrl, 
						imgPic.getWidth(), imgPic.getHeight());
				if(bm==null){
					//3 ��洢��Ҳ�����ڣ��������������
					bm = downloadImage(imgUrl);
				}//4  �ӱ��ض�ȡ��֮��Ҫ�ڷ��뵽�ڴ滺��
				ImageCache.getInstance().put(imgUrl, bm);
			} 
			return bm;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			pdDialog.dismiss();
			pdDialog= null;
			if (result != null) {
				imgPic.setImageBitmap(result);
			}
			super.onPostExecute(result);
		}
	}
	
	private  Bitmap downloadImage(String imgUrl){
		Bitmap bm=null;
		InputStream input=null;
		OutputStream out=null;
		
		try {
			URL url=new URL(imgUrl);
			HttpURLConnection conn=(HttpURLConnection)
					url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(15000);
			conn.setRequestProperty("Accept", "image/*");
			
			if(conn.getResponseCode()==200){
				input=conn.getInputStream();
				//ָ���ļ��������ļ������
				if(!ImageWorker.APP_DIR.exists()){
					ImageWorker.APP_DIR.mkdirs();
				}
				String fileName=String.valueOf(imgUrl.hashCode());
				File file=new File(ImageWorker.APP_DIR,fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				out=new FileOutputStream(file);
				//�������������е��ֽ�д����ļ������
				byte[] buffer=new byte[128];
				int len=0;
				while((len=input.read(buffer))!=-1){
					out.write(buffer, 0, len);
				}
				out.flush();
				bm=new ImageWorker().getBitmapByFilePath
						(imgUrl, imgPic.getWidth(), imgPic.getHeight());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			try {
				if(input != null){
				  input.close();
				} 
				if(out!=null){
					out.close();
				}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		return bm;	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
