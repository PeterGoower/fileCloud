package com.guu.file.Pages;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guu.file.R;
import com.guu.file.Global.BasePage;
import com.guu.file.Global.Global;
import com.guu.file.Global.Menu;
import com.guu.file.Global.Setting;
import com.guu.file.adapter.GridViewAdapter;
import com.guu.file.listener.TipEvent;
import com.guu.file.views.CircularBannerView;
import com.guu.file.views.CircularBannerView.OnBannerClickListener;
import com.guu.file.views.Tip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.GridView;

public class PageIndex extends BasePage implements OnClickListener, TipEvent, OnBannerClickListener, OnItemClickListener{
	private Tip tip = new Tip(this,this);
	
	private GridView gridView;
	private GridViewAdapter mAdapter;
	private List<Menu> menus = new ArrayList<Menu>(); 
	private CircularBannerView c;
	
	private EditText key;
	private String keyString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_index);
		
		getData();
		
		c = (CircularBannerView) findViewById(R.id.circularbanner);
		
		
		initGridData();
		gridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new GridViewAdapter(this, menus);
        gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
		
		key = (EditText) findViewById(R.id.key);
		key.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.d("Goower", "222");
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					Log.d("Goower", "333");
					search();
				}
				return true;
			}
		});
	}
	
	private void search(){
		keyString = key.getText().toString().trim();
		Log.d("Goower", keyString);
		if(keyString.length() < 1){
			tip.showHint("请输入搜索关键字");
			return;
		}
	}
	
	private void getData(){
		tip.showWaitting();
		String loginCode = Setting.getString(this, "loginCode");
		String username = Setting.getString(this, "username");
		String url = Global.IP + Global.API_BANNER + "?userName=" + username + "&loginCode=" + loginCode;
		Log.d("Goower", url);
		RequestQueue mQueue = Volley.newRequestQueue(this); 
		
	    StringRequest stringRequest = new StringRequest(url,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {
                    	tip.dismissWaitting();
                    	try {
							manaData(response);
						} catch (JSONException e) {
							e.printStackTrace();
						}
                    	Log.d("Goower", "onResponse"+response);
                    }  
                }, new Response.ErrorListener() { 
					@Override
					public void onErrorResponse(VolleyError error) {
						tip.dismissWaitting();
					}  
                });  
	    
	    mQueue.add(stringRequest);  
	}
	
	private void manaData(String response) throws JSONException{
		JSONObject data = new JSONObject(response);
		String loginValid = data.getString("loginValid");
		if(loginValid.equals("1")){
			JSONArray dataList = new JSONArray(data.getString("imageList"));
			int count = dataList.length();
			String[] url = new String[count];
	        String[] desc = new String[count];
	        for(int i= 0; i<count; i++){
	        	url[i] = manaImgUrl(dataList.getString(i));
	        	desc[i] = "";
	        }
			c.setImageResouce(url, desc, this );
		}
	}
	
	private String manaImgUrl(String url){
		String ret  = url;
		if(!url.contains("http")){
			ret = "https://220.178.7.169/bcloud/broadimage/" + ret;
		}
		
		return ret;
	}
	
	private void initGridData(){
		Menu m1 = new Menu();
		m1.icon = R.drawable.jieyue;
		m1.menuname = "我的借阅";
		m1.page = PageAbout.class;//
		menus.add(m1);
		
		Menu m2 = new Menu();
		m2.icon = R.drawable.shenpi;
		m2.menuname = "借阅审批";
		m2.page = PageAbout.class;//
		menus.add(m2);
		
		Menu m3 = new Menu();
		m3.icon = R.drawable.shoucang;
		m3.menuname = "我的收藏";
		m3.page = PageAbout.class;//
		menus.add(m3);
		
		Menu m4 = new Menu();
		m4.icon = R.drawable.lishi;
		m4.menuname = "历史查看";
		m4.page = PageAbout.class;//
		menus.add(m4);
		
		Menu m5 = new Menu();
		m5.icon = R.drawable.lixian;
		m5.menuname = "离线缓存";
		m5.page = PageAbout.class;//
		menus.add(m5);
		
		Menu m6 = new Menu();
		m6.icon = R.drawable.zixun;
		m6.menuname = "业务咨询";
		m6.page = PageAbout.class;//
		menus.add(m6);
	}
	
	@Override
	public void onClick(View v) {
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

	@Override
	public void onclick(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(PageIndex.this, menus.get(position).page);
		startActivity(intent);
	}
}
