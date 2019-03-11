package jerry.farmhelper;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jerry.farmhelper.farm.FarmConfig;


public class LoginFragment extends Fragment {
    public WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        webView = new WebView(container.getContext());
        webView.setScrollContainer(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("should", url);
                if (!url.startsWith("https://g")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CookieManager manager = CookieManager.getInstance();
                FarmConfig.cookies = manager.getCookie(url);
                Log.i("onPageStarted", url + "cookies=" + FarmConfig.cookies);
                if (url.startsWith("https://p")) {
                    if (FarmConfig.cookies != null) {
                        // webView.removeAllViews();
                        // webView.destroy();
                        webView.setVisibility(View.INVISIBLE);
                        webView.pauseTimers();
                        webView.stopLoading();
                        webView.loadUrl("about:blank");
                        webView.clearHistory();
                    }
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl("http://ui.ptlogin2.qq.com/cgi-bin/login?style=9&appid=549000912&daid=5&s_url=https%3A%2F%2Fgame.qzone.qq.com%2Fapp%2F353.html");
        return webView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
