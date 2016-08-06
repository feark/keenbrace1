package com.keenbrace.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;

public class StoreActivity extends BaseActivity implements View.OnClickListener {
    WebView webview;
    //ImageView back_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        //back_home = (ImageView) findViewById(R.id.back_home);
        //back_home.setOnClickListener(this);

        //webview = (WebView) findViewById(R.id.webView);
        String url = "http://www.keenbrace.com/v2/";

        //应用内打开网页 但有控件的网页不能显示
        /*
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        */

        //webview.loadUrl(url);

        Uri u = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, u);   this.startActivity(it);
        finish();
    }

    @Override
    public void initData()
    {

    }

    @Override
    public void initView()
    {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
