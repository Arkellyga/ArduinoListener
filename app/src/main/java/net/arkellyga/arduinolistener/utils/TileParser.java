package net.arkellyga.arduinolistener.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.ui.TileView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TileParser {
    private ArrayList<TileView> mViews;
    private static final String TAG = "TileParser";
    private Context mContext;

    //Keywords
    private int mBatLevel;
    private int mBatVoltage;
    private int mIsPlugged;

    private long mStartTime;
    private boolean mIsFirst = true;
    private long mEndTime;

    public TileParser(ArrayList<TileView> views, Context context) {
        mViews = views;
        mContext = context;
        registerBatteryReceiver();
    }

    public void addView(TileView view) {
        mViews.add(view);
    }

    public void parseString(String data) {
        Log.d(TAG, "parseString: " + data);
        if (mIsFirst) {
            mStartTime = System.currentTimeMillis();
            Toast.makeText(mContext, "first time " + mIsFirst, Toast.LENGTH_SHORT).show();
            mIsFirst = false;
        } else
            mEndTime = System.currentTimeMillis();

        Pattern startPattern = Pattern.compile("\\$(.+)\\$");
        Matcher startMatcher;
        Pattern pattern;
        Matcher matcher;
        String viewData;
        String parsedData;
        for (TileView tile : mViews) {
            viewData = tile.getData();
            startMatcher = startPattern.matcher(viewData);
            while (startMatcher.find()) {
                Log.d(TAG, "parseString: finded start " + startMatcher.group(1));
                pattern = Pattern.compile(startMatcher.group(1));
                matcher = pattern.matcher(data);
                while (matcher.find()) {
                    Log.d(TAG, "parseString: finded");
                    parsedData = matcher.group();
                    viewData = viewData.replaceAll("\\$.+\\$", parsedData);
                    //viewData = checkCustomKeywords(viewData);
                    tile.setText(viewData);
                    final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
                    anim.setDuration(100);
                    tile.startAnimation(anim);
                }
            }
            if (tile.getData().equals("%START_TIME%"))
                Toast.makeText(mContext, "start time", Toast.LENGTH_SHORT).show();
            tile.setText(checkCustomKeywords(tile.getText()));
            tile.invalidate();
        }
    }

    private String checkCustomKeywords(String data) {
        Log.d(TAG, "checkCustomKeywords: " + data);
        String result;
        result = data.replaceAll(Consts.KEYWORD_START_TIME, new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(mStartTime));
        result = result.replaceAll(Consts.KEYWORD_END_TIME, new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(mEndTime));
        result = result.replaceAll(Consts.KEYWORD_BAT, mBatLevel + "%");
        result = result.replaceAll(Consts.KEYWORD_BAT_VOLTAGE, mBatVoltage + "mv");
        result = result.replaceAll(Consts.KEYWORD_BAT_PLUGGED, (mIsPlugged == 0 ? "разряжается" : "заряжается"));
        Log.d(TAG, "checkCustomKeywords: result " + result);
        return result;
    }

    private void updateCustomData() {
        for (TileView view : mViews) {
            view.setText(checkCustomKeywords(view.getText()));
        }
    }

    public void parseCustomString(String data, String parse) {
        Pattern pattern;
        Matcher matcher;
        for (TileView tile : mViews) {
            //buffer = new StringBuffer();
            pattern = Pattern.compile(tile.getData());
            matcher = pattern.matcher(parse);
            while (matcher.find()) {
                Log.d(TAG, "parseString: " + matcher.group());
                tile.setText(data);
                tile.invalidate();
            }
        }
    }

    public void parseCustomString(int data, String parse) {
        Pattern pattern;
        Matcher matcher;
        for (TileView tile : mViews) {
            //buffer = new StringBuffer();
            pattern = Pattern.compile(tile.getData());
            matcher = pattern.matcher(parse);
            while (matcher.find()) {
                Log.d(TAG, "parseString: " + matcher.group());
                tile.setText(data + ""); // dirty hack:)
                tile.invalidate();
            }
        }
    }

    private void registerBatteryReceiver() {
        mContext.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void unregisterReceivers() {
        mContext.unregisterReceiver(mBatteryReceiver);
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            mIsPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            Log.d(TAG, "onReceive: " + mBatLevel);
            updateCustomData();
        }
    };
}
