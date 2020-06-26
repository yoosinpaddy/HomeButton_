package com.home.back.bottom.util;


import com.home.back.bottom.R;

public enum Action {
    NONE(0, R.string.action_none),
    HOME(1, R.string.action_home),
    RECENT_APPS(2, R.string.action_recent_apps),
    PULL_DOWN_NOTIF(3, R.string.action_notif_down),
    SETTINGS(4, R.string.action_settings),
    APPLICATION(5, R.string.action_application),
    BACK(6, R.string.action_back),
    SCREENSHOT(7, R.string.action_screen_shot),
    LOCK_SCREEN(8, R.string.action_lock_screen),
    QUICK_SETTINGS(9, R.string.action_quick_settings),
    POWER_DIALOG(10, R.string.action_power_dialog),
    SPLIT_SCREEN(11, R.string.action_split_screen),
    GOOGLE_ASSISTANT(12, R.string.action_google_assistant),
    TASK_MANAGER_2X(13, R.string.action_task_manager_2x);
    


    private int f53id;
    private int nameResId;
    private  boolean isChecked;

    private Action(int i, int i2) {
        this.f53id = i;
        this.nameResId = i2;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return this.f53id;
    }

    public int getNameResId() {
        return this.nameResId;
    }

    public static Action fromId(int i) {
        Action[] values;
        for (Action action : values()) {
            if (action.f53id == i) {
                return action;
            }
        }
        return NONE;
    }
}
