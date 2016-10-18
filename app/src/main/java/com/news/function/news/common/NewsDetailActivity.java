package com.news.function.news.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.news.R;
import com.news.base.BaseActivity;
import com.news.entity.NewsEntity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/13.
 */
public class NewsDetailActivity extends BaseActivity {
    private static final String TAG = "NewsDetailActivity";
    @Bind(R.id.tb_toolbar)
    Toolbar toolbar;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_news_detail_layout;
    }

    @Override
    protected void initView() {
        NewsEntity itemValue = (NewsEntity) getIntent().getSerializableExtra("DATA");
        loadWeb(itemValue.getUrl());
        //设置CollapsingToolbarLayout扩张时的标题颜色
        collapsing_toolbar.setExpandedTitleColor(Color.WHITE);
        //设置CollapsingToolbarLayout收缩时的标题颜色
        collapsing_toolbar.setCollapsedTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Glide.with(this).load(itemValue.getPicUrl()).error(R.drawable.downfail).into(imageView);
    }

    @Override
    protected void initData() {

    }

    /**
     * 加载网页信息
     * @param url
     */
    private void loadWeb(String url){
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setDomStorageEnabled(true);
        this.webview.getSettings().setBlockNetworkImage(false);
        this.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.webview.getSettings().setAllowFileAccess(true);

        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        this.webview.getSettings().setAppCachePath(appCacheDir);
        this.webview.getSettings().setAppCacheEnabled(true);
        this.webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.loadUrl(url);
        if(Build.VERSION.SDK_INT >= 19) {
            webview.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webview.getSettings().setLoadsImagesAutomatically(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
