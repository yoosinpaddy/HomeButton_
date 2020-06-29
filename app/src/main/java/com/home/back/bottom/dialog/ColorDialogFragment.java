package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.IconsModel;
import com.home.back.bottom.R;
import com.home.back.bottom.adapter.MyColorsListAdapter;
import com.home.back.bottom.adapter.MyPlainColorsListAdapter;
import com.home.back.bottom.fragment.ButtonSettingsFragment;
import com.home.back.bottom.interfaces.OnUpdateColor;
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorDialogFragment extends DialogFragment implements Inter_OnItemClickListener, ButtonSettingsFragment.ButtonSettingsListener {

    private static final String TAG = "ColorDialogFragment";
    private ArrayList<IconsModel> icons = new ArrayList<>();
    private ArrayList<IconsModel> plainColorList = new ArrayList<>();
    private MyColorsListAdapter adapter;
    private MyPlainColorsListAdapter plainAdapter;
    int selected = -1;
    private static ButtonSettingsFragment.PositionEnum positionEnum;
    private ButtonSettingsFragment.ButtonSettingsListener buttonSettingsListener;
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


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        buttonSettingsListener = (ButtonSettingsFragment.ButtonSettingsListener) activity;
    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_color, null);
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

        RecyclerView colorsRecyclerView = convertView.findViewById(R.id.recyclerViewIconsColor);
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
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position + 12).ordinal());
                onUpdateColor.updateColor(selectedColor.getIconResId());
                buttonSettingsListener.onRestartServiceNeeded();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        IconsModel[] plainColors = new IconsModel[]{
                new IconsModel(R.color.red_A700),
                new IconsModel(R.color.light_blue_A700),
                new IconsModel(R.color.green_A700),
                new IconsModel(R.color.purple_500),
                new IconsModel(R.color.white),
                new IconsModel(R.color.grey_700),
                new IconsModel(R.color.amber_700),
                new IconsModel(R.color.orange_700),
                new IconsModel(R.color.pink_700),
                new IconsModel(R.color.lime_700),
                new IconsModel(R.color.teal_700),
                new IconsModel(R.color.indigo_700)
        };

        plainColorList.addAll(Arrays.asList(plainColors));

        plainAdapter = new MyPlainColorsListAdapter(getContext(), plainColorList);
        plainColorsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        plainColorsRecyclerView.setAdapter(plainAdapter);

        plainColorsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                plainColorsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = position;
                Log.e(TAG, "onItemClick COLOR: " + position);
                IconsModel selectedPlainColor = plainColorList.get(position);
                selectedPlainColor.setChecked(true);
                plainAdapter.notifyDataSetChanged();
                PreferencesUtils.savePref(getPrefKey(PreferencesUtils.PREF_BUTTON_COLOR), ButtonSettingsFragment.ButtonColor.fromInt(position).ordinal());
                onUpdateColor.updateColor(selectedPlainColor.getIconResId());
                buttonSettingsListener.onRestartServiceNeeded();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return builder.create();
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
