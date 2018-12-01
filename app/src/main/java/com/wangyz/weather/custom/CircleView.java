package com.wangyz.weather.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wangyz.weather.R;

/**
 * @author wangyz
 * 自定义View，用于显示空气质量、舒适度。
 */
public class CircleView extends View {

    private String mTitle;
    private int mTitleColor;
    private int mTitleSize;
    private String mInnerTitle;
    private int mInnerTitleColor;
    private int mInnerTitleSize;
    private String mInnerContent;
    private int mInnerContentColor;
    private int mInnerContentSize;
    private int mInnerContentMarginTop;
    private int mCircleRadius;
    private int mCircleWidth;
    private int mCircleMinValue;
    private int mCircleMinValueSize;
    private int mCircleMinValueColor;
    private int mCircleMaxValue;
    private int mCircleMaxValueSize;
    private int mCircleMaxValueColor;
    private int mCircleValue;
    private int mCircleBackgroundColor;
    private int mCircleForeColor;
    private int mCircleMarginTop;
    private int mCircleMarginBottom;

    private Paint mPaint;
    private Rect mTitleBounds;
    private Rect mInnerTitleBounds;
    private Rect mInnerContentBounds;
    private Rect mCircleMinValueBounds;
    private Rect mCircleMaxValueBounds;
    private int mStokeWidth;
    private int mSweepAngle;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        mTitle = ta.getString(R.styleable.CircleView_title);
        mTitleColor = ta.getColor(R.styleable.CircleView_title_text_color, Color.WHITE);
        mTitleSize = (int) ta.getDimension(R.styleable.CircleView_title_text_size, 16);
        mInnerTitle = ta.getString(R.styleable.CircleView_inner_title);
        mInnerTitleColor = ta.getColor(R.styleable.CircleView_inner_title_text_color, Color.WHITE);
        mInnerTitleSize = (int) ta.getDimension(R.styleable.CircleView_inner_title_text_size, 16);
        mInnerContent = ta.getString(R.styleable.CircleView_inner_content);
        mInnerContentColor = ta.getColor(R.styleable.CircleView_inner_content_text_color, Color.WHITE);
        mInnerContentSize = (int) ta.getDimension(R.styleable.CircleView_inner_content_text_size, 16);
        mInnerContentMarginTop = (int) ta.getDimension(R.styleable.CircleView_inner_content_margin_top, 10);
        mCircleRadius = (int) ta.getDimension(R.styleable.CircleView_circle_radius, 100);
        mCircleWidth = (int) ta.getDimension(R.styleable.CircleView_circle_width, 10);
        mCircleMinValue = ta.getInt(R.styleable.CircleView_circle_min_value, 0);
        mCircleMinValueSize = (int) ta.getDimension(R.styleable.CircleView_circle_min_value_text_size, 16);
        mCircleMinValueColor = ta.getColor(R.styleable.CircleView_circle_min_value_text_color, Color.WHITE);
        mCircleMaxValue = ta.getInt(R.styleable.CircleView_circle_max_value, 100);
        mCircleMaxValueSize = (int) ta.getDimension(R.styleable.CircleView_circle_max_value_text_size, 16);
        mCircleMaxValueColor = ta.getColor(R.styleable.CircleView_circle_max_value_text_color, Color.WHITE);
        mCircleValue = ta.getInt(R.styleable.CircleView_circle_value, 0);
        mCircleBackgroundColor = ta.getColor(R.styleable.CircleView_circle_background_color, Color.GRAY);
        mCircleForeColor = ta.getColor(R.styleable.CircleView_circle_fore_color, Color.BLUE);
        mCircleMarginTop = (int) ta.getDimension(R.styleable.CircleView_circle_margin_top, 10);
        mCircleMarginBottom = (int) ta.getDimension(R.styleable.CircleView_circle_margin_bottom, 10);
        ta.recycle();

        mPaint = new Paint();
        mStokeWidth = (int) mPaint.getStrokeWidth();
        mPaint.setAntiAlias(true);

        setBounds();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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

        int circleHeight = (int) (mCircleRadius + mCircleRadius * Math.sin(Math.toRadians(60)) * 100 / 100);
        int valueHeight = mCircleMinValueBounds.height() > mCircleMaxValueBounds.height() ? mCircleMinValueBounds.height() : mCircleMaxValueBounds.height();
        int viewHeight = mTitleBounds.height() + mCircleMarginTop + circleHeight + mCircleMarginBottom + valueHeight;

        int baseY = centerY - viewHeight / 2;

        if (mTitle.length() > 0) {
            drawTitle(canvas, centerX, baseY);
        }

        if (mInnerTitle.length() > 0) {
            drawInnerTitle(canvas, centerX, baseY);
        }

        if (mInnerContent.length() > 0) {
            drawInnerContent(canvas, centerX, baseY);
        }

        if (mCircleRadius > 0) {
            int circleX = centerX;
            int circleY = baseY + mTitleBounds.height() + mCircleMarginTop + mCircleRadius;
            drawCircle(canvas, circleX, circleY);
            drawMinMax(canvas, circleX, circleY);
        }

    }

    /**
     * 设置Bounds
     */
    private void setBounds() {
        if (mTitle.length() > 0) {
            mPaint.setTextSize(mTitleSize);
            mTitleBounds = new Rect();
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTitleBounds);
        }

        if (mInnerTitle.length() > 0) {
            mPaint.setTextSize(mInnerTitleSize);
            mInnerTitleBounds = new Rect();
            mPaint.getTextBounds(mInnerTitle, 0, mInnerTitle.length(), mInnerTitleBounds);
        }

        if (mInnerContent.length() > 0) {
            mPaint.setTextSize(mInnerContentSize);
            mInnerContentBounds = new Rect();
            mPaint.getTextBounds(mInnerContent, 0, mInnerContent.length(), mInnerContentBounds);
        }

        String minValue = mCircleMinValue + "";
        mPaint.setTextSize(mCircleMinValueSize);
        mCircleMinValueBounds = new Rect();
        mPaint.getTextBounds(minValue, 0, minValue.length(), mCircleMinValueBounds);

        String maxValue = mCircleMaxValue + "";
        mPaint.setTextSize(mCircleMaxValueSize);
        mCircleMaxValueBounds = new Rect();
        mPaint.getTextBounds(maxValue, 0, maxValue.length(), mCircleMaxValueBounds);
    }

    /**
     * 绘制标题
     *
     * @param canvas
     * @param centerX
     * @param baseY
     */
    private void drawTitle(Canvas canvas, int centerX, int baseY) {
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(mStokeWidth);
        mPaint.setTextSize(mTitleSize);
        mPaint.setColor(mTitleColor);
        int x = centerX - mTitleBounds.width() / 2;
        int y = baseY + mTitleBounds.height() / 2;
        canvas.drawText(mTitle, x, y, mPaint);
    }

    /**
     * 绘制内部标题
     *
     * @param canvas
     * @param centerX
     * @param baseY
     */
    private void drawInnerTitle(Canvas canvas, int centerX, int baseY) {
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(mStokeWidth);
        mPaint.setTextSize(mInnerTitleSize);
        mPaint.setColor(mInnerTitleColor);
        int x = centerX - mInnerTitleBounds.width() / 2;
        int y = baseY + mTitleBounds.height() + mCircleMarginTop + mCircleRadius - mInnerContentBounds.height() / 2 - mInnerContentMarginTop - mInnerTitleBounds.height() / 2;
        canvas.drawText(mInnerTitle, x, y, mPaint);
    }

    /**
     * 绘制内部内容
     *
     * @param canvas
     * @param centerX
     * @param baseY
     */
    private void drawInnerContent(Canvas canvas, int centerX, int baseY) {
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(mStokeWidth);
        mPaint.setTextSize(mInnerContentSize);
        mPaint.setColor(mInnerContentColor);
        int x = centerX - mInnerContentBounds.width() / 2;
        int y = baseY + mTitleBounds.height() + mCircleMarginTop + mCircleRadius;
        canvas.drawText(mInnerContent, x, y, mPaint);
    }

    /**
     * 绘制圆环
     *
     * @param canvas
     * @param x      圆的中心点x
     * @param y      圆的中心点y
     */
    private void drawCircle(Canvas canvas, int x, int y) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(mCircleBackgroundColor);
        mPaint.setStyle(Paint.Style.STROKE);

        int left = x - mCircleRadius;
        int top = y - mCircleRadius;
        int right = x + mCircleRadius;
        int bottom = y + mCircleRadius;
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawArc(rectF, 120, 300, false, mPaint);

        if (mCircleValue > 0) {
            mPaint.setColor(mCircleForeColor);
            mSweepAngle++;
            canvas.drawArc(rectF, 120, mSweepAngle, false, mPaint);
            int sweepAngle = mCircleValue * 300 / (mCircleMaxValue - mCircleMinValue);
            if (mSweepAngle < sweepAngle) {
                SystemClock.sleep(10);
                postInvalidate();
            }
        }
    }

    /**
     * 绘制最小值最大值
     *
     * @param canvas
     * @param x      圆的中心点x
     * @param y      圆的中心点y
     */
    private void drawMinMax(Canvas canvas, int x, int y) {

        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(mStokeWidth);

        int circleX = x;
        int circleY = y;

        int minX = (int) (circleX - Math.cos(Math.toRadians(60)) * 100 * mCircleRadius / 100 - mCircleMinValueBounds.width() / 2);
        int minY = (int) (circleY + Math.sin(Math.toRadians(60)) * 100 * mCircleRadius / 100 + mCircleMarginBottom + mCircleMinValueBounds.height() / 2);
        mPaint.setTextSize(mCircleMinValueSize);
        mPaint.setColor(mCircleMinValueColor);
        canvas.drawText(mCircleMinValue + "", minX, minY, mPaint);

        int maxX = (int) (circleX + Math.cos(Math.toRadians(60)) * 100 * mCircleRadius / 100 - mCircleMaxValueBounds.width() / 2);
        int maxY = (int) (circleY + Math.sin(Math.toRadians(60)) * 100 * mCircleRadius / 100 + mCircleMarginBottom + mCircleMaxValueBounds.height() / 2);
        mPaint.setTextSize(mCircleMaxValueSize);
        mPaint.setColor(mCircleMaxValueColor);
        canvas.drawText(mCircleMaxValue + "", maxX, maxY, mPaint);
    }

    public void setCircleView(String innerTitle, String innerContent, int circleValue) {
        mInnerTitle = innerTitle;
        mInnerContent = innerContent;
        mCircleValue = circleValue;
        setBounds();
        postInvalidate();
    }
}
