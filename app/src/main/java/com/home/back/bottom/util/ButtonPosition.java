package com.home.back.bottom.util;

import androidx.annotation.StringRes;


import com.home.back.bottom.R;

import java.util.ArrayList;
import java.util.List;

public enum ButtonPosition {
    ON_KEYBOARD(0, R.string.button_position_behind),
    IN_FRONT_OF_KEYBOARD(1, R.string.button_position_in_front_of),
    BEHIND_KEYBOARD(2, R.string.button_position_on);
    


    private int f54id;
    @StringRes
    private int stringRes;

    public static ButtonPosition fromId(int i) {
        ButtonPosition[] values;
        for (ButtonPosition buttonPosition : values()) {
            if (buttonPosition.getId() == i) {
                return buttonPosition;
            }
        }
        return ON_KEYBOARD;
    }

    private ButtonPosition(int i, @StringRes int i2) {
        this.f54id = i;
        this.stringRes = i2;
    }

    public int getId() {
        return this.f54id;
    }

    public int getStringRes() {
        return this.stringRes;
    }

    public static List<ButtonPosition> getValues(boolean z) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ON_KEYBOARD);
        if (!z) {
            arrayList.add(IN_FRONT_OF_KEYBOARD);
        }
        arrayList.add(BEHIND_KEYBOARD);
        return arrayList;
    }
}
