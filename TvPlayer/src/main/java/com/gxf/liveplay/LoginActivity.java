package com.gxf.liveplay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static UserLoginTask mAuthTask = null;
    public static int state;
    private EditText userName;
    private EditText password;
    private Button loginBtn;
    private TextView promptText;
    private ImageButton settings;
    private int choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ServiceSettings.class);
                LoginActivity.this.startActivity(intent);
               /* SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = userSettings.edit();
                //默认为公网服务器
                choice = userSettings.getInt("serviceType",0);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择服务器类型");
                final String[] serviceType = {"公网服务器", "内网服务器"};
                //    设置一个单项选择下拉框
                *//**
                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
                 * 第三个参数给每一个单选项绑定一个监听器
                 *//*
                builder.setSingleChoiceItems(serviceType, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       choice = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putInt("serviceType", choice);
                        editor.commit();
                        if(choice == 0)
                            HttpUtils.apiPath = HttpUtils.apiPathOuter;
                        if(choice == 1)
                            HttpUtils.apiPath = HttpUtils.apiPathLocal;
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();*/
            }
        });
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        promptText = (TextView) findViewById(R.id.promptText);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String name = userName.getText().toString();
        if (name == null || name.trim().length() == 0) {
            promptText.setText("用户名不能为空...");
            return;
        }
        String pwd = password.getText().toString();

        if (pwd == null || pwd.trim().length() == 0) {
            promptText.setText("密码不能为空...");
            return;
        }
        mAuthTask = new UserLoginTask(name, pwd);
        mAuthTask.execute((Void) null);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return HttpUtils.login(mEmail, mPassword, LoginActivity.this);
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "登录失败...", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                //登录成功
                if (state == -1) {
                    //保存登录状态
                    SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userSettings.edit();
                    editor.putString("userName", mEmail);
                    editor.putString("password", mPassword);
                    editor.commit();
                    final String lastVedioUrl = userSettings.getString("lastVedioUrl", "none");
                    //初始化节目列表
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                PlayListCache.initPlayInfo(mEmail,mPassword);
                                //启动播放器，传递播放地址
                                String url = lastVedioUrl;
                                if (url.equals("none"))
                                    url = PlayListCache.playListMap.get(PlayListCache.playListMap.keySet().iterator().next());
                                LiveActivityRel.activityStart(LoginActivity.this, url);
                                finish();
                            } catch (Exception e) {
                                promptText.setText("5:获取节目列表失败...");
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else if (state == 0) {
                    promptText.setText("0:网络不通，请检查网络连接情况...");
                } else if (state == 1) {
                    promptText.setText("1:帐号不存在...");
                } else if (state == 2) {
                    promptText.setText("2:密码错误...");
                } else if (state == 3) {
                    promptText.setText("3:帐号失效...");
                } else if (state == 5) {
                    promptText.setText("5:获取设备uuid失败...");
                } else if (state == 6) {
                    promptText.setText("6:该帐号已被其他设备绑定无法登录,如需解除请联系管理员...");
                } else if (state == 7) {
                    promptText.setText("7:获取设备信息失败...");
                } else if (state == 8) {
                    promptText.setText("8:该帐号已被其他设备绑定无法登录...");
                } else {
                    promptText.setText("4:登录服务出现异常，请稍后重试...");
                }
            } else {
                promptText.setText("4:登录服务出现异常，请稍后重试...");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

