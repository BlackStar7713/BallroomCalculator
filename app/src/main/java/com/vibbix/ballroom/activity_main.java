package com.vibbix.ballroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;

public class activity_main extends AppCompatActivity {
    //region binded fields
    @BindView(R.id.decimalArea)
    EditText etArea;
    @BindView(R.id.decimalDepth)
    EditText etDepth;
    @BindView(R.id.decimalRadius)
    EditText etRadius;
    @BindView(R.id.decimalMoney)
    EditText etMoney;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.switchMetric)
    Switch swmetric;
    @BindView(R.id.txtResult)
    TextView finaltext;
    @BindView(R.id.switchEasy)
    Switch easyswitch;
    @BindView(R.id.llEfficiency)
    LinearLayout llEfficiency;
    @BindView(R.id.llRadius)
    LinearLayout llRadius;
    @BindView(R.id.llSwitch)
    LinearLayout llswitch;
    @BindView(R.id.txtunitmoney)
    TextView money;
    @BindView(R.id.txtunitsmall)
    TextView unitsmall;
    @BindView(R.id.txtunitSquared)
    TextView unitsquared;
    @BindView(R.id.txtunitstd)
    TextView unitstd;
    @BindView(R.id.seekEfficiency)
    SeekBar skpacking;
    @BindView(R.id.txtEfficiencyPercent)
    TextView tvPercent;
    @BindString(R.string.percentFormatter)
    String percentFormatter;
    @BindString(R.string.formattedresult)
    String formattedResult;
    @BindView(R.id.transition_container)
    ViewGroup transition_container;
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
    private boolean wasMetric = false;
    private boolean wasEasy = false;
    private String currency;
    /**
     * Rounds a number to a certain amount of decimal points
     *
     * @param value  The double value
     * @param places Number of place
     * @return Rounded number
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Parses a string as a double
     *
     * @param d input string
     * @return a verified double
     */
    public static double inputValidator(String d) {
        try {
            return Double.valueOf(d.isEmpty() ? "0.0" : d);
        } catch (Exception ex) {
            return 0.0D;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyNightMode();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        this.ballroomCalc = new BallroomCalc();
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
        //seekbar watcher
        skpacking.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPercent.setText(String.format(percentFormatter, Integer.toString(progress)));
                ballroomCalc.setEfficiency(progress);
                updateEstimate();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skpacking.setMax(BallroomCalc.MAX_DENSITY.intValue());
        this.loadPreferences();
    }

    @OnCheckedChanged(R.id.switchMetric)
    void onMetricSwitch() {
        this.ballroomCalc.setIsMetric(swmetric.isChecked());
        this.updateEstimate();
    }

    @OnCheckedChanged(R.id.switchEasy)
    void onSwitchEasy() {
        this.ballroomCalc.setEasymode(easyswitch.isChecked());
        this.updateEstimate();
    }

    @OnTextChanged(value = R.id.decimalArea, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void updateArea() {
        String area = etArea.getText().toString();
        double areaval = inputValidator(area);
        this.ballroomCalc.setArea(areaval);
        this.updateEstimate();
    }

    @OnTextChanged(value = R.id.decimalMoney, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void updateMoney() {
        String money = etMoney.getText().toString();
        double moneyval = inputValidator(money);
        this.ballroomCalc.setPrice(moneyval);
        this.updateEstimate();
    }

    @OnTextChanged(value = R.id.decimalDepth, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void updateDepth() {
        String depth = etDepth.getText().toString();
        double depthval = inputValidator(depth);
        this.ballroomCalc.setDepth(depthval);
        this.updateEstimate();
    }

    @OnTextChanged(value = R.id.decimalRadius, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void updateRadius() {
        String radius = etRadius.getText().toString();
        double radiusval = inputValidator(radius);
        this.ballroomCalc.setRadius(radiusval);
        this.updateEstimate();
    }

    /**
     * Updates the cost/ball count estimate TextView
     */
    public void updateEstimate() {
        if (wasEasy != this.ballroomCalc.isEasyMode()) {
            TransitionManager.beginDelayedTransition(transition_container);
            if (this.ballroomCalc.isEasyMode()) {
                llRadius.setVisibility(View.GONE);
                llEfficiency.setVisibility(View.GONE);
            } else {
                llRadius.setVisibility(View.VISIBLE);
                llEfficiency.setVisibility(View.VISIBLE);
            }
            wasEasy = easyswitch.isChecked();
        }
        if (wasMetric != this.ballroomCalc.isMetric()) {
            if (this.ballroomCalc.isMetric()) {
                unitsmall.setText(R.string.centimeter);
                unitsquared.setText(R.string.MeterSquared);
                unitstd.setText(R.string.Meter);
            } else {
                unitsmall.setText(R.string.Inchs);
                unitsquared.setText(R.string.FeetSquared);
                unitstd.setText(R.string.Feet);
            }
            wasMetric = this.ballroomCalc.isMetric();
        }
        try {
            finaltext.setText(String.format(this.formattedResult,
                    this.ballroomCalc.getBalls(), this.currency,
                    round(this.ballroomCalc.getCost(), 2)));
        } catch (Exception ex) {
            ex.printStackTrace();
            finaltext.setText("");
        }
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
        this.ballroomCalc.setArea(settings.getFloat(prefsArea, 674.0f));
        this.ballroomCalc.setDepth(settings.getFloat(prefsDepth, 2.0f));
        this.ballroomCalc.setEasymode(settings.getBoolean(prefsEasy, false));
        this.ballroomCalc.setEfficiency(settings.getFloat(prefsEfficiency, 64.0f));
        boolean useImperial = Locale.getDefault().getCountry().equals("US")
                || Locale.getDefault().getCountry().equals("LR")
                || Locale.getDefault().getCountry().equals("MM");
        this.ballroomCalc.setIsMetric(settings.getBoolean(prefsMetric, !useImperial));
        this.ballroomCalc.setPrice(settings.getFloat(prefsCost, 0.20f));
        this.refresh();
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

    private void refresh() {
        updateArea();
        updateDepth();
        updateMoney();
        updateRadius();
        wasEasy = !easyswitch.isChecked();
        wasMetric = !swmetric.isChecked();
        onMetricSwitch();
        onSwitchEasy();
        skpacking.setProgress(skpacking.getProgress());
    }

    @Override
    public void onStop() {
        super.onStop();
        this.savePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.savePreferences();
    }
}
