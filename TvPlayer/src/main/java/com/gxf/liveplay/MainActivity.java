package com.gxf.liveplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

/**
 * splash
 * 启动登录检测
 */
public class MainActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 1000; // 两秒后进入系统
    private boolean isUpdateChecked = false;
    public void checkLogin(){
        SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
        final String userName = userSettings.getString("userName", "none");
        final String password = userSettings.getString("password", "none");
        final String lastVedioUrl = userSettings.getString("lastVedioUrl", "none");
        final String serviceUrl = userSettings.getString("serviceUrl", "none");
        if(!"none".equals(serviceUrl))
            HttpUtils.apiPath = serviceUrl;
        final Toast toast = Toast.makeText(MainActivity.this, "获取节目列表错误，系统初始化失败...", Toast.LENGTH_SHORT);
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Thread.currentThread().sleep(SPLASH_DISPLAY_LENGHT);
                    //跳转登录
                    if ("none".equals(userName) || !HttpUtils.login(userName, password,MainActivity.this) || LoginActivity.state != -1) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        PlayListCache.initPlayInfo(userName,password);
                        String url = lastVedioUrl;
                        if (url.equals("none"))
                            url = PlayListCache.playListMap.get(PlayListCache.playListMap.keySet().iterator().next());
                        LiveActivityRel.activityStart(MainActivity.this, url);
                    }
                    MainActivity.this.finish();
                } catch (Exception e) {
                    toast.show();
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();
    }
}
