package com.home.back.bottom;

import java.io.Serializable;

public class IconsModel implements Serializable {
    int iconResId;
    boolean isChecked;
    boolean isPremium;

    public IconsModel(int iconResId) {
        this.iconResId = iconResId;
    }

    public IconsModel(int iconResId, boolean isPremium) {
        this.iconResId = iconResId;
        this.isPremium = isPremium;
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

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
