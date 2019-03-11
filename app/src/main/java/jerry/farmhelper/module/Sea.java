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

public class Sea {
    private Request.Listener listener;
    private Request.Listener listener_start;
    private Request.Listener listener_back;
    private Request.Listener listener_harvest;
    private TextView tv_info;
    private MainActivity activity;


    public Sea(final TextView tv_info_, MainActivity activity_) {
        tv_info = tv_info_;
        activity = activity_;
        listener = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    tv_info.setText("_ _ _探险_ _ _\n等级：");
                    tv_info.append(data.getString("level"));
                    tv_info.append("(");
                    tv_info.append(data.getString("exp"));
                    tv_info.append(")");
                    tv_info.append("\n金币：");
                    tv_info.append(data.getString("coin"));
                    tv_info.append("\n未解锁区域：");
                    tv_info.append(data.getString("unlockArea"));
                    JSONArray array = data.getJSONArray("vSubmarine");
                    int size = array.size();
                    tv_info.append(Integer.toString(size));
                    for (int i = 0; i < size; i++) {
                        JSONObject item = array.getJSONObject(i);
                        tv_info.append("\n潜艇");
                        String index = item.getString("index");
                        tv_info.append(index);
                        tv_info.append("：");
                        String areaid = item.getString("areaid");
                        if (areaid.charAt(0) == '0') {
                            char ind = index.charAt(0);
                            tv_info.append("可以出发");
                            char area = '3';
                            if (ind == '1') {
                                area = '2';
                            }
                            NetWork.queue.add(FarmURL.url_sea_back, getActionData(index, area), listener_start);
                            continue;
                        }
                        tv_info.append("区域");
                        tv_info.append(areaid);
                        //0：空闲 1:探索 2返回路上
                        char status = item.getString("status").charAt(0);
                        if (status == '1') {
                            String stamp = item.getString("stamp");
                            int when = Integer.valueOf(stamp, 10) - (int) (System.currentTimeMillis() / 1000);
                            if (when <= 0) {
                                tv_info.append("可以返回");
                                NetWork.queue.add(FarmURL.url_sea_back, getActionData(index), listener_back);
                            } else {
                                tv_info.append("探索倒计时：");
                                tv_info.append(Util.calculateTime(when));
                            }
                        } else if (status == '2') {
                            String stamp = item.getString("stamp");
                            int when = Integer.valueOf(stamp, 10) - (int) (System.currentTimeMillis() / 1000);
                            if (when <= 0) {
                                tv_info.append("可以收获");
                                NetWork.queue.add(FarmURL.url_sea_harvest, getActionData(index), listener_harvest);
                            } else {
                                tv_info.append("返回路上");
                            }
                        } else {
                            activity.tv_log.append(item.toString());
                        }
                    }
                }
            }
        };
        listener_start = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) == '0') {
                        activity.tv_log.append("\n潜艇");
                        activity.tv_log.append(data.getString("index"));
                        activity.tv_log.append("开始去区域");
                        activity.tv_log.append(data.getString("areaid"));
                        activity.tv_log.append("探险");
                    } else {
                        activity.tv_log.append("\n潜艇探险失败");
                    }

                }
            }
        };
        listener_back = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) == '0') {
                        activity.tv_log.append("\n潜艇");
                        activity.tv_log.append(data.getString("index"));
                        activity.tv_log.append("开始从区域");
                        activity.tv_log.append(data.getString("areaid"));
                        activity.tv_log.append("回来");
                    } else {
                        activity.tv_log.append("潜艇返回失败");
                    }
                }
            }
        };
        listener_harvest = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject data = (JSONObject) object;
                    if (data.getString("ecode").charAt(0) == '0') {
                        activity.tv_log.append("\n潜艇");
                        activity.tv_log.append(data.getString("index"));
                        activity.tv_log.append("从区域");
                        activity.tv_log.append(data.getString("areaid"));
                        activity.tv_log.append("归来并收获");
                    } else {
                        activity.tv_log.append("潜艇收获失败");
                    }
                }
            }
        };
    }

    public void start() {
        NetWork.queue.add(FarmURL.url_sea, FarmConfig.commonBody, listener);
    }

    private String getActionData(String index) {
        StringBuilder builder = new StringBuilder(165);
        builder.append(FarmConfig.commonBody).append("&index=").append(index);
        String data = builder.toString();
        Log.i("getActionData", "index" + data + "容量：" + builder.capacity() + "长度" + data.length());
        return data;
    }

    private String getActionData(String index, char area) {
        StringBuilder builder = new StringBuilder(170);
        builder.append(FarmConfig.commonBody).append("&index=").append(index).append("&iareaid=").append(area);
        String data = builder.toString();
        Log.i("getActionData", "area" + data + "容量：" + builder.capacity() + "长度" + data.length());
        return data;
    }
}
