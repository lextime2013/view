package com.lextime2013.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lextime2013.view.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 圆形图
 * Created by lex on 16/6/5.
 */
public class CircleView extends View {
    private final String TAG = CircleView.class.getSimpleName();

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mHourPaint;
    private Paint mMinutePaint;
    private Paint mSecondPaint;
    private float mRadius;
    private Context mContext;
    private Rect mRect = new Rect();
    private float mTextX;
    private float mTextY;
    private float mStartX;
    private float mStartY;
    private boolean mIsFirst = true;

    // 时间
    private float mSecondX;
    private float mSecondY;
    private float mMinuteX;
    private float mMinuteY;
    private float mHourX;
    private float mHourY;

    private String mTime;
    private boolean mIsDrag = false;

    private int[] timeArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        initPaint();
    }

    /**
     * 初始化画笔相关
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(mContext.getResources().getColor(R.color.black));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(80);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mHourPaint = new Paint();
        mHourPaint.setColor(mContext.getResources().getColor(R.color.grey));
        mHourPaint.setAntiAlias(true);
        mHourPaint.setDither(true);
        mHourPaint.setStrokeWidth(12);
        mHourPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mMinutePaint = new Paint();
        mMinutePaint.setColor(mContext.getResources().getColor(R.color.grey));
        mMinutePaint.setAntiAlias(true);
        mMinutePaint.setDither(true);
        mMinutePaint.setStrokeWidth(10);
        mMinutePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mSecondPaint = new Paint();
        mSecondPaint.setColor(mContext.getResources().getColor(R.color.red));
        mSecondPaint.setAntiAlias(true);
        mSecondPaint.setDither(true);
        mSecondPaint.setStrokeWidth(3);
        mSecondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);

        int circleRadius = Math.min(width, height);

        if(widthMode == MeasureSpec.UNSPECIFIED) {
            circleRadius = height;
        } else if(heightMode == MeasureSpec.UNSPECIFIED) {
            circleRadius = width;
        }

        setMeasuredDimension(circleRadius, circleRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 2F;
        mSecondX = mMinuteX = mHourX = mRadius;
        mSecondY = mMinuteY = mHourY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 圆
        mPaint.setColor(mContext.getResources().getColor(R.color.black));
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        mPaint.setColor(mContext.getResources().getColor(R.color.white));
        canvas.drawCircle(mRadius, mRadius, mRadius * 14F / 15F, mPaint);
        drawNumber(canvas);
        // 时分秒针
        canvas.drawLine(mRadius, mRadius, mSecondX, mSecondY, mSecondPaint);
        canvas.drawLine(mRadius, mRadius, mMinuteX, mMinuteY, mMinutePaint);
        canvas.drawLine(mRadius, mRadius, mHourX, mHourY, mHourPaint);
        // 中点
        canvas.drawCircle(mRadius, mRadius, 20, mMinutePaint);
        // 时间
        drawTime(canvas);
    }

    /**
     * 画数字
     * @param canvas 画布
     */
    private void drawNumber(Canvas canvas){
        for(int i = 0; i < timeArr.length; i++) {
            int hour = timeArr[i];

            float hourPercent = hour / 12F;
            float hourAlpha = (float) (hourPercent * Math.PI * 2);
            float x = (float) (mRadius + mRadius * Math.sin(hourAlpha) * 5F / 6F);
            float y = (float) (mRadius - mRadius * Math.cos(hourAlpha) * 5F / 6F);

            String number = String.valueOf(hour);
            Rect rect = new Rect();
            mTextPaint.getTextBounds(number, 0, number.length(), rect);

            canvas.drawText(String.valueOf(hour), x - rect.width() / 2F, y + rect.height() / 2F, mTextPaint);
        }
    }

    /**
     * 画时间
     */
    private void drawTime(Canvas canvas) {
        if(!TextUtils.isEmpty(mTime)) {
            if(mIsFirst) {
                mIsFirst = false;
                mTextPaint.getTextBounds(mTime, 0, mTime.length(), mRect);
                mTextX = mRadius - mRect.width() / 2F;
                mTextY = mRadius + 5F *mRect.height() / 2F;
            }
            canvas.drawText(mTime, mTextX, mTextY, mTextPaint);
        }
    }

    /**
     * 计算时间方法
     */
    public void calculateTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        mTime = df.format(calendar.getTime());

        int hour = calendar.getTime().getHours();
        int minute = calendar.getTime().getMinutes();
        int second = calendar.getTime().getSeconds();

        float hourPercent = hour / 12F;
        float minutePercent = minute / 60F;
        float secondPercent = second / 60F;

        float hourAlpha = (float) (hourPercent * Math.PI * 2);
        mHourX = (float) (mRadius + mRadius * Math.sin(hourAlpha) * 3F / 5F);
        mHourY = (float) (mRadius - mRadius * Math.cos(hourAlpha) * 3F / 5F);

        float minuteAlpha = (float) (minutePercent * Math.PI * 2);
        mMinuteX = (float) (mRadius + mRadius * Math.sin(minuteAlpha) * 4F / 5F);
        mMinuteY = (float) (mRadius - mRadius * Math.cos(minuteAlpha) * 4F / 5F);

        float secondAlpha = (float) (secondPercent * Math.PI * 2);
        mSecondX = (float) (mRadius + mRadius * Math.sin(secondAlpha));
        mSecondY = (float) (mRadius - mRadius * Math.cos(secondAlpha));

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                if(mRect.contains((int) (mStartX - mTextX), (int) (mStartY - mTextY))) {
                    mIsDrag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsDrag) {
                    float x = event.getX() - mStartX;
                    float y = event.getY() - mStartY;

                    mStartX = event.getX();
                    mStartY = event.getY();

                    float tempX1 = mTextX + x;
                    float tempX2 = tempX1 + mRect.width();
                    float tempY1 = mTextY + y;
                    float tempY2 = tempY1 - mRect.height();

                    // 左上角
                    float lenX1 = tempX1 - mRadius;
                    float lenY1 = tempY1 - mRadius;
                    float len1 = (float) Math.sqrt(lenX1 * lenX1 + lenY1 * lenY1);
                    // 左下角
                    float lenX2 = tempX1 - mRadius;
                    float lenY2 = tempY2 - mRadius;
                    float len2 = (float) Math.sqrt(lenX2 * lenX2 + lenY2 * lenY2);
                    // 右上角
                    float lenX3 = tempX2 - mRadius;
                    float lenY3 = tempY1 - mRadius;
                    float len3 = (float) Math.sqrt(lenX3 * lenX3 + lenY3 * lenY3);
                    // 右下角
                    float lenX4 = tempX2 - mRadius;
                    float lenY4 = tempY2 - mRadius;
                    float len4 = (float) Math.sqrt(lenX4 * lenX4 + lenY4 * lenY4);

                    if(len1 > mRadius || len2 > mRadius || len3 > mRadius || len4 > mRadius) {
                        return false;
                    }

                    mTextX = tempX1;
                    mTextY = tempY1;

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mIsDrag) {
                    mIsDrag = false;
                }
                break;
            default:break;
        }
        return true;
    }
}
