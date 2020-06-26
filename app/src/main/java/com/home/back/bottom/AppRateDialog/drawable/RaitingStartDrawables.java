package com.home.back.bottom.AppRateDialog.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.home.back.bottom.R;


public class RaitingStartDrawables {
    public static LayerDrawable getRatingStar(Context context) {
        LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.rate_dialog_rating_bar);
        layerDrawable.setDrawableByLayerId(16908288, getEmptyStar(context));
        layerDrawable.setDrawableByLayerId(16908303, getEmptyStar(context));
        layerDrawable.setDrawableByLayerId(16908301, getFilledStar(context));
        return layerDrawable;
    }

    public static StateListDrawable getEmptyStar(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_star_border_grey_36dp);
        Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.ic_star_border_green_36dp);
        drawable2.setColorFilter(getAccentColor(context), Mode.SRC_IN);
        return new StateDrawableBuilder().setNormalDrawable(drawable).setSelectedDrawable(drawable2).setPressedDrawable(drawable2).setDisabledDrawable(drawable).build();
    }

    public static StateListDrawable getFilledStar(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_star_green_36dp);
        drawable.setColorFilter(getAccentColor(context), Mode.SRC_IN);
        return new StateDrawableBuilder().setNormalDrawable(drawable).setSelectedDrawable(drawable).setPressedDrawable(drawable).setDisabledDrawable(drawable).build();
    }

    private static int getAccentColor(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.colorAccent});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }
}
