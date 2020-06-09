package com.chengte99.itstar19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.chengte99.itstar19.model.AppConfig;
import com.chengte99.itstar19.model.RequestParameter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_WEB_ACTIVITY = 100;
    private AppConfig appConfig;
    private String appId;
    private String appVersion;
    private String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHardwareInfo();
        getSoftwareInfo();

        getConfig();
    }

    private void getHardwareInfo() {
        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void getConfig() {
        String baseVersion = appVersion + getString(R.string.config_api_version_suffix);
        String keyCode = getString(R.string.config_app_keycode);
        String isDev = getString(R.string.config_is_dev);

        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameter("app_version", baseVersion));
        parameters.add(new RequestParameter("OS", "2"));
        parameters.add(new RequestParameter("App", keyCode));
        parameters.add(new RequestParameter("IsDev", isDev));
        parameters.add(new RequestParameter("device_id", device_id));

        FormBody.Builder builder = new FormBody.Builder();
        if (parameters != null && parameters.size() > 0) {
            for (RequestParameter p : parameters) {
                builder.add(p.getName(), p.getValue());
            }
        }
        RequestBody formBody = builder.build();

        String config_api_url = getString(R.string.config_api_url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(config_api_url)
                .post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String resStr = response.body().string();
                Log.d(TAG, "onResponse: " + resStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(resStr);
                    }
                });
            }
        });
    }

    private void getSoftwareInfo() {
        appId = this.getPackageName();
        PackageInfo packageInfo = null;
        try {
            packageInfo = getApplication().getPackageManager().getPackageInfo(appId, 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getConfig: " + appId + "/" + appVersion);

        int buildSDKVersion = Build.VERSION.SDK_INT;
    }

    private void parseJSON(String resStr) {
        try {
            JSONObject object = new JSONObject(resStr);
            appConfig = new AppConfig(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (appConfig.getFeedback() == -1) {
            new AlertDialog.Builder(this)
                    .setTitle("New Verion")
                    .setMessage("Please download and install new version app")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse(appConfig.getAppUpDate_url());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(Intent.createChooser(intent, "Choose browser"));
//                            startActivity(intent);
                        }
                    }).show();
        }else {
            // action check_link
            actionChecklink();
        }
    }

    private void actionChecklink() {
        String checkLinkURL = appConfig.getWeb_url() + getString(R.string.check_link_url);
        Log.d(TAG, "actionChecklink: " + checkLinkURL);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(checkLinkURL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String resStr = response.body().string();
                Log.d(TAG, "onResponse: " + resStr);

                if (resStr.equals(appConfig.getCheck_link())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goWeb();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Connection Error")
                                    .setMessage("Connection Failed, Please contact customer service")
                                    .show();
                        }
                    });
                }
            }
        });
    }

    private void goWeb() {
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        intent.putExtra("WEB_URL", appConfig.getWeb_url());
        startActivityForResult(intent, REQUEST_WEB_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_WEB_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
