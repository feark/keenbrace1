package com.keenbrace.core.datepicker.popup;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.keenbrace.core.R;
import com.keenbrace.core.datepicker.util.LogUtils;


/**
 * 弹窗
 *
 */
public class Popup {
    private android.app.Dialog dialog;
    private FrameLayout contentLayout;

    /**
     * Instantiates a new Popup.
     *
     * @param context the context
     */
    public Popup(Context context) {
        init(context);
    }

    private void init(Context context) {
        contentLayout = new FrameLayout(context);
        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLayout.setFocusable(true);
        contentLayout.setFocusableInTouchMode(true);
        dialog = new android.app.Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Animation_Popup);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //android.util.AndroidRuntimeException: requestFeature() must be called before adding content
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(contentLayout);
    }

    /**
     * Sets animation.
     *
     * @param animRes the anim res
     */
    public void setAnimationStyle(@StyleRes int animRes) {
        Window window = dialog.getWindow();
        window.setWindowAnimations(animRes);
    }

    /**
     * Is showing boolean.
     *
     * @return the boolean
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }

    /**
     * Show.
     */
    public void show() {
        dialog.show();
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * Sets content view.
     *
     * @param view the view
     */
    public void setContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }

    /**
     * Gets content view.
     *
     * @return the content view
     */
    public View getContentView() {
        return contentLayout.getChildAt(0);
    }

    /**
     * Sets size.
     *
     * @param width  the width
     * @param height the height
     */
    public void setSize(int width, int height) {
        LogUtils.debug(String.format("will set popup width/height to: %s/%s", width, height));
        ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        contentLayout.setLayoutParams(params);
    }

    /**
     * Sets on dismiss listener.
     *
     * @param onDismissListener the on dismiss listener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
    }

}
