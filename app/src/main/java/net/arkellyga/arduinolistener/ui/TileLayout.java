package net.arkellyga.arduinolistener.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import net.arkellyga.arduinolistener.R;

import static net.arkellyga.arduinolistener.utils.Consts.*;

public class TileLayout extends ViewGroup {
    private static final String TAG = "TilesView";
    private SharedPreferences mSharedPreferences;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mColumns;
    private int mRows;
    private int mCellWidth;
    private int mCellHeight;
    private boolean mIsDrawGrid;

    public TileLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        init(context);
    }

    private void init(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y - getStatusBarHeight() - 56;
        mColumns = Integer.parseInt(mSharedPreferences.getString(PREF_TILES_COLUMN, "10"));
        mRows = Integer.parseInt(mSharedPreferences.getString(PREF_TILES_ROW, "10"));
        int backgroundColor = mSharedPreferences.getInt(PREF_BACKGROUND_COLOR, Color.WHITE);
        mIsDrawGrid = mSharedPreferences.getBoolean(PREF_LAYOUT_GRID, true);
        setBackgroundColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsDrawGrid) {
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            for (int i = 0; i < mScreenHeight; i += mCellHeight) {
                for (int j = 0; j < mScreenWidth; j += mCellWidth) {
                    canvas.drawRect(j, i, j + mCellWidth, i + mCellHeight, paint);
                }
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childCount = getChildCount();

        View child;
        for (int j = 0; j < childCount; j++) {
            child = getChildAt(j);

            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            int top = (layoutParams.getTop() * mCellHeight) + getPaddingTop();
            int left = (layoutParams.getLeft() * mCellWidth) + getPaddingLeft();
            int right = ((layoutParams.getLeft() + layoutParams.getWidth()) * mCellWidth) + getPaddingLeft();
            int bottom = ((layoutParams.getTop() + layoutParams.getHeight()) * mCellHeight) + getPaddingTop();
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        int height = 0;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            mCellWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / mColumns;
        } else {
            mCellWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    getResources().getDisplayMetrics());
            width = (mColumns * mCellWidth);
        }

        int childCount = getChildCount();
        View child;
        int maxRow = 0;

        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);

            LayoutParams params = (LayoutParams) child.getLayoutParams();

            int top = params.getTop();
            int w = params.getWidth();
            int h = params.getHeight();
            int bottom = top + h;
            int childWidthSpec = MeasureSpec.makeMeasureSpec(w * mCellWidth, MeasureSpec.EXACTLY);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(h * mCellHeight, MeasureSpec.EXACTLY);
            if (bottom > maxRow)
                maxRow = bottom;
        }
        mCellHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                getResources().getDisplayMetrics());
        int measuredHeight = maxRow * mCellHeight + getPaddingTop() + getPaddingBottom();
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            mCellHeight = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / mRows;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int atMostHeight = MeasureSpec.getSize(heightMeasureSpec);
            heightMeasureSpec = Math.min(atMostHeight, measuredHeight);
            mCellHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    getResources().getDisplayMetrics());
        } else {
            height = mScreenHeight;
            mCellHeight = height / mRows;
        }
        setMeasuredDimension(width, height);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TileLayout.LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof TileLayout.LayoutParams;
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new TileLayout.LayoutParams(p);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private int mTop = 0;
        private int mLeft = 0;
        private int mWidth = 1;
        private int mHeight = 1;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileLayout);
            mLeft = a.getInt(R.styleable.TileLayout_layout_left, 0);
            mHeight = a.getInt(R.styleable.TileLayout_layout_height, 0);
            mWidth = a.getInt(R.styleable.TileLayout_layout_width, 1);
            mTop = a.getInt(R.styleable.TileLayout_layout_top, 1);
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams params) {
            super(params);

            if (params instanceof LayoutParams) {
                LayoutParams tileParams = (LayoutParams) params;
                mTop = ((LayoutParams) params).getTop();
                mLeft = ((LayoutParams) params).getLeft();
                mWidth = ((LayoutParams) params).getWidth();
                mHeight = ((LayoutParams) params).getHeight();
            }
        }

        public LayoutParams() {
            this(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public int getTop() {
            return mTop;
        }

        public void setTop(int top) {
            mTop = top;
        }

        public int getLeft() {
            return mLeft;
        }

        public void setLeft(int left) {
            mLeft = left;
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
    }
}