package jerry.farmhelper.module;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jerry.farmhelper.MainActivity;
import jerry.farmhelper.farm.FarmConfig;
import jerry.farmhelper.farm.FarmURL;
import jerry.farmhelper.net.NetWork;
import jerry.farmhelper.net.Request;

public class Farm {
    private MainActivity activity;

    private int h_num = 0, w_num = 0, wt_num = 0, p_num = 0, d_num = 0, v_num = 0;
    private String[] harvest = new String[24];
    private TextView tv_info;
    private String vacantPlace;
    private String diePlace;
    private String[] die = new String[24];
    private String[] vacant = new String[24];
    private String[] water = new String[24];
    private String[] weed = new String[72];
    private String[] pest = new String[72];
    private Request.Listener listener_main;
    private Request.Listener listener_harvest;
    private Request.Listener listener_die;
    private Request.Listener listener_weed;
    private Request.Listener listener_water;
    private Request.Listener listener_pest;
    private Request.Listener listener_plant;
    private int exp;


    public Farm(TextView tv_info_, MainActivity activity_) {
        tv_info = tv_info_;
        activity = activity_;
        listener_main = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (!(object instanceof JSONObject)) {
                    Log.i("Farm", "返回数据错误");
                    return;
                }
                JSONObject data = (JSONObject) object;
                JSONObject user = data.getJSONObject("user");
                if (user == null) {
                    String content = data.getString("ecode");
                    activity.tv_log.append("\n获取农场数据失败(错误码：");
                    activity.tv_log.append(content);
                    String errorContent = data.getString("errorContent");
                    if (errorContent != null) {
                        activity.tv_log.append("):");
                        activity.tv_log.append(errorContent);
                    }
                    activity.login();
                    return;
                }
                activity.tv_log.append("\n获取土地数据成功");
                int exp = Integer.parseInt(user.getString("exp"), 10);
                int level = (int) (Math.sqrt(exp / 100 + 0.25) - 0.5);
                StringBuilder info = new StringBuilder("\n等级：").append(Integer.toString(level))
                        .append("\n金币：").append(user.getString("money"))
                        .append("\n点劵：").append(user.getString("coupon"));
                SpannableString string = new SpannableString("_ _ _土地_ _ _");
                ClickableSpan span = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        checkAndStart();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(0xffffbb01);
                    }
                };
                string.setSpan(span, 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_info.setText(string);
                tv_info.append(info);

                JSONArray landList = data.getJSONArray("farmlandStatus");
                int size = landList.size();
                if (size > 24) {
                    activity.tv_log.append("\n不支持VIP土地");
                    size = 24;
                }
                for (int i = 0; i < size; i++) {
                    JSONObject item = (JSONObject) landList.get(i);
                    char b = item.getString("b").charAt(0);
                    switch (b) {
                        case '6':
                            harvest[h_num++] = Integer.toString(i);
                            break;
                        case '7':
                            die[d_num++] = Integer.toString(i);
                            break;
                        case '0':
                            vacant[v_num++] = Integer.toString(i);
                            break;
                    }
                    b = item.getString("f").charAt(0);
                    for (; b > '0'; b--) {
                        weed[w_num++] = Integer.toString(i);
                    }
                    b = item.getString("g").charAt(0);
                    for (; b > '0'; b--) {
                        pest[p_num++] = Integer.toString(i);
                    }
                    if (item.getString("h").charAt(0) == '0') {
                        water[wt_num++] = Integer.toString(i);
                    }
                }

                StringBuilder builder = new StringBuilder(16);
                if (h_num > 0) {
                    builder.append(harvest[0]);
                    for (int i = 1; i < h_num; i++) {
                        builder.append(",").append(harvest[i]);
                    }
                    h_num = 0;
                    String place = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(place);
                    activity.tv_log.append("号土地成熟");
                    NetWork.queue.add(FarmURL.url_harvest, getActionData(place), listener_harvest);
                }
                if (d_num > 0) {
                    builder.append(die[0]);
                    for (int i = 1; i < d_num; i++) {
                        builder.append(",").append(die[i]);
                    }
                    d_num = 0;
                    diePlace = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(diePlace);
                    activity.tv_log.append("号土地枯萎");
                    NetWork.queue.add(FarmURL.url_scarify, getActionData(diePlace), listener_die);
                }
                if (w_num > 0) {
                    builder.setLength(0);
                    builder.append(weed[0]);
                    for (int i = 1; i < w_num; i++) {
                        builder.append(",").append(weed[i]);
                    }
                    w_num = 0;
                    String place = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(place);
                    activity.tv_log.append("号土地杂草");
                    NetWork.queue.add(FarmURL.url_weed, getActionData(place), listener_weed);
                }
                if (wt_num > 0) {
                    builder.setLength(0);
                    builder.append(water[0]);
                    for (int i = 1; i < wt_num; i++) {
                        builder.append(",").append(water[i]);
                    }
                    wt_num = 0;
                    String place = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(place);
                    activity.tv_log.append("号土地浇水");
                    NetWork.queue.add(FarmURL.url_water, getActionData(place), listener_weed);
                }
                if (p_num > 0) {
                    builder.setLength(0);
                    builder.append(pest[0]);
                    for (int i = 1; i < p_num; i++) {
                        builder.append(",").append(pest[i]);
                    }
                    p_num = 0;
                    String place = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(place);
                    activity.tv_log.append("号土地害虫");
                    NetWork.queue.add(FarmURL.url_spray, getActionData(place), listener_weed);
                }
                if (v_num > 0) {
                    builder.setLength(0);
                    builder.append(vacant[0]);
                    for (int i = 1; i < v_num; i++) {
                        builder.append(",").append(vacant[i]);
                    }
                    v_num = 0;
                    vacantPlace = builder.toString();
                    activity.tv_log.append("\n");
                    activity.tv_log.append(vacantPlace);
                    activity.tv_log.append("号土地空地");
                    StringBuilder places = new StringBuilder(180);
                    places.append(FarmConfig.commonBody)
                            .append("&cId=2714")
                            .append(FarmConfig.place).append(vacantPlace);
                    NetWork.queue.add(FarmURL.url_plant, places.toString(), listener_plant);
                }
                activity.hive.start();
            }
        };
        listener_harvest = new Request.Listener() {
            int exp;

            @Override
            public void onResponse(Object object) {
                exp = 0;
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        exp += parseHarvest(item);
                    }
                } else if (object instanceof JSONObject) {
                    exp += parseHarvest((JSONObject) object);
                } else return;
                activity.tv_log.append("\n收获共获得经验");
                activity.tv_log.append(Integer.toString(exp));
            }
        };
        listener_weed = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        exp += parseAction(item);
                    }
                } else if (object instanceof JSONObject) {
                    exp += parseAction((JSONObject) object);
                } else return;
                activity.tv_log.append("\n操作共获得经验");
                activity.tv_log.append(Integer.toString(exp));
            }
        };
        listener_die = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                activity.tv_log.append("\n铲除土地");
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        exp += parseAction(item);
                    }
                } else if (object instanceof JSONObject) {
                    exp += parseAction((JSONObject) object);
                } else {
                    activity.tv_log.append("\n铲除失败");
                    return;
                }
                StringBuilder builder = new StringBuilder(180);
                builder.append(FarmConfig.commonBody)
                        .append("&cId=2714")
                        .append(FarmConfig.place).append(diePlace);
                diePlace = null;
                NetWork.queue.add(FarmURL.url_plant, builder.toString(), listener_plant);
            }
        };
        listener_plant = new Request.Listener() {
            @Override
            public void onResponse(Object object) {
                activity.tv_log.append("\n播种土地");
                if (object instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) object;
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        exp += parseAction(item);
                    }
                } else if (object instanceof JSONObject) {
                    exp += parseAction((JSONObject) object);
                } else {
                    activity.tv_log.append("\n播种失败");
                }
            }
        };
    }

    /**
     * a:412
     * b:6 作物状态
     * c:0
     * d:0
     * e:1
     * f:0 草
     * g:0 虫
     * goldLand:true
     * h:1 浇水
     * i:100
     * isGoldLand:0
     * j:1 季数
     * k:32
     * l:19
     * m:32
     * n:{}
     * o:0
     * p:{}
     * pId:0
     * q:1534191407 plantTime
     * r:0
     * s:8 土地类型
     */
    public int parseHarvest(JSONObject item) {
        activity.tv_log.append("\n收获");
        activity.tv_log.append(item.getString("farmlandIndex"));
        activity.tv_log.append("号土地+");
        activity.tv_log.append(item.getString("harvest"));
        activity.tv_log.append(",经验+");
        String exp = item.getString("exp");
        activity.tv_log.append(exp);
        return Integer.parseInt(exp, 10);
    }

    public int parseAction(JSONObject item) {
        if (item.getString("code").charAt(0) == '1') {
            activity.tv_log.append("\n操作");
            activity.tv_log.append(item.getString("farmlandIndex"));
            activity.tv_log.append("号土地经验+");
            String exp = item.getString("exp");
            activity.tv_log.append(exp);
            return Integer.parseInt(exp, 10);
        } else {
            Log.e("parseAction", item.toString());
        }
        return 0;
    }

    public void checkAndStart() {
        exp = 0;
        NetWork.queue.add(FarmURL.url_main, "farmTime=0&farmKey=db03aaa4c6ae8e56943f5dda8791e48b", listener_main);
    }

    private String getActionData(String place) {
        StringBuilder builder = new StringBuilder(180);
        builder.append(FarmConfig.commonBody)
                .append(FarmConfig.place).append(place);
        String data = builder.toString();
        Log.i("getActionData", "容量：" + builder.capacity() + "长度" + data.length());
        return data;
    }
}
