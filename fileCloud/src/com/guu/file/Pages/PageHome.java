package com.guu.file.Pages;

import com.guu.file.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageHome extends ActivityGroup implements OnClickListener{
	@SuppressWarnings("unused")
	private LinearLayout bodyView;
	private LinearLayout one, two, three, four;
	private TextView txt1, txt2, txt3, txt4;
	private ImageView img1, img2, img3, img4;
	private int flag = 0; // 通过标记跳转不同的页面，显示不同的菜单项
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.page_home);
		
		txt1 = (TextView)this.findViewById(R.id.txt1);
		txt2 = (TextView)this.findViewById(R.id.txt2);
		txt3 = (TextView)this.findViewById(R.id.txt3);
		txt4 = (TextView)this.findViewById(R.id.txt4);
		
		img1 = (ImageView)this.findViewById(R.id.img1);
		img2 = (ImageView)this.findViewById(R.id.img2);
		img3 = (ImageView)this.findViewById(R.id.img3);
		img4 = (ImageView)this.findViewById(R.id.img4);
		
		initMainView();
		// 显示标记页面
		showView(flag);
		one.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 0;
				showView(flag);
				}
		});
		two.setOnClickListener(new OnClickListener() {					
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 1;
				showView(flag);
			}
		});
		three.setOnClickListener(new OnClickListener() {				
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 2;
				showView(flag);
			}
		});
		four.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
				// TODO Auto-generated method stub
					flag = 3;
					showView(flag);
			}
		});
	}
	/*
	 * 初始化主界面
	 */
    public void initMainView() {
		bodyView=(LinearLayout) findViewById(R.id.body);
		one=(LinearLayout) findViewById(R.id.one);
		two=(LinearLayout) findViewById(R.id.two);
		three=(LinearLayout) findViewById(R.id.three);
		four=(LinearLayout) findViewById(R.id.four);
	}
    
    private void nullView(){
    	txt1.setTextColor(this.getResources().getColor(R.color.gray));
		txt2.setTextColor(this.getResources().getColor(R.color.gray));
		txt3.setTextColor(this.getResources().getColor(R.color.gray));
		txt4.setTextColor(this.getResources().getColor(R.color.gray));
		
		img1.setBackgroundResource(R.drawable.h2);
		img2.setBackgroundResource(R.drawable.s2);
		img3.setBackgroundResource(R.drawable.d2);
		img4.setBackgroundResource(R.drawable.m2);
    }
    
   // 在主界面中显示其他界面
	public void showView(int flag) {
		switch (flag) {
		case 0:
			bodyView.removeAllViews();
			View v = getLocalActivityManager().startActivity("one",
					new Intent(PageHome.this, PageIndex.class)).getDecorView();
                    nullView();
                    txt1.setTextColor(this.getResources().getColor(R.color.blue));
            		img1.setBackgroundResource(R.drawable.h1);
			
		
			bodyView.addView(v);
			break;
		case 1:
			bodyView.removeAllViews();
			bodyView.addView(getLocalActivityManager().startActivity("two",
					new Intent(PageHome.this, PageSearch.class))
					.getDecorView());
			 nullView();
			 txt2.setTextColor(this.getResources().getColor(R.color.blue));
     		 img2.setBackgroundResource(R.drawable.s1);
			break;
		case 2:			
			bodyView.removeAllViews();
			bodyView.addView(getLocalActivityManager().startActivity(
					"three", new Intent(PageHome.this, PageAbout.class))
					.getDecorView());
			 nullView();
			 txt3.setTextColor(this.getResources().getColor(R.color.blue));
     		img3.setBackgroundResource(R.drawable.d1);
			break;
		case 3:			
			bodyView.removeAllViews();
			bodyView.addView(getLocalActivityManager().startActivity(
					"four", new Intent(PageHome.this, PageMy.class))
					.getDecorView());
			 nullView();
			 txt4.setTextColor(this.getResources().getColor(R.color.blue));
     		img4.setBackgroundResource(R.drawable.m1);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}
}
