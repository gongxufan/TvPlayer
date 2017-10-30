/*
 * Copyright (C) 2016 hejunlin <hejunlin2013@gmail.com>
 * 
 * Github:https://github.com/hejunlin2013/LivePlayback
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
