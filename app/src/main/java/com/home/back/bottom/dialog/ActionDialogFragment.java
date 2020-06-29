package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.adapter.MyActionListAdapter;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionDialogFragment extends DialogFragment implements Inter_OnItemClickListener {

    public static final String TAG = "ActionDialogFragment";
    ArrayList<Action> actions = new ArrayList<>();
    private MyActionListAdapter adapter;
    int selected = -1;
    private static final String ARG_CANCEL = "ARG_CANCEL";
    private static final String ARG_ITEMS = "ARG_ITEMS";
    private static final String ARG_OK = "ARG_OK";
    private static final String ARG_TITLE = "ARG_TITLE";
    public int choice = -1;
    private String title;
    private String okButton;
    private String cancelButton;
    public SingleChoiceListener singleChoiceListener;

    public interface SingleChoiceListener {
        void onNegativeButtonPressed(ActionDialogFragment singleChoiceDialogFragment);

        void onPositiveButtonPressed(ActionDialogFragment singleChoiceDialogFragment, int i);
    }
    public static ActionDialogFragment createInstance(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, str);
        bundle.putString(ARG_OK, str2);
        bundle.putString(ARG_CANCEL, str3);
        ActionDialogFragment simpleDialogFragment = new ActionDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        return simpleDialogFragment;
    }


    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.okButton = getArguments().getString(ARG_OK);
            this.cancelButton = getArguments().getString(ARG_CANCEL);
        }

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActionDialogFragment.SingleChoiceListener) {
            this.singleChoiceListener = (ActionDialogFragment.SingleChoiceListener) activity;
        }

    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_select_action, null);
        View btnOk = convertView.findViewById(R.id.btnOk);
        View btnBackAction = convertView.findViewById(R.id.btnBackAction);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActionDialogFragment.this.singleChoiceListener != null) {
                    ActionDialogFragment.SingleChoiceListener access$000 = ActionDialogFragment.this.singleChoiceListener;
                    ActionDialogFragment singleChoiceDialogFragment = ActionDialogFragment.this;
                    access$000.onPositiveButtonPressed(singleChoiceDialogFragment, singleChoiceDialogFragment.choice);
                }else {
                    Log.e(TAG, "onClick: listener is null" );
                }
                if (ActionDialogFragment.this.getTargetFragment() != null) {
                    Intent intent = new Intent();
                    intent.putExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, ActionDialogFragment.this.choice);
                    ActionDialogFragment.this.getTargetFragment().onActivityResult(ActionDialogFragment.this.getTargetRequestCode(), 1, intent);
                }else{
                    Log.e(TAG, "onClick: listener is getTargetFragment" );
                }
                dismiss();
            }
        });
        btnBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActionDialogFragment.this.singleChoiceListener != null) {
                    ActionDialogFragment.this.singleChoiceListener.onNegativeButtonPressed(ActionDialogFragment.this);
                }
                dismiss();
            }
        });

        actions = new ArrayList(Arrays.asList(Action.values()));

        /*ArrayList arrayList = new ArrayList();
        for (Action nameResId : actions) {
            arrayList.add(getString(nameResId.getNameResId()));
        }*/

        adapter = new MyActionListAdapter(getContext(), actions);
        RecyclerView actionsRecyclerView = convertView.findViewById(R.id.recyclerViewActions);
        actionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        actionsRecyclerView.setAdapter(adapter);

        actionsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                actionsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selected = position;
                Log.e(TAG, "onItemClick: " + position);
                Action selectedAction = actions.get(position);
                selectedAction.setChecked(true);
                adapter.notifyDataSetChanged();
                ActionDialogFragment.this.choice = position;
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        Log.e(TAG, "onCreateDialog: " + actions.size());

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
