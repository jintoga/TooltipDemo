package com.jintoga.tutorialdemo.viewtooltip;

import android.animation.Animator;
import android.view.View;

public class FadeTooltipAnimation implements ViewTooltip.TooltipAnimation {

    private long fadeDuration = 350;

    public FadeTooltipAnimation() {
    }

    public FadeTooltipAnimation(long fadeDuration) {
        this.fadeDuration = fadeDuration;
    }

    @Override
    public void animateEnter(View view, Animator.AnimatorListener animatorListener) {
        view.setAlpha(0);
        view.animate().alpha(1).setDuration(fadeDuration).setListener(animatorListener);
    }

    @Override
    public void animateExit(View view, Animator.AnimatorListener animatorListener) {
        view.animate().alpha(0).setDuration(fadeDuration).setListener(animatorListener);
    }
}
