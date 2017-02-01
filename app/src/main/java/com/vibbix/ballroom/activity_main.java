package com.vibbix.ballroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
        updateArea();
        updateDepth();
        updateMoney();
        updateRadius();
        skpacking.setMax(BallroomCalc.maxdensity.intValue());
        skpacking.setProgress(skpacking.getProgress());
    }

    @OnClick(R.id.switchMetric)
    void onMetricSwitch() {
        this.ballroomCalc.setIsMetric(swmetric.isChecked());
        this.updateEstimate();
    }

    @OnClick(R.id.switchEasy)
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
        if (id == R.id.action_settings) {
            Intent intentabout = new Intent(this, activity_about.class);
            startActivity(intentabout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
