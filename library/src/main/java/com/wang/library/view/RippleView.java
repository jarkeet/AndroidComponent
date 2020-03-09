package com.wang.library.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.library.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RippleView extends FrameLayout {


    private int mMaskColor;
    private Paint mMaskPaint;

    ImageView mIvMask;
    CircleProgress mCircleProgress;
    TextView mTvClicked;
    int mClickCount;


    public RippleView(@NonNull Context context) {
        this(context, null, 0);
    }

    public RippleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        mMaskColor = typedArray.getColor(R.styleable.RippleView_mask_color, 0xFA3C3C);
        typedArray.recycle();

        initPaint();
        initViews(context);

    }

    private void initViews(Context context) {
        View rootView = inflate(context, R.layout.layout_ripple_view, this);
        mIvMask = rootView.findViewById(R.id.iv_mask);
        mCircleProgress = (CircleProgress)rootView.findViewById(R.id.circle_progress);
        mTvClicked = (TextView)rootView.findViewById(R.id.tv_click_count);

        mIvMask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mClickCount;
                mTvClicked.setText("x" + mClickCount);

                mCircleProgress.setProgress(mClickCount % 100);
                getScaleAnimator();
            }
        });
        mIvMask.setAlpha(0f);

    }

    private void getScaleAnimator() {
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.8f, 2f);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.setDuration(300);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float)animation.getAnimatedValue();

                if(value <= 1.5f) {
                    mIvMask.setAlpha(1f);
                    mIvMask.setScaleX(value);
                    mIvMask.setScaleY(value);

                    mCircleProgress.setScaleX(value);
                    mCircleProgress.setScaleY(value);
                } else if(value > 1.5f) {
                    float alpha = (2f -  value) / 0.5f;
                    mIvMask.setAlpha(alpha);
                    mIvMask.setScaleX(value);
                    mIvMask.setScaleY(value);

                    float scale = 1.5f - (value - 1.5f);
                    mCircleProgress.setScaleX(scale);
                    mCircleProgress.setScaleY(scale);

                }
            }
        });

        scaleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCircleProgress.setCountDownListener(new CircleProgress.CountDownListener() {
                    @Override
                    public void onCountDownEnd() {
                        mClickCount = 0;
                        mTvClicked.setText(null);
                        mCircleProgress.setProgress(0);
                    }
                });

                mCircleProgress.countDown();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        scaleAnimator.start();
    }

    private void initPaint() {
        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setStyle(Paint.Style.FILL);
        mMaskPaint.setColor(mMaskColor);
        mMaskPaint.setAlpha(255);
    }




}
