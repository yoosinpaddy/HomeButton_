package com.home.back.bottom.fragment;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.home.back.bottom.R;
import com.home.back.bottom.activity.BillingActivity;
import com.home.back.bottom.activity.EnableAccessibilityActivity;
import com.home.back.bottom.activity.EnableAdminActivity;
import com.home.back.bottom.activity.MainActivity;
import com.home.back.bottom.broadcast.reciever.LockScreenAdmin;
import com.home.back.bottom.dialog.ActionDialogFragment;
import com.home.back.bottom.dialog.ColorDialogFragment;
import com.home.back.bottom.dialog.SingleChoiceDialogFragment;
import com.home.back.bottom.dialog.SliderDialogFragment;
import com.home.back.bottom.interfaces.ActivateButton;
import com.home.back.bottom.interfaces.OnUpdateColor;
import com.home.back.bottom.service.AccessibilityActionService;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.ButtonPosition;
import com.home.back.bottom.util.PackageUtils;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.Util_Share;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonSettingsFragment extends Fragment implements OnClickListener, ActionDialogFragment.SingleChoiceListener, OnCheckedChangeListener, ActivateButton, OnUpdateColor {
    private static final String ARG_POSITION = "ARG_POSITION";
    private static final int BASE_VIBRATION_STRENGTH = 50;
    private static final int BUTTON_HEIGHT_REQUEST = 200;
    private static final int BUTTON_WIDTH_REQUEST = 201;
    private static final int CLICK_CHOICE_REQUEST = 100;
    private static final int DOUBLE_CLICK_CHOICE_REQUEST = 101;
    private static final int LEFT_MARGIN_REQUEST = 202;
    private static final int LONG_CLICK_CHOICE_REQUEST = 102;
    private static final int REQUEST_BUTTON_POSITION = 4641;
    private static final int RIGHT_MARGIN_REQUEST = 203;
    public static final String TAG = "ButtonSettingsFragment";
    public static int F_ = -1;
    public static int S_ = -1;
    public static int T_ = -1;
    private static final int VIBRATION_STRENGTH_REQUEST = 204;
    private List<Action> actions;
    private SingleChoiceDialogFragment actionsDialogFragment;
    private SwitchCompat activationSwitch;
    private TextView activationTextView;
    private CardView behaviorCardView;
    private RelativeLayout buttonColorLayout;
    private RelativeLayout buttonHeightLayout;
    private TextView buttonHeightSubtitleTextView;
    private CheckBox buttonInvisibleCheckBox;
    private RelativeLayout buttonInvisibleLayout;
    private TextView buttonPositionTextView;

    public ButtonSettingsListener buttonSettingsListener;
    private RelativeLayout buttonWidthLayout;
    private TextView buttonWidthSubtitleTextView;
    private ImageView clickAppImageView;
    private RelativeLayout clickAppLayout;
    private TextView clickAppTextView;
    private LinearLayout clickLayout;
    private TextView clickSubtitleTextView;

    public ImageView colorSelectedImageView;
    private ImageView doubleClickAppImageView;
    private RelativeLayout doubleClickAppLayout;
    private TextView doubleClickAppTextView;
    private LinearLayout doubleClickLayout;
    private TextView doubleClickSubtitleTextView;
    private RelativeLayout keyboardLayout;
    private RelativeLayout leftMarginLayout;
    private TextView leftMarginSubtitleTextView;
    private ImageView longClickAppImageView;
    private RelativeLayout longClickAppLayout;
    private TextView longClickAppTextView;
    private LinearLayout longClickLayout;
    private TextView longClickSubtitleTextView;
    private CardView notificationCardView;
    private CheckBox notificationEnableCheckBox;
    private RelativeLayout notificationLayout;
    public PositionEnum positionEnum = PositionEnum.CENTER;
    private RelativeLayout proLockedLayout;
    private boolean proVersionUnlock;
    private RelativeLayout rightMarginLayout;
    private TextView rightMarginSubtitleTextView;
    private View rootView;
    private CheckBox rotationCheckBox;
    private RelativeLayout rotationLayout;
    private Button selectAppClickButton;
    private ClickEnum selectAppClickEnum = ClickEnum.CLICK;
    private Button selectAppDoubleClickButton;
    private Button selectAppLongClickButton;
    private SliderDialogFragment sliderDialogFragment;
    private Button unlockProButton;
    private CheckBox vibrationCheckBox;
    private RelativeLayout vibrationLayout;
    private RelativeLayout vibrationStrengthLayout;
    private TextView vibrationStrengthTextView;
    private MainActivity mainActivity;
    private OnUpdateColor onUpdateColor;
    private int myCurrentPosition=-1;

    @Override
    public void buttonClicked(boolean z) {
        Log.e(TAG, "onCheckedChanged " + positionEnum);
        activationSwitch.setChecked(z);
//        onCheckedChanged(activationSwitch,z);
    }

    @Override
    public void updateColor(int i) {
        Log.e(TAG, "updateColor: " + i);
        int color = R.color.red_A700;
        int color2 = R.color.light_blue_A700;
        int color3 = R.color.green_A700;
        int color4 = R.color.purple_500;
        int color5 = R.color.white;
        int color6 = R.color.grey_700;
        int amber = R.color.amber_700;
        int orange = R.color.orange_700;
        int pink = R.color.pink_700;
        int lime = R.color.lime_700;
        int teal = R.color.teal_700;
        int indigo = R.color.indigo_700;
        int icon_11 = R.drawable.icon_11;
        int icon_12 = R.drawable.icon_12;
        int icon_13 = R.drawable.icon_13;
        int icon_14 = R.drawable.icon_14;
        int icon_15 = R.drawable.icon_15;
        int icon_16 = R.drawable.icon_16;
        int icon_17 = R.drawable.icon_17;
        int icon_18 = R.drawable.icon_18;
        int icon_19 = R.drawable.icon_19;
        int icon_20 = R.drawable.icon_20;
        int icon_21 = R.drawable.icon_21;
        int icon_22 = R.drawable.icon_22;
        int icon_23 = R.drawable.icon_23;
        int icon_24 = R.drawable.icon_24;
        int icon_25 = R.drawable.icon_25;
        int icon_26 = R.drawable.icon_26;
        int icon_27 = R.drawable.icon_27;
        int icon_28 = R.drawable.icon_28;
        int icon_29 = R.drawable.icon_29;


        if (i == color) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_red));
        } else if (i == color2) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_blue));
        } else if (i == color3) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_green));
        } else if (i == color4) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_purple));
        } else if (i == color5) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_white));
        } else if (i == color6) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_black));
        } else if (i == amber) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_amber));
        } else if (i == orange) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_orange));
        } else if (i == pink) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_pink));
        } else if (i == lime) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_lime));
        } else if (i == teal) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_teal));
        } else if (i == indigo) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_indigo));
        } else if (i == icon_11) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_11));
        } else if (i == icon_12) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_12));
        } else if (i == icon_13) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_13));
        } else if (i == icon_14) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_14));
        } else if (i == icon_15) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_15));
        } else if (i == icon_16) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_16));
        } else if (i == icon_17) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_17));
        } else if (i == icon_18) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_18));
        } else if (i == icon_19) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_19));
        } else if (i == icon_20) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_20));
        } else if (i == icon_21) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_21));
        } else if (i == icon_22) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_22));
        } else if (i == icon_23) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_23));
        } else if (i == icon_24) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_24));
        } else if (i == icon_25) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_25));
        } else if (i == icon_26) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_26));
        } else if (i == icon_27) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_27));
        } else if (i == icon_28) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_28));
        } else if (i == icon_29) {
            colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_29));
        } else {
            Toast.makeText(mainActivity, "Color not given", Toast.LENGTH_SHORT).show();
        }
    }

    public enum ButtonColor {
        RED,
        BLUE,
        GREEN,
        PURPLE,
        WHITE,
        BLACK,
        AMBER,
        ORANGE,
        PINK,
        LIME,
        TEAL,
        INDIGO,
        icon_12,
        icon_13,
        icon_14,
        icon_15,
        icon_16,
        icon_17,
        icon_18,
        icon_19,
        icon_20,
        icon_21,
        icon_22,
        icon_23,
        icon_24,
        icon_25,
        icon_26,
        icon_27,
        icon_28,
        icon_29,
        icon_11;

                /*case 11:
                        if (i != 0) {
            ViewHelper.setBackground(button, new BitmapDrawable(getResources(), ViewHelper.rotateBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_6), (float) i)));
            return;
        }
                    ViewHelper.setBackground(button, ContextCompat.getDrawable(this, R.drawable.icon_6));
                    return;*/
        public static ButtonColor fromInt(int i) {
            switch (i) {
                case 0:
                    return RED;
                case 1:
                    return BLUE;
                case 2:
                    return GREEN;
                case 3:
                    return PURPLE;
                case 4:
                    return WHITE;
                case 5:
                    return BLACK;
                case 6:
                    return AMBER;
                case 7:
                    return ORANGE;
                case 8:
                    return PINK;
                case 9:
                    return LIME;
                case 10:
                    return TEAL;
                case 11:
                    return INDIGO;
                case 12:
                    return icon_12;
                case 13:
                    return icon_13;
                case 14:
                    return icon_14;
                case 15:
                    return icon_15;
                case 16:
                    return icon_16;
                case 17:
                    return icon_17;
                case 18:
                    return icon_18;
                case 19:
                    return icon_19;
                case 20:
                    return icon_20;
                case 21:
                    return icon_21;
                case 22:
                    return icon_22;
                case 23:
                    return icon_23;
                case 24:
                    return icon_24;
                case 25:
                    return icon_25;
                case 26:
                    return icon_26;
                case 27:
                    return icon_27;
                case 28:
                    return icon_28;
                case 29:
                    return icon_29;
                case 30:
                    return icon_11;
                default:
                    return RED;
            }
        }
    }

    public interface ButtonSettingsListener {
        void onAppSelectionPressed(PositionEnum positionEnum);

        void onRestartServiceNeeded();
    }

    public enum ClickEnum {
        CLICK,
        DOUBLE_CLICK,
        LONG_CLICK
    }

    public enum PositionEnum {
        LEFT,
        RIGHT,
        CENTER;

        public static String getPrefPrefix(PositionEnum positionEnum) {
            switch (positionEnum) {
                case LEFT:
                    return "left_";
                case CENTER:
                    return "";
                case RIGHT:
                    return "right_";
                default:
                    return "";
            }
        }
    }

    public static ButtonSettingsFragment newInstance(int i) {
        ButtonSettingsFragment buttonSettingsFragment = new ButtonSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, i);
        buttonSettingsFragment.setArguments(bundle);
        return buttonSettingsFragment;
    }

    public static void setPositionEnum(int i) {
        /*switch (i) {
            case 0:
                positionEnum = PositionEnum.LEFT;
                break;
            case 1:
                positionEnum = PositionEnum.CENTER;
                break;
            case 2:
                positionEnum = PositionEnum.RIGHT;
                break;
        }

        Log.e(TAG, "onCreate: ButtonsFragment positionEnum: " + positionEnum);*/
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onUpdateColor = this;
        if (getArguments() != null) {
            switch (getArguments().getInt(ARG_POSITION, 1)) {
                case 0:
                    myCurrentPosition=0;
                    positionEnum = PositionEnum.LEFT;
                    break;
                case 1:
                    myCurrentPosition=1;
                    positionEnum = PositionEnum.CENTER;
                    break;
                case 2:
                    myCurrentPosition=2;
                    positionEnum = PositionEnum.RIGHT;
                    break;
            }

        }
        mainActivity = (MainActivity) getActivity();
        actions = new ArrayList(Arrays.asList(Action.values()));
        if (VERSION.SDK_INT < 21) {
            actions.remove(Action.SCREENSHOT);
            actions.remove(Action.POWER_DIALOG);
            actions.remove(Action.TASK_MANAGER_2X);
        }
        if (VERSION.SDK_INT < 17) {
            actions.remove(Action.QUICK_SETTINGS);
        }
        if (VERSION.SDK_INT < 24) {
            actions.remove(Action.SPLIT_SCREEN);
        }
//        if (mainActivity!-null && mainActivity instanceof )
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = layoutInflater.inflate(R.layout.fragment_button_settings, viewGroup, false);
        initViews(rootView);
        setupTexts();
        return rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        buttonSettingsListener = (ButtonSettingsListener) context;
    }


    private void initViews(View view) {
        clickLayout = (LinearLayout) view.findViewById(R.id.click_layout);
        doubleClickLayout = (LinearLayout) view.findViewById(R.id.double_click_layout);
        longClickLayout = (LinearLayout) view.findViewById(R.id.long_click_layout);
        clickAppLayout = (RelativeLayout) view.findViewById(R.id.select_app_click_layout);
        doubleClickAppLayout = (RelativeLayout) view.findViewById(R.id.select_app_double_click_layout);
        longClickAppLayout = (RelativeLayout) view.findViewById(R.id.select_app_long_click_layout);
        clickAppLayout.setVisibility(View.GONE);
        doubleClickAppLayout.setVisibility(View.GONE);
        longClickAppLayout.setVisibility(View.GONE);
        buttonInvisibleLayout = (RelativeLayout) view.findViewById(R.id.button_invisible_layout);
        buttonColorLayout = (RelativeLayout) view.findViewById(R.id.color_layout);
        leftMarginLayout = (RelativeLayout) view.findViewById(R.id.button_left_margin_layout);
        rightMarginLayout = (RelativeLayout) view.findViewById(R.id.button_right_margin_layout);
        buttonWidthLayout = (RelativeLayout) view.findViewById(R.id.button_width_layout);
        buttonHeightLayout = (RelativeLayout) view.findViewById(R.id.button_height_layout);
        vibrationLayout = (RelativeLayout) view.findViewById(R.id.vibration_layout);
        rotationLayout = (RelativeLayout) view.findViewById(R.id.rotation_layout);
        keyboardLayout = (RelativeLayout) view.findViewById(R.id.keyboard_layout);
        notificationLayout = (RelativeLayout) view.findViewById(R.id.notification_layout);
        proLockedLayout = (RelativeLayout) view.findViewById(R.id.pro_locked_layout);
        vibrationStrengthLayout = (RelativeLayout) view.findViewById(R.id.vibration_strength_layout);
        clickLayout.setOnClickListener(this);
        doubleClickLayout.setOnClickListener(this);
        longClickLayout.setOnClickListener(this);
        buttonInvisibleLayout.setOnClickListener(this);
        buttonColorLayout.setOnClickListener(this);
        buttonWidthLayout.setOnClickListener(this);
        buttonHeightLayout.setOnClickListener(this);
        vibrationLayout.setOnClickListener(this);
        rotationLayout.setOnClickListener(this);
        keyboardLayout.setOnClickListener(this);
        notificationLayout.setOnClickListener(this);
        leftMarginLayout.setOnClickListener(this);
        rightMarginLayout.setOnClickListener(this);
        proLockedLayout.setOnClickListener(this);
        vibrationStrengthLayout.setOnClickListener(this);
        clickSubtitleTextView = (TextView) view.findViewById(R.id.click_subtitle_textview);
        doubleClickSubtitleTextView = (TextView) view.findViewById(R.id.double_click_subtitle_textview);
        longClickSubtitleTextView = (TextView) view.findViewById(R.id.long_click_subtitle_textview);
        buttonHeightSubtitleTextView = (TextView) view.findViewById(R.id.button_height_subtitle_textview);
        buttonWidthSubtitleTextView = (TextView) view.findViewById(R.id.button_width_subtitle_textview);
        clickAppTextView = (TextView) view.findViewById(R.id.selected_app_click_textview);
        doubleClickAppTextView = (TextView) view.findViewById(R.id.selected_app_double_click_textview);
        longClickAppTextView = (TextView) view.findViewById(R.id.selected_app_long_click_textview);
        activationTextView = (TextView) view.findViewById(R.id.switch_textview);
        leftMarginSubtitleTextView = (TextView) view.findViewById(R.id.left_margin_subtitle_textview);
        rightMarginSubtitleTextView = (TextView) view.findViewById(R.id.right_margin_subtitle_textview);
        vibrationStrengthTextView = (TextView) view.findViewById(R.id.vibration_strength_subtitle_textview);
        buttonPositionTextView = (TextView) view.findViewById(R.id.button_position_subtitle);
        activationSwitch = (SwitchCompat) view.findViewById(R.id.activation_switch);
        activationSwitch.setOnCheckedChangeListener(this);
        buttonInvisibleCheckBox = (CheckBox) view.findViewById(R.id.button_invisible_checkbox);
        vibrationCheckBox = (CheckBox) view.findViewById(R.id.vibration_checkbox);
        rotationCheckBox = (CheckBox) view.findViewById(R.id.rotation_checkbox);
        notificationEnableCheckBox = (CheckBox) view.findViewById(R.id.notification_checkbox);
        selectAppClickButton = (Button) view.findViewById(R.id.select_app_click_button);
        selectAppDoubleClickButton = (Button) view.findViewById(R.id.select_app_double_click_button);
        selectAppLongClickButton = (Button) view.findViewById(R.id.select_app_long_click_button);
        unlockProButton = (Button) view.findViewById(R.id.unlock_pro_button);
        selectAppClickButton.setOnClickListener(this);
        selectAppDoubleClickButton.setOnClickListener(this);
        selectAppLongClickButton.setOnClickListener(this);
        unlockProButton.setOnClickListener(this);
        buttonInvisibleCheckBox.setOnCheckedChangeListener(this);
        vibrationCheckBox.setOnCheckedChangeListener(this);
        rotationCheckBox.setOnCheckedChangeListener(this);
        notificationEnableCheckBox.setOnCheckedChangeListener(this);
        colorSelectedImageView = (ImageView) view.findViewById(R.id.color_selected_imageview);
        clickAppImageView = (ImageView) view.findViewById(R.id.selected_app_click_imageview);
        doubleClickAppImageView = (ImageView) view.findViewById(R.id.selected_app_double_click_imageview);
        longClickAppImageView = (ImageView) view.findViewById(R.id.selected_app_long_click_imageview);
        if (positionEnum == PositionEnum.LEFT || positionEnum == PositionEnum.RIGHT) {
            behaviorCardView = (CardView) view.findViewById(R.id.behavior_cardview);
            notificationCardView = (CardView) view.findViewById(R.id.notification_cardview);
            behaviorCardView.setVisibility(View.GONE);
            notificationCardView.setVisibility(View.GONE);
        }
        if (positionEnum != PositionEnum.LEFT) {
            leftMarginLayout.setVisibility(View.GONE);
        }
        if (positionEnum != PositionEnum.RIGHT) {
            rightMarginLayout.setVisibility(View.GONE);
        }
    }

    private void setupTexts() {
        Action fromId = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK), Action.NONE.getId()));
        Action fromId2 = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), Action.NONE.getId()));
        Action fromId3 = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), Action.NONE.getId()));
        clickSubtitleTextView.setText(fromId.getNameResId());
        String a=getString(fromId.getNameResId());
        Log.e(TAG, "setupTexts: choice"+a );

        doubleClickSubtitleTextView.setText(fromId2.getNameResId());
        F_=fromId.getNameResId();
       S_=fromId2.getNameResId();
        T_=fromId3.getNameResId();
        longClickSubtitleTextView.setText(fromId3.getNameResId());
        TextView textView = leftMarginSubtitleTextView;
        StringBuilder sb = new StringBuilder();
        sb.append(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_LEFT_MARGIN), 0));
        sb.append(" %");
        textView.setText(sb.toString());
        TextView textView2 = rightMarginSubtitleTextView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_RIGHT_MARGIN), 0));
        sb2.append(" %");
        textView2.setText(sb2.toString());
        TextView textView3 = buttonWidthSubtitleTextView;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_WIDTH), 40));
        sb3.append(" %");
        textView3.setText(sb3.toString());
        TextView textView4 = buttonHeightSubtitleTextView;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_HEIGHT), 15));
        sb4.append(" %");
        textView4.setText(sb4.toString());
        if (PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK), 0) == 5) {
            clickAppLayout.setVisibility(View.VISIBLE);
        } else {
            clickAppLayout.setVisibility(View.GONE);
        }
        if (PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), 0) == 5) {
            doubleClickAppLayout.setVisibility(View.VISIBLE);
        } else {
            doubleClickAppLayout.setVisibility(View.GONE);
        }
        if (PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), 0) == 5) {
            longClickAppLayout.setVisibility(View.VISIBLE);
        } else {
            longClickAppLayout.setVisibility(View.GONE);
        }
        buttonInvisibleCheckBox.setChecked(!PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_VISIBLE), true));
        vibrationCheckBox.setChecked(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_VIBRATION_ENABLE), true));
        vibrationStrengthTextView.setText(String.valueOf(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_VIBRATION_STRENGTH), 50)));
        rotationCheckBox.setChecked(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ROTATION_ENABLE), false));
        notificationEnableCheckBox.setChecked(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_NOTIFICATION_ENABLE), true));
        buttonPositionTextView.setText(ButtonPosition.fromId(PreferencesUtils.getPref(PreferencesUtils.PREF_BUTTON_POSITION, ButtonPosition.ON_KEYBOARD.getId())).getStringRes());
        int pref = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), 1);
        switch (ButtonColor.fromInt(pref)) {
            case RED:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_red));
                break;
            case BLUE:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_blue));
                break;
            case GREEN:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_green));
                break;
            case PURPLE:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_purple));
                break;
            case WHITE:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_white));
                break;
            case BLACK:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_black));
                break;
            case AMBER:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_amber));
                break;
            case ORANGE:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_orange));
                break;
            case PINK:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_pink));
                break;
            case LIME:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_lime));
                break;
            case TEAL:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_teal));
                break;
            case INDIGO:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_indigo));
                break;
            case icon_12:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_12));
                break;
            case icon_13:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_13));
                break;
            case icon_14:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_14));
                break;
            case icon_15:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_15));
                break;
            case icon_16:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_16));
                break;
            case icon_17:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_17));
                break;
            case icon_18:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_18));
                break;
            case icon_19:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_19));
                break;
            case icon_20:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_20));
                break;
            case icon_21:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_21));
                break;
            case icon_22:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_22));
                break;
            case icon_23:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_23));
                break;
            case icon_24:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_24));
                break;
            case icon_25:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_25));
                break;
            case icon_26:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_26));
                break;
            case icon_27:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_27));
                break;
            case icon_28:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_28));
                break;
            case icon_29:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_29));
                break;
            case icon_11:
                colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_11));
                break;
        }
        clickAppTextView.setText(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_NAME), ""));
        doubleClickAppTextView.setText(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_NAME), ""));
        longClickAppTextView.setText(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_NAME), ""));
        try {
            clickAppImageView.setImageDrawable(getActivity().getPackageManager().getApplicationIcon(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_PKG), "")));
        } catch (NameNotFoundException unused) {
            Log.w(TAG, "pas de package mais po grave");
        }
        try {
            doubleClickAppImageView.setImageDrawable(getActivity().getPackageManager().getApplicationIcon(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG), "")));
        } catch (NameNotFoundException unused2) {
            Log.w(TAG, "pas de package mais po grave");
        }
        try {
            longClickAppImageView.setImageDrawable(getActivity().getPackageManager().getApplicationIcon(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_PKG), "")));
        } catch (NameNotFoundException unused3) {
            Log.w(TAG, "pas de package mais po grave");
        }
        boolean pref2 = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE), false);
        activationSwitch.setChecked(pref2);
        switch (positionEnum) {
            case LEFT:
                TextView textView5 = activationTextView;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(getString(R.string.home_left));
                sb5.append(" ");
                sb5.append(pref2 ? getString(R.string.button_on) : getString(R.string.button_off));
                textView5.setText(sb5.toString());
                break;
            case CENTER:
                TextView textView6 = activationTextView;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(getString(R.string.home_center));
                sb6.append(" ");
                sb6.append(pref2 ? getString(R.string.button_on) : getString(R.string.button_off));
                textView6.setText(sb6.toString());
                break;
            case RIGHT:
                TextView textView7 = activationTextView;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(getString(R.string.home_right));
                sb7.append(" ");
                sb7.append(pref2 ? getString(R.string.button_on) : getString(R.string.button_off));
                textView7.setText(sb7.toString());
                break;
        }
        if (positionEnum == PositionEnum.CENTER) {
            proLockedLayout.setVisibility(View.GONE);
        }
    }

    public void checkProVersion() {
        proVersionUnlock = PreferencesUtils.getPref(PreferencesUtils.PREF_PRO_VERSION, false) || PreferencesUtils.getPref(PreferencesUtils.PREF_REAL_PRO_VERSION, false);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkProVersion unlocked ? => ");
        sb.append(proVersionUnlock);
        Log.d(str, sb.toString());
        if (proVersionUnlock || positionEnum == PositionEnum.CENTER) {
            proLockedLayout.setVisibility(View.GONE);
        } else {
            proLockedLayout.setVisibility(View.VISIBLE);
        }
    }

    private void startActionDialog(int i1, int i) {
        /*ArrayList arrayList = new ArrayList();
        for (Action nameResId : actions) {
            arrayList.add(getString(nameResId.getNameResId()));
        }
        actionsDialogFragment = SingleChoiceDialogFragment.createInstance(getString(R.string.actions_select), getString(R.string.ok), getString(R.string.cancel), arrayList);
        actionsDialogFragment.setTargetFragment(this, i);
        actionsDialogFragment.show(getFragmentManager(), SingleChoiceDialogFragment.TAG);*/

        ActionDialogFragment ad = ActionDialogFragment.createInstance(getString(R.string.actions_select), getString(R.string.ok), getString(R.string.cancel),i,myCurrentPosition,i1);
//        ActionDialogFragment ad = ActionDialogFragment.createInstance(getString(R.string.actions_select), getString(R.string.ok), getString(R.string.cancel));
        ad.setTargetFragment(ButtonSettingsFragment.this,i1);
        ad.show(getFragmentManager(), ActionDialogFragment.TAG);

    }

    private void startSliderDialog(int i, int i2, String str, String str2) {
        sliderDialogFragment = SliderDialogFragment.createInstance(str, str2, getString(R.string.ok), getString(R.string.cancel), i2);
        sliderDialogFragment.setTargetFragment(this, i);
        sliderDialogFragment.show(getFragmentManager(), SliderDialogFragment.TAG);
    }

    private void showColorDialog() {

        ColorDialogFragment ad = ColorDialogFragment.createInstance(positionEnum, onUpdateColor);
        ad.show(getFragmentManager(), "dialog_demo_1");

        /*new Builder(getContext()).setColors((int) R.array.picker_colors).setDismissOnColorSelected(true).setOutlineWidth(2).setSelectedColorRes(R.color.accent).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(boolean z, @ColorInt int i) {
                if (z) {

                    int color = getResources().getColor(R.color.red_A700);
                    int color2 = getResources().getColor(R.color.light_blue_A700);
                    int color3 = getResources().getColor(R.color.green_A700);
                    int color4 = getResources().getColor(R.color.purple_500);
                    int color5 = getResources().getColor(R.color.white);
                    int color6 = getResources().getColor(R.color.grey_700);
                    int amber = getResources().getColor(R.color.amber_700);
                    int orange = getResources().getColor(R.color.orange_700);
                    int pink = getResources().getColor(R.color.pink_700);
                    int lime = getResources().getColor(R.color.lime_700);
                    int teal = getResources().getColor(R.color.teal_700);
                    int indigo = getResources().getColor(R.color.indigo_700);

                    if (i == color) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_red));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.RED.ordinal());
                    } else if (i == color2) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_blue));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.BLUE.ordinal());
                    } else if (i == color3) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_green));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.GREEN.ordinal());
                    } else if (i == color4) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_purple));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.PURPLE.ordinal());
                    } else if (i == color5) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_white));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.WHITE.ordinal());
                    } else if (i == color6) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_black));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.BLACK.ordinal());
                    } else if (i == amber) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_amber));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.AMBER.ordinal());
                    } else if (i == orange) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_orange));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.ORANGE.ordinal());
                    } else if (i == pink) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_pink));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.PINK.ordinal());
                    } else if (i == lime) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_lime));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.LIME.ordinal());
                    } else if (i == teal) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_teal));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.TEAL.ordinal());
                    } else if (i == indigo) {
                        colorSelectedImageView.setImageDrawable(getResources().getDrawable(R.drawable.disk_indigo));
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonColor.INDIGO.ordinal());
                    }
                    buttonSettingsListener.onRestartServiceNeeded();
                }
            }
        }).build().show(getFragmentManager(), "dialog_demo_1");*/
    }

    private void showVibrationStrengthDialog() {
        startSliderDialog(VIBRATION_STRENGTH_REQUEST, PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_VIBRATION_STRENGTH), 50), getString(R.string.vibration_strength_dialog_title), getString(R.string.vibration_strength_dialog_message));
    }


    public void onClick(View view) {
        if (view == clickLayout) {
//            Intent intent=new Intent(getActivity(), ActionListActivity.class);
//            intent.putExtra("clickaction","100");
//            intent.putExtra("position",""+positionEnum);
//            Log.e(TAG, "onClick: =====///1///"+positionEnum );
//            startActivity(intent);
            startActionDialog(100,F_);
        } else if (view == doubleClickLayout) {
//            Intent intent=new Intent(getActivity(), ActionListActivity.class);
//            intent.putExtra("clickaction","101");
//            intent.putExtra("position",""+positionEnum);
//            Log.e(TAG, "onClick: =====///2///"+positionEnum );
//            startActivity(intent);
            startActionDialog(101, S_);
        } else if (view == longClickLayout) {
//            Intent intent=new Intent(getActivity(), ActionListActivity.class);
//            intent.putExtra("clickaction","102");
//            intent.putExtra("position",""+positionEnum);
//            Log.e(TAG, "onClick: =====///3///"+positionEnum );
//            startActivity(intent);
            startActionDialog(102, T_);
        } else if (view == buttonInvisibleLayout) {
            CheckBox checkBox = buttonInvisibleCheckBox;
            checkBox.setChecked(!checkBox.isChecked());
        } else if (view == vibrationLayout) {
            CheckBox checkBox2 = vibrationCheckBox;
            checkBox2.setChecked(!checkBox2.isChecked());
        } else if (view == rotationLayout) {
            CheckBox checkBox3 = rotationCheckBox;
            checkBox3.setChecked(!checkBox3.isChecked());
        } else if (view == keyboardLayout) {
            manageButtonPosition();
        } else if (view == notificationLayout) {
            CheckBox checkBox4 = notificationEnableCheckBox;
            checkBox4.setChecked(!checkBox4.isChecked());
        } else if (view == buttonColorLayout) {
            showColorDialog();
        } else if (view == buttonHeightLayout) {
            startSliderDialog(200, PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_HEIGHT), 15), getString(R.string.touch_area_height), getString(R.string.touch_area_height_select));
        } else if (view == buttonWidthLayout) {
            startSliderDialog(BUTTON_WIDTH_REQUEST, PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_BUTTON_WIDTH), 40), getString(R.string.touch_area_width), getString(R.string.touch_area_width_select));
        } else if (view == leftMarginLayout) {
            startSliderDialog(LEFT_MARGIN_REQUEST, PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_LEFT_MARGIN), 0), getString(R.string.appearance_left_margin), getString(R.string.appearance_left_margin_select));
        } else if (view == rightMarginLayout) {
            startSliderDialog(RIGHT_MARGIN_REQUEST, PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_RIGHT_MARGIN), 0), getString(R.string.appearance_right_margin), getString(R.string.appearance_right_margin_select));
        } else if (view == selectAppClickButton) {
            selectAppClickEnum = ClickEnum.CLICK;
            buttonSettingsListener.onAppSelectionPressed(positionEnum);
        } else if (view == selectAppDoubleClickButton) {
            selectAppClickEnum = ClickEnum.DOUBLE_CLICK;
            buttonSettingsListener.onAppSelectionPressed(positionEnum);
        } else if (view == selectAppLongClickButton) {
            selectAppClickEnum = ClickEnum.LONG_CLICK;
            buttonSettingsListener.onAppSelectionPressed(positionEnum);
        } else if (view == unlockProButton) {
            startProActivity();
        } else if (view == vibrationStrengthLayout) {
            showVibrationStrengthDialog();
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton == buttonInvisibleCheckBox) {
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_VISIBLE), !buttonInvisibleCheckBox.isChecked());
            buttonSettingsListener.onRestartServiceNeeded();
        } else if (compoundButton == vibrationCheckBox) {
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_VIBRATION_ENABLE), vibrationCheckBox.isChecked());
            buttonSettingsListener.onRestartServiceNeeded();
        } else if (compoundButton == rotationCheckBox) {
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ROTATION_ENABLE), rotationCheckBox.isChecked());
            buttonSettingsListener.onRestartServiceNeeded();
        } else if (compoundButton == notificationEnableCheckBox) {
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_NOTIFICATION_ENABLE), notificationEnableCheckBox.isChecked());
            buttonSettingsListener.onRestartServiceNeeded();
        } else if (compoundButton == activationSwitch) {
            Log.e(TAG, "onCheckedChanged: " + z);
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE), activationSwitch.isChecked());
            setupTexts();
            buttonSettingsListener.onRestartServiceNeeded();
        }
    }

    public void onPositiveButtonPressed(ActionDialogFragment singleChoiceDialogFragment, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPositiveButtonPressed : choice : ");
        sb.append(i);
        Log.d(str, sb.toString());
        actionsDialogFragment.dismiss();
    }


    public void onNegativeButtonPressed(ActionDialogFragment singleChoiceDialogFragment) {
        Log.d(TAG, "onNegativeButtonPressed");
        actionsDialogFragment.dismiss();
    }

    public void onResume() {
        super.onResume();
        if (!Util_Share.click_action.equalsIgnoreCase("click_action")) {
            if (!Util_Share.click_action.equalsIgnoreCase("")) {
                if (!Util_Share.position.equalsIgnoreCase("position")) {
                    if (!Util_Share.position.equalsIgnoreCase("")) {
                        if (Util_Share.click_action.equalsIgnoreCase("100")) {
                            if (Util_Share.choice >= 0 && Util_Share.choice < actions.size()) {
                                Action action = (Action) actions.get(Util_Share.choice);
                                if (canAccessAction(action)) {
                                    PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK), action.getId());
                                    onActionSelected(action);
                                    return;
                                }
                                startProActivity();
                            }
                        } else if (Util_Share.click_action.equalsIgnoreCase("101")) {
                            if (Util_Share.choice >= 0 && Util_Share.choice < actions.size()) {
                                Action action2 = (Action) actions.get(Util_Share.choice);
                                if (canAccessAction(action2)) {
                                    PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), action2.getId());
                                    onActionSelected(action2);
                                    return;
                                }
                                startProActivity();
                            }
                        } else if (Util_Share.click_action.equalsIgnoreCase("102")) {
                            if (Util_Share.choice >= 0 && Util_Share.choice < actions.size()) {
                                Action action3 = (Action) actions.get(Util_Share.choice);
                                if (canAccessAction(action3)) {
                                    PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), action3.getId());
                                    onActionSelected(action3);
                                    return;
                                }
                                startProActivity();
                            }
                        }
                    }
                }
            }
        }
        checkProVersion();
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.e(TAG, "onActivityResult: here11" + i);

        if (i == 100) {
            Log.e(TAG, "onActivityResult: here22" + i);

            if (i2 == 1) {
                Log.e(TAG, "onActivityResult: here33" + i);
                int intExtra = intent.getIntExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, 0);
                if (intExtra >= 0 && intExtra < actions.size()) {
                    Action action = (Action) actions.get(intExtra);
                    if (canAccessAction(action)) {
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK), action.getId());
                        onActionSelected(action);
                        return;
                    }
                    startProActivity();
                }
            }
        } else if (i == 101) {
            if (i2 == 1) {
                int intExtra2 = intent.getIntExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, 0);
                if (intExtra2 >= 0 && intExtra2 < actions.size()) {
                    Action action2 = (Action) actions.get(intExtra2);
                    if (canAccessAction(action2)) {
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), action2.getId());
                        onActionSelected(action2);
                        return;
                    }
                    startProActivity();
                }
            }
        } else if (i == 102) {
            if (i2 == 1) {
                int intExtra3 = intent.getIntExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, 0);
                if (intExtra3 >= 0 && intExtra3 < actions.size()) {
                    Action action3 = (Action) actions.get(intExtra3);
                    if (canAccessAction(action3)) {
                        PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), action3.getId());
                        onActionSelected(action3);
                        return;
                    }
                    startProActivity();
                }
            }
        } else if (i == BUTTON_WIDTH_REQUEST) {
            dismissSliderDialog();
            if (i2 == 1) {
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_WIDTH), intent.getIntExtra(SliderDialogFragment.EXTRA_VALUE, 0));
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
            }
        } else if (i == 200) {
            dismissSliderDialog();
            if (i2 == 1) {
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_HEIGHT), intent.getIntExtra(SliderDialogFragment.EXTRA_VALUE, 0));
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
            }
        } else if (i == LEFT_MARGIN_REQUEST) {
            dismissSliderDialog();
            if (i2 == 1) {
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_LEFT_MARGIN), intent.getIntExtra(SliderDialogFragment.EXTRA_VALUE, 0));
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
            }
        } else if (i == RIGHT_MARGIN_REQUEST) {
            dismissSliderDialog();
            if (i2 == 1) {
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_RIGHT_MARGIN), intent.getIntExtra(SliderDialogFragment.EXTRA_VALUE, 0));
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
            }
        } else if (i == VIBRATION_STRENGTH_REQUEST) {
            dismissSliderDialog();
            if (i2 == 1) {
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_VIBRATION_STRENGTH), intent.getIntExtra(SliderDialogFragment.EXTRA_VALUE, 0));
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
            }
        } else if (i == REQUEST_BUTTON_POSITION) {
            saveButtonPosition(intent.getIntExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, 0));
        }
    }

    private void dismissSliderDialog() {
        SliderDialogFragment sliderDialogFragment2 = sliderDialogFragment;
        if (sliderDialogFragment2 != null) {
            sliderDialogFragment2.dismiss();
        }
    }

    private boolean canAccessAction(Action action) {
        if ((action == Action.SCREENSHOT || action == Action.LOCK_SCREEN || action == Action.QUICK_SETTINGS || action == Action.POWER_DIALOG || action == Action.SPLIT_SCREEN || action == Action.GOOGLE_ASSISTANT || action == Action.TASK_MANAGER_2X) && !PreferencesUtils.getPref(PreferencesUtils.PREF_PRO_VERSION, false) && !PreferencesUtils.getPref(PreferencesUtils.PREF_REAL_PRO_VERSION, false)) {
            return false;
        }
        return true;
    }

    private void startProActivity() {
        startActivity(new Intent(getActivity(), BillingActivity.class));
    }

    private void onActionSelected(Action action) {
        SingleChoiceDialogFragment singleChoiceDialogFragment = actionsDialogFragment;
        if (singleChoiceDialogFragment != null) {
            singleChoiceDialogFragment.dismiss();
        }
        buttonSettingsListener.onRestartServiceNeeded();
        setupTexts();
        if (action == Action.BACK) {
            if (VERSION.SDK_INT >= 16) {
                manageAccessibility();
            } else {
                Toast.makeText(getActivity(), getString(R.string.warning_version_back), 1).show();
            }
        } else if (action == Action.RECENT_APPS && VERSION.SDK_INT >= 24) {
            manageAccessibility();
        } else if (action == Action.PULL_DOWN_NOTIF && VERSION.SDK_INT >= 21 && (Build.MANUFACTURER.equals("Samsung") || Build.MANUFACTURER.equals("samsung"))) {
            manageAccessibility();
        } else if (action == Action.LOCK_SCREEN) {
            manageAdmin();
        } else if (action == Action.QUICK_SETTINGS || action == Action.POWER_DIALOG) {
            manageAccessibility();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Manufacturer : ");
        sb.append(Build.MANUFACTURER);
        Log.d(str, sb.toString());
    }

   /* public String getPrefKey(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(PositionEnum.getPrefPrefix(positionEnum));
        Log.e(TAG, "getPrefKey: positionEnum: " + positionEnum);
        sb.append(str);
        return sb.toString();
    }*/

    public String getPrefKey(String str) {
        PositionEnum pp;
        switch (myCurrentPosition){
            case 0:
                pp= PositionEnum.LEFT;
                break;
            case 1:
                pp = PositionEnum.CENTER;
                break;
            case 2:
                pp = PositionEnum.RIGHT;
                break;
            default:
                pp = PositionEnum.CENTER;
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PositionEnum.getPrefPrefix(pp));
        Log.e(TAG, "getPrefKey: positionEnum: " + pp);
        sb.append(str);
        return sb.toString();
    }

    public void setSelectedApp(ApplicationInfo applicationInfo) {
        switch (selectAppClickEnum) {
            case CLICK:
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_NAME), applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_CLICK_PKG), applicationInfo.packageName);
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
                return;
            case DOUBLE_CLICK:
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_NAME), applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG), applicationInfo.packageName);
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
                return;
            case LONG_CLICK:
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_NAME), applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_APP_LONG_CLICK_PKG), applicationInfo.packageName);
                setupTexts();
                buttonSettingsListener.onRestartServiceNeeded();
                return;
            default:
                return;
        }
    }

    private void manageAccessibility() {
        if (!PackageUtils.isAccessibilitySettingsOn(getActivity(), AccessibilityActionService.class)) {
            startActivity(new Intent(getActivity(), EnableAccessibilityActivity.class));
        }
    }

    private void manageAdmin() {
        if (!isAdminGranted()) {
            requestAdminRights();
        }
    }

    private boolean isAdminGranted() {
        return ((DevicePolicyManager) getActivity().getSystemService("device_policy")).isAdminActive(new ComponentName(getActivity(), LockScreenAdmin.class));
    }

    private void requestAdminRights() {
        startActivity(new Intent(getActivity(), EnableAdminActivity.class));
    }

    private void manageButtonPosition() {
        List<ButtonPosition> values = ButtonPosition.getValues(VERSION.SDK_INT >= 26);
        ArrayList arrayList = new ArrayList();
        for (ButtonPosition stringRes : values) {
            arrayList.add(getString(stringRes.getStringRes()));
        }
        SingleChoiceDialogFragment createInstance = SingleChoiceDialogFragment.createInstance(getString(R.string.button_position_title), getString(R.string.ok), getString(R.string.cancel), arrayList);
        createInstance.setTargetFragment(this, REQUEST_BUTTON_POSITION);
        createInstance.show(getFragmentManager(), SingleChoiceDialogFragment.TAG);
    }

    private void saveButtonPosition(int i) {
        try {
            PreferencesUtils.savePref(PreferencesUtils.PREF_BUTTON_POSITION, ((ButtonPosition) ButtonPosition.getValues(VERSION.SDK_INT >= 26).get(i)).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupTexts();
        buttonSettingsListener.onRestartServiceNeeded();
    }
}
