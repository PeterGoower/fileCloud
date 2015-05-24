package com.guu.file.Pages;

import java.util.List;

import com.guu.file.R;
import com.guu.file.Global.BasePage;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PageHome extends BasePage implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_home);
	}
	
	
	@Override
	public void onClick(View v) {
	}
}
