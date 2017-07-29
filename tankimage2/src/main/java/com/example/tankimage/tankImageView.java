package com.example.tankimage;

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

import java.util.ArrayList;
import java.util.List;


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
    private Rect rect;
    private int mWidth;
    private int mHeight;

    private int mSpeed = 3;
    private int mProgress = 0;
    private Bitmap mImage;
    private int mImageScale = 0;
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    private int mTextColor;
    private int mTextSize = 17;
    private boolean mRepeat = true;
    private int mTankRows = 5;
    private int mSpace = 300;
    private int mDirection = 0;
    private static final int TANK_DIRECTION_LEFT = 0;
    private static final int TANK_DIRECTION_RIGHT = 1;

    private List<dataBean> mData;
    private boolean isTurn;
    private TypedArray typedArray;
    private boolean isFirst = true;

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public int getmImageScale() {
        return mImageScale;
    }

    public void setmImageScale(int mImageScale) {
        this.mImageScale = mImageScale;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public boolean ismRepeat() {
        return mRepeat;
    }

    public void setmRepeat(boolean mRepeat) {
        this.mRepeat = mRepeat;
    }

    public int getmTankRows() {
        return mTankRows;
    }

    public void setmTankRows(int mTankRows) {
        this.mTankRows = mTankRows;
    }

    public int getmSpace() {
        return mSpace;
    }

    public void setmSpace(int mSpace) {
        this.mSpace = mSpace;
    }

    public int getmDirection() {
        return mDirection;
    }

    public void setmDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    public void setmSpeed(int speed) {
        this.mSpeed = speed;
    }

    public int getmSpeed() {
        return mSpeed;
    }

    /**
     * 遍历整理数组
     *
     * @param mLists
     */
    public void setLists(List<String> mLists) {
        int num = 0;
        for (int i = 0; i < mLists.size(); i++) {
            double random = Math.random();
            Log.d(TAG, "setLists: " + random);
            //调整条目间隔
            num -= (int) (random * mSpace) + mSpace;
            if (i == 0)
                mData.add(new dataBean(mLists.get(i), -mBound.width(), (int) (random * mTankRows), false));
            else
                mData.add(new dataBean(mLists.get(i), num, (int) (random * mTankRows), false));
        }
    }

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
        typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.tankImageView, defStyleAttr, 0);
        ergodic();

        //获得绘制文本的宽和高
        mPaint = new Paint();
        rect = new Rect();
        mPaint.setTextSize(mTextSize);
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
                    if (isTurn && mRepeat) {
                        if (mDirection == 1) {
                            mProgress = 0;
                        } else {
                            mProgress = -700;
                        }
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

    /**
     * 遍历获取XML参数
     */
    private void ergodic() {
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.tankImageView_image) {
                mImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));

            } else if (attr == R.styleable.tankImageView_imageScaleType) {
                mImageScale = typedArray.getInt(attr, 0);

            } else if (attr == R.styleable.tankImageView_direction) {
                mDirection = typedArray.getInt(attr, 0);

            } else if (attr == R.styleable.tankImageView_textColor) {
                mTextColor = typedArray.getColor(attr, Color.WHITE);

            } else if (attr == R.styleable.tankImageView_textSize) {
                mTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.tankImageView_speed) {
                mSpeed = typedArray.getInteger(attr, 3);

            } else if (attr == R.styleable.tankImageView_progress) {
                mProgress = -1 * typedArray.getInteger(attr, 700);

            } else if (attr == R.styleable.tankImageView_repeat) {
                mRepeat = typedArray.getBoolean(attr, true);

            } else if (attr == R.styleable.tankImageView_rows) {
                mTankRows = typedArray.getInt(attr, 5);

            } else if (attr == R.styleable.tankImageView_space) {
                mSpace = typedArray.getInt(attr, 300);

            }
        }

        typedArray.recycle();
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
        //单条弹幕的高度
        int itemHeight = (getHeight()) / mTankRows;

        mPaint.setColor(Color.BLACK);
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

        //绘制弹幕
        if (mData.size() != 0) {
            for (int i = 0; i < mData.size(); i++) {
                //判断弹幕方向
                if (mDirection == 0) {
                    if (!mData.get(i).isEnd()) {
                        if (isFirst) {
                            //弹幕第一遍播放完成
                            mProgress = -700;
                            isFirst = false;
                        }
                        //获取弹幕的X轴位置
                        int progress = mData.get(i).getProgress() + mProgress;
                        if (progress >= mWidth - getPaddingRight()) {
                            //弹幕播放完成
                            mData.get(i).setEnd(true);
                        }
                        canvas.drawText(mData.get(i).getString(), progress, itemHeight * (mData.get(i).getCount() + 0.9f), mPaint);
                    }
                } else {
                    if (!mData.get(i).isEnd()) {
                        if (isFirst) {
                            mProgress = 0;
                            isFirst = false;
                        }
                        int progress = Math.abs(mData.get(i).getProgress()) + getWidth() - Math.abs(mProgress);
                        if (progress <= -700) {
                            mData.get(i).setEnd(true);
                        }
                        canvas.drawText(mData.get(i).getString(), progress, itemHeight * (mData.get(i).getCount() + 0.9f), mPaint);
                    }
                }
            }
        }

    }

}

class dataBean {
    String string;  //弹幕的内容
    int progress;   //弹幕的初始进度
    int count;      //弹幕应该出现的第count行
    boolean end;    //弹幕是否播放结束

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


