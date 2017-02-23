package com.vibbix.ballroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;
import com.vibbix.ballroom.databinding.ActivityMainBinding;

import org.parceler.Parcels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class activity_main extends AppCompatActivity {
    //region binded fields
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.llSwitch)
    LinearLayout llswitch;
    @BindView(R.id.seekEfficiency)
    SeekBar skpacking;
    @BindString(R.string.link_github)
    String githublink;
    @BindString(R.string.link_project)
    String projectlink;
    @BindString(R.string.prefs_name)
    String prefsName;
    @BindString(R.string.prefs_area)
    String prefsArea;
    @BindString(R.string.prefs_cost)
    String prefsCost;
    @BindString(R.string.prefs_depth)
    String prefsDepth;
    @BindString(R.string.prefs_easy)
    String prefsEasy;
    @BindString(R.string.prefs_efficiency)
    String prefsEfficiency;
    @BindString(R.string.prefs_metric)
    String prefsMetric;
    @BindString(R.string.prefs_radius)
    String prefsRadius;
    @BindString(R.string.prefs_nightmode)
    String prefsNightmode;
    @BindArray(R.array.spinner_nightmode_items)
    String[] nightmodeItems;
    //endregion
    private BallroomCalc ballroomCalc;
    private ObservableBallRoomCalculator observableBallRoomCalculator;
    private boolean wasMetric = false;
    private boolean wasEasy = false;
    private String currency;
    private static final String TAG = "activity_main";
    private static final String PARCEL = "main_parcel";

    @BindingAdapter("fadeVisible")
    public static void setFadeVisible(final View view, boolean visible) {
        if (view.getTag() == null) {
            view.setTag(true);
        } else {
            ViewGroup transitions_container = (ViewGroup) view.getParent();
            TransitionManager.beginDelayedTransition(transitions_container);
        }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //applyNightMode();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null){
            this.observableBallRoomCalculator = new ObservableBallRoomCalculator();
        } else {
            this.observableBallRoomCalculator = Parcels.unwrap(savedInstanceState.getParcelable(PARCEL));
        }
        binding.setCalc(this.observableBallRoomCalculator);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        try {
            this.currency = Currency.getInstance(Locale.getDefault()).getSymbol();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.currency = "$";
        }
        //fix for linear layout issue
        float density = getResources().getDisplayMetrics().density;
        if (density <= 1.5f){
            llswitch.setOrientation(LinearLayout.VERTICAL);
        }
        skpacking.setMax(BallroomCalc.MAX_DENSITY.intValue());
        this.loadPreferences();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intentabout = new Intent(this, activity_about.class);
                startActivity(intentabout);
                return true;
            case R.id.action_project:
                Intent projectIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(this.projectlink));
                startActivity(projectIntent);
                return true;
            case R.id.action_github:
                Intent githubIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(this.githublink));
                startActivity(githubIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CommitPrefEdits")
    private void savePreferences() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(prefsName, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putFloat(prefsArea, (float) this.ballroomCalc.getArea());
        edit.putFloat(prefsDepth, (float) this.ballroomCalc.getDepth());
        edit.putBoolean(prefsEasy, this.ballroomCalc.isEasyMode());
        edit.putFloat(prefsEfficiency, (float) this.ballroomCalc.getEfficiency());
        edit.putBoolean(prefsMetric, this.ballroomCalc.isMetric());
        edit.putFloat(prefsCost, (float) this.ballroomCalc.getPrice());
        //must commit, background thread will not save
        edit.commit();
    }

    private void loadPreferences() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(prefsName, 0);
//        this.ballroomCalc.setArea(settings.getFloat(prefsArea, 674.0f));
//        this.ballroomCalc.setDepth(settings.getFloat(prefsDepth, 2.0f));
//        this.ballroomCalc.setEasymode(settings.getBoolean(prefsEasy, false));
//        this.ballroomCalc.setEfficiency(settings.getFloat(prefsEfficiency, 64.0f));
//        boolean useImperial = Locale.getDefault().getCountry().equals("US")
//                || Locale.getDefault().getCountry().equals("LR")
//                || Locale.getDefault().getCountry().equals("MM");
//        this.ballroomCalc.setIsMetric(settings.getBoolean(prefsMetric, !useImperial));
//        this.ballroomCalc.setPrice(settings.getFloat(prefsCost, 0.20f));
    }

    private void applyNightMode() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(prefsName, 0);
        String nightMode = settings.getString(prefsNightmode, "Auto");
        switch (nightMode) {
            case "Auto":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            case "Night":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Day":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        this.getDelegate().applyDayNight();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL, Parcels.wrap(this.observableBallRoomCalculator));
    }
}
