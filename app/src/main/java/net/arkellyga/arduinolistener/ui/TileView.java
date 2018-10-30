package net.arkellyga.arduinolistener.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TileView extends View{
    private int mColor = Color.BLACK;
    private int mBorderColor = Color.WHITE;
    private int mBorderSize = 5;
    private String mName = "wait data";
    private String mData;
    private String mText = mName;
    private int mTextSize = 15;
    private int mTextColor = Color.WHITE;
    private int mTextX = 0;
    private int mTextY = 0;
    private boolean mSwitch = true;
    private String mOnFirstClick;
    private String mOnSecondClick;

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TileView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(mColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);
        p.setColor(mTextColor);
        p.setTextSize(mTextSize);
        canvas.drawText(mText, mTextX, mTextY, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(mBorderSize);
        p.setColor(mBorderColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);
        super.onDraw(canvas);
    }

    public int getBorderSize() {
        return mBorderSize;
    }

    public void setBorderSize(int borderSize) {
        mBorderSize = borderSize;
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

    public void setSwitch() {
        mSwitch = !mSwitch;
    }

    public boolean isSwitch() {
        return mSwitch;
    }

    public String getOnFirstClick() {
        return mOnFirstClick;
    }

    public void setOnFirstClick(String onFirstClick) {
        mOnFirstClick = onFirstClick;
    }

    public String getOnSecondClick() {
        return mOnSecondClick;
    }

    public void setOnSecondClick(String onSecondClick) {
        mOnSecondClick = onSecondClick;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
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
