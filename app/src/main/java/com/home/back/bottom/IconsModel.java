package com.home.back.bottom;

import java.io.Serializable;

public class IconsModel implements Serializable {
    int iconResId;
    boolean isChecked;

    public IconsModel(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
