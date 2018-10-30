package net.arkellyga.arduinolistener.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ColorSquare extends View {
    private int mColor = Color.BLACK;
    private Canvas mCanvas;

    public ColorSquare(Context context) {
        super(context);
    }

    public ColorSquare(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorSquare(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        Paint p = new Paint();
        p.setColor(mColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        super.onDraw(canvas);
    }

    public void setColor(int color) {
        Log.d("ColorSquare", "setColor: " + color);
        if (mColor != -1) {
            mColor = color;
        }
        invalidate();
    }

    public int getColor() {
        return mColor;
    }
}
