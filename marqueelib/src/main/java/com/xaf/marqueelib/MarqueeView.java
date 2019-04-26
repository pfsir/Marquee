package com.xaf.marqueelib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * @author oyangpengfei
 * @date 2019/4/24.
 * description
 */
public class MarqueeView extends ViewFlipper {
    /**
     * 当前横向滚动文本的索引
     */
    private int mCurrentIndex;
    /**
     * 横向滚动文本的宽
     */
    private int mTextWith = 500;
    /**
     * 横向滚动文本的高
     */
    private int mTextHeight = 70;
    /**
     * 横向滚动文本字体大小
     */
    private int mTextSize = 40;
    /**
     * 文本横向滚动的速度
     */
    private int mTextScrollSpeed = 200;
    /**
     * 竖向滚动的时间
     */
    private int mSwitchTime = 1000;
    /**
     * 用来做延迟操作
     */
    private Handler handler = new Handler(Looper.getMainLooper());

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @SuppressLint("RestrictedApi")
    private void init(AttributeSet attrs) {
        TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.MarqueeView);
        mTextScrollSpeed = typedArray.getInt(R.styleable.MarqueeView_horizontal_text_speed, mTextScrollSpeed);
        mTextSize = (int) typedArray.getDimension(R.styleable.MarqueeView_horizontal_text_size, mTextSize);
        mTextWith = (int) typedArray.getDimension(R.styleable.MarqueeView_horizontal_text_width, mTextWith);
        mTextHeight = (int) typedArray.getDimension(R.styleable.MarqueeView_horizontal_text_height, mTextHeight);
        mSwitchTime = typedArray.getInt(R.styleable.MarqueeView_horizontal_text_switch_time, mSwitchTime);
        typedArray.recycle();
    }

    public void setDataAndScroll(List<String> textList, List<Integer> colorList) {
        if (textList == null || textList.size() == 0 || colorList == null || colorList.size() == 0) {
            return;
        }
        //添加横向滚动view到当前控件
        addTextView(textList, colorList);
        //初始化监听，当进入动画完毕的
        initListener(textList.size());
        //开启滚动第一行
        startScroll(0, textList.size());
    }

    private void addTextView(final List<String> textList, List<Integer> colorList) {
        if (textList.size() == 1) {
            textList.addAll(textList);
        }
        //回收资源
        recyclerResource();
        removeAllViews();

        for (int i = 0; i < textList.size(); i++) {
            final TextHorizontalView textHorizontalView = new TextHorizontalView(getContext());

            LayoutParams params = new LayoutParams(mTextWith, mTextHeight);
            //设置速度
            textHorizontalView.setTextSpeedTime(mTextScrollSpeed);
            //设置文字大小
            textHorizontalView.setTextSize(mTextSize);
            textHorizontalView.setData(textList.get(i));
            if (colorList.size() == 1) {
                textHorizontalView.setTextColor(getResources().getColor(colorList.get(0)));
            } else if (colorList.size() > i) {
                textHorizontalView.setTextColor(getResources().getColor(colorList.get(i)));
            }

            final int finalI = i;
            textHorizontalView.setOnTextScrollFinishListener(new TextHorizontalView.OnTextScrollFinishListener() {

                @Override
                public void scrollFinish() {
                    int position = finalI + 1;
                    if (position == textList.size()) {
                        position = 0;
                    }
                    mCurrentIndex = position;
                    //横向滚动完毕，延时mSwitchTime开始下一个竖向滚动
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isFlipping()) {
                                return;
                            }
                            if (finalI == 0) {
                                stopFlipping();
                                startFlipping();
                            }
                            showNext();
                        }
                    }, mSwitchTime);

                }
            });
            textHorizontalView.setLayoutParams(params);
            addView(textHorizontalView);
        }
    }

    private void initListener(final int size) {
        //设置动画
        Animation inAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_come_in);
        setInAnimation(inAnim);
        Animation outAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_get_out);
        setOutAnimation(outAnim);

        getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //竖向滚动完毕，开始横滚
                startScroll(mCurrentIndex, size);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startScroll(int index, int size) {
        //停止竖滚，开始横向滚动
        stopFlipping();

        int lastIndex;
        if (index == 0) {
            lastIndex = size - 1;
        } else {
            lastIndex = index - 1;
        }
        //上次的重置
        TextHorizontalView horizontalLastView = (TextHorizontalView) getChildAt(lastIndex);
        horizontalLastView.resetVew();
        //当前滚动
        TextHorizontalView horizontalView = (TextHorizontalView) getChildAt(index);
        horizontalView.startRoll();
    }


    private void stopScroll() {
        //停止竖滚，开始横向滚动
        stopFlipping();
        //当前滚动
        TextHorizontalView horizontalView = (TextHorizontalView) getChildAt(mCurrentIndex);
        if (horizontalView != null) {
            horizontalView.stopScroll();
        }
    }


    private void recyclerResource() {
        //回收资源
        stopScroll();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recyclerResource();
        if (handler != null) {
            handler = null;
        }
    }
}
