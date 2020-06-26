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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.adapter.MyActionListAdapter;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionDialogFragment extends DialogFragment implements Inter_OnItemClickListener {

    private static final String TAG = "ActionDialogFragment";
    ArrayList<Action> actions=new ArrayList<>();
    int selected=-1;
    public static ActionDialogFragment createInstance() {
        Bundle bundle = new Bundle();
        ActionDialogFragment simpleDialogFragment = new ActionDialogFragment();
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
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_action, null);
        View v= convertView.findViewById(R.id.btnOk);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ()
            }
        });
        actions = new ArrayList(Arrays.asList(Action.values()));
        RecyclerView actionsRecyclerView = convertView.findViewById(R.id.recyclerViewActions);

        actionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        actionsRecyclerView.setAdapter(new MyActionListAdapter(getContext(), actions));
        Log.e(TAG, "onCreateDialog: "+actions.size() );

        builder.setView(convertView);
        return builder.create();
    }

    @Override
    public void onItemClickLister(View v, int position, RadioButton rd_button) {

    }

    @Override
    public void onItemClickLister(View v, int position) {
        selected=position;
    }
}
