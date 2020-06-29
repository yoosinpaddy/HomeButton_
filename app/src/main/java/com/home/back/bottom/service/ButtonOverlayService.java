package com.home.back.bottom.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.home.back.bottom.R;
import com.home.back.bottom.activity.MainActivity;
import com.home.back.bottom.broadcast.reciever.RestartServiceReceiver;
import com.home.back.bottom.fragment.ButtonSettingsFragment;
import com.home.back.bottom.helper.ViewHelper;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.ButtonPosition;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.ScreenUtils;

public class ButtonOverlayService extends Service implements OnClickListener, OnLongClickListener {
    private static final int BASE_VIBRATION_STRENGTH = 50;
    private static final int CLICK_CENTER = 10;
    private static final int CLICK_ID = 1;
    private static final int CLICK_LEFT = 11;
    private static final int CLICK_RIGHT = 12;
    private static final int DOUBLE_CLICK_ID = 2;
    public static boolean IS_ACTIVE = false;
    private static final int LONG_CLICK_ID = 3;
    public static final String NOTIFICATION_CHANNEL_ID = "mahb_channel";
    private static final int NOTIFICATION_ID = 69;
    private static final int ONCLICEVENT = 1234;
    private static final int ONCLICTIMER = 1235;
    private static final String TAG = "ButtonOverlayService";
    private static final int WAKE_UP_DELAY = 60000;
    private ButtonPosition buttonPosition;

    public boolean canClick = false;
    private boolean centerActive;
    private Button centerButton;
    private int centerButtonColor;

    public Action centerClickAction;

    public String centerClickPackage;

    public Action centerDoubleClickAction;

    public String centerDoubleClickPackage;
    private int centerHeight;
    private Action centerLongClickAction;
    private String centerLongClickPackage;
    private boolean centerVisible;
    private int centerWidth;

    public int doubleClickTimer = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    private boolean followRotation;

    public Handler handlerDoubleClick;
    private boolean leftActive;
    private Button leftButton;
    private int leftButtonColor;

    public Action leftClickAction;

    public String leftClickPackage;

    public Action leftDoubleClickAction;

    public String leftDoubleClickPackage;
    private int leftHeight;
    private Action leftLongClickAction;
    private String leftLongClickPackage;
    private int leftMargin;
    private boolean leftVisible;
    private int leftWidth;

    public SharedPreferences prefs;
    private Handler restartServiceHandler;
    private boolean rightActive;
    private Button rightButton;
    private int rightButtonColor;

    public Action rightClickAction;

    public String rightClickPackage;

    public Action rightDoubleClickAction;

    public String rightDoubleClickPackage;
    private int rightHeight;
    private Action rightLongClickAction;
    private String rightLongClickPackage;
    private int rightMargin;
    private boolean rightVisible;
    private int rightWidth;
    private int screenHeight;
    private int screenWidth;
    private boolean vibration;
    private int vibrationStrength;
    private Vibrator vibrator;
    private WindowManager windowManager;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            loadPreferences();
            init();
            centerButton = initOverlayButton(centerButtonColor, centerVisible);
            leftButton = initOverlayButton(leftButtonColor, leftVisible);
            rightButton = initOverlayButton(rightButtonColor, rightVisible);
            placeButtons();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            return super.onStartCommand(intent, i, i2);
        }
        checkServiceRunning();
        if (prefs == null) {
            loadPreferences();
        }
        if (!prefs.getBoolean(PreferencesUtils.PREF_NOTIFICATION_ENABLE, true)) {
            return 1;
        }
        runAsForeground();
        return 1;
    }

    public void onDestroy() {
        IS_ACTIVE = false;
        super.onDestroy();
        if (centerButton != null && windowManager != null) {
            removeButtons();
            centerButton = null;
            leftButton = null;
            rightButton = null;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        removeButtons();
        placeButtons();
    }

    private void loadPreferences() {
        prefs = getSharedPreferences("com.home.button.bottom", 0);
        PreferencesUtils.initPreferences(this);
        loadActions();
        centerButtonColor = prefs.getInt(PreferencesUtils.PREF_BUTTON_COLOR, 1);
        vibration = prefs.getBoolean(PreferencesUtils.PREF_VIBRATION_ENABLE, true);
        vibrationStrength = PreferencesUtils.getPref(PreferencesUtils.PREF_VIBRATION_STRENGTH, 50);
        followRotation = prefs.getBoolean(PreferencesUtils.PREF_ROTATION_ENABLE, false);
        centerVisible = prefs.getBoolean(PreferencesUtils.PREF_BUTTON_VISIBLE, false);
        centerWidth = prefs.getInt(PreferencesUtils.PREF_BUTTON_WIDTH, 40);
        centerHeight = prefs.getInt(PreferencesUtils.PREF_BUTTON_HEIGHT, 12);
        buttonPosition = ButtonPosition.fromId(PreferencesUtils.getPref(PreferencesUtils.PREF_BUTTON_POSITION, ButtonPosition.ON_KEYBOARD.getId()));
        centerActive = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.CENTER), true);
        leftActive = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.LEFT), false);
        rightActive = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.RIGHT), false);
        leftVisible = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_VISIBLE, ButtonSettingsFragment.PositionEnum.LEFT), true);
        rightVisible = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_VISIBLE, ButtonSettingsFragment.PositionEnum.RIGHT), true);
        leftButtonColor = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR, ButtonSettingsFragment.PositionEnum.LEFT), 1);
        rightButtonColor = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR, ButtonSettingsFragment.PositionEnum.RIGHT), 1);
        leftMargin = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_LEFT_MARGIN, ButtonSettingsFragment.PositionEnum.LEFT), 0);
        rightMargin = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_RIGHT_MARGIN, ButtonSettingsFragment.PositionEnum.RIGHT), 0);
        leftHeight = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_HEIGHT, ButtonSettingsFragment.PositionEnum.LEFT), 15);
        rightHeight = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_HEIGHT, ButtonSettingsFragment.PositionEnum.RIGHT), 15);
        leftWidth = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_WIDTH, ButtonSettingsFragment.PositionEnum.LEFT), 40);
        rightWidth = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_WIDTH, ButtonSettingsFragment.PositionEnum.RIGHT), 40);
    }

    private void loadActions() {
        centerClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK, ButtonSettingsFragment.PositionEnum.CENTER), Action.HOME.getId()));
        centerLongClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK, ButtonSettingsFragment.PositionEnum.CENTER), Action.NONE.getId()));
        centerDoubleClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK, ButtonSettingsFragment.PositionEnum.CENTER), Action.NONE.getId()));
        leftClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK, ButtonSettingsFragment.PositionEnum.LEFT), Action.NONE.getId()));
        leftLongClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK, ButtonSettingsFragment.PositionEnum.LEFT), Action.NONE.getId()));
        leftDoubleClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK, ButtonSettingsFragment.PositionEnum.LEFT), Action.NONE.getId()));
        rightClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK, ButtonSettingsFragment.PositionEnum.RIGHT), Action.NONE.getId()));
        rightLongClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK, ButtonSettingsFragment.PositionEnum.RIGHT), Action.NONE.getId()));
        rightDoubleClickAction = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK, ButtonSettingsFragment.PositionEnum.RIGHT), Action.NONE.getId()));
        centerClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_PKG, ButtonSettingsFragment.PositionEnum.CENTER), "");
        centerLongClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_PKG, ButtonSettingsFragment.PositionEnum.CENTER), "");
        centerDoubleClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG, ButtonSettingsFragment.PositionEnum.CENTER), "");
        leftClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_PKG, ButtonSettingsFragment.PositionEnum.LEFT), "");
        leftDoubleClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG, ButtonSettingsFragment.PositionEnum.LEFT), "");
        leftLongClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_PKG, ButtonSettingsFragment.PositionEnum.LEFT), "");
        rightClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_PKG, ButtonSettingsFragment.PositionEnum.RIGHT), "");
        rightDoubleClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG, ButtonSettingsFragment.PositionEnum.RIGHT), "");
        rightLongClickPackage = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_PKG, ButtonSettingsFragment.PositionEnum.RIGHT), "");
    }

    public String getPrefKey(String str, ButtonSettingsFragment.PositionEnum positionEnum) {
        StringBuilder sb = new StringBuilder();
        sb.append(ButtonSettingsFragment.PositionEnum.getPrefPrefix(positionEnum));
        sb.append(str);
        return sb.toString();
    }

    private void init() {
        IS_ACTIVE = true;
        vibrator = (Vibrator) getSystemService("vibrator");
        windowManager = (WindowManager) getSystemService("window");
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        if (point.x < point.y) {
            screenWidth = point.x;
            screenHeight = point.y;
        } else {
            screenWidth = point.y;
            screenHeight = point.x;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("screenWidth : ");
        sb.append(screenWidth);
        sb.append(" / screenHeight : ");
        sb.append(screenHeight);
        Log.d(str, sb.toString());
        handlerDoubleClick = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case ButtonOverlayService.ONCLICEVENT /*1234*/:
                        if (!canClick) {
                            canClick = true;
                            Message message2 = new Message();
                            message2.what = ButtonOverlayService.ONCLICTIMER;
                            message2.arg1 = message.arg1;
                            handlerDoubleClick.sendMessageDelayed(message2, (long) doubleClickTimer);
                            return;
                        }
                        canClick = false;
                        switch (message.arg1) {
                            case 10:
                                ButtonOverlayService buttonOverlayService = ButtonOverlayService.this;
                                buttonOverlayService.manageEvent(buttonOverlayService.centerDoubleClickAction, centerDoubleClickPackage);
                                return;
                            case 11:
                                ButtonOverlayService buttonOverlayService2 = ButtonOverlayService.this;
                                buttonOverlayService2.manageEvent(buttonOverlayService2.leftDoubleClickAction, leftDoubleClickPackage);
                                return;
                            case 12:
                                ButtonOverlayService buttonOverlayService3 = ButtonOverlayService.this;
                                buttonOverlayService3.manageEvent(buttonOverlayService3.rightDoubleClickAction, rightDoubleClickPackage);
                                return;
                            default:
                                return;
                        }
                    case ButtonOverlayService.ONCLICTIMER /*1235*/:
                        if (canClick) {
                            canClick = false;
                            switch (message.arg1) {
                                case 10:
                                    ButtonOverlayService buttonOverlayService4 = ButtonOverlayService.this;
                                    buttonOverlayService4.manageEvent(buttonOverlayService4.centerClickAction, centerClickPackage);
                                    return;
                                case 11:
                                    ButtonOverlayService buttonOverlayService5 = ButtonOverlayService.this;
                                    buttonOverlayService5.manageEvent(buttonOverlayService5.leftClickAction, leftClickPackage);
                                    return;
                                case 12:
                                    ButtonOverlayService buttonOverlayService6 = ButtonOverlayService.this;
                                    buttonOverlayService6.manageEvent(buttonOverlayService6.rightClickAction, rightClickPackage);
                                    return;
                                default:
                                    return;
                            }
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        };
    }

    private Button initOverlayButton(int i, boolean z) {
        Button button = new Button(this);
        if (z) {
            Log.e(TAG, "initOverlayButton: ====>" + i);
            switch (i) {
                case 0:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_red));
                    break;
                case 1:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_blue));
                    break;
                case 2:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_green));
                    break;
                case 3:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_purlple));
                    break;
                case 4:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_withe));
                    break;
                case 5:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_black));
                    break;
                case 6:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_32));
                    break;
                case 7:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_31));
                    break;
                case 8:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_8));
                    break;
                case 9:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_2));
                    break;
                case 10:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_4));
                    break;
                case 11:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_6));
                    break;
                default:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_blue));
                    break;
            }
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
        return button;
    }

    private void placeButtons() {
        if (centerActive) {
            placeOverlayButton(centerButton, followRotation, centerVisible, centerButtonColor, centerWidth, centerHeight, ButtonSettingsFragment.PositionEnum.CENTER);
        }
        if (leftActive) {
            placeOverlayButton(leftButton, followRotation, leftVisible, leftButtonColor, leftWidth, leftHeight, ButtonSettingsFragment.PositionEnum.LEFT);
        }
        if (rightActive) {
            placeOverlayButton(rightButton, followRotation, rightVisible, rightButtonColor, rightWidth, rightHeight, ButtonSettingsFragment.PositionEnum.RIGHT);
        }
    }

    private void placeOverlayButton(Button button, boolean z, boolean z2, int overlay_button_color_inner, int i2, int i3, ButtonSettingsFragment.PositionEnum positionEnum) {
        boolean z3;
        int i4;
        int i5;
        Button button2 = button;
        boolean z4 = z2;
        int overlayButtonFunction = overlay_button_color_inner;
        int i7 = i3;
        ButtonSettingsFragment.PositionEnum positionEnum2 = positionEnum;
        if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            LayoutParams layoutParams = getLayoutParams();
            if (z) {
                if (getResources().getConfiguration().orientation == 2) {
                    int i8 = screenHeight;
                    double d = (double) (i8 / 2);
                    double d2 = (double) leftMargin;
                    Double.isNaN(d2);
                    double d3 = d2 / 100.0d;
                    Double.isNaN(d);
                    i4 = (int) (d * d3);
                    double d4 = (double) (i8 / 2);
                    double d5 = (double) rightMargin;
                    Double.isNaN(d5);
                    double d6 = d5 / 100.0d;
                    Double.isNaN(d4);
                    i5 = (int) (d4 * d6);
                } else {
                    int i9 = screenWidth;
                    double d7 = (double) (i9 / 2);
                    double d8 = (double) leftMargin;
                    Double.isNaN(d8);
                    double d9 = d8 / 100.0d;
                    Double.isNaN(d7);
                    i4 = (int) (d7 * d9);
                    double d10 = (double) (i9 / 2);
                    double d11 = (double) rightMargin;
                    Double.isNaN(d11);
                    double d12 = d11 / 100.0d;
                    Double.isNaN(d10);
                    i5 = (int) (d10 * d12);
                }
                layoutParams.height = (int) ScreenUtils.convertDpToPixel((float) i7, this);
                layoutParams.width = (int) ScreenUtils.convertDpToPixel((float) (i2 * 2), this);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("params.width : ");
                sb.append(layoutParams.width);
                Log.d(str, sb.toString());
                if (positionEnum2 == ButtonSettingsFragment.PositionEnum.LEFT) {
                    layoutParams.x = i4 - layoutParams.width;
                    layoutParams.y = 0;
                    layoutParams.gravity = 83;
                } else if (positionEnum2 == ButtonSettingsFragment.PositionEnum.CENTER) {
                    layoutParams.x = 0;
                    layoutParams.y = 0;
                    layoutParams.gravity = 80;
                } else {
                    if (getResources().getConfiguration().orientation == 2) {
                        layoutParams.x = (screenHeight - layoutParams.width) - (i5 - layoutParams.width);
                    } else {
                        layoutParams.x = (screenWidth - layoutParams.width) - (i5 - layoutParams.width);
                    }
                    layoutParams.y = 0;
                    layoutParams.gravity = 83;
                }
            } else {
                int i10 = screenWidth;
                double d13 = (double) (i10 / 2);
                double d14 = (double) leftMargin;
                Double.isNaN(d14);
                double d15 = d14 / 100.0d;
                Double.isNaN(d13);
                int i11 = (int) (d13 * d15);
                double d16 = (double) (i10 / 2);
                double d17 = (double) rightMargin;
                Double.isNaN(d17);
                double d18 = d17 / 100.0d;
                Double.isNaN(d16);
                int i12 = (int) (d16 * d18);
                if (getResources().getConfiguration().orientation == 2) {
                    boolean z5 = ScreenUtils.getScreenOrientation(this) == 8;
                    layoutParams.height = (int) ScreenUtils.convertDpToPixel((float) (i2 * 2), this);
                    layoutParams.width = (int) ScreenUtils.convertDpToPixel((float) i7, this);
                    if ((positionEnum2 == ButtonSettingsFragment.PositionEnum.RIGHT && !z5) || (positionEnum2 == ButtonSettingsFragment.PositionEnum.LEFT && z5)) {
                        layoutParams.y = i11 - layoutParams.height;
                        layoutParams.x = 0;
                    } else if (positionEnum2 == ButtonSettingsFragment.PositionEnum.CENTER) {
                        layoutParams.x = 0;
                        layoutParams.y = 0;
                    } else if ((positionEnum2 == ButtonSettingsFragment.PositionEnum.LEFT && !z5) || (positionEnum2 == ButtonSettingsFragment.PositionEnum.RIGHT && z5)) {
                        layoutParams.y = (screenWidth - layoutParams.height) - (i12 - layoutParams.height);
                        layoutParams.x = 0;
                    }
                    if (z5) {
                        setImageButton(button2, 90, z4, overlayButtonFunction);
                        layoutParams.gravity = getGravity(positionEnum2, 2, true);
                    } else {
                        setImageButton(button2, -90, z4, overlayButtonFunction);
                        layoutParams.gravity = getGravity(positionEnum2, 2, false);
                    }
                } else if (getResources().getConfiguration().orientation == 1) {
                    boolean z6 = ScreenUtils.getScreenOrientation(this) == 9;
                    layoutParams.height = (int) ScreenUtils.convertDpToPixel((float) i7, this);
                    layoutParams.width = (int) ScreenUtils.convertDpToPixel((float) (i2 * 2), this);
                    if ((positionEnum2 != ButtonSettingsFragment.PositionEnum.LEFT || z6) && (positionEnum2 != ButtonSettingsFragment.PositionEnum.RIGHT || !z6)) {
                        z3 = false;
                        if (positionEnum2 == ButtonSettingsFragment.PositionEnum.CENTER) {
                            layoutParams.x = 0;
                            layoutParams.y = 0;
                        } else if ((positionEnum2 != ButtonSettingsFragment.PositionEnum.RIGHT || z6) && (positionEnum2 != ButtonSettingsFragment.PositionEnum.LEFT || !z6)) {
                            z3 = false;
                        } else {
                            layoutParams.x = (screenWidth - layoutParams.width) - (i12 - layoutParams.width);
                            z3 = false;
                            layoutParams.y = 0;
                        }
                    } else {
                        layoutParams.x = i11 - layoutParams.width;
                        z3 = false;
                        layoutParams.y = 0;
                    }
                    if (z6) {
                        setImageButton(button2, 180, z4, overlayButtonFunction);
                        layoutParams.gravity = getGravity(positionEnum2, 1, true);
                    } else {
                        setImageButton(button2, z3 ? 1 : 0, z4, overlayButtonFunction);
                        layoutParams.gravity = getGravity(positionEnum2, 1, z3);
                    }
                }
            }
            try {
                windowManager.addView(button2, layoutParams);
            } catch (Exception unused) {
                Toast.makeText(this, R.string.error_draw_permission, 0).show();
            }
            return;
        }
        Toast.makeText(this, getString(R.string.no_permission_dialog_message_toast), 1).show();
    }

    private LayoutParams getLayoutParams() {
        if (VERSION.SDK_INT >= 26) {
            LayoutParams layoutParams = new LayoutParams(-2, -2, 2038, 8519720, -3);
            if (buttonPosition == ButtonPosition.ON_KEYBOARD) {
                layoutParams.softInputMode = 16;
                return layoutParams;
            }
            layoutParams.softInputMode = 48;
            return layoutParams;
        } else if (buttonPosition == ButtonPosition.BEHIND_KEYBOARD) {
            LayoutParams layoutParams2 = new LayoutParams(-2, -2, 2007, 8519720, -3);
            return layoutParams2;
        } else if (buttonPosition == ButtonPosition.ON_KEYBOARD) {
            LayoutParams layoutParams3 = new LayoutParams(-2, -2, 2003, 131112, -3);
            layoutParams3.softInputMode = 16;
            return layoutParams3;
        } else {
            LayoutParams layoutParams4 = new LayoutParams(-2, -2, 2003, 40, -3);
            return layoutParams4;
        }
    }

    private int getGravity(ButtonSettingsFragment.PositionEnum positionEnum, int i, boolean z) {
        return positionEnum == ButtonSettingsFragment.PositionEnum.CENTER ? i == 2 ? z ? 19 : 21 : z ? 49 : 81 : positionEnum == ButtonSettingsFragment.PositionEnum.LEFT ? i == 2 ? z ? 51 : 53 : z ? 51 : 83 : i == 2 ? z ? 51 : 53 : z ? 51 : 83;
    }

    private void removeButtons() {
        try {
            windowManager.removeView(centerButton);
        } catch (IllegalArgumentException | NullPointerException e) {
            Log.w(TAG, e);
        }
        try {
            windowManager.removeView(leftButton);
        } catch (IllegalArgumentException | NullPointerException e2) {
            Log.w(TAG, e2);
        }
        try {
            windowManager.removeView(rightButton);
        } catch (IllegalArgumentException | NullPointerException e3) {
            Log.w(TAG, e3);
        }
    }

    public void onClick(View view) {
        if (vibration) {
            vibrator.vibrate((long) vibrationStrength);
        }
        if (view == centerButton) {
            if (centerDoubleClickAction != Action.NONE) {
                manageDoubleClick(10);
            } else {
                manageEvent(centerClickAction, centerClickPackage);
            }
        } else if (view == leftButton) {
            if (leftDoubleClickAction != Action.NONE) {
                manageDoubleClick(11);
            } else {
                manageEvent(leftClickAction, leftClickPackage);
            }
        } else if (view != rightButton) {
        } else {
            if (rightDoubleClickAction != Action.NONE) {
                manageDoubleClick(12);
            } else {
                manageEvent(rightClickAction, rightClickPackage);
            }
        }
    }

    private void manageDoubleClick(int i) {
        Message message = new Message();
        message.what = ONCLICEVENT;
        message.arg1 = i;
        handlerDoubleClick.sendMessage(message);
    }

    public boolean onLongClick(View view) {
        if (vibration) {
            vibrator.vibrate((long) vibrationStrength);
        }
        if (view == centerButton) {
            manageEvent(centerLongClickAction, centerLongClickPackage);
        } else if (view == leftButton) {
            manageEvent(leftLongClickAction, leftLongClickPackage);
        } else if (view == rightButton) {
            manageEvent(rightLongClickAction, rightLongClickPackage);
        }
        return true;
    }


    public void manageEvent(Action action, String str) {
        switch (action) {
            case HOME:
                ActionManager.startHome(this);
                return;
            case RECENT_APPS:
                ActionManager.startMultitask(this);
                return;
            case PULL_DOWN_NOTIF:
                ActionManager.openNotifications(this);
                return;
            case LOCK_SCREEN:
                ActionManager.lockScreen(this);
                return;
            case SETTINGS:
                ActionManager.goToSettings(this);
                return;
            case BACK:
                ActionManager.startBackEvent(this);
                return;
            case SCREENSHOT:
                ActionManager.takeScreenShot(this);
                return;
            case APPLICATION:
                ActionManager.startApplicationEvent(this, str);
                return;
            case QUICK_SETTINGS:
                ActionManager.showQuickSettings(this);
                return;
            case POWER_DIALOG:
                ActionManager.showPowerMenu(this);
                return;
            case SPLIT_SCREEN:
                ActionManager.startSplitScreen(this);
                return;
            case GOOGLE_ASSISTANT:
                ActionManager.openGoogleAssistant(this);
                return;
            case TASK_MANAGER_2X:
                ActionManager.startTaskManager2X(this);
                return;
            default:
                return;
        }
    }

    private void setImageButton(Button button, int i, boolean z, int overlaybuttonColor) {
        Log.e(TAG, "setImageButton: "+overlaybuttonColor );
        if (z) {
            switch (overlaybuttonColor) {
                case 0:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_red), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_red));
                    return;
                case 1:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_blue), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_blue));
                    return;
                case 2:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_green), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_green));
                    return;
                case 3:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_purlple), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_purlple));
                    return;
                case 4:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_withe), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_withe));
                    return;
                case 5:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.button_black), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_black));
                    return;
                case 6:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_32), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_32));
                    return;
                case 7:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_31), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_31));
                    return;
                case 8:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_8), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_8));
                    return;
                case 9:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_2), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_2));
                    return;
                case 10:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_4), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_4));
                    return;
                case 11:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_6), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_6));
                    return;

                case 12:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_12), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_12));
                    return;
                case 13:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_13), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_13));
                    return;
                case 14:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_14), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_14));
                    return;
                case 15:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_15), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_15));
                    return;
                case 16:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_16), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_16));
                    return;
                case 17:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_17), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_17));
                    return;
                case 18:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_18), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_18));
                    return;
                case 19:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_19), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_19));
                    return;
                case 20:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_20), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_20));
                    return;
                case 21:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_21), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_21));
                    return;
                case 22:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_22), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_22));
                    return;
                case 23:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_23), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_23));
                    return;
                case 24:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_24), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_24));
                    return;
                case 25:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_25), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_25));
                    return;
                case 26:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_26), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_26));
                    return;
                case 27:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_27), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_27));
                    return;
                case 28:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_28), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_28));
                    return;
                case 29:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_29), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_29));
                    return;
                case 30:
                    if (i != 0) {
                        ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_11), (float) i)));
                        return;
                    }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_11));
                    return;
                default:
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.button_green));
                    button.setBackgroundColor(getResources().getColor(R.color.button_blue));
                    return;
            }
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    private void runAsForeground() {
        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Intent intent = new Intent(this, DeactivateService.class);
        intent.putExtra(DeactivateService.EXTRA_ORDER, 0);
        PendingIntent service = PendingIntent.getService(this, 1, intent, 1073741824);
        Intent intent2 = new Intent(this, DeactivateService.class);
        intent2.putExtra(DeactivateService.EXTRA_ORDER, 1);
        PendingIntent service2 = PendingIntent.getService(this, 2, intent2, 1073741824);
        if (VERSION.SDK_INT >= 26) {
            setupNotificationChannel();
        }
        startForeground(69, new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.ic_status).setContentTitle(getString(R.string.app_name)).setContentText(getString(R.string.home_button_activated)).setContentIntent(activity).setColor(getResources().getColor(R.color.primary)).setPriority(-2).addAction(new NotificationCompat.Action(R.drawable.ic_close, getString(R.string.notification_action_deactivate), service)).addAction(new NotificationCompat.Action(R.drawable.ic_clock, getString(R.string.notification_action_deactivate_10sec), service2)).setVibrate(new long[]{0}).setOnlyAlertOnce(true).build());
    }

    private void checkServiceRunning() {
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(this, RestartServiceReceiver.class), 268435456);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        restartServiceHandler = new Handler() {
            public void handleMessage(Message message) {
                if (prefs.getBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, true) || prefs.getBoolean("left_serviceActive", false) || prefs.getBoolean("right_serviceActive", false)) {
                    alarmManager.set(3, SystemClock.elapsedRealtime() + 1000, broadcast);
                }
                sendEmptyMessageDelayed(0, 60000);
            }
        };
        restartServiceHandler.removeCallbacksAndMessages(null);
        restartServiceHandler.sendEmptyMessageDelayed(0, 60000);
    }

    @RequiresApi(api = 26)
    private void setupNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(PreferencesUtils.PREF_NOTIFICATION_ENABLE);
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.app_name), 3);
            notificationChannel.setDescription("");
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
