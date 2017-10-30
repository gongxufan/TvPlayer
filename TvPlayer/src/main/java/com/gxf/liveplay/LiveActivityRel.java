package com.gxf.liveplay;

/**
 * Created by gongxufan on 2017/9/4.
 */

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gxf.liveplay.ijkplayer.media.IjkVideoView;
import com.gxf.liveplay.update.UpdateAppReceiver;
import com.gxf.liveplay.update.UpdateAppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 直播
 */
public class LiveActivityRel extends Activity {

    private IjkVideoView mVideoView;
    private RelativeLayout mVideoViewLayout;
    private RelativeLayout mLoadingLayout;
    private LinearLayout leftMenuCtrl;
    private TextView mLoadingText;
    private TextView mTextClock;
    private String mVideoUrl = "";
    private int mRetryTimes = 0;
    private static final int CONNECTION_TIMES = 5;
    private ListView listView;
    private TextView tvGroupTitle;
    private AlertDialog.Builder builder;
    private int choice = 1;
    private UpdateAppReceiver updateAppReceiver = new UpdateAppReceiver();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                Map<String, String> versionInfo = (Map<String, String>) message.obj;
                //版本检测
                UpdateAppUtils.from(LiveActivityRel.this)
                        .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                        .serverVersionName(versionInfo.get("version"))
                        .apkPath(HttpUtils.apkPath)
                        .showNotification(true) //是否显示下载进度到通知栏，默认为true
                        .updateInfo(versionInfo.get("updateInfo"))  //更新日志信息 String
                        .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP) //下载方式：app下载、手机浏览器下载。默认app下载
                        .isForce(false) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                        .update();
            }
            return false;
        }
    });
   /* //显示时钟
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);//时间显示格式
                    //更新时间
                    mTextClock.setText(getDateFormate());
                    break;
                default:
                    break;

            }
        }
    };

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_rel);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> versionInfo = HttpUtils.getVersionInfo();
                Message message = Message.obtain();
                message.obj = versionInfo;
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
        initUI();
        initDialog();
        initVideo();
        //new TimeThread().start(); //启动新的线程刷新时间
    }

    private void initUI() {
        mVideoUrl = getIntent().getStringExtra("url");
        //定位当前频道位置，用于上下切换频道
        PlayListCache.locateCurrChannel(mVideoUrl);
        SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("lastVedioUrl", mVideoUrl);
        editor.commit();
        leftMenuCtrl = (LinearLayout) findViewById(R.id.leftMenuCtrl);
        tvGroupTitle = (TextView) findViewById(R.id.tvGroupTitle);
        //侧滑节目列表
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SimpleAdapter(this, getItem(), R.layout.item_menu, new String[]{"title", "no"}, new int[]{R.id.tvInfo, R.id.no}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.tvInfo);
                String tvInfo = (String) textView.getText();
                //mMenu.toggle();
                LiveActivityRel.activityStart(LiveActivityRel.this, PlayListCache.playListMap.get(tvInfo));
                //结束当前播放
                LiveActivityRel.this.finish();
            }
        });
        mVideoView = (IjkVideoView) findViewById(R.id.videoview);
        mVideoViewLayout = (RelativeLayout) findViewById(R.id.fl_videoview);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.rl_loading);
        mLoadingText = (TextView) findViewById(R.id.tv_load_msg);
        mLoadingText.setText("节目加载中...");
        //mTextClock = (TextView) findViewById(R.id.tv_time);
    }

    private ArrayList<HashMap<String, String>> reNo(ArrayList<HashMap<String, String>> group) {
        int index = 0;
        if (group != null && group.size() > 0) {
            for (int i = 0; i < group.size(); i++) {
                HashMap<String, String> stirngHashMap = group.get(i);
                Iterator<String> it = stirngHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if ("no".equals(key))
                        stirngHashMap.put("no", String.valueOf(++index));
                }
            }
        }
        return group;
    }

    public ArrayList<HashMap<String, String>> getItem() {

        if (PlayListCache.index == 0) {
            tvGroupTitle.setText("我的频道");
            int no = 0;
            ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
            Iterator<String> stringKyeSet = PlayListCache.groupInfo.keySet().iterator();
            while (stringKyeSet.hasNext()) {
                ArrayList<HashMap<String, String>> hashMapArrayList = PlayListCache.groupInfo.get(stringKyeSet.next());
                if (hashMapArrayList != null && hashMapArrayList.size() > 0) {
                    for (int i = 0; i < hashMapArrayList.size(); i++) {
                        HashMap<String, String> h = hashMapArrayList.get(i);
                        for (String k : h.keySet()) {
                            if ("no".equals(k))
                                h.put(k, String.valueOf(++no));
                        }

                    }
                }
                item.addAll(hashMapArrayList);
            }
            return item;
        } else {
            tvGroupTitle.setText(PlayListCache.groupInfoArray[PlayListCache.index]);
            return reNo(PlayListCache.groupInfo.get(PlayListCache.groupInfoArray[PlayListCache.index]));
        }

    }

    private String getDateFormate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void initVideo() {
        // init player`
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setVideoURI(Uri.parse(mVideoUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.setVolume(100, 100);
                mVideoView.start();
            }
        });

        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mLoadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                mLoadingLayout.setVisibility(View.VISIBLE);
                mVideoView.stopPlayback();
                mVideoView.release(true);
                mVideoView.setVideoURI(Uri.parse(mVideoUrl));
            }
        });

        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (++mRetryTimes > CONNECTION_TIMES) {
                    new AlertDialog.Builder(LiveActivityRel.this)
                            .setMessage("该频道媒体资源出现错误，节目暂时不能播放...")
                            .setPositiveButton(R.string.VideoView_error_button,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                            //LiveActivityRel.this.finish();
                                        }
                                    })
                            .setCancelable(false)
                            .show();
                } else {
                    mVideoView.stopPlayback();
                    mVideoView.release(true);
                    mVideoView.setVideoURI(Uri.parse(mVideoUrl));
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        this.registerReceiver(updateAppReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(updateAppReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    public static void activityStart(Context context, String url) {
        Intent intent = new Intent(context, LiveActivityRel.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void turnLeft(View view) {
        PlayListCache.index--;
        if (PlayListCache.index < 0) {
            PlayListCache.index = 0;
        }
        listView.setAdapter(new SimpleAdapter(this, getItem(), R.layout.item_menu, new String[]{"title", "no"}, new int[]{R.id.tvInfo, R.id.no}));
    }

    public void turnRight(View view) {
        PlayListCache.index++;
        if (PlayListCache.index > PlayListCache.groupInfoArray.length - 1) {
            PlayListCache.index = PlayListCache.groupInfoArray.length - 1;
        }
        listView.setAdapter(new SimpleAdapter(this, getItem(), R.layout.item_menu, new String[]{"title", "no"}, new int[]{R.id.tvInfo, R.id.no}));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isCtrl = leftMenuCtrl.getVisibility() == View.GONE;
        //确认键弹出频道列表界面
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (isCtrl) {
                leftMenuCtrl.setVisibility(View.VISIBLE);
                leftMenuCtrl.requestFocus();
            } else
                leftMenuCtrl.setVisibility(View.GONE);
        }
        //左右切换频道列表
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            turnRight(null);
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            turnLeft(null);
        }
        int size = PlayListCache.channelInfoList.size() - 1;
        //上下换台
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && isCtrl) {
            PlayListCache.currChannel--;
            if (PlayListCache.currChannel < 0)
                PlayListCache.currChannel = size;
            LiveActivityRel.activityStart(LiveActivityRel.this, PlayListCache.channelInfoList.get(PlayListCache.currChannel));
            //结束当前播放
            LiveActivityRel.this.finish();
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && isCtrl) {
            PlayListCache.currChannel++;
            if (PlayListCache.currChannel > size)
                PlayListCache.currChannel = 0;
            LiveActivityRel.activityStart(LiveActivityRel.this, PlayListCache.channelInfoList.get(PlayListCache.currChannel));
            //结束当前播放
            LiveActivityRel.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化好确认退出的对话框
     */
    private void initDialog() {

        builder = new AlertDialog.Builder(LiveActivityRel.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择操作");
        final String[] serviceType = {"退出当前登录", "关闭程序"};
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(serviceType, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //注销登录
                if (choice == 0) {
                   /* SharedPreferences userSettings = getSharedPreferences("auth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userSettings.edit();
                    editor.remove("userName");
                    editor.remove("password");
                    editor.commit();*/
                    startActivity(new Intent(LiveActivityRel.this, LoginActivity.class));
                    finish();
                }
                if (choice == 1)
                    finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 重写点击返回建的事件处理，展示 我们写好的dialog
     */
    @Override
    public void onBackPressed() {
        if (leftMenuCtrl.getVisibility() == View.GONE)
            builder.show();
        else
            leftMenuCtrl.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸控制频道界面
        boolean isCtrl = leftMenuCtrl.getVisibility() == View.GONE;
        if (isCtrl) {
            leftMenuCtrl.setVisibility(View.VISIBLE);
            leftMenuCtrl.requestFocus();
        }
        return false;
    }


}