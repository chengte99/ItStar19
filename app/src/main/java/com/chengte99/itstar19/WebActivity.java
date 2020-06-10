package com.chengte99.itstar19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.chengte99.itstar19.model.AndroidJS;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = WebActivity.class.getSimpleName();
    private static final String JS_INTERFACE_NAME = "SgadmkApp";
    private WebView mainWebview;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String web_url = getIntent().getStringExtra("WEB_URL");
        Log.d(TAG, "onCreate: " + web_url);

        findViewSetup();
        backgroundSetup();
        webviewSetup(web_url);
    }

    private void backgroundSetup() {
        progressText.setTextColor(getResources().getColorStateList(R.color.white));
    }

    private void webviewSetup(String web_url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        this.mainWebview.setVisibility(View.GONE);

        initWebview(mainWebview);

        this.mainWebview.addJavascriptInterface(new AndroidJS(), JS_INTERFACE_NAME);
        this.mainWebview.loadUrl(web_url);
//        webView.loadUrl("https://188spo.fbdemo.info");
    }

    private void initWebview(WebView webView) {
        webSettingSetup(webView);
        webClientSetup(webView);
        webChromeClientSetup(webView);
    }

    private void webChromeClientSetup(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    progressText.setText("Resource loading ... " + newProgress + "%");
                }else {
                    progressText.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d(TAG, "onReceivedTitle: " + title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                Log.d(TAG, "onCreateWindow: " + isDialog);
                Log.d(TAG, "onCreateWindow: " + isUserGesture);
                Log.d(TAG, "onCreateWindow: " + resultMsg);

//                WebView newWebview = new WebView(WebActivity.this);
//                mainWebview.addView(newWebview);
//                initWebview(newWebview);
//                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                transport.setWebView(newWebview);
//                resultMsg.sendToTarget();
//
//                return true;

                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.d(TAG, "onJsAlert: ");
                new AlertDialog.Builder(WebActivity.this)
                        .setTitle("JS Alert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Log.d(TAG, "onJsConfirm: ");
                new AlertDialog.Builder(WebActivity.this)
                        .setTitle("JS Confirm")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                Log.d(TAG, "onJsPrompt: ");
                final EditText input = new EditText(WebActivity.this);
                input.setText("");
                new AlertDialog.Builder(WebActivity.this)
                        .setTitle(message)
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    private void webClientSetup(WebView webView) {
        mainWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
//                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished: " + url);
                super.onPageFinished(view, url);

//                view.evaluateJavascript("alert(123)", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d(TAG, "onReceiveValue: " + value);
//                    }
//                });
            }

            @Override
            public void onLoadResource(WebView view, String url) {
//                Log.d(TAG, "onLoadResource: " + url);
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG, "onReceivedError: ");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG, "onReceivedSslError: ");
            }
        });
    }

    private void webSettingSetup(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //useragent
        userAgentSetup(webSettings);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        /*if (NetStatusUtil.isConnected(getApplicationContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }*/

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        String cacheDirPath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(cacheDirPath);

        /*//清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        webView.clearCache(true);

        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory();

        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData();*/
    }

    private void userAgentSetup(WebSettings webSettings) {
        String defaultUserAgent = webSettings.getUserAgentString();
        int j = defaultUserAgent.indexOf("Version/");
        String fr = defaultUserAgent.substring(0, j);
        String tmp = defaultUserAgent.substring(j);
        int k = tmp.indexOf("Chrome/");
        String br = tmp.substring(0, k);
        String mr = tmp.substring(k);
        String customizeUserAgent = fr + mr + " " + br + "AppAn";
//        String customizeUserAgent = fr + mr + " " + br;
        webSettings.setUserAgentString(customizeUserAgent);
    }

    private void findViewSetup() {
        mainWebview = findViewById(R.id.webview_main);
        progressText = findViewById(R.id.web_progress_text);
    }

    @Override
    protected void onDestroy() {
        if (mainWebview != null) {
            mainWebview.loadDataWithBaseURL(null, "",
                    "text/html", "utf-8", null);
            mainWebview.clearHistory();

            ((ViewGroup) mainWebview.getParent()).removeView(mainWebview);
            mainWebview.destroy();
            mainWebview = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mainWebview.canGoBack()) {
            mainWebview.goBack();
            return true;
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Tips")
                    .setMessage("Are you sure to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
