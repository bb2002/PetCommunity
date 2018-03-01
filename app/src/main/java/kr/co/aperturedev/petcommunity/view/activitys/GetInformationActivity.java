package kr.co.aperturedev.petcommunity.view.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.co.aperturedev.petcommunity.R;

public class GetInformationActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_information);
        WebView webView=(WebView)findViewById(R.id.cafeWeb);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();
        webView.loadUrl("http://www.goldntree.co.kr");
    }
}
