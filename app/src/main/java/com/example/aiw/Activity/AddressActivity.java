package com.example.aiw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aiw.R;

public class AddressActivity extends Activity {
    WebView daum_webView;
    TextView daum_result;
    Handler handler;
    Intent itent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        daum_result = (TextView) findViewById(R.id.daum_result);
        itent = getIntent();
        init_webView();
        handler = new Handler();
    }


    public void init_webView() {
        daum_webView = (WebView) findViewById(R.id.daum_webview);
        daum_webView.getSettings().setJavaScriptEnabled(true);
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        daum_webView.setWebChromeClient(new WebChromeClient());
        daum_webView.loadUrl("http://alstjd720.ivyro.net/minsung.php");
    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("%s %s", arg2, arg3));
                    init_webView();
                    itent.putExtra("address", daum_result.getText().toString());
                    setResult(2, itent);
                    finish();
                }
            });
        }
    }
}
