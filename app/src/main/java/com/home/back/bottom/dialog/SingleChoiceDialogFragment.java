package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.home.back.bottom.R;
import com.home.back.bottom.util.Util_NativeAdvanceHelper;
import com.home.back.bottom.util.Util_Share;

import java.util.ArrayList;
import java.util.List;

public class SingleChoiceDialogFragment extends DialogFragment {
    private static final String ARG_CANCEL = "ARG_CANCEL";
    private static final String ARG_ITEMS = "ARG_ITEMS";
    private static final String ARG_OK = "ARG_OK";
    private static final String ARG_TITLE = "ARG_TITLE";
    public static final String EXTRA_CHOICE = "EXTRA_CHOICE";
    public static final int SINGLE_CHOICE_DIALOG_CODE = 888;
    public static final int SINGLE_CHOICE__DIALOG_OK_RESPONSE = 1;
    public static final String TAG = "SingleChoiceDialogFragment";
    private String cancelButton;

    public int choice = -1;
    private List<String> items;
    private String okButton;

    public SingleChoiceListener singleChoiceListener;
    private String title;

    public interface SingleChoiceListener {
        void onNegativeButtonPressed(SingleChoiceDialogFragment singleChoiceDialogFragment);

        void onPositiveButtonPressed(SingleChoiceDialogFragment singleChoiceDialogFragment, int i);
    }

    public static SingleChoiceDialogFragment createInstance(String str, String str2, String str3, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, str);
        bundle.putString(ARG_OK, str2);
        bundle.putString(ARG_CANCEL, str3);
        bundle.putStringArrayList(ARG_ITEMS, arrayList);
        SingleChoiceDialogFragment singleChoiceDialogFragment = new SingleChoiceDialogFragment();
        singleChoiceDialogFragment.setArguments(bundle);
        return singleChoiceDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.okButton = getArguments().getString(ARG_OK);
            this.cancelButton = getArguments().getString(ARG_CANCEL);
            this.items = getArguments().getStringArrayList(ARG_ITEMS);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SingleChoiceListener) {
            this.singleChoiceListener = (SingleChoiceListener) activity;
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Builder builder = new Builder(getActivity());
        String str = this.title;
        if (str != null) {
            builder.setTitle(str);
        }
        String str2 = this.cancelButton;
        if (str2 != null) {
            builder.setNegativeButton(str2, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (SingleChoiceDialogFragment.this.singleChoiceListener != null) {
                        SingleChoiceDialogFragment.this.singleChoiceListener.onNegativeButtonPressed(SingleChoiceDialogFragment.this);
                    }
                }
            });
        }
        String str3 = this.okButton;
        if (str3 != null) {
            builder.setPositiveButton(str3, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (SingleChoiceDialogFragment.this.singleChoiceListener != null) {
                        SingleChoiceListener access$000 = SingleChoiceDialogFragment.this.singleChoiceListener;
                        SingleChoiceDialogFragment singleChoiceDialogFragment = SingleChoiceDialogFragment.this;
                        access$000.onPositiveButtonPressed(singleChoiceDialogFragment, singleChoiceDialogFragment.choice);
                    }
                    if (SingleChoiceDialogFragment.this.getTargetFragment() != null) {
                        Intent intent = new Intent();
                        intent.putExtra(SingleChoiceDialogFragment.EXTRA_CHOICE, SingleChoiceDialogFragment.this.choice);
                        SingleChoiceDialogFragment.this.getTargetFragment().onActivityResult(SingleChoiceDialogFragment.this.getTargetRequestCode(), 1, intent);
                    }
                }
            });
        }


        List<String> list = this.items;
        builder.setSingleChoiceItems((CharSequence[]) list.toArray(new String[list.size()]), -1, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SingleChoiceDialogFragment.this.choice = i;
            }
        });
        return builder.create();
    }
}
