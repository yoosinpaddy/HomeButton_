package com.home.back.bottom.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.home.back.bottom.R;
import com.home.back.bottom.helper.PackageHelper;
import com.home.back.bottom.util.PreferencesUtils;

public class DrawerFragment extends Fragment implements OnClickListener {
    public static final int NAVIGATION_DONATE_CODE = 3;
    public static final int NAVIGATION_FAQ_CODE = 5;
    public static final int NAVIGATION_RATE = 6;
    public static final int NAVIGATION_SETTINGS_CODE = 0;
    public static final int NAVIGATION_SHARE_CODE = 1;
    public static final int NAVIGATION_SUGGESTION_CODE = 2;
    public static final int NAVIGATION_VERSION_CODE = 4;
    private TextView donateTextView;
    private DrawerActionsListener drawerActionsListener;
    private int easterProCount = 0;
    private TextView faqTextView;
    private TextView privacyPolicyTextView;
    private TextView rateTextView;
    private View rootView;
    private TextView settingsTextView;
    private TextView shareTextView;
    private TextView suggestionTextView;
    private TextView versionTextView;

    public interface DrawerActionsListener {
        void onNavigation(int i);
    }

    public static DrawerFragment newInstance() {
        DrawerFragment drawerFragment = new DrawerFragment();
        drawerFragment.setArguments(new Bundle());
        return drawerFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_drawer, viewGroup, false);
        initViews(this.rootView);
        return this.rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DrawerActionsListener) {
            this.drawerActionsListener = (DrawerActionsListener) context;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(context.toString());
        sb.append(" must implement OnFragmentInteractionListener");
        throw new RuntimeException(sb.toString());
    }

    public void onDetach() {
        super.onDetach();
        this.drawerActionsListener = null;
    }

    private void initViews(View view) {
        this.versionTextView = (TextView) view.findViewById(R.id.version_textview);
        this.settingsTextView = (TextView) view.findViewById(R.id.settings_textview);
        this.shareTextView = (TextView) view.findViewById(R.id.share_textview);
        this.donateTextView = (TextView) view.findViewById(R.id.donate_textview);
        this.suggestionTextView = (TextView) view.findViewById(R.id.suggestion_textview);
        this.faqTextView = (TextView) view.findViewById(R.id.faq_textview);
        this.privacyPolicyTextView = (TextView) view.findViewById(R.id.privacy_policy_textview);
        this.rateTextView = (TextView) view.findViewById(R.id.rate_textview);
        this.versionTextView.setText(getString(R.string.drawer_version, PackageHelper.getVersionName(getActivity())));
        this.versionTextView.setOnClickListener(this);
        this.settingsTextView.setOnClickListener(this);
        this.shareTextView.setOnClickListener(this);
        this.donateTextView.setOnClickListener(this);
        this.suggestionTextView.setOnClickListener(this);
        this.faqTextView.setOnClickListener(this);
        this.privacyPolicyTextView.setOnClickListener(this);
        this.rateTextView.setOnClickListener(this);
    }

    private void startPrivacyPolicyIntent() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.privacy_policy_url))));
    }

    public void onClick(View view) {
        if (view == this.versionTextView) {
            this.drawerActionsListener.onNavigation(4);
            invertProLock();
        } else if (view == this.settingsTextView) {
            this.drawerActionsListener.onNavigation(0);
        } else if (view == this.shareTextView) {
            this.drawerActionsListener.onNavigation(1);
        } else if (view == this.suggestionTextView) {
            this.drawerActionsListener.onNavigation(2);
        } else if (view == this.donateTextView) {
            this.drawerActionsListener.onNavigation(3);
        } else if (view == this.faqTextView) {
            this.drawerActionsListener.onNavigation(5);
        } else if (view == this.privacyPolicyTextView) {
            startPrivacyPolicyIntent();
        } else if (view == this.rateTextView) {
            this.drawerActionsListener.onNavigation(6);
        }
    }

    private void invertProLock() {
        this.easterProCount++;
        if (this.easterProCount % 10 == 0) {
            PreferencesUtils.savePref(PreferencesUtils.PREF_PRO_VERSION, !PreferencesUtils.getPref(PreferencesUtils.PREF_PRO_VERSION, false));
        }
    }
}
