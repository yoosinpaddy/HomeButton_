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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.home.back.bottom.R;
import com.home.back.bottom.activity.ActionListActivity;
import com.home.back.bottom.activity.BillingActivity;
import com.home.back.bottom.activity.MainActivity;
import com.home.back.bottom.broadcast.reciever.LockScreenAdmin;

import com.home.back.bottom.activity.EnableAccessibilityActivity;
import com.home.back.bottom.activity.EnableAdminActivity;
import com.home.back.bottom.dialog.SingleChoiceDialogFragment;
import com.home.back.bottom.dialog.SliderDialogFragment;
import com.home.back.bottom.interfaces.ActivateButton;
import com.home.back.bottom.service.AccessibilityActionService;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.ButtonPosition;
import com.home.back.bottom.util.PackageUtils;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.Util_Share;
import com.thebluealliance.spectrum.SpectrumDialog.Builder;
import com.thebluealliance.spectrum.SpectrumDialog.OnColorSelectedListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonSettingsFragment extends Fragment implements OnClickListener, SingleChoiceDialogFragment.SingleChoiceListener, OnCheckedChangeListener, ActivateButton {
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
    private PositionEnum positionEnum = PositionEnum.CENTER;
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

    @Override
    public void buttonClicked(boolean z) {
        Log.e(TAG, "buttonClicked: Inteface" );
        activationSwitch.setChecked(z);
//        onCheckedChanged(activationSwitch,z);
    }

    public enum ButtonColor {
        RED,
        BLUE,
        GREEN,
        PURPLE,
        WHITE,
        BLACK;

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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            switch (getArguments().getInt(ARG_POSITION, 1)) {
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
        }
        mainActivity=(MainActivity)getActivity();
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
        clickAppLayout.setVisibility(8);
        doubleClickAppLayout.setVisibility(8);
        longClickAppLayout.setVisibility(8);
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
            behaviorCardView.setVisibility(8);
            notificationCardView.setVisibility(8);
        }
        if (positionEnum != PositionEnum.LEFT) {
            leftMarginLayout.setVisibility(8);
        }
        if (positionEnum != PositionEnum.RIGHT) {
            rightMarginLayout.setVisibility(8);
        }
    }

    private void setupTexts() {
        Action fromId = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_CLICK), Action.NONE.getId()));
        Action fromId2 = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), Action.NONE.getId()));
        Action fromId3 = Action.fromId(PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), Action.NONE.getId()));
        clickSubtitleTextView.setText(fromId.getNameResId());
        doubleClickSubtitleTextView.setText(fromId2.getNameResId());
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
            clickAppLayout.setVisibility(0);
        } else {
            clickAppLayout.setVisibility(8);
        }
        if (PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK), 0) == 5) {
            doubleClickAppLayout.setVisibility(0);
        } else {
            doubleClickAppLayout.setVisibility(8);
        }
        if (PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK), 0) == 5) {
            longClickAppLayout.setVisibility(0);
        } else {
            longClickAppLayout.setVisibility(8);
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
            proLockedLayout.setVisibility(8);
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
            proLockedLayout.setVisibility(8);
        } else {
            proLockedLayout.setVisibility(0);
        }
    }

    private void startActionDialog(int i) {
        ArrayList arrayList = new ArrayList();
        for (Action nameResId : actions) {
            arrayList.add(getString(nameResId.getNameResId()));
        }
        actionsDialogFragment = SingleChoiceDialogFragment.createInstance(getString(R.string.actions_select), getString(R.string.ok), getString(R.string.cancel), arrayList);
        actionsDialogFragment.setTargetFragment(this, i);
        actionsDialogFragment.show(getFragmentManager(), SingleChoiceDialogFragment.TAG);
    }

    private void startSliderDialog(int i, int i2, String str, String str2) {
        sliderDialogFragment = SliderDialogFragment.createInstance(str, str2, getString(R.string.ok), getString(R.string.cancel), i2);
        sliderDialogFragment.setTargetFragment(this, i);
        sliderDialogFragment.show(getFragmentManager(), SliderDialogFragment.TAG);
    }

    private void showColorDialog() {
        new Builder(getContext()).setColors((int) R.array.picker_colors).setDismissOnColorSelected(true).setOutlineWidth(2).setSelectedColorRes(R.color.accent).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(boolean z, @ColorInt int i) {
                if (z) {

                    int color = getResources().getColor(R.color.red_A700);
                    int color2 = getResources().getColor(R.color.light_blue_A700);
                    int color3 = getResources().getColor(R.color.green_A700);
                    int color4 = getResources().getColor(R.color.purple_500);
                    int color5 = getResources().getColor(R.color.white);
                    int color6 = getResources().getColor(R.color.grey_700);
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
                    }
                    buttonSettingsListener.onRestartServiceNeeded();
                }
            }
        }).build().show(getFragmentManager(), "dialog_demo_1");
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
            startActionDialog(100);
        } else if (view == doubleClickLayout) {
//            Intent intent=new Intent(getActivity(), ActionListActivity.class);
//            intent.putExtra("clickaction","101");
//            intent.putExtra("position",""+positionEnum);
//            Log.e(TAG, "onClick: =====///2///"+positionEnum );
//            startActivity(intent);
            startActionDialog(101);
        } else if (view == longClickLayout) {
//            Intent intent=new Intent(getActivity(), ActionListActivity.class);
//            intent.putExtra("clickaction","102");
//            intent.putExtra("position",""+positionEnum);
//            Log.e(TAG, "onClick: =====///3///"+positionEnum );
//            startActivity(intent);
            startActionDialog(102);
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
            Log.e(TAG, "onCheckedChanged: "+z );
            PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE), activationSwitch.isChecked());
            setupTexts();
            buttonSettingsListener.onRestartServiceNeeded();
        }
    }

    public void onPositiveButtonPressed(SingleChoiceDialogFragment singleChoiceDialogFragment, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPositiveButtonPressed : choice : ");
        sb.append(i);
        Log.d(str, sb.toString());
        actionsDialogFragment.dismiss();
    }


    public void onNegativeButtonPressed(SingleChoiceDialogFragment singleChoiceDialogFragment) {
        Log.d(TAG, "onNegativeButtonPressed");
        actionsDialogFragment.dismiss();
    }

    public void onResume() {
        super.onResume();
        if (!Util_Share.click_action.equalsIgnoreCase("click_action")){
            if (!Util_Share.click_action.equalsIgnoreCase("")){
                if (!Util_Share.position.equalsIgnoreCase("position")){
                    if (!Util_Share.position.equalsIgnoreCase("")){
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
        Log.e(TAG, "onActivityResult: here11"+i);

        if (i == 100) {
            Log.e(TAG, "onActivityResult: here22"+i);

            if (i2 == 1) {
                Log.e(TAG, "onActivityResult: here33"+i);
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

    public String getPrefKey(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(PositionEnum.getPrefPrefix(positionEnum));
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
        }catch (Exception e){
            e.printStackTrace();
        }
        setupTexts();
        buttonSettingsListener.onRestartServiceNeeded();
    }
}
