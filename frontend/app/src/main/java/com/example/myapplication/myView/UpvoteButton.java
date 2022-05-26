//package com.example.androidapp.myView;
package com.example.myapplication.myView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.myapplication.R;

/**
 * usage:
 * xml:
 * <com.example.androidapp.component.FocusButton
 * android:layout_width="100dp"
 * android:layout_height="50dp"
 * android:textSize="25dp"
 * app:radius="40dp"
 * app:border_width="10"
 * app:border_color="@color/green"
 * app:border_color_pressed="@color/blue"
 * app:bg_color="@color/colorPrimary"
 * app:bg_color_pressed="@color/black"
 * app:text_color="@color/md_yellow_50"
 * app:text_color_pressed="@color/md_pink_100"
 * app:text="收藏"
 * app:text_pressed="已收藏"
 * >
 * </com.example.androidapp.component.FocusButton>
 * <p>
 * java:
 * boolean isPressed()
 */

/**
 * 关注按钮，带动画
 */
public class UpvoteButton extends androidx.appcompat.widget.AppCompatImageButton implements View.OnClickListener {

    private int bg_color_pressed = Color.parseColor("#EEEEEE");
    private int bg_color = Color.WHITE;
    private int text_color_pressed = Color.DKGRAY;
    private int text_color = Color.BLUE;
    private String text_pressed = "已关注";
    private String text = "+ 关注";
    private float radius = 20;
    private int border_color_pressed = Color.TRANSPARENT;
    private int border_color = Color.BLUE;
    private int border_width = 3;

    private Drawable drawable_pressed;
    private Drawable drawable;

    private boolean pressed = false;// true：已关注

    // 开始Loading时的回调
    private OnStartListener startListener;

    // 结束Loading时的回调
    private OnFinishListener finishListener;

    // 开始和结束Loading时的回调
    private OnLoadingListener listener;

    // Loading动画旋转周期
    private int rotateDuration = 1000;

    // 按钮缩成Loading动画的时间
    private int reduceDuration = 350;

    // Loading旋转动画控制器
    private Interpolator rotateInterpolator;

    // 按钮缩成Loading动画的控制器
    private Interpolator reduceInterpolator;

    private boolean isLoading = false;
    private int width;
    private int height;
    private Context context;


    public UpvoteButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public UpvoteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initParams(context, attrs);
        init();
    }

    public UpvoteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initParams(context, attrs);
        init();
    }

    private void init() {
        setOnClickListener(UpvoteButton.this);

        drawable = getResources().getDrawable(R.drawable.ic_upvote_before);

        drawable_pressed = getResources().getDrawable(R.drawable.ic_upvote_after);

        if (pressed) {
            setBackgroundDrawable(drawable_pressed);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0) width = getMeasuredWidth();
        if (height == 0) height = getMeasuredHeight();
    }

    private void initParams(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusButton);
        if (typedArray != null) {
            bg_color_pressed = typedArray.getColor(R.styleable.FocusButton_bg_color_pressed, Color.parseColor("#EEEEEE"));
            bg_color = typedArray.getColor(R.styleable.FocusButton_bg_color, Color.WHITE);
            text_color_pressed = typedArray.getColor(R.styleable.FocusButton_text_color_pressed, Color.DKGRAY);
            text_color = typedArray.getColor(R.styleable.FocusButton_text_color, getResources().getColor(R.color.label_pressed_text));
            text_pressed = typedArray.getString(R.styleable.FocusButton_text_pressed);
            if (text_pressed == null) {
                text_pressed = "已关注";
            }
            text = typedArray.getString(R.styleable.FocusButton_text);
            if (text == null) {
                text = "+ 关注";
            }
            radius = typedArray.getDimension(R.styleable.FocusButton_radius, 20);
            border_width = typedArray.getInteger(R.styleable.FocusButton_border_width, 3);
            border_color_pressed = typedArray.getColor(R.styleable.FocusButton_border_color_pressed, Color.TRANSPARENT);
            border_color = typedArray.getColor(R.styleable.FocusButton_border_color, getResources().getColor(R.color.label_pressed_text));
            typedArray.recycle();
        }
    }


    @Override
    public void onClick(View v) {
        if (!pressed) {
            pressed = true;
            setBackgroundDrawable(drawable_pressed);

        } else {
            pressed = false;
            setBackgroundDrawable(drawable);
        }
    }

    public void clickSuccess() {
        finishLoading();
        if (!pressed) {
            pressed = true;
            setBackgroundDrawable(drawable_pressed);

        } else {
            pressed = false;
            setBackgroundDrawable(drawable);
        }
    }

    public void clickFail() {
        finishLoading();
        if (pressed) {
            setBackgroundDrawable(drawable_pressed);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public void loading() {
//        setText("...");
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed_(boolean pressed) {
        this.pressed = pressed;
        if (pressed) {
            setBackgroundDrawable(drawable_pressed);
        } else {
            setBackgroundDrawable(drawable);
        }

    }

    /**
     * 播放按钮缩成Loading的动画
     */
    private void showStartLoadAnimation() {
        ValueAnimator animator = new ValueAnimator().ofInt(width, height);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showLoadingAnimation();
            }
        });
        animator.start();
    }

    /**
     * 播放Loading动画
     */
    private void showLoadingAnimation() {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(rotateDuration);
        animation.setInterpolator(rotateInterpolator != null ? rotateInterpolator : new LinearInterpolator());
        animation.setRepeatCount(-1);
        setBackgroundDrawable(context.getDrawable(R.drawable.circle_loading));
        if (startListener != null) {
            startListener.onStart();
        } else if (listener != null) {
            listener.onStart();
        }
        startAnimation(animation);
        isLoading = true;
    }

    /**
     * 播放Loading拉伸成按钮的动画
     */
    private void showFinishLoadAnimation() {
        ValueAnimator animator = new ValueAnimator().ofInt(height, width);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setEnabled(true);
                if (finishListener != null) {
                    finishListener.onFinish();
                } else if (listener != null) {
                    listener.onFinish();
                }
            }
        });
        animator.start();
        isLoading = false;
    }

    /**
     * 开始Loading
     */
    public void startLoading() {
        if (!isLoading) {
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 开始Loading
     *
     * @param listener Loading开始时的回调
     */
    public void startLoading(OnStartListener listener) {
        if (!isLoading) {
            this.startListener = listener;
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 结束Loading
     */
    public void finishLoading() {
        if (isLoading) {
            clearAnimation();
            showFinishLoadAnimation();
        }
    }

    /**
     * 结束Loading
     *
     * @param listener Loading结束时的回调
     */
    public void finishLoading(OnFinishListener listener) {
        if (isLoading) {
            this.finishListener = listener;
            clearAnimation();
            showFinishLoadAnimation();
        }
    }

    /**
     * 设置Loading开始和结束时的回调接口
     *
     * @param listener
     */
    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    /**
     * 设置按钮缩成Loading动画的时间
     *
     * @param reduceDuration 时间，单位毫秒
     */
    public void setReduceDuration(int reduceDuration) {
        this.reduceDuration = reduceDuration;
    }

    /**
     * 设置Loading动画旋转周期
     *
     * @param rotateDuration 旋转周期，单位毫秒
     */
    public void setRotateDuration(int rotateDuration) {
        this.rotateDuration = rotateDuration;
    }

    /**
     * 获取是否正在Loading
     *
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 设置Loading旋转动画控制器
     *
     * @param rotateInterpolator
     */
    public void setRotateInterpolator(Interpolator rotateInterpolator) {
        this.rotateInterpolator = rotateInterpolator;
    }

    /**
     * 按钮缩成Loading动画的控制器
     *
     * @param reduceInterpolator
     */
    public void setReduceInterpolator(Interpolator reduceInterpolator) {
        this.reduceInterpolator = reduceInterpolator;
    }

    /**
     * Loading开始时的回调接口
     */
    public interface OnStartListener {
        void onStart();
    }

    /**
     * Loading结束时的回调接口
     */
    public interface OnFinishListener {
        void onFinish();
    }

    /**
     * Loading开始和结束时的回调接口
     */
    public interface OnLoadingListener {
        void onStart();

        void onFinish();
    }
}
