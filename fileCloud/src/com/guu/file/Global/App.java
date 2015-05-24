package com.guu.file.Global;

import com.avos.avoscloud.AVOSCloud;

import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AVOSCloud.useAVCloudCN();

		AVOSCloud.initialize(this,
				"nbqr09szdfb4azq0fpvbbonzw9kgkn4ao534dorzvqzbtyj6",
				"opu1o724e2t5hi917okrji5p4dpsshm5pbxb6vtktwlywvza");
	}
}
