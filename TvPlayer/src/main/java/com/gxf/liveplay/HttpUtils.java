package com.gxf.liveplay;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gongxufan on 2017/9/5.
 */

public class HttpUtils {
    public static String apiPath = "http://220.248.174.237:3390/api/";
    public static String apiPathLocal = "http://172.17.30.14:8080/api/";
    public static String apiPathOuter = "http://220.248.174.237:3390/api/";
    public static String apkPath = apiPath + "downloadApk";

    public static String getPlayList(String userName, String password) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiPath + "getPlayList?userName=" + userName + "&password=" + password)
                .build();
        Response response = client.newCall(request).execute();
        String playList = response.body().string();
        return playList;
    }

    public static String getOfflinePlayList() {
        return "[{\"group\":\"省内频道\",\"list\":[{\"湖南都市\":\"http://220.248.175.231:6610/001/2/ch00000090990000001049/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南经视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001052/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南都市\":\"http://220.248.175.230:6610/001/2/ch00000090990000001049/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南公共\":\"http://220.248.175.230:6610/001/2/ch00000090990000001051/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南电影\":\"http://220.248.175.230:6610/001/2/ch00000090990000001070/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南剧场\":\"http://220.248.175.230:6610/001/2/ch00000090990000001050/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南时尚\":\"http://220.248.175.230:6610/001/2/ch00000090990000001085/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南娱乐\":\"http://220.248.175.230:6610/001/2/ch00000090990000001013/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"金鹰纪实\":\"http://220.248.175.230:6610/001/2/ch00000090990000001057/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"金鹰卡通\":\"http://220.248.175.230:6610/001/2/ch00000090990000001058/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南国际\":\"http://220.248.175.230:6610/001/2/ch00000090990000001044/index.m3u8?virtualDomain=001.live_hls.zte.com\"}]},{\"group\":\"其他频道\",\"list\":[{\"安徽卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001024/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"北京卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001025/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"东方卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001036/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"东南卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001037/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"甘肃卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001109/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"广东卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001042/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"广西卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001045/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"贵州卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001046/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"河北卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001110/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"河南卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001111/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"黑龙江卫\":\"http://220.248.175.230:6610/001/2/ch00000090990000001048/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖北卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001047/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"吉林卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001112/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"江苏卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001055/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"江西卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001056/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"辽宁卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001061/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"旅游卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001062/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"南方卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001118/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"内蒙古卫\":\"http://220.248.175.230:6610/001/2/ch00000090990000001119/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"宁夏卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001120/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"青海卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001121/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"山东卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001065/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"山西卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001123/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"陕西卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001067/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"深圳卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001068/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"四川卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001064/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"天津卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001069/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"西藏卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001129/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"新疆卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001128/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"云南卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001072/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"浙江卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001073/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"重庆卫视\":\"http://220.248.175.230:6610/001/2/ch00000090990000001033/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"长沙经贸\":\"http://220.248.175.230:6610/001/2/ch00000090990000001034/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"长沙女性\":\"http://220.248.175.230:6610/001/2/ch00000090990000001106/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"长沙新闻\":\"http://220.248.175.230:6610/001/2/ch00000090990000001104/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"长沙政法\":\"http://220.248.175.230:6610/001/2/ch00000090990000001107/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果直播\":\"http://220.248.175.230:6610/001/2/ch00000090990000001140/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果独播\":\"http://220.248.175.230:6610/001/2/ch00000090990000001089/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果儿歌\":\"http://220.248.175.230:6610/001/2/ch00000090990000001096/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果港台\":\"http://220.248.175.230:6610/001/2/ch00000090990000001101/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果古装\":\"http://220.248.175.230:6610/001/2/ch00000090990000001092/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果韩国\":\"http://220.248.175.230:6610/001/2/ch00000090990000001094/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果好莱坞\":\"http://220.248.175.230:6610/001/2/ch00000090990000001091/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果快乐购\":\"http://220.248.175.230:6610/001/2/ch00000090990000001090/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果欧美A\":\"http://220.248.175.230:6610/001/2/ch00000090990000001100/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果欧美B\":\"http://220.248.175.230:6610/001/2/ch00000090990000001108/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果汽车\":\"http://220.248.175.230:6610/001/2/ch00000090990000001095/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果亲子\":\"http://220.248.175.230:6610/001/2/ch00000090990000001086/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果综艺\":\"http://220.248.175.230:6610/001/2/ch00000090990000001088/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"芒果军事\":\"http://220.248.175.230:6610/001/2/ch00000090990000001087/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"卡酷少儿\":\"http://220.248.175.230:6610/001/2/ch00000090990000001059/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CGTN\":\"http://220.248.175.230:6610/001/2/ch00000090990000001032/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CGTN DOCUMENTARY\":\"http://220.248.175.230:6610/001/2/ch00000090990000001026/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CNTV经典电影\":\"http://220.248.175.230:6610/001/2/ch00000090990000001054/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CNTV魅力时尚\":\"http://220.248.175.230:6610/001/2/ch00000090990000001117/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CNTV热播剧场\":\"http://220.248.175.230:6610/001/2/ch00000090990000001063/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"电子体育\":\"http://220.248.175.230:6610/001/2/ch00000090990000001038/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"高尔夫\":\"http://220.248.175.230:6610/001/2/ch00000090990000001043/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"国家卫计\\r\\n\":\"http://220.248.175.230:6610/001/2/ch00000090990000001102/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"快乐垂钓\":\"http://220.248.175.230:6610/001/2/ch00000090990000001060/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"先锋乒羽\":\"http://220.248.175.230:6610/001/2/ch00000090990000001127/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"家庭理财\":\"http://220.248.175.230:6610/001/2/ch00000090990000001114/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"财富天下\":\"http://220.248.175.230:6610/001/2/ch00000090990000001103/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"江苏靓妆\":\"http://220.248.175.230:6610/001/2/ch00000090990000001113/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"快乐宠物\":\"http://220.248.175.230:6610/001/2/ch00000090990000001115/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"快乐购\":\"http://220.248.175.230:6610/001/2/ch00000090990000001116/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"美食天府\":\"http://220.248.175.230:6610/001/2/ch00000090990000001132/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"人物频道\":\"http://220.248.175.230:6610/001/2/ch00000090990000001122/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"四海钓鱼\":\"http://220.248.175.230:6610/001/2/ch00000090990000001066/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"天元围棋\":\"http://220.248.175.230:6610/001/2/ch00000090990000001125/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"休闲棋牌\":\"http://220.248.175.230:6610/001/2/ch00000090990000001134/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"优漫卡通\":\"http://220.248.175.230:6610/001/2/ch00000090990000001071/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"优优宝贝\":\"http://220.248.175.230:6610/001/2/ch00000090990000001131/index.m3u8?virtualDomain=001.live_hls.zte.com\"}]},{\"group\":\"高清频道\",\"list\":[{\"CCTV1综合HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001075/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"北京卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001077/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"东方卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001081/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"黑龙江卫HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001084/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"江苏卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001082/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"山东卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001083/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"深圳卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001078/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"浙江卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001076/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"天津卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001135/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南卫视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001079/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"湖南经视HD\":\"http://220.248.175.230:6610/001/2/ch00000090990000001080/index.m3u8?virtualDomain=001.live_hls.zte.com\"}]},{\"group\":\"央视频道\",\"list\":[{\"CCTV1综合\":\"http://220.248.175.230:6610/001/2/ch00000090990000001075/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV2财经\":\"http://220.248.175.230:6610/001/2/ch00000090990000001014/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV3综艺\":\"http://220.248.175.230:6610/001/2/ch00000090990000001023/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV4国际\":\"http://220.248.175.230:6610/001/2/ch00000090990000001015/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV5体育\":\"http://220.248.175.230:6610/001/2/ch00000090990000001016/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV5赛事\":\"http://220.248.175.230:6610/001/2/ch00000090990000001074/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV7农业\":\"http://220.248.175.230:6610/001/2/ch00000090990000001018/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV9记录\":\"http://220.248.175.230:6610/001/2/ch00000090990000001020/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV10科教\":\"http://220.248.175.230:6610/001/2/ch00000090990000001021/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV11戏曲\":\"http://220.248.175.230:6610/001/2/ch00000090990000001027/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV12法制\":\"http://220.248.175.230:6610/001/2/ch00000090990000001028/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV13新闻\":\"http://220.248.175.230:6610/001/2/ch00000090990000001029/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV14少儿\":\"http://220.248.175.230:6610/001/2/ch00000090990000001030/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV15音乐\":\"http://220.248.175.230:6610/001/2/ch00000090990000001031/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV足球\":\"http://220.248.175.230:6610/001/2/ch00000090990000001041/index.m3u8?virtualDomain=001.live_hls.zte.com\"},{\"CCTV证券\":\"http://220.248.175.230:6610/001/2/ch00000090990000001133/index.m3u8?virtualDomain=001.live_hls.zte.com\"}]}]";
    }

    public static Boolean login(String mEmail, String mPassword, Context context) throws Exception {
        try {
            //获取uuid
            SharedPreferences userSettings = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
            String uuid = userSettings.getString("uuid", "none");
            if ("none".equals(uuid)) {
                SharedPreferences.Editor editor = userSettings.edit();
                uuid = UUID.randomUUID().toString();
                editor.putString("uuid", uuid);
                editor.commit();
            }
            String mac = DeviceUtils.getAdresseMAC(context);
            String eth0 = DeviceUtils.getMac();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiPath + "auth?userName=" + mEmail + "&password=" + mPassword + "&mac=" + mac + "&eth0=" + eth0 + "&uuid=" + uuid)
                    .build();
            Response response = client.newCall(request).execute();
            //网络连接错误
            if (response.code() != 200) {
                LoginActivity.state = 0;
                return false;
            }
            String result = response.body().string();
            JSONObject jsonObject = (JSONObject) JSON.parse(result);
            LoginActivity.state = (Integer) jsonObject.get("result");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LoginActivity.state = 0;
            return false;
        }
    }

    public static Map<String, String> getVersionInfo() {
        Map<String, String> versionInfo = new HashMap<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiPath + "getAppVersion").build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = (JSONObject) JSON.parse(result);
            versionInfo.put("version", jsonObject.getString("version"));
            versionInfo.put("updateInfo", jsonObject.getString("updateInfo"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return versionInfo;
    }
}
