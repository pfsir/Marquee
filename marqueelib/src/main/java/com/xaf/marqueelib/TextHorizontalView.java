package com.xaf.marqueelib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author oyangpengfei
 * @date 2019/4/24.
 * description
 */

public class TextHorizontalView extends View {
    /**
     * 文字颜色,默认黑色
     */
    private int mTextColor = Color.BLACK;
    /**
     * 文字大小
     */
    private int mTextSize = 12;
    /**
     * 文本的x坐标
     */
    private float mXLocation = 0;
    /**
     * 滚动每个字的时间
     */
    private int mScrollSpeedTime = 300;
    /**
     * 画笔
     */
    private TextPaint mPaint;
    private Rect mRect;
    /**
     * 内容
     */
    private String mContent;
    /**
     * 文字高度
     */
    private float mTextHeight;
    /**
     * 值动画，配合postInvalidate刷新界面
     */
    private ValueAnimator verticalSwitchAnimator;
    /**
     * 开启动画
     */
    private boolean startRoll;

    /**
     * 滚动完毕的监听
     */
    public interface OnTextScrollFinishListener {
        void scrollFinish();
    }

    private OnTextScrollFinishListener onTextScrollFinishListener;

    public void setOnTextScrollFinishListener(OnTextScrollFinishListener onTextScrollFinishListener) {
        this.onTextScrollFinishListener = onTextScrollFinishListener;
    }

    public TextHorizontalView(Context context) {
        this(context, null);
    }

    public TextHorizontalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initPaint();
    }

    @SuppressLint("RestrictedApi")
    private void initAttrs(AttributeSet attrs) {
        TintTypedArray tta = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.TextHorizontalView);
        mTextColor = tta.getColor(R.styleable.TextHorizontalView_text_color, mTextColor);
        mTextSize = tta.getInt(R.styleable.TextHorizontalView_text_size, mTextSize);
        mScrollSpeedTime = tta.getInt(R.styleable.TextHorizontalView_text_scroll_speed_time, mScrollSpeedTime);
        tta.recycle();
    }

    private void initPaint() {
        mRect = new Rect();
        //初始化文本画笔
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        //文字颜色值,可以不设定
        mPaint.setColor(mTextColor);
        //文字大小
        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startRoll) {
            startRoll = false;
            operateScroll();
        }
        //把文字画出来
        if (mContent != null) {
            getContentWidth(mContent);
            canvas.drawText(mContent, mXLocation, getHeight() / 2 + mTextHeight / 2, mPaint);
        }
    }

    /**
     * 开始滚动
     * postInvalidate调用onDraw中的operateScroll开始绘制
     */
    public void startRoll() {
        startRoll = true;
        postInvalidate();
    }

    /**
     * 停止滚动，将值动画cancel
     */
    public void stopScroll() {
        if (verticalSwitchAnimator != null) {
            verticalSwitchAnimator.cancel();
        }
    }

    /**
     * 重置视图
     */
    public void resetVew() {
        mXLocation = 0;
        postInvalidate();
    }

    private void operateScroll() {
        final int moreWidth = (int) (getContentWidth(mContent) - getMeasuredWidth()) + mTextSize;
        //文字是否超出
        if (moreWidth <= 0) {
            if (onTextScrollFinishListener != null) {
                onTextScrollFinishListener.scrollFinish();
            }
        } else {
            verticalSwitchAnimator = ValueAnimator.ofFloat(0, 1);
            verticalSwitchAnimator.setDuration(mScrollSpeedTime * moreWidth / mTextSize);
            verticalSwitchAnimator.setInterpolator(new LinearInterpolator());
            verticalSwitchAnimator.start();
            verticalSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    mXLocation = -value * moreWidth;
                    postInvalidate();
                }
            });
            verticalSwitchAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (onTextScrollFinishListener != null) {
                        onTextScrollFinishListener.scrollFinish();
                    }
                }
            });
        }
    }

    private float getContentWidth(String mContent) {
        if (TextUtils.isEmpty(mContent)) {
            return 0;
        }
        if (mRect == null) {
            mRect = new Rect();
        }
        mPaint.getTextBounds(mContent, 0, mContent.length(), mRect);
        mTextHeight = getContentHeight();
        return mRect.width();
    }

    private float getContentHeight() {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        return Math.abs((fontMetrics.bottom - fontMetrics.top)) / 2;
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int mTextColor) {
        if (mTextColor != 0) {
            this.mTextColor = mTextColor;
            //文字颜色值,可以不设定
            mPaint.setColor(mTextColor);
        }
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(int mTextSize) {
        if (mTextSize > 0) {
            this.mTextSize = mTextSize;
            mPaint.setTextSize(mTextSize);
        }
    }

    /**
     * 设置每个字的滚动时间
     */
    public void setTextSpeedTime(int mScrollSpeedTime) {
        this.mScrollSpeedTime = mScrollSpeedTime;
    }


    /**
     * 设置滚动的条目内容  字符串形式的
     */
    public void setData(String mContent) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        this.mContent = mContent;
    }
}
