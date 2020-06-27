package com.home.back.bottom;

import java.io.Serializable;

public class IconsModel implements Serializable {
    int iconRedId;
    boolean isChecked;

    public IconsModel(int iconRedId) {
        this.iconRedId = iconRedId;
    }

    public int getIconRedId() {
        return iconRedId;
    }

    public void setIconRedId(int iconRedId) {
        this.iconRedId = iconRedId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
