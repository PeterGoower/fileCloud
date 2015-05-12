package com.example.openfile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText et1;
    private String LOG_TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.editText1);
    }

    public void click(View v) {
        if(et1.getText().toString().trim()!=""){
            
            try {
                IntentBuilder.viewFile(this, et1.getText().toString().trim());
            } catch (ActivityNotFoundException e) {
                Log.e(LOG_TAG, "fail to view file: " + e.toString());
            }
      
        }else{
            Toast.makeText(getApplicationContext(), "请输入打开文件路径", 1).show();
        }
    }

}
