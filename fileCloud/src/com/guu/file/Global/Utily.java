package com.guu.file.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 封装�?些常用功�?
 */

public class Utily {
	
	/**
     * 隐藏输入�?
     */
    public static void hideInputMethod(Activity a){
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            View focus = a.getCurrentFocus();
            if(focus != null){
                IBinder binder = focus.getWindowToken();
                if(binder != null){
                    imm.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }
    
    /**
     * �?出应用程�?
     */
    public static void quitApplication(Activity a){
    	a.finish();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    /**
     * 显示Toast提示
     */
    public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
    
    /**
     * 显示Toast提示，使用资源字符串
     */
    public static void showToast(Context context, int res) {
		Toast.makeText(context, context.getResources().getString(res), Toast.LENGTH_SHORT).show();
	}
    
    /**
	 * 获取屏幕宽度
	 * @param a 传入Activity
	 * @return
	 */
	public static int getScreenW(Activity a) {
		Display display = a.getWindowManager().getDefaultDisplay();

		int w = display.getWidth();
		int h = display.getHeight();

		return w;
	}

	/**
	 * 获取屏幕高度
	 * @param a 传入Activity
	 * @return
	 */
	public static int getScreenH(Activity a) {
		Display display = a.getWindowManager().getDefaultDisplay();

		int w = display.getWidth();
		int h = display.getHeight();

		return h;
	}
	
	/**
	 * BASE64解码
	 * @param encodedText base64�?
	 * @return
	 */
	public static String decodeBase64(String encodedText)
    {
		String base64chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        encodedText = encodedText.replaceAll("[^" + base64chars + "=]", "");

        String p = (encodedText.charAt(encodedText.length() - 1) == '=' ? (encodedText.charAt(encodedText.length() - 2) == '=' ? "AA" : "A") : "");
        String r = "";
        encodedText = encodedText.substring(0, encodedText.length() - p.length()) + p;

        for (int c = 0; c < encodedText.length(); c += 4){
            int n = (base64chars.indexOf(encodedText.charAt(c)) << 18) + (base64chars.indexOf(encodedText.charAt(c + 1)) << 12)
                + (base64chars.indexOf(encodedText.charAt(c + 2)) << 6) + base64chars.indexOf(encodedText.charAt(c + 3));

            r += "" + (char) ((n >>> 16) & 0xFF) + (char) ((n >>> 8) & 0xFF) + (char) (n & 0xFF);
        }
        return r.substring(0, r.length() - p.length());
    }

	/**
	 * 绝对时间转为可显示在界面上的字符串，类似2012-11-12 12:30:05
	 */
	public static String getShowTime()
    {
		Date date = new Date(System.currentTimeMillis());
        
        String year = String.valueOf(date.getYear()+1900);
        
        int month = date.getMonth()+1;
        String monthString = String.valueOf(month);
        if(month < 10){
      	  monthString = "0" + month; 
        }
        
//        int day = date.getDate();
//        String dayString = String.valueOf(day);
//        if(day < 10){
//      	  dayString = "0" + day; 
//        }
//        
//        int hour = date.getHours();
//        String hourString = String.valueOf(hour);
//        if(hour < 10){
//      	  hourString = "0" + hour; 
//        }
//        
//        int min = date.getMinutes();
//        String minString = String.valueOf(min);
//        if(min < 10){
//      	  minString = "0" + min; 
//        }
//        
//        int sec = date.getSeconds();
//        String secString = String.valueOf(sec);
//        if(sec < 10){
//      	  secString = "0" + sec; 
//        }
        return year+"-"+monthString;
    }
	
	/** 
     * 根据手机的分辨率�? dp 的单�? 转成�? px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /** 
     * 判断存储卡里文件是否存在
     */  
    public static boolean fileIsExists(String path){
        File f = new File(path);
        if(!f.exists()){
            return false;
        }
        return true;
    }
    
    /** 
     * 获取存储卡路�?
     */  
    public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存�?
		  if (sdCardExist){
		       sdDir = Environment.getExternalStorageDirectory();//获取跟目�?
		  }else{
			  return null;
		  }
		  return sdDir.toString();   
   }
    
    /** 
     * 获取客户端版本号
     */  
    public static String getVersionName(Context con) throws Exception{
            // 获取packagemanager的实�?
            PackageManager packageManager = con.getPackageManager();
            // getPackageName()是你当前类的包名�?0代表是获取版本信�?
            PackageInfo packInfo = packageManager.getPackageInfo(con.getPackageName(),0);
            String version = packInfo.versionName;
            return version;
    }
    
    /** 
     * 获取当前运行应用名称
     */  
    public static String getCurrAppName(Context con){
    	ActivityManager am = (ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(10);
        String currApp = list.get(0).topActivity.getPackageName();
        Log.d("Goower", "curr app:"+currApp);
        return currApp;
    }
    
    /** 
     * 获取当前运行页面名称
     */  
    public static String getCurrPageName(Context con){
    	ActivityManager am = (ActivityManager)con.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(10);
        String currPage = list.get(0).topActivity.getClassName();
        Log.d("Goower", "curr page:"+currPage);
        return currPage;
    }
    
    /** 
     * html标签无法解析，可采用以下方法给全部去除掉
     */
    public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
    }
    
    /** 
     * 判断邮箱地址输入合法性
     */
    public static boolean isEmail(String email) {
    	String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    	Pattern p = Pattern.compile(str);
    	Matcher m = p.matcher(email);

    	return m.matches();
    }
    
    public static boolean isNumeric(String str){
  	  for (int i = 0; i < str.length(); i++){
  	   System.out.println(str.charAt(i));
  	   if (!Character.isDigit(str.charAt(i))){
  	    return false;
  	   }
  	  }
  	  return true;
  	}
}
