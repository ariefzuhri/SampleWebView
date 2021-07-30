package com.ariefzuhri.dicoding;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ariefzuhri.dicoding.databinding.ActivityMainBinding;

/* To do list:
*  - Support file upload */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String HOMEPAGE_URL = "https://www.dicoding.com/";

    private ActivityMainBinding binding;
    private LoadingDialog loadingDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new LoadingDialog(this);

        binding.webView.getSettings().setUserAgentString(getString(R.string.app_name));
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAppCacheEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);

        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                // Mencegah webview membuka browser
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Simpan session data di dalam cookies
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.acceptCookie();
                cookieManager.flush();

                loadingDialog.dismiss();
            }
        });

        binding.webView.loadUrl(HOMEPAGE_URL);

        binding.tvTitle.setOnClickListener(this);
        binding.ibHome.setOnClickListener(this);
        binding.ibRefresh.setOnClickListener(this);
        binding.ibWebpage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == binding.ibHome.getId() || id == binding.tvTitle.getId()) {
            binding.webView.loadUrl(HOMEPAGE_URL);
        } else if (id == binding.ibRefresh.getId()) {
            binding.webView.reload();
        } else if (id == binding.ibWebpage.getId()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_webpage_title)
                    .setMessage(R.string.dialog_webpage_message)
                    .setNeutralButton(R.string.cancel, null)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) ->
                            openWebPage(binding.webView.getUrl()))
                    .create().show();
        }
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) binding.webView.goBack();
        else{
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_exit_title)
                    .setMessage(R.string.dialog_exit_message)
                    .setNeutralButton(R.string.cancel, null)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) ->
                            MainActivity.super.onBackPressed())
                    .create().show();
        }
    }
}