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
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import static androidx.core.content.ContextCompat.getDrawable;

public class AnimatedView extends View {

    private boolean isLevelFalling;
    private Drawable drawable, drawable_cup_overlay;
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
        drawable_cup_overlay = getDrawable(context, R.drawable.cup_with_fade_overlay);
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
        drawable_cup_overlay.setAlpha(100);
        drawable_cup_overlay.setBounds(drawableRect);
        drawable_cup_overlay.draw(canvas);
    }

    public void startAnimation(float current, float max) {

        if (animator != null) animator.removeAllListeners();

        float currentProgressPercent = (float) newPos / (float) drawable.getIntrinsicHeight();
        float targetProgressPercent = current / max;

        if(isLevelFalling || targetProgressPercent > currentProgressPercent) {

            // isLevelFalling is set the first time the level is detected to have fallen.  However, the visual level
            // does not reverse course until it is detected to have fallen again.

            if(targetProgressPercent > currentProgressPercent) isLevelFalling = false;
            animator = ValueAnimator.ofFloat((float) newPos, targetProgressPercent * drawable.getIntrinsicHeight());
            animator.setDuration(200);
            animator.setInterpolator(new DecelerateInterpolator());
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
        AnimatedView.this.invalidate();
    }
}
