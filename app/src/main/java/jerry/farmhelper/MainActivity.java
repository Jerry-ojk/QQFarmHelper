package jerry.farmhelper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SafeBrowsingResponse;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.FileInputStream;

import jerry.farmhelper.farm.FarmConfig;
import jerry.farmhelper.module.Farm;
import jerry.farmhelper.module.Hive;
import jerry.farmhelper.module.Magic;
import jerry.farmhelper.module.Sea;
import jerry.farmhelper.module.Wish;
import jerry.farmhelper.net.NetWork;


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    public TextView tv_log;
    public Farm farm;
    public Hive hive;
    public Wish wish;
    public Sea sea;
    public Magic magic;
    private boolean isStart = false;
    private NetWork netWork = new NetWork();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long a = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        long b = System.currentTimeMillis();
        Log.i("onCreate", "时间" + (b - a));
        Toolbar toolbar = findViewById(R.id.toolbar);
        tv_log = findViewById(R.id.tv_log);

        TextView tv_farm = findViewById(R.id.tv_farm);
        TextView tv_hive = findViewById(R.id.tv_hive);
        TextView tv_wish = findViewById(R.id.tv_wish);
        TextView tv_magic = findViewById(R.id.tv_magic);
        TextView tv_sea = findViewById(R.id.tv_sea);
        long c = System.currentTimeMillis();
        Log.i("onCreate", "时间" + (c - b));
        setSupportActionBar(toolbar);
        netWork.start();
        tv_log.setVerticalScrollBarEnabled(true);


        farm = new Farm(tv_farm, this);
        hive = new Hive(tv_hive, this);
        wish = new Wish(tv_wish, this);
        magic = new Magic(tv_magic, this);
        sea = new Sea(tv_sea, this);
        try {
            FileInputStream input = openFileInput("sss.txt");
            byte[] bytes = new byte[33];
            int len = input.read(bytes, 0, 33);
            if (len > 25 && len < 33) {
                FarmConfig.setCookiesFormFile(bytes, len);
                Log.i("onCreate", "获取cookies：" + FarmConfig.cookies);
            } else {
                Log.i("onCreate", "获取cookies失败");
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (FarmConfig.cookies == null) {
            login();
        } else {
            check();
        }
    }

    private void check() {
        if (FarmConfig.cookies == null) {
            tv_log.append("\n请登录");
        }
        if (tv_log.getText().length() != 0) {
            tv_log.append("\n\n");
        }
        tv_log.append("助手开始工作");
        farm.checkAndStart();
    }

    public void start() {
        hive.start();
    }


    public void login() {
        CookieManager manager = CookieManager.getInstance();
        manager.removeAllCookies(null);
        isStart = false;
        tv_log.setText("");
        if (webView == null) {
            webView = findViewById(R.id.webView);
            webView.setScrollContainer(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i("should", url);
                    if (url.charAt(8) == 'p') {
                        view.loadUrl(url);
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    CookieManager manager = CookieManager.getInstance();
                    Log.i("onPageStarted", url + "  cookies= " + manager.getCookie(url));
                    if (url.charAt(8) == 'p') {
                        FarmConfig.setCookies(MainActivity.this, manager.getCookie(url));
                        if (FarmConfig.cookies != null) {
                            webView.removeAllViews();
                            webView.setVisibility(View.GONE);
                            webView.pauseTimers();
                            webView.stopLoading();
                            webView.loadUrl("about:blank");
                            webView.clearHistory();
                            check();
                        }
                    }
                }

                @Override
                public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
                    super.onSafeBrowsingHit(view, request, threatType, callback);
                }
            });
        }
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl("http://ui.ptlogin2.qq.com/cgi-bin/login?style=9&appid=549000912&daid=5&s_url=https%3A%2F%2Fgame.qzone.qq.com%2Fapp%2F353.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
                finish();
                break;
            case R.id.refresh:
                check();
                break;
            case R.id.clear:
                tv_log.setText("");
                break;
            case R.id.login:
                login();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        netWork.stopSelf();
        super.onDestroy();
    }
}
