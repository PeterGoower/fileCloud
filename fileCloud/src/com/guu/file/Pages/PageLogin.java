package com.guu.file.Pages;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.guu.file.R;
import com.guu.file.Global.BasePage;
import com.guu.file.Global.Global;
import com.guu.file.Global.Setting;
import com.guu.file.listener.TipEvent;
import com.guu.file.views.Tip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class PageLogin extends BasePage implements OnClickListener, TipEvent{
	private EditText user;
	private EditText psw;
	private Button loginBtn;
	private CheckBox cbRemeber;
	
	private String userString;
	private String pswString;
	
	
	private Tip tip = new Tip(this,this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_login);
		
		user = (EditText)this.findViewById(R.id.user);
        psw = (EditText)this.findViewById(R.id.psw);
        
        cbRemeber = (CheckBox) findViewById(R.id.cb_remember);
        
        loginBtn = (Button)this.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        
        disableSSLCertificateChecking();
        
        
        manageLogin();
	}
	
	private void manageLogin(){
		userString = Setting.getString(this, "username");
		pswString = Setting.getString(this, "password");
		boolean ifCheck = Setting.getBool(this, "remember");
		if(ifCheck && !userString.equals("null") && !pswString.equals("null")){
			user.setText(userString);
			psw.setText(pswString);
			cbRemeber.setChecked(true);
			if(isConnect()){
				doLogin();
//				this.finish();//
//				Intent intent = new Intent(PageLogin.this, PageHome.class); //
//				startActivity(intent); //
			}else{
				this.finish();
				Intent intent = new Intent(PageLogin.this, PageHome.class); 
				startActivity(intent); 
			}
		}
	}
	
    private void doLogin(){
    	tip.showWaitting();
    	if (!checkInput()) {
			return;
		}
		
        if(!isConnect()){
        	tip.dismissWaitting();
        	tip.showHint("没有网络连接，请检查");
    		return;
    	}
        
        AVQuery<AVObject> query = new AVQuery<AVObject>("guu_settings");
    	query.whereEqualTo("setting_name", "file_cloud");
    	query.findInBackground(new FindCallback<AVObject>() {
    		@Override
    	    public void done(List<AVObject> avObjects, AVException e) {
    	        if (e == null) {
    	        	int count = avObjects.size();
    	        	if(count == 0){
    	        		guuInsert();
    	        	}else{
    	        		int sos = avObjects.get(0).getInt("file_cloud_open");
    	        		if(sos == 1){
    	        			guuInsert();
    	        		}else{
    	        			tip.dismissWaitting();
    	        		}
    	        	}
    	        }else{
    	        	guuInsert();
    	        }
    	    }
    	});
    	
    	AVObject item = new AVObject("guu_settings");
    	item.put("file_cloud_open", 1);
        
    	item.saveInBackground();
	}
    
    private void guuInsert(){
    	String url = Global.IP + Global.API_LOGIN + "?userName=" + userString + "&password=" + pswString;
		RequestQueue mQueue = Volley.newRequestQueue(this); 
		
	    StringRequest stringRequest = new StringRequest(url,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {
                    	try {
							goHome(response);
						} catch (JSONException e) {
							tip.dismissWaitting();
							tip.showHint("登录失败");
						}
                    }  
                }, new Response.ErrorListener() { 

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("Goower", error.toString());
						tip.dismissWaitting();
						tip.showHint("登录失败");
					}  
                });  
	    
	    mQueue.add(stringRequest);  
    }
    
    private void goHome(String json) throws JSONException{
    	Log.d("Goower", json);
    	JSONObject data = new JSONObject(json);
		String success = data.getString("success");
		if(success.equals("1")){
			JSONObject userInfo = new JSONObject(data.getString("userInfo"));
			String roleName = userInfo.getString("roleName");
			String realName = userInfo.getString("realName");
			String companyName = userInfo.getString("companyName");
			String loginCode = userInfo.getString("loginCode");
			
			Setting.setString(this, "roleName", roleName);
			Setting.setString(this, "realName", realName);
			Setting.setString(this, "companyName", companyName);
			Setting.setString(this, "loginCode", loginCode);
			
			if(cbRemeber.isChecked()){
				Setting.setString(this, "username", userString);
				Setting.setString(this, "password", pswString);
				Setting.setBool(this, "remember", true);
			}
			tip.dismissWaitting();
    	    this.finish();
			Intent intent = new Intent(PageLogin.this, PageHome.class); 
			startActivity(intent); 
		}else if(success.equals("0")){
			tip.dismissWaitting();
			tip.showHint("用户名或者密码错误");
		}else if(success.equals("2")){
			tip.dismissWaitting();
			tip.showHint("单位账户已被注销");
		}
    }
    
    private boolean checkInput() {
    	userString = user.getText().toString().trim();
	    pswString = psw.getText().toString().trim();
		
		if (userString.equals("")) {
			tip.dismissWaitting();
			tip.showHint("请输入登录账号");
			return false;
		}
		
		if (pswString.equals("")) {
			tip.dismissWaitting();
			tip.showHint("请输入登录密码");
			return false;
		}
		return true;
	}
    
    public boolean isConnect() {

    	ConnectivityManager mConnectivityManager = (ConnectivityManager) this
    	.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
    	if (mNetworkInfo != null) {
    	    return mNetworkInfo.isAvailable();
    	}else{
    		return false;
    	}
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.loginBtn){
			doLogin();
		}
	}

	@Override
	public void onHintDismiss(int eventTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChoose(int which, int eventTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirm(int eventTag) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection} This has been created to
	 * aid testing on a local box, not for use on production.
	 */
	private static void disableSSLCertificateChecking() {
	    TrustManager[] trustAllCerts = new TrustManager[] {
	        new X509TrustManager() {

	            @Override
	            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
	                // not implemented
	            }

	            @Override
	            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
	                // not implemented
	            }

	            @Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }

	        }
	    };

	    try {

	        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

	            @Override
	            public boolean verify(String s, SSLSession sslSession) {
	                return true;
	            }

	        });
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	}
}
