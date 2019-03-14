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

public class Wish {
    private Request.Listener listener;
    private Request.Listener listener_start;
    private Request.Listener listener_wish;
    private Request.Listener listener_harvest;
    private TextView tv_info;
    private MainActivity activity;
    private String lv;
    private String grow;

    public Wish(final TextView tv_info_, MainActivity activity_) {
        tv_info = tv_info_;
        activity = activity_;
        listener = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    activity.tv_log.append("\n获取许愿树数据成功");
                    JSONObject data = (JSONObject) object;
                    tv_info.setText("_ _ _许愿树_ _ _\n等级：");
                    tv_info.append(data.getString("lv"));
                    tv_info.append("(");
                    tv_info.append(data.getString("star_lv"));
                    tv_info.append("阶) ");
                    tv_info.append(data.getString("grow"));
                    tv_info.append("/");
                    tv_info.append(data.getString("next_lv"));
                    tv_info.append("\n摘星倒计时：");
                    int when = Integer.parseInt(data.getString("freeStarTime"), 10);
                    int now = (int) (System.currentTimeMillis() / 1000);
                    int time = when + 28800 - now;
                    if (time < 0) {
                        tv_info.append("可以摘星");
                        JSONArray starList = data.getJSONArray("starlist");
                        int size = starList.size();
                        char id = (char) ('1' + size);
                        for (int i = 0; i < size; i++) {
                            if (starList.getIntValue(i) != i + 1) {
                                id = (char) (i + '0');
                                break;
                            }
                        }
                        Log.i("Wish", starList.toString());
                        Log.i("Wish", "getActionData(id):" + (id - '0'));
                        NetWork.queue.add(FarmURL.url_wish_start, getActionData(id), listener_start);
                    } else {
                        tv_info.append(Util.calculateTime(time));
                    }
                    if (data.containsKey("gift_list")) {
                        tv_info.append("\n可以许愿");
                    } else {
                        tv_info.append("\n祝福倒计时：");
                        time = Integer.parseInt(data.getString("self_lasttime"), 10) - now;
                        if (time < 0) {
                            tv_info.append("可以祝福");
                            NetWork.queue.add(FarmURL.url_wish, FarmConfig.commonBody, listener_wish);
                        } else {
                            tv_info.append(Util.calculateTime(time));
                        }
                        tv_info.append("\n收获进度：");
                        String w_num = data.getString("w_num");
                        tv_info.append("/");
                        tv_info.append(data.getString("max_wish"));
                        if (w_num.length() == 2) {
                            NetWork.queue.add(FarmURL.url_wish_harvest, FarmConfig.commonBody, listener_harvest);
                        }
                        tv_info.append(w_num);
                    }
                    activity.magic.start();
                }
            }
        };
        listener_start = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("code").charAt(0) != '1') {
                        activity.tv_log.append("\n许愿树摘星失败");
                        return;
                    }
                    activity.tv_log.append("\n许愿树在摘星获得:");
                    activity.tv_log.append(data.getString("pkg"));
                }
            }
        };
        listener_wish = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) != '0') {
                        activity.tv_log.append("\n许愿树祝福失败：\n");
                        activity.tv_log.append(data.getString("direction"));
                        return;
                    }
                    activity.tv_log.append("\n许愿树在祝福获得:");
                    activity.tv_log.append(data.getString("self"));
                }
            }
        };
        listener_harvest = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) != '0') {
                        activity.tv_log.append("\n许愿树收获失败：\n");
                        activity.tv_log.append(data.getString("direction"));
                        return;
                    }
                    activity.tv_log.append("\n许愿树在收获获得:");
                    activity.tv_log.append(data.getString("pkg"));
                }
            }
        };
    }

    public void start() {
        NetWork.queue.add(FarmURL.url_wish_main, FarmConfig.commonBody, listener);
    }

    private String getActionData(char id) {
        StringBuilder builder = new StringBuilder(170);
        builder.append(FarmConfig.commonBody).append("&id=").append(id).append("&type=0");
        String data = builder.toString();
        Log.i("getActionData", data + "容量：" + builder.capacity() + "长度" + data.length());
        return data;
    }
}
