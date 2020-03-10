package com.wang.library.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.library.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RippleView extends FrameLayout {


    /**
     * 最上层遮罩
     */
    private ImageView mIvMask;
    private int mMaskColor;
    private Paint mMaskPaint;
    /**
     * 上层icon
     */
    private ImageView mIvFront;
    /**
     * 下层进度
     */
    private CircleProgress mCircleProgress;
    /**
     * 最最上层点击数
     */
    private TextView mTvClicked;
    private int mClickCount;
    private ScaleAnimation textScaleAnimation;

    /**
     * 点击回调
     **/
    private ClickCallBack mClickCallBack;


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
        mCircleProgress = (CircleProgress) rootView.findViewById(R.id.circle_progress);
        mTvClicked = (TextView) rootView.findViewById(R.id.tv_click_count);
        mIvFront = (ImageView) rootView.findViewById(R.id.iv_front);

        mIvMask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mClickCount;

                if (mClickCallBack != null) {
                    if (mClickCount % 100 == 0) {
                        mClickCallBack.onHundredClicked();
                    }
                    if (mClickCount % 5 == 0) {
                        mClickCallBack.onFiveClicked();
                    }
                }

                mTvClicked.setText("x" + mClickCount);
                mIvFront.setVisibility(GONE);
                mCircleProgress.setProgress(mClickCount % 100);
                getScaleAnimator();

//                ScaleAnimation scaleAnimation = getTextScaleAnimation();
//                mTvClicked.startAnimation(scaleAnimation);
            }
        });
        mIvMask.setAlpha(0f);

    }

    /**
     * 外圈放大渐变
     */
    private void getScaleAnimator() {
        final float SCALE_LARGE = 1.5f;
        final float SCALE_MAX = 2f;
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.8f, 2f);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.setDuration(300);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                if (value <= SCALE_LARGE) {
                    mIvMask.setAlpha(1f);
                    mIvMask.setScaleX(value);
                    mIvMask.setScaleY(value);

                    mCircleProgress.setScaleX(value);
                    mCircleProgress.setScaleY(value);

                    mTvClicked.setScaleX(value);
                    mTvClicked.setScaleY(value);

                } else if (value > SCALE_LARGE) {
                    float alpha = (SCALE_MAX - value) / (SCALE_MAX - SCALE_LARGE);
                    mIvMask.setAlpha(alpha);
                    mIvMask.setScaleX(value);
                    mIvMask.setScaleY(value);

                    float scale = SCALE_LARGE - (value - SCALE_LARGE);
                    mCircleProgress.setScaleX(scale);
                    mCircleProgress.setScaleY(scale);

                    mTvClicked.setScaleX(scale);
                    mTvClicked.setScaleY(scale);

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
                        mIvFront.setVisibility(VISIBLE);
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

    /**
     * 文字放大
     * ScaleAnimation 放大后回到原来状态存在跳帧
     * @return
     */
    private ScaleAnimation getTextScaleAnimation() {
        if (textScaleAnimation == null) {
            textScaleAnimation = new ScaleAnimation(1f, 1.34f, 1f, 1.34f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            textScaleAnimation.setDuration(3000);
            textScaleAnimation.setRepeatMode(Animation.REVERSE);
        }
        return textScaleAnimation;
    }


    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }


    public interface ClickCallBack {

        void onFiveClicked();

        void onHundredClicked();
    }


}
