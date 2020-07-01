package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.home.back.bottom.IconsModel;
import com.home.back.bottom.R;
import com.home.back.bottom.activity.BillingActivity;
import com.home.back.bottom.adapter.MyColorsListAdapter;
import com.home.back.bottom.adapter.MyPlainColorsListAdapter;
import com.home.back.bottom.fragment.ButtonSettingsFragment;
import com.home.back.bottom.interfaces.ActivateOnBackPressed;
import com.home.back.bottom.interfaces.OnUpdateColor;
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.RecyclerItemClickListener;
import com.home.back.bottom.util.Util_NativeAdvanceHelper;
import com.home.back.bottom.util.Util_Share;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorDialogFragment extends DialogFragment implements Inter_OnItemClickListener, ButtonSettingsFragment.ButtonSettingsListener {

    private static final String TAG = "ColorDialogFragment";
    private ArrayList<IconsModel> icons = new ArrayList<>();
    private ArrayList<IconsModel> plainColorList = new ArrayList<>();
    private MyColorsListAdapter adapter;
    private MyPlainColorsListAdapter plainAdapter;
    int selected = -1;
    boolean proVersionUnlock = false;
    boolean isBackPressedActivated = false;
    private static ButtonSettingsFragment.PositionEnum positionEnum;
    private ButtonSettingsFragment.ButtonSettingsListener buttonSettingsListener;
    public BackPressedListener backPressedListener;
    private static OnUpdateColor onUpdateColor;

    public static ColorDialogFragment createInstance(ButtonSettingsFragment.PositionEnum mPositionEnum, OnUpdateColor onUpdateColor1) {
        Bundle bundle = new Bundle();
        ColorDialogFragment simpleDialogFragment = new ColorDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        Log.e(TAG, "createInstance: positionEnum: " + mPositionEnum);
        positionEnum = mPositionEnum;
        onUpdateColor = onUpdateColor1;
        return simpleDialogFragment;
    }


    public interface BackPressedListener {
        void activateBackPressed(Boolean sendEvents);

        void callEventRegister(ActivateOnBackPressed activateOnBackPressed);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);

    }


    @Override
    public void onStart() {
        super.onStart();
        ;
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        buttonSettingsListener = (ButtonSettingsFragment.ButtonSettingsListener) activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        backPressedListener = (BackPressedListener) context;
    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_color, null);

        if (Util_Share.isNeedToAdShow(getActivity())) {
            Util_NativeAdvanceHelper.loadSmallNativeAd(getActivity(), (FrameLayout) convertView.findViewById(R.id.fl2_adplaceholder));
        }

        View okBtn = convertView.findViewById(R.id.okBtn);
        View btnBackColor = convertView.findViewById(R.id.btnBackColor);
        View.OnClickListener dismissMe = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }

        };
        okBtn.setOnClickListener(dismissMe);
        btnBackColor.setOnClickListener(dismissMe);
        builder.setView(convertView);

        //not used
        RecyclerView colorsRecyclerView = convertView.findViewById(R.id.recyclerViewIconsColor);
        //use this
        RecyclerView plainColorsRecyclerView = convertView.findViewById(R.id.recyclerViewPlainColor);

        /*new IconsModel(R.drawable.icon_1),
                new IconsModel(R.drawable.icon_2),
                new IconsModel(R.drawable.icon_3),
                new IconsModel(R.drawable.icon_4),
                new IconsModel(R.drawable.icon_5),
                new IconsModel(R.drawable.icon_6),
                new IconsModel(R.drawable.icon_7),
                new IconsModel(R.drawable.icon_8),
                new IconsModel(R.drawable.icon_9),
                new IconsModel(R.drawable.icon_10),*/

        IconsModel[] allIcons = new IconsModel[]{
                new IconsModel(R.drawable.icon_12),
                new IconsModel(R.drawable.icon_13),
                new IconsModel(R.drawable.icon_14),
                new IconsModel(R.drawable.icon_15),
                new IconsModel(R.drawable.icon_16),
                new IconsModel(R.drawable.icon_17),
                new IconsModel(R.drawable.icon_18),
                new IconsModel(R.drawable.icon_19),
                new IconsModel(R.drawable.icon_20),
                new IconsModel(R.drawable.icon_21),
                new IconsModel(R.drawable.icon_22),
                new IconsModel(R.drawable.icon_23),
                new IconsModel(R.drawable.icon_24),
                new IconsModel(R.drawable.icon_25),
                new IconsModel(R.drawable.icon_26),
                new IconsModel(R.drawable.icon_27),
                new IconsModel(R.drawable.icon_28),
                new IconsModel(R.drawable.icon_29),
                new IconsModel(R.drawable.icon_11)
        };

        icons.addAll(Arrays.asList(allIcons));

        adapter = new MyColorsListAdapter(getContext(), icons);

        colorsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        colorsRecyclerView.setAdapter(adapter);
        colorsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                colorsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = position;
                Log.e(TAG, "onItemClick: " + position);
                IconsModel selectedColor = icons.get(position);
                selectedColor.setChecked(true);
                adapter.notifyDataSetChanged();
//                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position + 12).ordinal());
                onUpdateColor.updateColor(selectedColor.getIconResId());
                buttonSettingsListener.onRestartServiceNeeded();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        IconsModel[] plainColors = new IconsModel[]{
                new IconsModel(R.color.red_A700, false),
                new IconsModel(R.color.light_blue_A700, false),
                new IconsModel(R.color.green_A700, false),
                new IconsModel(R.color.purple_500, false),
                new IconsModel(R.color.white, false),
                new IconsModel(R.color.grey_700, false),
                new IconsModel(R.color.amber_700, false),
                new IconsModel(R.color.orange_700, false),
                new IconsModel(R.color.pink_700, false),
                new IconsModel(R.color.lime_700, false),
                new IconsModel(R.color.teal_700, false),
                new IconsModel(R.color.indigo_700, false),
                new IconsModel(R.drawable.icon_12, false),
                new IconsModel(R.drawable.icon_13, false),
                new IconsModel(R.drawable.icon_14, true),
                new IconsModel(R.drawable.icon_15, true),
                new IconsModel(R.drawable.icon_16, false),
                new IconsModel(R.drawable.icon_17, true),
                new IconsModel(R.drawable.icon_18, false),
                new IconsModel(R.drawable.icon_19, false),
                new IconsModel(R.drawable.icon_20, false),
                new IconsModel(R.drawable.icon_21, false),
                new IconsModel(R.drawable.icon_22, false),
                new IconsModel(R.drawable.icon_23, false),
                new IconsModel(R.drawable.icon_24, false),
                new IconsModel(R.drawable.icon_25, false),
                new IconsModel(R.drawable.icon_26, false),
                new IconsModel(R.drawable.icon_27, false),
                new IconsModel(R.drawable.icon_28, false),
                new IconsModel(R.drawable.icon_29, false),
                new IconsModel(R.drawable.icon_11, true)
        };

        plainColorList.addAll(Arrays.asList(plainColors));

        plainAdapter = new MyPlainColorsListAdapter(getContext(), plainColorList);
        plainColorsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        plainColorsRecyclerView.setAdapter(plainAdapter);

        plainColorsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                plainColorsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                selected = position;
                Log.e(TAG, "onItemClick COLOR: " + position);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                final IconsModel selectedPlainColor = plainColorList.get(position);
                if (selectedPlainColor.isPremium() && !checkProVersion()) {
                    /*selectedPlainColor.setChecked(true);
                    plainAdapter.notifyDataSetChanged();
                    PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position).ordinal());
                    onUpdateColor.updateColor(selectedPlainColor.getIconResId());
                    buttonSettingsListener.onRestartServiceNeeded();*/
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_pro_icon_locked, null);
                    alertDialog.setView(v);
//                    alertDialog.setCancelable(false);

                    Button unlockProButton = (Button) v.findViewById(R.id.unlock_pro_button_icon);
                    unlockProButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), BillingActivity.class));
                        }
                    });
                    isBackPressedActivated=true;
//                    if (backPressedListener != null) {
//                        backPressedListener.activateBackPressed(true);
                        final RewardedVideoAd mRewardedVideoAd;

                        final AlertDialog[] alertDialog2 = new AlertDialog[1];
                        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
                        // Use an activity context to get the rewarded video instance.
                        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
                        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                            @Override
                            public void onRewardedVideoAdLoaded() {
                                Log.e(TAG, "onRewardedVideoAdLoaded: " );
                            }

                            @Override
                            public void onRewardedVideoAdOpened() {
                                Log.e(TAG, "onRewardedVideoAdOpened: " );
                            }

                            @Override
                            public void onRewardedVideoStarted() {
                                Log.e(TAG, "onRewardedVideoStarted: " );
                            }

                            @Override
                            public void onRewardedVideoAdClosed() {
                                Log.e(TAG, "onRewardedVideoAdClosed: " );
                            }

                            @Override
                            public void onRewarded(RewardItem rewardItem) {
                                Log.e(TAG, "onRewarded: " );
                                if (alertDialog2[0]!=null){
                                    alertDialog2[0].dismiss();
                                }
                                selectedPlainColor.setChecked(true);
                                plainAdapter.notifyDataSetChanged();
                                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position).ordinal());
                                onUpdateColor.updateColor(selectedPlainColor.getIconResId());
                                buttonSettingsListener.onRestartServiceNeeded();
                            }

                            @Override
                            public void onRewardedVideoAdLeftApplication() {
                                Log.e(TAG, "onRewardedVideoAdLeftApplication: ");
                            }

                            @Override
                            public void onRewardedVideoAdFailedToLoad(int i) {
                                Log.e(TAG, "onRewardedVideoAdFailedToLoad: " );
                            }

                            @Override
                            public void onRewardedVideoCompleted() {
                                Log.e(TAG, "onRewardedVideoCompleted: " );
                            }
                        });

                        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                                new AdRequest.Builder().build());
                        /*backPressedListener.callEventRegister(new ActivateOnBackPressed() {
                            @Override
                            public void OnCallEvent() {

                            }
                        });*/
//                    } else {
//                        Log.e(TAG, "onItemClick: backPressedListener is null");
//                    }
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e(TAG, "onDismiss: " );
                            if (isBackPressedActivated){
                                isBackPressedActivated=false;
                                //watch video
                                alertDialog2[0] =new AlertDialog.Builder(getContext()).setTitle("Watch ad and continue to activate?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //load add
                                                Log.e(TAG, "onClick: showing add" );
                                                mRewardedVideoAd.show();

                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                alertDialog.dismiss();
                                            }
                                        }).show();
                            }
                        }
                    });
                    alertDialog.show();

                } else {
                    Log.e(TAG, "onItemClick: checkProVersion is pro" );
                    selectedPlainColor.setChecked(true);
                    plainAdapter.notifyDataSetChanged();
                    PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position).ordinal());
                    onUpdateColor.updateColor(selectedPlainColor.getIconResId());
                    buttonSettingsListener.onRestartServiceNeeded();

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    public boolean checkProVersion() {
        Log.e(TAG, "checkProVersion: " + positionEnum);
        proVersionUnlock = PreferencesUtils.getPref(PreferencesUtils.PREF_PRO_VERSION, false) || PreferencesUtils.getPref(PreferencesUtils.PREF_REAL_PRO_VERSION, false);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkProVersion unlocked ? => ");
        sb.append(proVersionUnlock);
        Log.d(str, sb.toString());
        /*if (positionEnum == PositionEnum.CENTER) {
            proLockedLayout.setVisibility(View.GONE);
        } else {
            proLockedLayout.setVisibility(View.VISIBLE);
        }*/
        if (proVersionUnlock) {
            Log.e(TAG, "checkProVersion: true" );
            return true;
        } else {
            Log.e(TAG, "checkProVersion: false" );
            return false;
        }
    }

    @Override
    public void onItemClickLister(View v, int position, RadioButton rd_button) {

    }

    @Override
    public void onItemClickLister(View v, int position) {
        selected = position;
    }

    @Override
    public void onAppSelectionPressed(ButtonSettingsFragment.PositionEnum positionEnum) {

    }

    @Override
    public void onRestartServiceNeeded() {

    }

    public String getPrefKey(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(ButtonSettingsFragment.PositionEnum.getPrefPrefix(positionEnum));
        sb.append(str);
        return sb.toString();
    }
}
