package com.example.god.tankimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by god on 2017/7/21.
 */

public class tankImageView extends View {
    private static final String TAG = "tankImageView";
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;
    private int mSpeed = 3;
    private int mProgress = -700;

    private int mWidth;
    private int mHeight;

    private Bitmap mImage;
    private int mImageScale;
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;

    private List<String> mLists;
    private List<dataBean> mData;
    private int mTextColor;
    private int mTitleTextSize;
    private Rect rect;
    private boolean isTurn;

    public tankImageView(Context context) {
        this(context, null);
    }

    public tankImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public tankImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mData = new ArrayList<>();

        //遍历获取XML参数
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.tankImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.tankImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.tankImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.tankImageView_textColor:
                    mTextColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.tankImageView_textSize:
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.tankImageView_speed:
                    mSpeed = a.getInteger(attr, 3);
                    break;
            }
        }

        a.recycle();
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        rect = new Rect();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTextColor);
        mBound = new Rect();

        new Thread() {
            public void run() {
                while (true) {
                    mProgress++;
                    //单次播放完成判断
                    isTurn = true;
                    for (int i = 0; i < mData.size(); i++) {
                        if (!mData.get(i).isEnd()) {
                            isTurn = false;
                        }
                    }
                    //开启循环
                    if (isTurn) {
                        mProgress = -700;
                        for (int i = 0; i < mData.size(); i++) {
                            mData.get(i).setEnd(false);
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    public void setLists(List<String> mLists) {
        this.mLists = mLists;
        //遍历整理数组
        int num = 0;
        for (int i = 0; i < mLists.size(); i++) {
            double random = Math.random();
            Log.d(TAG, "setLists: " + random);
            //调整条目间隔
            num -= (int) (random * 200) + 300;
            if (i == 0)
                mData.add(new dataBean(mLists.get(i), -mBound.width(), (int) (random * 3), false));
            else
                mData.add(new dataBean(mLists.get(i), num, (int) (random * 3), false));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        } else {
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            // 由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mBound.width();

            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
                Log.e("xxx", "AT_MOST");
            }
        }


        //设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mBound.height();
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int itemHeight = getHeight() / 3;

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTextColor);

        //绘制图像
        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            //计算居中的矩形范围
            rect.left = mWidth / 2 - mImage.getWidth() / 2;
            rect.right = mWidth / 2 + mImage.getWidth() / 2;
            rect.top = (mHeight - mBound.height()) / 2 - mImage.getHeight() / 2;
            rect.bottom = (mHeight - mBound.height()) / 2 + mImage.getHeight() / 2;
            canvas.drawBitmap(mImage, null, rect, mPaint);
        }
        if (mData.size() != 0) {
            for (int i = 0; i < mData.size(); i++) {
                if (!mData.get(i).isEnd()) {
                    int progress = mData.get(i).getProgress() + mProgress;
                    if (progress >= mWidth - getPaddingRight()) {
                        mData.get(i).setEnd(true);
                    }
                    //canvas.drawText(mText, mProgress, getHeight() / 2 + mBound.height() / 2, mPaint);
                    canvas.drawText(mData.get(i).getString(), progress, +itemHeight * mData.get(i).getCount() + 100, mPaint);
                }
            }
        }

    }

}

class dataBean {
    String string;
    int progress;
    int count;
    boolean end;

    public dataBean(String string, int progress, int count, boolean end) {
        this.string = string;
        this.progress = progress;
        this.count = count;
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


