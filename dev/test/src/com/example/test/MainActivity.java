package com.example.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;


public class MainActivity extends Activity {
	private DownloadManager downloadManager;  
    private SharedPreferences prefs;  
    private static final String DL_ID = "downloadId";  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);  
        prefs = PreferenceManager.getDefaultSharedPreferences(this); 
        
        Button btn = (Button)this.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doDown();
			}
		});
    }
    
    @Override  
    protected void onPause() {  
        // TODO Auto-generated method stub  
        super.onPause();  
        unregisterReceiver(receiver);  
    } 
    
    private void doDown(){
    	

        String serviceString = Context.DOWNLOAD_SERVICE; 
        DownloadManager downloadManager; 
        downloadManager = (DownloadManager)getSystemService(serviceString); 
         
        Uri uri = Uri.parse("http://220.178.7.169:8080/bcloud/app/MobileAppServlet?op=fileTest&id=69"); 
        DownloadManager.Request request = new Request(uri); 
        request.setDestinationInExternalPublicDir("/download/fileCloud", "G3.pdf");  
        request.setTitle("fileCloud");   
        long reference = downloadManager.enqueue(request);  
        
//    	if(!prefs.contains(DL_ID)) {   
//            String url = "http://220.178.7.169:8080/bcloud/app/MobileAppServlet?op=fileTest&id=243";  
//            //开始下载   
//            Uri resource = Uri.parse(encodeGB(url));   
//            DownloadManager.Request request = new DownloadManager.Request(resource);   
//            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);   
//            request.setAllowedOverRoaming(false);   
//            //设置文件类型  
//            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
//            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));  
//            request.setMimeType(mimeString);  
//            //在通知栏中显示   
//            request.setShowRunningNotification(true);  
//            request.setVisibleInDownloadsUi(true);  
//            //sdcard的目录下的download文件夹  
//            request.setDestinationInExternalPublicDir("/download/", "G3.txt");  
//            request.setTitle("移动G3广告");   
//            long id = downloadManager.enqueue(request);   
//            //保存id   
//            prefs.edit().putLong(DL_ID, id).commit();   
//        } else {   
//            //下载已经开始，检查状态  
//            queryDownloadStatus();   
//        }   
//  
//        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));  
    
    }
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {   
        @Override   
        public void onReceive(Context context, Intent intent) {   
            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听  
            Log.v("Goower", ""+intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));  
            queryDownloadStatus();   
        }   
    };   
    
    
    private void queryDownloadStatus() {   
        DownloadManager.Query query = new DownloadManager.Query();   
        query.setFilterById(prefs.getLong(DL_ID, 0));   
        Cursor c = downloadManager.query(query);   
        if(c.moveToFirst()) {   
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));   
            switch(status) {   
            case DownloadManager.STATUS_PAUSED:   
                Log.v("Goower", "STATUS_PAUSED");  
            case DownloadManager.STATUS_PENDING:   
                Log.v("Goower", "STATUS_PENDING");  
            case DownloadManager.STATUS_RUNNING:   
                //正在下载，不做任何事情  
                Log.v("Goower", "STATUS_RUNNING");  
                break;   
            case DownloadManager.STATUS_SUCCESSFUL:   
                //完成  
                Log.v("Goower", "下载完成");  
                break;   
            case DownloadManager.STATUS_FAILED:   
                //清除已下载的内容，重新下载  
                Log.v("Goower", "STATUS_FAILED");  
                downloadManager.remove(prefs.getLong(DL_ID, 0));   
                prefs.edit().clear().commit();   
                break;   
            }   
        }  
    }  


    /** 
     * 如果服务器不支持中文路径的情况下需要转换url的编码。 
     * @param string 
     * @return 
     */  
    public String encodeGB(String string)  
    {  
        //转换中文编码  
        String split[] = string.split("/");  
        for (int i = 1; i < split.length; i++) {  
            try {  
                split[i] = URLEncoder.encode(split[i], "GB2312");  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }  
            split[0] = split[0]+"/"+split[i];  
        }  
        split[0] = split[0].replaceAll("\\+", "%20");//处理空格  
        return split[0];  
    }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
