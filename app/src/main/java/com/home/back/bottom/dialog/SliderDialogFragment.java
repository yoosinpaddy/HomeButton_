package com.home.back.bottom.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.home.back.bottom.R;
import com.home.back.bottom.util.Util_Share;

import com.home.back.bottom.util.Util_NativeAdvanceHelper;

public class SliderDialogFragment extends DialogFragment implements OnClickListener {
    private static final String ARG_CANCEL = "ARG_CANCEL";
    private static final String ARG_DEFAULT_VALUE = "ARG_DEFAULT_VALUE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_OK = "ARG_OK";
    private static final String ARG_TITLE = "ARG_TITLE";
    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    public static final int SLIDER_DIALOG_CODE = 998;
    public static final int SLIDER_DIALOG_KO_RESPONSE = 0;
    public static final int SLIDER_DIALOG_OK_RESPONSE = 1;
    public static final String TAG = "SliderDialogFragment";
    private String cancelButton;
    private Button cancelTextView;
    private int defaultValue = 0;
    private String message;
    private String okButton;
    private Button okTextView;
    private View rootView;
    private SeekBar seekBar;
    private SliderFragmentListener sliderFragmentListener;
    private TextView subtitleTextView;
    private String title;
    private TextView titleTextView;

    public int value = 0;

    public TextView valueTextView;

    public interface SliderFragmentListener {
        void onNegativeButtonPressed(SliderDialogFragment sliderDialogFragment);

        void onPositiveButtonPressed(SliderDialogFragment sliderDialogFragment, int i);
    }

    public static SliderDialogFragment createInstance(String str, String str2, String str3, String str4, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, str);
        bundle.putString(ARG_MESSAGE, str2);
        bundle.putString(ARG_OK, str3);
        bundle.putString(ARG_CANCEL, str4);
        bundle.putInt(ARG_DEFAULT_VALUE, i);
        SliderDialogFragment sliderDialogFragment = new SliderDialogFragment();
        sliderDialogFragment.setArguments(bundle);
        return sliderDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.message = getArguments().getString(ARG_MESSAGE);
            this.okButton = getArguments().getString(ARG_OK);
            this.cancelButton = getArguments().getString(ARG_CANCEL);
            this.defaultValue = getArguments().getInt(ARG_DEFAULT_VALUE);
            this.value = this.defaultValue;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SliderFragmentListener) {
            this.sliderFragmentListener = (SliderFragmentListener) activity;
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SliderFragmentListener) {
            this.sliderFragmentListener = (SliderFragmentListener) context;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.dialog_slider, viewGroup, false);
        initViews(this.rootView);
        return this.rootView;
    }

    private void initViews(View view) {
        if (Util_Share.isNeedToAdShow(getActivity())) {
            Util_NativeAdvanceHelper.loadSmallNativeAd(getActivity(), (FrameLayout) view.findViewById(R.id.fl_adplaceholder));
        }
        this.titleTextView = (TextView) view.findViewById(R.id.title_textview);
        this.subtitleTextView = (TextView) view.findViewById(R.id.subtitle_textview);
        this.okTextView = (Button) view.findViewById(R.id.ok_textview);
        this.cancelTextView = (Button) view.findViewById(R.id.cancel_textview);
        this.valueTextView = (TextView) view.findViewById(R.id.seekbar_value_textview);
        this.seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        this.seekBar.setProgress(this.defaultValue);
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TextView access$000 = SliderDialogFragment.this.valueTextView;
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(" %");
                access$000.setText(sb.toString());
                SliderDialogFragment.this.value = i;
            }
        });
        this.okTextView.setOnClickListener(this);
        this.cancelTextView.setOnClickListener(this);
        String str = this.title;
        if (str != null) {
            this.titleTextView.setText(str);
        }
        String str2 = this.message;
        if (str2 != null) {
            this.subtitleTextView.setText(str2);
        }
        String str3 = this.okButton;
        if (str3 != null) {
            this.okTextView.setText(str3);
        }
        String str4 = this.cancelButton;
        if (str4 != null) {
            this.cancelTextView.setText(str4);
        }
        TextView textView = this.valueTextView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.defaultValue);
        sb.append("%");
        textView.setText(sb.toString());
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().requestFeature(1);
        return onCreateDialog;
    }

    public void onClick(View view) {
        if (view == this.okTextView) {
            SliderFragmentListener sliderFragmentListener2 = this.sliderFragmentListener;
            if (sliderFragmentListener2 != null) {
                sliderFragmentListener2.onPositiveButtonPressed(this, this.value);
            }
            if (getTargetFragment() != null) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_VALUE, this.value);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
            }
        } else if (view == this.cancelTextView) {
            SliderFragmentListener sliderFragmentListener3 = this.sliderFragmentListener;
            if (sliderFragmentListener3 != null) {
                sliderFragmentListener3.onNegativeButtonPressed(this);
            }
            if (getTargetFragment() != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
            }
        }
    }

}
