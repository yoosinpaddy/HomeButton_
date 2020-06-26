package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SimpleDialogFragment extends DialogFragment {
    private static final String ARG_CANCEL = "ARG_CANCEL";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_OK = "ARG_OK";
    private static final String ARG_TITLE = "ARG_TITLE";
    public static final int SIMPLE_DIALOG_CODE = 999;
    public static final int SIMPLE_DIALOG_OK_RESPONSE = 1;
    public static final String TAG = "SimpleDialogFragment";
    private String cancelButton;
    private String message;
    private String okButton;

    public SimpleFragmentListener simpleFragmentListener;
    private String title;

    public interface SimpleFragmentListener {
        void onNegativeButtonPressed(SimpleDialogFragment simpleDialogFragment);

        void onPositiveButtonPressed(SimpleDialogFragment simpleDialogFragment);
    }

    public static SimpleDialogFragment createInstance(String str, String str2, String str3, String str4) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, str);
        bundle.putString(ARG_MESSAGE, str2);
        bundle.putString(ARG_OK, str3);
        bundle.putString(ARG_CANCEL, str4);
        SimpleDialogFragment simpleDialogFragment = new SimpleDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        return simpleDialogFragment;
    }

    public static SimpleDialogFragment createInstance(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, str);
        bundle.putString(ARG_MESSAGE, str2);
        bundle.putString(ARG_OK, str3);
        SimpleDialogFragment simpleDialogFragment = new SimpleDialogFragment();
        simpleDialogFragment.setArguments(bundle);
        return simpleDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.message = getArguments().getString(ARG_MESSAGE);
            this.okButton = getArguments().getString(ARG_OK);
            this.cancelButton = getArguments().getString(ARG_CANCEL);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SimpleFragmentListener) {
            this.simpleFragmentListener = (SimpleFragmentListener) activity;
        }
    }

    @NonNull
    public AlertDialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String str = this.title;
        if (str != null) {
            builder.setTitle(str);
        }
        String str2 = this.message;
        if (str2 != null) {
            builder.setMessage(str2);
        }
        String str3 = this.okButton;
        if (str3 != null) {
            builder.setPositiveButton(str3, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (simpleFragmentListener != null) {
                        simpleFragmentListener.onPositiveButtonPressed(SimpleDialogFragment.this);
                    }
                    if (getTargetFragment() != null) {
                        getTargetFragment().onActivityResult(SimpleDialogFragment.SIMPLE_DIALOG_CODE, 1, null);
                    }
                }
            });
        }
        String str4 = this.cancelButton;
        if (str4 != null) {
            builder.setNegativeButton(str4, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (simpleFragmentListener != null) {
                        simpleFragmentListener.onNegativeButtonPressed(SimpleDialogFragment.this);
                    }
                }
            });
        }
        return builder.create();
    }
}
