package com.gxf.liveplay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gongxufan on 2017/8/25.
 */

public class PlayListCache {
    public static Map<String, String> playListMap = new HashMap<String, String>();
    public static Map<String, ArrayList<HashMap<String, String>>> groupInfo = new HashMap<String, ArrayList<HashMap<String, String>>>();
    //1=全部频道 2=央视频道 3=卫视频道 4=省内频道 5=其他频道
    public static String[] groupInfoArray;
    public static List<String> channelInfoList = new ArrayList<>();
    //当前频道分组编号
    public static int index = 0;
    //当前播放的节目编号
    public static int currChannel = 0;

    public static void  locateCurrChannel(String url){
        for (int i = 0; i < channelInfoList.size(); i++) {
            String s = channelInfoList.get(i);
            if(url.equals(s)){
                PlayListCache.currChannel = i;
                return;
            }
        }
    }
    public static void initPlayInfo(String userName,String password) throws Exception{
        String playList = HttpUtils.getOfflinePlayList();
        JSONArray jsonArray = JSON.parseArray(playList);
        PlayListCache.groupInfoArray = new String[jsonArray.size() + 1];
        PlayListCache.groupInfoArray[0] = "我的频道";
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                //分组
                String group = (String) jo.get("group");
                PlayListCache.groupInfoArray[i + 1] = group;
                JSONArray list = jo.getJSONArray("list");
                if (list != null && list.size() > 0) {
                    ArrayList<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();
                    int no = 0;
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject o = list.getJSONObject(j);
                        HashMap<String, String> m = new HashMap<String, String>();
                        for (String k : o.keySet()) {
                            String v = (String) o.get(k);
                            m.put("title", k);
                            m.put("no", String.valueOf(++no));
                            channelInfoList.add(v);
                            PlayListCache.playListMap.put(k, v);
                            hashMaps.add(m);
                        }
                    }
                    PlayListCache.groupInfo.put(group, hashMaps);
                }
            }
        }
    }
}
