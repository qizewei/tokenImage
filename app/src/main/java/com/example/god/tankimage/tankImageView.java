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
import android.widget.ImageView;


/**
 * Created by god on 2017/7/21.
 */

public class tankImageView extends ImageView {
    private static final String TAG = "tankImageView";
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;
    private int mSpeed = 3;
    private int mProgress = 0;

    private int mWidth;
    private int mHeight;

    private Bitmap mImage;
    private int mImageScale;
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;

    private String mText;
    private int mTextColor;
    private int mTitleTextSize;
    private Rect rect;

    public tankImageView(Context context) {
        this(context, null);
    }

    public tankImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public tankImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
                case R.styleable.tankImageView_text:
                    mText = a.getString(attr);
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

        new Thread() {
            public void run() {
                while (true) {
                    mProgress++;
                    if (mProgress == mWidth) {
                        mProgress = 0;
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

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        rect = new Rect();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
    }

    public void setText(String mText) {
        this.mText = mText;
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

        //绘制文字
        canvas.drawText(mText, mProgress, getHeight() / 2 + mBound.height() / 2, mPaint);

    }

}


