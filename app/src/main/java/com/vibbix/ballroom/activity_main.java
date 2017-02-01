package com.vibbix.ballroom;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class activity_main extends AppCompatActivity {
    //region views
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
    private double balls = 0.0D;
    private double cost = 0.0D;
    private int efficiency = 65;
    private boolean isMetric = false;
    private boolean EasyMode = false;

    private static double estimateCosts(int ballcount, double price) {
        return Math.ceil(ballcount * price);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //fix for linear layout issue
        float density = getResources().getDisplayMetrics().density;
        if (density <= 1.5f){
            llswitch.setOrientation(LinearLayout.VERTICAL);
        }
        //bar watcher
        skpacking.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tvPercent = (TextView)findViewById(R.id.txtEfficiencyPercent);
                String z = Integer.toString(progress) + "%";
                tvPercent.setText(z);
                efficiency = progress;
                SetCount();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @OnClick(R.id.switchMetric)
    void onMetricSwitch() {
        this.MetricCheck();
        this.SetCount();
    }

    @OnClick(R.id.switchEasy)
    void onSwitchEasy() {
        this.EasySwitch();
    }

    private void MetricCheck()
    {
        //is metric
        if (swmetric.isChecked())
        {
            this.isMetric = true;
            money.setText(R.string.CommieCash);
            unitsmall.setText(R.string.centimeter);
            unitsquared.setText(R.string.MeterSquared);
            unitstd.setText(R.string.Meter);
            if (EasyMode) {
                etRadius.setText("7.6");
            }
        } else {
            this.isMetric = false;
            money.setText(R.string.muricadollars);
            unitsmall.setText(R.string.Inchs);
            unitsquared.setText(R.string.FeetSquared);
            unitstd.setText(R.string.Feet);
            if (EasyMode) {
                etRadius.setText("1.675");
            }
        }
    }

    private void EasySwitch()
    {
        if (easyswitch.isChecked())
        {
            this.efficiency = 64;
            llRadius.setVisibility(View.GONE);
            llEfficiency.setVisibility(View.GONE);
            EditText et = (EditText)findViewById(R.id.decimalRadius);
            if (isMetric){
                et.setText("7.6");
            } else {
                et.setText(("1.675"));
            }
            EasyMode = true;
        } else {
            llRadius.setVisibility(View.VISIBLE);
            llEfficiency.setVisibility(View.VISIBLE);
            EasyMode = false;
        }
    }

    private void SetCount()
    {
        try {
            String strarea = etArea.getText().toString();
            String strdepth = etDepth.getText().toString();
            String strradius = etRadius.getText().toString();
            String strmoney = etMoney.getText().toString();
            String textresult = "";
            if (!strarea.isEmpty() && !strdepth.isEmpty() && !strradius.isEmpty() &&
                    !strmoney.isEmpty()) {
                Double area = Double.parseDouble(strarea);
                Double depth = Double.parseDouble(strdepth);
                Double radius = Double.parseDouble(strradius);
                Double money = Double.parseDouble(strmoney);
                if (area > 0D && depth > 0 && radius > 0 && money > 0) {
                    if (isMetric) {
                        //MetricEstimate(efficiency, area, depth, radius, money);
                        this.balls = BallroomCalc.estimateBalls(efficiency, area, depth, radius,
                                money, BallroomCalc.cubcmpercubm);
                    } else {
                        this.balls = BallroomCalc.estimateBalls(efficiency, area, depth, radius,
                                money, BallroomCalc.cubinpercubft);
                    }
                    Resources res = getResources();
                    textresult = String.format(res.getString(R.string.formatedresult),
                            round(balls, 2), round(cost, 2));
                }
            }
            finaltext.setText(textresult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnTextChanged(value = {R.id.decimalArea, R.id.decimalDepth, R.id.decimalMoney,
            R.id.decimalRadius}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void updateCount() {
        SetCount();
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
