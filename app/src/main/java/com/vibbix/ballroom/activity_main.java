package com.vibbix.ballroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;
import com.vibbix.ballroom.databinding.ActivityMainBinding;

import org.parceler.Parcels;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class activity_main extends AppCompatActivity {
    private static final String TAG = "activity_main";
    private static final String PARCEL = "main_parcel";
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
    @BindView(R.id.txtunitsmall)
    TextView unitsmall;
    //endregion
    private ObservableBallRoomCalculator observableBallRoomCalculator;
    private String currency;

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        //applyNightMode();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null){
            this.observableBallRoomCalculator = new ObservableBallRoomCalculator();
            this.loadPreferences();
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
        if (getResources().getDisplayMetrics().density <= 1.5f)
            llswitch.setOrientation(LinearLayout.VERTICAL);
        skpacking.setMax(ObservableBallRoomCalculator.MAX_DENSITY.intValue());

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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = settings.edit();
        edit.putFloat(prefsArea, (float) this.observableBallRoomCalculator.area.get());
        edit.putFloat(prefsDepth, (float) this.observableBallRoomCalculator.depth.get());
        edit.putBoolean(prefsEasy, this.observableBallRoomCalculator.isEasy.get());
        edit.putFloat(prefsEfficiency, (float) this.observableBallRoomCalculator.efficiency.get());
        edit.putBoolean(prefsMetric, this.observableBallRoomCalculator.isMetric.get());
        edit.putFloat(prefsRadius, (float) this.observableBallRoomCalculator.radius.get());
        edit.putFloat(prefsCost, (float) this.observableBallRoomCalculator.price.get());
        //must commit, background thread will not save
        edit.commit();
        Log.d(TAG, "Saving to preferences");
    }

    private void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        float area = settings.getFloat(prefsArea, 674.0f);
        float depth = settings.getFloat(prefsDepth, 2.0f);
        boolean isEasy = settings.getBoolean(prefsEasy, false);
        int efficiency = (int) settings.getFloat(prefsEfficiency, 64.0f);
        boolean useImperial = Locale.getDefault().getCountry().equals("US")
                || Locale.getDefault().getCountry().equals("LR")
                || Locale.getDefault().getCountry().equals("MM");
        float radius = settings.getFloat(prefsRadius, 1.675f);
        float price = settings.getFloat(prefsCost, 0.20f);
        Log.v(TAG, "loadPref area: " + area);
        Log.v(TAG, "loadPref depth: " + depth);
        Log.v(TAG, "loadPref isEasy: " + isEasy);
        Log.v(TAG, "loadPref efficiency: " + efficiency);
        Log.v(TAG, "loadPref isMetric: " + useImperial);
        Log.v(TAG, "loadPref radius: " + radius);
        Log.v(TAG, "loadPref price: " + price);
        this.observableBallRoomCalculator.area.set(area);
        this.observableBallRoomCalculator.depth.set(depth);
        this.observableBallRoomCalculator.isEasy.set(isEasy);
        this.observableBallRoomCalculator.efficiency.set(efficiency);
        this.observableBallRoomCalculator.isMetric.set(settings.getBoolean(prefsMetric, !useImperial));
        this.observableBallRoomCalculator.radius.set(radius);
        this.observableBallRoomCalculator.price.set(price);
        Log.d(TAG, "Loaded from preferences");

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
        Log.d(TAG, "Night mode applied");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL, Parcels.wrap(this.observableBallRoomCalculator));
        Log.d(TAG, "InstanceState bundle saved");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.savePreferences();
        Log.d(TAG, "Main Activity stopped");
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.savePreferences();
        Log.d(TAG, "Main Activity paused");
    }
}
