package net.arkellyga.arduinolistener.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static net.arkellyga.arduinolistener.utils.Consts.*;

public class UpdateService extends Service {
    private SharedPreferences mSharedPreferences;
    private String mUrl;
    private boolean mIsUpdated;

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUrl = mSharedPreferences.getString(PREF_HOST_SERVER, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mUrl.isEmpty()) {
            log("call stopSelf");
            stopSelf();
            return START_NOT_STICKY;
        }
        log("onStartCommand");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedInputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                StringBuilder json = new StringBuilder();
                String path;
                int version = mSharedPreferences.getInt(PREF_APP_VERSION, 1);
                try {
                    urlConnection = (HttpURLConnection) new URL(mUrl).openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int inputData = reader.read();
                    while (inputData != -1) {
                        char current = (char) inputData;
                        inputData = reader.read();
                        json.append(current);
                    }
                    log("answer " + json.toString() + " current version " + version);
                    JSONObject js = new JSONObject(json.toString());
                    if (js.getInt("version") > version) {
                        log("start downloading");
                        inputStream = new BufferedInputStream(
                                new URL(js.getString("url")).openStream());
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                + "/carmanager.apk";
                        fileOutputStream = new FileOutputStream(path);
                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = inputStream.read(data, 0, 1024)) != -1) {
                            fileOutputStream.write(data, 0, count);
                        }
                        mIsUpdated = true;
                        log("stop downloading");
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    try {
                        if (inputStream != null)
                            inputStream.close();
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                            log("file stream was closed");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (mIsUpdated) {
            log("starting install");
            Intent updateIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            //updateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "carmanager.apk");
            Uri uri = Uri.fromFile(file);
            updateIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, getPackageName(), file);
            }
            updateIntent.setDataAndType(uri, "application/vnd.android" + ".package-archive");
            updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            updateIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(updateIntent);
        } else {
            log("have no new app");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void log(String text) {
        Log.d("UpdateService", text);
    }
}
