package com.home.back.bottom.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.home.back.bottom.AppRateDialog.AppRate;
import com.home.back.bottom.AppRateDialog.RateDialogFragment;
import com.home.back.bottom.R;
import com.home.back.bottom.adapter.AppsListAdapter;
import com.home.back.bottom.dialog.SimpleDialogFragment;
import com.home.back.bottom.dialog.XHomeBarDialog;
import com.home.back.bottom.fragment.ButtonSettingsFragment;
import com.home.back.bottom.fragment.DrawerFragment;
import com.home.back.bottom.interfaces.ActivateButton;
import com.home.back.bottom.interfaces.OnAppSelectedLis;
import com.home.back.bottom.service.ButtonOverlayService;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.PreferencesUtils;
import com.home.back.bottom.util.Util_NativeAdvanceHelper;
import com.home.back.bottom.util.Util_Share;
import com.home.back.bottom.util.billing.IabHelper;
import com.home.back.bottom.util.billing.IabResult;
import com.home.back.bottom.util.billing.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ButtonSettingsFragment.ButtonSettingsListener, OnClickListener, SimpleDialogFragment.SimpleFragmentListener, DrawerFragment.DrawerActionsListener {
    private static final int DAY_IN_MILLISEC = 86400000;
    private static final int REQUEST_CODE_ADMIN = 112;
    private static final int REQUEST_CODE_PERMISION_SYSTEM_ALERT_WINDOW = 6969;
    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 111;
    private static final String TAG = "MainActivity";

    public List<ApplicationInfo> appLaunchable;

    ButtonSettingsFragment.PositionEnum positionEnum = ButtonSettingsFragment.PositionEnum.CENTER;
    public LinearLayout bottomBar;

    public ButtonSettingsFragment centerFragment;
    private int currentNavigationItem = -1;

    public ButtonSettingsFragment.PositionEnum currentPositionEnum;
    private DrawerFragment drawerFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private boolean isServiceRunning = false;

    public ButtonSettingsFragment leftFragment;
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener;
    private IabHelper mHelper;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public ViewPager mViewPager;

    public SimpleDialogFragment noPermissionDialog;
    private SharedPreferences prefs;

    public ProgressDialog progressDialog;

    public SimpleDialogFragment readPhoneDialog;

    public ButtonSettingsFragment rightFragment;
    private Intent service;
    private androidx.appcompat.widget.Toolbar toolbar;
    private RelativeLayout tutoLayout;
    ImageView left, center, right;

    /*Mine code*/
    private ToggleButton switchOnOff;

    private class LoadAppTask extends AsyncTask<String, Void, Void> {
        private LoadAppTask() {
        }


        public void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }


        public Void doInBackground(String... strArr) {
            if (appLaunchable == null) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.appLaunchable = MainActivity.getAllInstalledApplications(mainActivity);
                Collections.sort(appLaunchable, new Comparator<ApplicationInfo>() {
                    public int compare(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
                        return applicationInfo.loadLabel(getPackageManager()).toString().compareTo(applicationInfo2.loadLabel(getPackageManager()).toString());
                    }
                });
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            progressDialog.dismiss();
            startAppDialog();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int i) {
            switch (i) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                default:
                    return null;
            }
        }

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            if (i == 0) {
                leftFragment = ButtonSettingsFragment.newInstance(0);
                return leftFragment;
            } else if (i == 1) {
                centerFragment = ButtonSettingsFragment.newInstance(1);
                return centerFragment;
            } else {
                rightFragment = ButtonSettingsFragment.newInstance(2);
                return rightFragment;
            }
        }
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        initActionBar();
        initViews();
        initDrawer();
        loadPreferences();
        initInventoryListener();
        setupBillingConnexion();
        if (bundle == null) {
            initFragments();
        }
        checkReadPhonePermission_first_stage();
//        initRateDialog(bundle);
    }


    public void onResume() {
        super.onResume();
        checkReadPhonePermission_first_stage();
        isServiceRunning = isServiceRunning();
        ButtonSettingsFragment buttonSettingsFragment = leftFragment;
        if (buttonSettingsFragment != null) {
            buttonSettingsFragment.checkProVersion();
        }
        if (rightFragment != null) {
            leftFragment.checkProVersion();
        }
        if (Util_Share.isNeedToAdShow(getApplicationContext())) {
            Util_Share.loadGoogleAds(MainActivity.this, getClass().getSimpleName());
        }
    }

    public void onSaveInstanceState(Bundle bundle, PersistableBundle persistableBundle) {
        super.onSaveInstanceState(bundle, persistableBundle);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(3)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }


    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    public void onAppSelectionPressed(ButtonSettingsFragment.PositionEnum positionEnum) {
        currentPositionEnum = positionEnum;
        checkReadPhonePermission();
    }

    public void onClick(View view) {
        RelativeLayout relativeLayout = tutoLayout;
        if (view == relativeLayout) {
            relativeLayout.setVisibility(8);
        }
    }

    public void onPositiveButtonPressed(SimpleDialogFragment simpleDialogFragment) {
        if (simpleDialogFragment == noPermissionDialog) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(getPackageName());
                startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), REQUEST_CODE_PERMISION_SYSTEM_ALERT_WINDOW);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, R.string.error_no_setting_draw_on_top, 1).show();
            }
        } else {
            SimpleDialogFragment simpleDialogFragment2 = readPhoneDialog;
            if (simpleDialogFragment == simpleDialogFragment2) {
                simpleDialogFragment2.dismiss();
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 111);
            }
        }
    }

    public void onNegativeButtonPressed(SimpleDialogFragment simpleDialogFragment) {
        SimpleDialogFragment simpleDialogFragment2 = noPermissionDialog;
        if (simpleDialogFragment == simpleDialogFragment2) {
            simpleDialogFragment2.dismiss();
            finish();
        }
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Button");
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        /*Mine code*/
        switchOnOff = findViewById(R.id.switchOnOff);
        boolean pref2 = PreferencesUtils.getPref("serviceActive", false);
        switchOnOff.setChecked(pref2);
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: Interface here
                int currentFrag = mViewPager.getCurrentItem();
                Log.e(TAG, "onCheckedChanged: " + currentFrag + isChecked);
                if (currentFrag == 0) {
                    if (leftFragment != null && leftFragment instanceof ActivateButton) {
                        ((ActivateButton) leftFragment).buttonClicked(isChecked);
                    }
                } else if (currentFrag == 1) {
                    if (centerFragment != null && centerFragment instanceof ActivateButton) {
                        ((ActivateButton) centerFragment).buttonClicked(isChecked);
                    }
                } else if (currentFrag == 2) {
                    if (rightFragment != null && rightFragment instanceof ActivateButton) {
                        ((ActivateButton) rightFragment).buttonClicked(isChecked);
                    }
                }
            }
        });

        bottomBar = findViewById(R.id.bottom_bar);
        left = findViewById(R.id.left);
        center = findViewById(R.id.center);
        right = findViewById(R.id.right);
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left_select));
                center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center));
                right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_new));
                mViewPager.setCurrentItem(0);
            }
        });

        center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left));
                center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center_select));
                right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_new));
                mViewPager.setCurrentItem(1);
            }
        });

        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left));
                center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center));
                right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_select));
                mViewPager.setCurrentItem(2);
            }
        });
//        bottomBar.setDefaultTabPosition(1);
//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            public void onTabSelected(@IdRes int i) {
//                if (mViewPager == null) {
//                    return;
//                }
//                if (i == R.id.tab_left) {
//                    mViewPager.setCurrentItem(0);
//                } else if (i == R.id.tab_center) {
//                    mViewPager.setCurrentItem(1);
//                } else if (i == R.id.tab_right) {
//                    mViewPager.setCurrentItem(2);
//                }
//            }
//        });
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
//                bottomBar.selectTabAtPosition(i);
                Log.e(TAG, "onPageSelected: " + i);
                boolean pref2 = false;
                switch (i) {
                    case 0:
                        left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left_select));
                        center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center));
                        right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_new));
                        positionEnum = ButtonSettingsFragment.PositionEnum.LEFT;
                        pref2 = PreferencesUtils.getPref("left_serviceActive", false);
                        switchOnOff.setChecked(pref2);
                        //ButtonSettingsFragment.setPositionEnum(0);
                        break;
                    case 1:
                        left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left));
                        center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center_select));
                        right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_new));
                        positionEnum = ButtonSettingsFragment.PositionEnum.CENTER;
                        pref2 = PreferencesUtils.getPref("serviceActive", false);
                        switchOnOff.setChecked(pref2);
                        //ButtonSettingsFragment.setPositionEnum(1);
                        break;
                    case 2:
                        left.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.left));
                        center.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.center));
                        right.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.right_select));
                        positionEnum = ButtonSettingsFragment.PositionEnum.RIGHT;
                        pref2 = PreferencesUtils.getPref("right_serviceActive", false);
                        switchOnOff.setChecked(pref2);
                        //ButtonSettingsFragment.setPositionEnum(2);

                        break;
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.app_folder_loading));
        progressDialog.setCancelable(false);
        tutoLayout = (RelativeLayout) findViewById(R.id.layout_tuto);
        tutoLayout.setOnClickListener(this);
    }

    public String getPrefKey(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(ButtonSettingsFragment.PositionEnum.getPrefPrefix(positionEnum));
        sb.append(str);
        return sb.toString();
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle r1 = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                onNavigationItemSelected();
            }
        };

        drawerToggle = r1;
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void initFragments() {
        drawerFragment = DrawerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add((int) R.id.container_drawer, (Fragment) drawerFragment).commit();
    }

    private void loadPreferences() {
        prefs = getSharedPreferences("com.home.button.bottom", 0);
        if (prefs.getBoolean(PreferencesUtils.PREF_FIRST_RUN, true)) {
            tutoLayout.setVisibility(0);
            prefs.edit().putBoolean(PreferencesUtils.PREF_FIRST_RUN, false).apply();
            prefs.edit().putBoolean(PreferencesUtils.PREF_SERVICE_ACTIVE, true).apply();
            prefs.edit().putInt(PreferencesUtils.PREF_ACTION_ON_CLICK, Action.HOME.getId()).apply();
            prefs.edit().putInt(PreferencesUtils.PREF_ACTION_ON_LONG_CLICK, Action.NONE.getId()).apply();
            prefs.edit().putInt(PreferencesUtils.PREF_ACTION_ON_DOUBLE_CLICK, Action.NONE.getId()).apply();
            prefs.edit().putBoolean(PreferencesUtils.PREF_BUTTON_VISIBLE, true).apply();
            prefs.edit().putBoolean(PreferencesUtils.PREF_VIBRATION_ENABLE, true).apply();
            prefs.edit().putInt(PreferencesUtils.PREF_BUTTON_COLOR, 1).apply();
            prefs.edit().putBoolean(PreferencesUtils.PREF_NOTIFICATION_ENABLE, true).apply();
            prefs.edit().putBoolean(PreferencesUtils.PREF_NOTIFICATION_ICON_VISIBLE, false).apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_CLICK_NAME, "").apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_LONG_CLICK_NAME, "").apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_DOUBLE_CLICK_NAME, "").apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_CLICK_PKG, "").apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_LONG_CLICK_PKG, "").apply();
            prefs.edit().putString(PreferencesUtils.PREF_APP_DOUBLE_CLICK_PKG, "").apply();
            prefs.edit().putInt(PreferencesUtils.PREF_BUTTON_WIDTH, 40).apply();
            prefs.edit().putInt(PreferencesUtils.PREF_BUTTON_HEIGHT, 12).apply();
        }
        if (PreferencesUtils.getPref(PreferencesUtils.PREF_FIRST_RUN_V2, true)) {
            PreferencesUtils.savePref(PreferencesUtils.PREF_FIRST_RUN_V2, false);
            PreferencesUtils.savePref("left_serviceActive", false);
            PreferencesUtils.savePref("right_serviceActive", false);
            PreferencesUtils.savePref(PreferencesUtils.PREF_FIRST_OPENED, String.valueOf(new Date().getTime()));
        } else if (PreferencesUtils.getPref(PreferencesUtils.PREF_FIRST_OPENED, (String) null) == null) {
            PreferencesUtils.savePref(PreferencesUtils.PREF_FIRST_OPENED, String.valueOf(new Date().getTime()));
        }
    }

    private void initRateDialog(Bundle bundle) {
        AppRate.with(this).setFirstShow(3).setShowInterval(2).setIntervalMultiplier(1.5f).setAppPackage(getPackageName()).addBadRateButton(getString(R.string.rate_dialog_suggestion_button), getFAQIntent()).addBadRateButton(getString(R.string.drawer_item_faq), getFAQIntent());
        if (bundle == null) {
            AppRate.showDialogIfNeeded(this);
        }
    }

    public void onNavigation(int i) {
        currentNavigationItem = i;
        closeDrawer();
    }


    public void onNavigationItemSelected() {
        switch (currentNavigationItem) {
            case 1:
                shareTheApp();
                break;
            case 2:
//                sendContactUsEmail();
                break;
            case 5:
                startFAQActivity();
                break;
            case 6:
                rateTheApp();
                break;
        }
        currentNavigationItem = -1;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    private void shareTheApp() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.home.button.bottom");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, null));
    }

    private void rateTheApp() {
        RateDialogFragment.newInstance().show(getSupportFragmentManager(), RateDialogFragment.TAG);
    }


    private void startFAQActivity() {
        Intent intent = new Intent(this, WebviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebviewActivity.BUNDLE_TITLE, getString(R.string.drawer_item_faq));
        bundle.putString(WebviewActivity.BUNDLE_FILE_NAME, getString(R.string.faq_file_name));
        intent.putExtras(bundle);
        startActivity(getFAQIntent());
    }

    private Intent getFAQIntent() {
        Intent intent = new Intent(this, WebviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebviewActivity.BUNDLE_TITLE, getString(R.string.drawer_item_faq));
        bundle.putString(WebviewActivity.BUNDLE_FILE_NAME, getString(R.string.faq_file_name));
        intent.putExtras(bundle);
        return intent;
    }

    public void startService() {
        Log.d(TAG, "startService()");
        service = new Intent(this, ButtonOverlayService.class);
        startService(service);
    }

    public void stopService() {
        Log.d(TAG, "stopService()");
        try {
            stopService(service);
        } catch (Exception e) {
            Log.w(TAG, "stopService()", e);
        }
    }

    private void relaunchService() {
        Log.d(TAG, "relaunchService()");
        if (isServiceOn()) {
            stopService();
            startService();
        }
    }

    private boolean isServiceRunning() {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (ButtonOverlayService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onRestartServiceNeeded() {
        if (isServiceOn()) {
            relaunchService();
        } else {
            stopService();
        }
    }

    private boolean isServiceOn() {
        boolean pref = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.CENTER), true);
        boolean pref2 = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.LEFT), true);
        boolean pref3 = PreferencesUtils.getPref(getPrefKey(PreferencesUtils.PREF_SERVICE_ACTIVE, ButtonSettingsFragment.PositionEnum.RIGHT), true);
        if (pref || pref2 || pref3) {
            return true;
        }
        return false;
    }

    private void showNoPermissionDialog() {
        new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.noPermissionDialog = SimpleDialogFragment.createInstance(mainActivity.getString(R.string.no_permission_dialog_title), getString(R.string.no_permission_dialog_message), getString(R.string.ok), getString(R.string.cancel));
                noPermissionDialog.setCancelable(false);
                noPermissionDialog.show(getSupportFragmentManager(), "noPermissionDialog");
            }
        }.sendEmptyMessageDelayed(0, 100);
    }

    private void showReadPhoneDialog() {
        new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                readPhoneDialog = SimpleDialogFragment.createInstance(getString(R.string.read_phone_dialog_title), getString(R.string.read_phone_dialog_message), getString(R.string.ok));
                readPhoneDialog.setCancelable(false);
                readPhoneDialog.show(getSupportFragmentManager(), "readPhoneDialog");
            }
        }.sendEmptyMessageDelayed(0, 100);
    }

    private void checkDrawPermission() {
        if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            relaunchService();
        } else {
            showNoPermissionDialog();
        }
    }

    private void checkReadPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            showReadPhoneDialog();
        } else {
            new LoadAppTask().execute(new String[0]);
        }
    }

    private void checkReadPhonePermission_first_stage() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            showReadPhoneDialog();
        } else {
            checkDrawPermission();
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != REQUEST_CODE_PERMISION_SYSTEM_ALERT_WINDOW) {
            return;
        }
        if (VERSION.SDK_INT < 23) {
            relaunchService();
        } else if (Settings.canDrawOverlays(this)) {
            relaunchService();
        } else {
            showNoPermissionDialog();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
//        if (i == 111 && iArr.length > 0 && iArr[0] == 0) {
//            new LoadAppTask().execute(new String[0]);
//        }
    }

    public static List<ApplicationInfo> getAllInstalledApplications(Context context) {
        List installedApplications = context.getPackageManager().getInstalledApplications(128);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < installedApplications.size(); i++) {
            if (context.getPackageManager().getLaunchIntentForPackage(((ApplicationInfo) installedApplications.get(i)).packageName) != null) {
                arrayList.add(installedApplications.get(i));
            }
        }
        return arrayList;
    }


    public void startAppDialog() {
        String[] strArr = new String[appLaunchable.size()];
        for (int i = 0; i < appLaunchable.size(); i++) {
            strArr[i] = ((ApplicationInfo) appLaunchable.get(i)).loadLabel(getPackageManager()).toString();
        }
        View view= LayoutInflater.from(this).inflate(R.layout.apps_dialog,null);

        if (Util_Share.isNeedToAdShow(this)) {
            Util_NativeAdvanceHelper.loadSmallNativeAd(this, (FrameLayout) view.findViewById(R.id.fl4_adplaceholder));
        }
        final AlertDialog b=new Builder(this).setTitle(getString(R.string.choose_app_button)).create();
    /*.setSingleChoiceItems(strArr, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();*/
        RecyclerView r= view.findViewById(R.id.appsRecyclerView);
        AppsListAdapter adapter= new AppsListAdapter(this,strArr);
        adapter.setOnItemClickListener(new OnAppSelectedLis() {
            @Override
            public void appSelected(int app) {
                int i= app;
                b.dismiss();
                switch (currentPositionEnum) {
                    case CENTER:
                        if (centerFragment != null) {
                            centerFragment.setSelectedApp((ApplicationInfo) appLaunchable.get(i));
                            return;
                        }
                        return;
                    case LEFT:
                        if (leftFragment != null) {
                            leftFragment.setSelectedApp((ApplicationInfo) appLaunchable.get(i));
                            return;
                        }
                        return;
                    case RIGHT:
                        if (rightFragment != null) {
                            rightFragment.setSelectedApp((ApplicationInfo) appLaunchable.get(i));
                            return;
                        }
                        return;
                    default:
                        return;
                }

            }
        });
        r.setLayoutManager(new LinearLayoutManager(this));
        r.setAdapter(adapter);
        b.setView(view);
        b.show();
    }

    private void setupBillingConnexion() {
        mHelper = new IabHelper(this, getString(R.string.billing_key));
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                if (!iabResult.isSuccess()) {
                    MainActivity mainActivity = MainActivity.this;
                    Toast.makeText(mainActivity, mainActivity.getString(R.string.billing_loading_error), 0).show();
                    return;
                }
                String str = MainActivity.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Success setting up In-app Billing: ");
                sb.append(iabResult);
                Log.d(str, sb.toString());
                loadPurchasedItems();
            }
        });
    }


    public void loadPurchasedItems() {
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (NullPointerException unused) {
            Toast.makeText(this, getString(R.string.billing_loading_error), 0).show();
        }
    }

    private void initInventoryListener() {
        mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
                if (iabResult.isFailure()) {
                    MainActivity mainActivity = MainActivity.this;
                    Toast.makeText(mainActivity, mainActivity.getString(R.string.billing_loading_error), 0).show();
                    return;
                }
                String str = MainActivity.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Success loading inventory : ");
                sb.append(iabResult);
                Log.d(str, sb.toString());
                PreferencesUtils.savePref(PreferencesUtils.PREF_REAL_PRO_VERSION, false);
            }
        };
    }

    private String getPrefKey(String str, ButtonSettingsFragment.PositionEnum positionEnum) {
        StringBuilder sb = new StringBuilder();
        sb.append(ButtonSettingsFragment.PositionEnum.getPrefPrefix(positionEnum));
        sb.append(str);
        return sb.toString();
    }

    private void showXHomeBarIfNeeded() {
        if (!PreferencesUtils.getPref(PreferencesUtils.PREF_X_HOME_BAR_SHOWN, false)) {
            String pref = PreferencesUtils.getPref(PreferencesUtils.PREF_FIRST_OPENED, (String) null);
            if (pref != null) {
                if (new Date().getTime() - Long.parseLong(pref) > 172800000) {
                    XHomeBarDialog.createInstance().show(getSupportFragmentManager());
                }
            }
        }
    }
}
