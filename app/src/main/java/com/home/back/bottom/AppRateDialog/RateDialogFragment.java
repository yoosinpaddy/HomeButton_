package com.home.back.bottom.AppRateDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.home.back.bottom.AppRateDialog.drawable.RaitingStartDrawables;
import com.home.back.bottom.AppRateDialog.utils.Constants;
import com.home.back.bottom.R;


public class RateDialogFragment extends DialogFragment implements OnRatingBarChangeListener {
    public static final String TAG = "RateDialogFragment";
    private LinearLayout buttonsLayout;
    private Button cancelButton;
    private String cancelMessage;
    private String message;
    private TextView messageTextView;
    /* access modifiers changed from: private */
    public CheckBox neverShowAgainCheckBox;
    private String neverShowAgainMessage;
    private TextView rateDescriptionTextView;
    private String title;

    public static RateDialogFragment newInstance() {
        RateDialogFragment rateDialogFragment = new RateDialogFragment();
        rateDialogFragment.setArguments(new Bundle());
        return rateDialogFragment;
    }

    public static RateDialogFragment newInstance(String str, String str2, String str3, String str4) {
        RateDialogFragment rateDialogFragment = new RateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_TITLE, str);
        bundle.putString(Constants.ARG_MESSAGE, str2);
        bundle.putString(Constants.ARG_CANCEL_TEXT, str3);
        bundle.putString(Constants.ARG_NEVER_SHOW_AGAIN_TEXT, str4);
        rateDialogFragment.setArguments(bundle);
        return rateDialogFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            initWithArguments(getArguments());
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.rate_dialog_fragment_rate_dialog, viewGroup);
        initView(inflate);
        setTexts();
        return inflate;
    }

    public void onCancel(DialogInterface dialogInterface) {
        AppRate.saveNeverShowAgain(this.neverShowAgainCheckBox.isChecked());
        super.onCancel(dialogInterface);
    }

    private void initWithArguments(Bundle bundle) {
        this.title = bundle.getString(Constants.ARG_TITLE);
        this.message = bundle.getString(Constants.ARG_MESSAGE);
        this.cancelMessage = bundle.getString(Constants.ARG_CANCEL_TEXT);
        this.neverShowAgainMessage = bundle.getString(Constants.ARG_NEVER_SHOW_AGAIN_TEXT);
    }

    private void initView(View view) {
        this.messageTextView = (TextView) view.findViewById(R.id.rate_message_textview);
        this.rateDescriptionTextView = (TextView) view.findViewById(R.id.rate_description_textview);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rate_ratingbar);
        if (VERSION.SDK_INT >= 21) {
            ratingBar.setProgressDrawableTiled(RaitingStartDrawables.getRatingStar(getContext()));
        }
        ratingBar.setOnRatingBarChangeListener(this);
        this.buttonsLayout = (LinearLayout) view.findViewById(R.id.buttons_layout);
        this.cancelButton = (Button) view.findViewById(R.id.cancel_button);
        this.cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppRate.saveNeverShowAgain(RateDialogFragment.this.neverShowAgainCheckBox.isChecked());
                RateDialogFragment.this.dismiss();
            }
        });
        this.neverShowAgainCheckBox = (CheckBox) view.findViewById(R.id.never_show_again_checkbox);
    }

    private void setTexts() {
        if (this.title != null) {
            getDialog().setTitle(this.title);
        } else {
            getDialog().setTitle(getString(R.string.rate_dialog_title));
        }
        String str = this.message;
        if (str != null) {
            this.messageTextView.setText(str);
        }
        String str2 = this.cancelMessage;
        if (str2 != null) {
            this.cancelButton.setText(str2);
        }
        String str3 = this.neverShowAgainMessage;
        if (str3 != null) {
            this.neverShowAgainCheckBox.setText(str3);
        }
    }

    public void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
        int i = (int) f;
        this.rateDescriptionTextView.setVisibility(0);
        switch (i) {
            case 0:
                this.rateDescriptionTextView.setVisibility(8);
                hideButtons();
                return;
            case 1:
                this.rateDescriptionTextView.setText(getString(R.string.rate_dialog_1_star_description));
                showBadRateButtons();
                return;
            case 2:
                this.rateDescriptionTextView.setText(getString(R.string.rate_dialog_2_star_description));
                showBadRateButtons();
                return;
            case 3:
                this.rateDescriptionTextView.setText(getString(R.string.rate_dialog_3_star_description));
                showBadRateButtons();
                return;
            case 4:
                this.rateDescriptionTextView.setText(getString(R.string.rate_dialog_4_star_description));
                showGoogRateButtons();
                return;
            case 5:
                this.rateDescriptionTextView.setText(getString(R.string.rate_dialog_5_star_description));
                showGoogRateButtons();
                return;
            default:
                return;
        }
    }

    private void showBadRateButtons() {
        this.buttonsLayout.removeAllViews();
        for (Pair pair : AppRate.getBadRateIntents()) {
            addButton((String) pair.first, (Intent) pair.second);
        }
    }

    private void showGoogRateButtons() {
        this.buttonsLayout.removeAllViews();
        for (Pair pair : AppRate.getGoodRateIntents()) {
            addButton((String) pair.first, (Intent) pair.second);
        }
    }

    private void hideButtons() {
        this.buttonsLayout.removeAllViews();
    }

    private void addButton(String str, final Intent intent) {
        Button button = new Button(new ContextThemeWrapper(getContext(), R.style.Rate_Dialog_RateButton), null, 0);
        button.setText(str);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppRate.saveNeverShowAgain(true);
                RateDialogFragment.this.startActivity(intent);
                RateDialogFragment.this.dismiss();
            }
        });
        this.buttonsLayout.addView(button);
    }
}
