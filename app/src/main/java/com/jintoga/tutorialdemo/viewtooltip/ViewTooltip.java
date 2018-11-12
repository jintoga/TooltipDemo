package com.jintoga.tutorialdemo.viewtooltip;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * Created by florentchampigny on 02/06/2017.
 */

public class ViewTooltip {

    private View rootView;
    private final View view;
    private final TooltipView tooltip_view;
    private final BackgroundView backgroundView;

    private ViewTooltip(MyContext myContext, View view) {
        this.view = view;
        this.backgroundView = new BackgroundView(myContext.getContext());
        this.tooltip_view = new TooltipView(myContext.getContext());
        final NestedScrollView scrollParent = findScrollParent(view);
        if (scrollParent != null) {
            scrollParent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    tooltip_view.setTranslationY(tooltip_view.getTranslationY() - (scrollY - oldScrollY));
                }
            });
        }
    }

    private ViewTooltip(MyContext myContext, View rootView, View view) {
        this.rootView = rootView;
        this.view = view;
        this.backgroundView = new BackgroundView(myContext.getContext());
        this.tooltip_view = new TooltipView(myContext.getContext());
        final NestedScrollView scrollParent = findScrollParent(view);
        if (scrollParent != null) {
            scrollParent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    tooltip_view.setTranslationY(tooltip_view.getTranslationY() - (scrollY - oldScrollY));
                }
            });
        }
    }

    private ViewTooltip(View view) {
        this(new MyContext(getActivityContext(view.getContext())), view);
    }

    public static ViewTooltip on(final View view) {
        return new ViewTooltip(new MyContext(getActivityContext(view.getContext())), view);
    }

    public static ViewTooltip on(Fragment fragment, final View view) {
        return new ViewTooltip(new MyContext(fragment), view);
    }

    public static ViewTooltip on(Activity activity, final View view) {
        return new ViewTooltip(new MyContext(getActivityContext(activity)), view);
    }

    public static ViewTooltip on(Activity activity, final View rootView, final View view) {
        return new ViewTooltip(new MyContext(getActivityContext(activity)), rootView, view);
    }

    private NestedScrollView findScrollParent(View view) {
        if (view.getParent() == null || !(view.getParent() instanceof View)) {
            return null;
        } else if (view.getParent() instanceof NestedScrollView) {
            return ((NestedScrollView) view.getParent());
        } else {
            return findScrollParent(((View) view.getParent()));
        }
    }

    private static Activity getActivityContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public ViewTooltip position(Position position) {
        this.tooltip_view.setPosition(position);
        return this;
    }

    public ViewTooltip withShadow(boolean withShadow) {
        this.tooltip_view.setWithShadow(withShadow);
        return this;
    }

    public ViewTooltip shadowColor(@ColorInt int shadowColor) {
        this.tooltip_view.setShadowColor(shadowColor);
        return this;
    }

    public ViewTooltip customView(View customView) {
        this.tooltip_view.setCustomView(customView);
        return this;
    }

    public ViewTooltip customView(int viewId) {
        this.tooltip_view.setCustomView(((Activity) view.getContext()).findViewById(viewId));
        return this;
    }

    public ViewTooltip arrowWidth(int arrowWidth) {
        this.tooltip_view.setArrowWidth(arrowWidth);
        return this;
    }

    public ViewTooltip arrowHeight(int arrowHeight) {
        this.tooltip_view.setArrowHeight(arrowHeight);
        return this;
    }

    public ViewTooltip arrowSourceMargin(int arrowSourceMargin) {
        this.tooltip_view.setArrowSourceMargin(arrowSourceMargin);
        return this;
    }

    public ViewTooltip arrowTargetMargin(int arrowTargetMargin) {
        this.tooltip_view.setArrowTargetMargin(arrowTargetMargin);
        return this;
    }

    public ViewTooltip align(ALIGN align) {
        this.tooltip_view.setAlign(align);
        return this;
    }

    public TooltipView show() {
        final Context activityContext = tooltip_view.getContext();
        if (activityContext instanceof Activity) {
            final ViewGroup decorView = rootView != null ?
                    (ViewGroup) rootView :
                    (ViewGroup) ((Activity) activityContext).getWindow().getDecorView();


            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);

                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    rect.left = location[0];

                    decorView.addView(backgroundView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    decorView.addView(tooltip_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    tooltip_view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            tooltip_view.setup(rect, decorView.getWidth());

                            if (backgroundView.isClickBackgroundToHide()) {
                                setBackgroundViewClickListener();
                            }

                            tooltip_view.getViewTreeObserver().removeOnPreDrawListener(this);

                            return false;
                        }
                    });
                }
            }, 100);
        }
        return tooltip_view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBackgroundViewClickListener() {
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        tooltip_view.remove();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void close() {
        tooltip_view.close();
    }

    public ViewTooltip duration(long duration) {
        this.tooltip_view.setDuration(duration);
        return this;
    }

    public ViewTooltip color(int color) {
        this.tooltip_view.setColor(color);
        return this;
    }

    public ViewTooltip color(Paint paint) {
        this.tooltip_view.setPaint(paint);
        return this;
    }

    public ViewTooltip onDisplay(ListenerDisplay listener) {
        this.tooltip_view.setListenerDisplay(listener);
        return this;
    }

    public ViewTooltip onHide(ListenerHide listener) {
        this.tooltip_view.setListenerHide(listener);
        return this;
    }

    public ViewTooltip animation(TooltipAnimation tooltipAnimation) {
        this.tooltip_view.setTooltipAnimation(tooltipAnimation);
        return this;
    }

    public ViewTooltip text(String text) {
        this.tooltip_view.setText(text);
        return this;
    }

    public ViewTooltip text(@StringRes int text) {
        this.tooltip_view.setText(text);
        return this;
    }

    public ViewTooltip corner(int corner) {
        this.tooltip_view.setCorner(corner);
        return this;
    }

    public ViewTooltip textColor(int textColor) {
        this.tooltip_view.setTextColor(textColor);
        return this;
    }

    public ViewTooltip textTypeFace(Typeface typeface) {
        this.tooltip_view.setTextTypeFace(typeface);
        return this;
    }

    public ViewTooltip textSize(int unit, float textSize) {
        this.tooltip_view.setTextSize(unit, textSize);
        return this;
    }

    public ViewTooltip setTextGravity(int textGravity) {
        this.tooltip_view.setTextGravity(textGravity);
        return this;
    }

    public ViewTooltip clickToHide(boolean clickToHide) {
        this.tooltip_view.setClickToHide(clickToHide);
        return this;
    }

    public ViewTooltip autoHide(boolean autoHide, long duration) {
        this.tooltip_view.setAutoHide(autoHide);
        this.tooltip_view.setDuration(duration);
        return this;
    }

    public ViewTooltip distanceWithView(int distance) {
        this.tooltip_view.setDistanceWithView(distance);
        return this;
    }

    public ViewTooltip border(int color, float width) {
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(color);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(width);
        this.tooltip_view.setBorderPaint(borderPaint);
        return this;
    }

    public ViewTooltip backgroundColor(int color) {
        this.backgroundView.setColor(color);
        return this;
    }

    public ViewTooltip extraMarginLeftRight(int extraMarginLeftRight) {
        this.tooltip_view.setExtraMarginLeftRight(extraMarginLeftRight);
        return this;
    }

    public ViewTooltip backGroundClickToHide(boolean clickToHide) {
        this.backgroundView.setClickBackgroundToHide(clickToHide);
        return this;
    }

    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    }

    public enum ALIGN {
        START,
        CENTER,
        END
    }

    public interface TooltipAnimation {
        void animateEnter(View view, Animator.AnimatorListener animatorListener);

        void animateExit(View view, Animator.AnimatorListener animatorListener);
    }

    public interface ListenerDisplay {
        void onDisplay(View view);
    }

    public interface ListenerHide {
        void onHide(View view);
    }


    public static class MyContext {
        private Fragment fragment;
        private Context context;
        private Activity activity;

        public MyContext(Activity activity) {
            this.activity = activity;
        }

        public MyContext(Fragment fragment) {
            this.fragment = fragment;
        }

        public MyContext(Context context) {
            this.context = context;
        }

        public Context getContext() {
            if (activity != null) {
                return activity;
            } else {
                return ((Context) fragment.getActivity());
            }
        }

        public Activity getActivity() {
            if (activity != null) {
                return activity;
            } else {
                return fragment.getActivity();
            }
        }


        public Window getWindow() {
            if (activity != null) {
                return activity.getWindow();
            } else {
                if (fragment instanceof DialogFragment) {
                    return ((DialogFragment) fragment).getDialog().getWindow();
                }
                return fragment.getActivity().getWindow();
            }
        }
    }

}
