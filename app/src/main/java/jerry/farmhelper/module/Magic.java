package jerry.farmhelper.module;

import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jerry.farmhelper.MainActivity;
import jerry.farmhelper.Util;
import jerry.farmhelper.farm.FarmConfig;
import jerry.farmhelper.farm.FarmURL;
import jerry.farmhelper.net.NetWork;
import jerry.farmhelper.net.Request;

public class Magic {
    private TextView tv_info;
    private MainActivity activity;
    private Request.Listener listener;
    private Request.Listener listener_tower;
    private int score;
    private int blood;
    private int tblood;

    public Magic(TextView tv_info_, final MainActivity activity_) {
        tv_info = tv_info_;
        activity = activity_;
        listener = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) != '0') {
                        Log.e("Magic", data.toString());
                        return;
                    }

                    activity.tv_log.append("\n获取魔法池数据成功");
                    score = 0;
                    blood = 0;
                    tblood = 0;

                    JSONArray heroList = data.getJSONArray("hero");
                    if (heroList == null) {
                        Log.e("Magic", data.toString());
                        return;
                    }
                    int size = heroList.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = heroList.getJSONObject(i);
                        score += item.getInteger("attack") + item.getInteger("defend");
                        tblood += item.getInteger("physical");
                        blood += item.getInteger("leftPhysical");
                    }
                    score += tblood;
                    tv_info.setText("_ _ _魔法池_ _ _\n战力：");
                    tv_info.append(Integer.toString(score));
                    tv_info.append("(");
                    tv_info.append(Integer.toString(size));
                    tv_info.append(")\n体力：");
                    tv_info.append(Integer.toString(blood));
                    tv_info.append("/");
                    tv_info.append(Integer.toString(tblood));
                    NetWork.queue.add(FarmURL.url_magic_tower, FarmConfig.commonBody, listener_tower);
                } else {
                    activity.tv_log.append("魔法池返回数据类型错误");
                    Log.e("Magic", "返回数据类型错误");
                }
            }
        };
        listener_tower = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) != '0') {
                        Log.e("Magic", data.toString());
                    } else {
                        tv_info.append("\n通天塔：");
                        tv_info.append(data.getString("layer"));
                        tv_info.append("层\n魔力：");
                        String moli = data.getString("vt1133");
                        int molis = Integer.parseInt(moli, 10);
                        tv_info.append(moli);
                        tv_info.append("/200");
                        if (molis == 200) return;
                        tv_info.append("\n魔力倒计时：");
                        int now = (int) (System.currentTimeMillis() / 1000);
                        int when = now - data.getInteger("towerMoliStamp") + 35820 - molis * 180;
                        tv_info.append(Util.calculateTime(when));
                    }
                    activity.sea.start();
                } else {
                    activity.tv_log.append("魔法池返回数据类型错误");
                    Log.e("Magic", "返回数据类型错误");
                }
            }
        };
    }

    public void start() {
        NetWork.queue.add(FarmURL.url_magic, FarmConfig.commonBody, listener);
    }
}
