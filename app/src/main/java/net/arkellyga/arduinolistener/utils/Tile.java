package net.arkellyga.arduinolistener.utils;

import android.graphics.Color;

public class Tile {
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;
    private int mColor;
    private int mBorderColor;
    private int mBorderSize;
    private int mTextSize;
    private int mTextColor;
    private int mTextX;
    private int mTextY;
    private String mName;
    private String mData;
    private boolean mSwitch;
    private String onFirstClick;
    private String onSecondClick;

    public Tile() {
        mX = 0;
        mY = 0;
        mWidth = 1;
        mHeight = 1;
        mColor = Color.BLACK;
        mBorderColor = Color.WHITE;
        mBorderSize = 5;
        mTextColor = Color.WHITE;
        mTextSize = 16;
        mTextX = 16;
        mTextY = 16;
        mName = "New";
        mData = "$parse$";
        mSwitch = false;
        onFirstClick = "";
        onSecondClick = "";
    }

    public int getBorderSize() {
        return mBorderSize;
    }

    public void setBorderSize(int borderSize) {
        mBorderSize = borderSize;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getTextX() {
        return mTextX;
    }

    public void setTextX(int textX) {
        mTextX = textX;
    }

    public int getTextY() {
        return mTextY;
    }

    public void setTextY(int textY) {
        mTextY = textY;
    }

    public boolean isSwitch() {
        return mSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        mSwitch = aSwitch;
    }

    public String getOnFirstClick() {
        return onFirstClick;
    }

    public void setOnFirstClick(String onFirstClick) {
        this.onFirstClick = onFirstClick;
    }

    public String getOnSecondClick() {
        return onSecondClick;
    }

    public void setOnSecondClick(String onSecondClick) {
        this.onSecondClick = onSecondClick;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }
}
