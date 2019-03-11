package jerry.farmhelper.module;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import jerry.farmhelper.MainActivity;
import jerry.farmhelper.Util;
import jerry.farmhelper.farm.FarmConfig;
import jerry.farmhelper.farm.FarmURL;
import jerry.farmhelper.net.NetWork;
import jerry.farmhelper.net.Request;


public class Hive {
    private TextView tv_info;
    private MainActivity activity;
    private Request.Listener listener;
    private Request.Listener listener_work;
    private Request.Listener listener_harvest;

    /**
     * freeCD:35
     * fwj:4627
     * helperType:0
     * helperVaildTime:0
     * helperWorktime:0
     * honey:402
     * level:29
     * payCD:6
     * stamp:1534409461
     * status:1
     */
    public Hive(TextView tv_info_, MainActivity activity_) {
        tv_info = tv_info_;
        activity = activity_;
        listener = new Request.Listener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if ("1".equals(data.getString("code"))) {
                        activity.tv_log.append("\n获取蜂巢数据成功");
                    } else {
                        activity.tv_log.append("\n获取蜂巢数据失败");
                        return;
                    }
                    tv_info.setText("_ _ _蜂巢_ _ _\n等级：");
                    tv_info.append(data.getString("level"));
                    tv_info.append("\n免费花粉：");
                    tv_info.append(data.getString("freeCD"));
                    tv_info.append("\n付费花粉：");
                    tv_info.append(data.getString("payCD"));
                    tv_info.append("\n蜂蜜量：");
                    tv_info.append(data.getString("honey"));
                    char s = data.getString("status").charAt(0);
                    int when = Integer.parseInt(data.getString("stamp"), 10);
                    if ('1' == s) {
                        tv_info.append("\n觅食倒计时：");
                        int time = when + 14400 - (int) (System.currentTimeMillis() / 1000);
                        if (time <= 0) {
                            tv_info.append("可以收获");
                            NetWork.queue.add(FarmURL.url_hive_col, FarmConfig.commonBody, listener_harvest);
                        } else {
                            tv_info.append(Util.calculateTime(time));
                        }
                    } else if ('2' == s) {
                        tv_info.append("\n休息倒计时：");
                        int time = when + 18000 - (int) (System.currentTimeMillis() / 1000);
                        if (time <= 0) {
                            tv_info.append("可以觅食");
                            NetWork.queue.add(FarmURL.url_hive_work, FarmConfig.commonBody, listener_work);
                        } else {
                            tv_info.append(Util.calculateTime(time));
                        }
                    } else {
                        Log.e("Hive", data.getString("stamp"));
                    }
                    activity.wish.start();
                }
            }
        };
        listener_work = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    if ('1' == ((JSONObject) object).getString("code").charAt(0)) {
                        activity.tv_log.append("\n蜜蜂开始觅食");
                    }
                }
            }
        };
        listener_harvest = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if ('1' != data.getString("code").charAt(0)) {
                        activity.tv_log.append("\n收获失败");
                        return;
                    }
                    activity.tv_log.append("\n收获蜜蜂：");
                    activity.tv_log.append("金币+");
                    activity.tv_log.append(data.getString("addMoney"));
                    activity.tv_log.append("蜂蜜+");
                    activity.tv_log.append(data.getString("addHoney"));
                    start();
                } else {
                    activity.tv_log.append("\n收获失败,返回数据类型错误");
                }
            }
        };
    }

    public void start() {
        NetWork.queue.add(FarmURL.url_hive, FarmConfig.commonBody, listener);
    }
}
