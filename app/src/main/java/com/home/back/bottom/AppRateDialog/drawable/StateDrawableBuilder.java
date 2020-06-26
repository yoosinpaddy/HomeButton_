package com.home.back.bottom.AppRateDialog.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class StateDrawableBuilder {
    private static final int[] STATE_DISABLED = {-16842910};
    private static final int[] STATE_ENABLED = {16842910};
    private static final int[] STATE_PRESSED = {16842919};
    private static final int[] STATE_SELECTED = {16842913};
    private Drawable disabledDrawable;
    private Drawable normalDrawable;
    private Drawable pressedDrawable;
    private Drawable selectedDrawable;

    public StateDrawableBuilder setNormalDrawable(Drawable drawable) {
        this.normalDrawable = drawable;
        return this;
    }

    public StateDrawableBuilder setPressedDrawable(Drawable drawable) {
        this.pressedDrawable = drawable;
        return this;
    }

    public StateDrawableBuilder setSelectedDrawable(Drawable drawable) {
        this.selectedDrawable = drawable;
        return this;
    }

    public StateDrawableBuilder setDisabledDrawable(Drawable drawable) {
        this.disabledDrawable = drawable;
        return this;
    }

    public StateListDrawable build() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable drawable = this.selectedDrawable;
        if (drawable != null) {
            stateListDrawable.addState(STATE_SELECTED, drawable);
        }
        Drawable drawable2 = this.pressedDrawable;
        if (drawable2 != null) {
            stateListDrawable.addState(STATE_PRESSED, drawable2);
        }
        Drawable drawable3 = this.normalDrawable;
        if (drawable3 != null) {
            stateListDrawable.addState(STATE_ENABLED, drawable3);
        }
        Drawable drawable4 = this.disabledDrawable;
        if (drawable4 != null) {
            stateListDrawable.addState(STATE_DISABLED, drawable4);
        }
        return stateListDrawable;
    }
}
