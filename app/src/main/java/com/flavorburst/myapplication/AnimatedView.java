package com.flavorburst.myapplication;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import static androidx.core.content.ContextCompat.getDrawable;

public class AnimatedView extends View {

    private boolean isAnimating;
    private Drawable drawable;
    private Paint fluidLevelPaint, backgroundPaint;
    private Rect drawableRect, backgroundRect, fluidLevelRect;
    ValueAnimator animator;
    private int newPos=0;

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawable = getDrawable(context, R.drawable.oval_cutout);
        drawableRect= new Rect();
        drawableRect.left = 0;
        drawableRect.top = newPos;
        drawableRect.right = drawable.getIntrinsicWidth();
        drawableRect.bottom = newPos + drawable.getIntrinsicHeight();
        backgroundPaint = new Paint(Color.BLACK);
        backgroundRect = new Rect();
        backgroundRect.left = 0;
        backgroundRect.right = drawable.getIntrinsicWidth();
        backgroundRect.top = 0;
        backgroundRect.bottom = drawable.getIntrinsicHeight();

        fluidLevelPaint = new Paint();
        fluidLevelPaint.setColor(Color.RED);
        fluidLevelRect = new Rect();
        fluidLevelRect.left = 0;
        fluidLevelRect.right = drawable.getIntrinsicWidth();
        fluidLevelRect.top = drawable.getIntrinsicHeight();
        fluidLevelRect.bottom = drawable.getIntrinsicHeight();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        fluidLevelRect.top = drawable.getIntrinsicHeight() - newPos;

        canvas.drawRect(backgroundRect, backgroundPaint);
        canvas.drawRect(fluidLevelRect, fluidLevelPaint);

        drawable.setBounds(drawableRect);
        drawable.draw(canvas);
    }

    public void startAnimation(float current, float max) {

        if (animator != null) animator.removeAllListeners();

        float currentProgressPercent = (float) newPos / (float) drawable.getIntrinsicHeight();
        float targetProgressPercent = current / max;

        if(targetProgressPercent > currentProgressPercent) {

            animator = ValueAnimator.ofFloat((float) newPos, targetProgressPercent * drawable.getIntrinsicHeight());
            animator.setDuration(100);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    newPos = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    AnimatedView.this.invalidate();
                }
            });

            animator.start();
            invalidate();
        }
    }

    public void reset() {
        newPos = 0;
    }
}
