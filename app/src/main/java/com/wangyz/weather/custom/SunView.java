package com.wangyz.weather.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wangyz.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyz
 * 显示日出日落的视图
 */
public class SunView extends View {

    private Paint mPaint;

    private String mTitle = "日出日落";
    private String mSunrise = "06:00";
    private String mSunset = "18:00";

    private int mBaseLineMarginTop = 20;
    private int mBaseLineMarginBottom = 30;
    private int mTitleSize = 30;
    private int mSunriseSize = 24;
    private int mSunsetSize = 24;
    private int mLineSize = 2;
    private int mArcLine = 2;
    private int sweepAngle = 0;
    private int currentAngle = 0;

    private Rect mTitleBounds;
    private Rect mSunriseBounds;
    private Rect mSunsetBounds;

    private int mRadius = 300;

    public SunView(Context context) {
        this(context, null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setBounds();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int availableWidth = getWidth() - paddingStart - paddingEnd;
        int availableHeight = getHeight() - paddingTop - paddingBottom;

        int centerX = availableWidth / 2 + paddingStart;
        int centerY = availableHeight / 2 + paddingTop;

        int valueHeight = mSunriseBounds.height() > mSunsetBounds.height() ? mSunriseBounds.height() : mSunsetBounds.height();

        int baseY = centerY + (mRadius / 2 - (valueHeight + mBaseLineMarginBottom) / 2);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mLineSize);
        canvas.drawLine(paddingStart, baseY, getWidth() - paddingEnd, baseY, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mArcLine);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        int left = centerX - mRadius;
        int top = baseY - mRadius;
        int right = centerX + mRadius;
        int bottom = baseY + mRadius;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawArc(rectF, 180, 180, false, mPaint);

        mPaint.setColor(Color.YELLOW);
        currentAngle++;
        canvas.drawArc(rectF, 180, currentAngle, false, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
        int bitmapLeft = (int) (centerX - mRadius * Math.cos(Math.toRadians(currentAngle))) - bitmap.getWidth() / 2;
        int bitmapTop = (int) (baseY - mRadius * Math.sin(Math.toRadians(currentAngle))) - bitmap.getHeight() / 2;
        canvas.drawBitmap(bitmap, bitmapLeft, bitmapTop, mPaint);

        if (currentAngle < sweepAngle) {
            SystemClock.sleep(10);
            postInvalidate();
        }

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTitleSize);
        int titleX = centerX - mTitleBounds.width() / 2;
        int titleY = baseY - mBaseLineMarginTop - mTitleBounds.height() / 2;
        canvas.drawText(mTitle, titleX, titleY, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mSunriseSize);
        int sunriseX = centerX - mRadius - mSunriseBounds.width() / 2;
        int sunriseY = baseY + mBaseLineMarginBottom + mSunriseBounds.height() / 2;
        canvas.drawText(mSunrise, sunriseX, sunriseY, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mSunsetSize);
        int sunsetX = centerX + mRadius - mSunsetBounds.width() / 2;
        int sunsetY = baseY + mBaseLineMarginBottom + mSunsetBounds.height() / 2;
        canvas.drawText(mSunset, sunsetX, sunsetY, mPaint);
    }

    public void setSunView(String sunrise, String sunset) {
        this.mSunrise = sunrise;
        this.mSunset = sunset;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            String date = format.format(d);
            sunrise = date + " " + sunrise;
            sunset = date + " " + sunset;
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date start = format.parse(sunrise);
            Date end = format.parse(sunset);
            long minutes = (end.getTime() - start.getTime()) / 1000 / 60;
            long elapsedMinutes = (d.getTime() - start.getTime()) / 1000 / 60;
            sweepAngle = (int) (elapsedMinutes * 180 * 100 / (minutes * 100));
            sweepAngle = sweepAngle > 180 ? 180 : sweepAngle;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setBounds();
        postInvalidate();
    }

    private void setBounds() {
        mPaint.setTextSize(mTitleSize);
        mTitleBounds = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTitleBounds);

        mPaint.setTextSize(mSunriseSize);
        mSunriseBounds = new Rect();
        mPaint.getTextBounds(mSunrise, 0, mSunrise.length(), mSunriseBounds);

        mPaint.setTextSize(mSunsetSize);
        mSunsetBounds = new Rect();
        mPaint.getTextBounds(mSunset, 0, mSunset.length(), mSunsetBounds);
    }
}
