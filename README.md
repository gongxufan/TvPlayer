# TvPlayer

1，android智能电视播放器，可以播放各电视台节目，播放基于ijkplayer的实现

2，测试的源地址可能失效，如需测试可以自己更换播放源。HttpUtils.getOfflinePlayList()返回的是测试源地址

3，项目有MainActivityOffline和MainActivity，MainActivityOffline是离线使用的，MainActivity涉及到登录和节目列表获取

4，编译环境：Android Studio3.0,jdk8

本项目中的播放源地址可能会失效，如需测试需要更改可用的播放源，具体代码在com.gxf.liveplay.HttpUtils#getOfflinePlayList，数据格式如下：

[  
  {  
    "group": "省内频道",  
    "list": [  
      {  
        "湖南都市": "http://220.248.175.231:6610/001/2/ch00000090990000001049/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      },  
      {  
        "湖南经视": "http://220.248.175.230:6610/001/2/ch00000090990000001052/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      }  
    ]  
  },  
  {  
    "group": "其他频道",  
    "list": [  
      {  
        "安徽卫视": "http://220.248.175.230:6610/001/2/ch00000090990000001024/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      },  
      {  
        "北京卫视": "http://220.248.175.230:6610/001/2/ch00000090990000001025/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      }  
    ]  
  },  
  {  
    "group": "高清频道",  
    "list": [  
      {  
        "CCTV1综合HD": "http://220.248.175.230:6610/001/2/ch00000090990000001075/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      },  
      {  
        "湖南经视HD": "http://220.248.175.230:6610/001/2/ch00000090990000001080/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      }  
    ]  
  },  
  {  
    "group": "央视频道",  
    "list": [  
      {  
        "CCTV1综合": "http://220.248.175.230:6610/001/2/ch00000090990000001075/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      },  
      {  
        "CCTV证券": "http://220.248.175.230:6610/001/2/ch00000090990000001133/index.m3u8?virtualDomain=001.live_hls.zte.com"  
      }  
    ]  
  }  
]  

