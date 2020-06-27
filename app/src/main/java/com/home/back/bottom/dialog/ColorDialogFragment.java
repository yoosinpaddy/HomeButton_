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
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorDialogFragment extends DialogFragment implements Inter_OnItemClickListener {

    private static final String TAG = "ColorDialogFragment";
    ArrayList<IconsModel> icons = new ArrayList<>();
    private MyColorsListAdapter adapter;
    int selected = -1;

    public static ColorDialogFragment createInstance() {
        Bundle bundle = new Bundle();
        ColorDialogFragment simpleDialogFragment = new ColorDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        return simpleDialogFragment;
    }


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_color, null);


        IconsModel[] allIcons = new IconsModel[]{
                new IconsModel(R.drawable.icon_1),
                new IconsModel(R.drawable.icon_2),
                new IconsModel(R.drawable.icon_3),
                new IconsModel(R.drawable.icon_4),
                new IconsModel(R.drawable.icon_5),
                new IconsModel(R.drawable.icon_6),
                new IconsModel(R.drawable.icon_7),
                new IconsModel(R.drawable.icon_8),
                new IconsModel(R.drawable.icon_9),
                new IconsModel(R.drawable.icon_10),
                new IconsModel(R.drawable.icon_11),
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
                new IconsModel(R.drawable.icon_30),
                new IconsModel(R.drawable.icon_31),
                new IconsModel(R.drawable.icon_32)
        };

        icons.addAll(Arrays.asList(allIcons));

        adapter = new MyColorsListAdapter(getContext(), icons);
        RecyclerView actionsRecyclerView = convertView.findViewById(R.id.recyclerViewIconsColor);
        actionsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        actionsRecyclerView.setAdapter(adapter);

        actionsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                actionsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = position;
                Log.e(TAG, "onItemClick: " + position);
                IconsModel selectedAction = icons.get(position);
                selectedAction.setChecked(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        Log.e(TAG, "onCreateDialog: " + icons.size());

        builder.setView(convertView);
        return builder.create();
    }

    @Override
    public void onItemClickLister(View v, int position, RadioButton rd_button) {

    }

    @Override
    public void onItemClickLister(View v, int position) {
        selected = position;
    }
}
