package com.gxf.liveplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ServiceSettings extends AppCompatActivity {

    private EditText editText;
    private Button saveUrl;
    private Button reloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_settings);
        final SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String serviceUrl = userSettings.getString("serviceUrl","none");
        editText = (EditText) findViewById(R.id.serviceUrl);
        saveUrl = (Button) findViewById(R.id.saveServiceUrl);
        reloadUrl = (Button) findViewById(R.id.reloadServiceUrl);
        final Toast toast = Toast.makeText(this, "服务地址提交成功...", Toast.LENGTH_SHORT);
        final Toast toast2 = Toast.makeText(this, "服务地址恢复默认值成功...", Toast.LENGTH_SHORT);
        //设置默认地址
        if("none".equals(serviceUrl)){
            editText.setText(HttpUtils.apiPathOuter);
        }else{
            editText.setText(serviceUrl);
        }
        saveUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userSettings.edit();
                String newUrl = editText.getText().toString();
                editor.putString("serviceUrl",newUrl);
                HttpUtils.apiPath = newUrl;
                editor.commit();
                toast.show();
                ServiceSettings.this.finish();
            }
        });
        reloadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userSettings.edit();
                editText.setText(HttpUtils.apiPathOuter);
                editor.putString("serviceUrl",HttpUtils.apiPathOuter);
                HttpUtils.apiPath = HttpUtils.apiPathOuter;
                editor.commit();
                toast2.show();
                ServiceSettings.this.finish();
            }
        });
    }

}
