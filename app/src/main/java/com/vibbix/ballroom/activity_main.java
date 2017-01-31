package com.vibbix.ballroom;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

public class activity_main extends AppCompatActivity {
    public double balls;
    public double cost;
    public int efficiency;
    public boolean isMetric;
    public boolean EasyMode;
    BigDecimal cubcmpercubm = BigDecimal.valueOf(Math.pow(10, 6));
    BigDecimal cubinpercubft = BigDecimal.valueOf(1728D);

    //views
    EditText etArea;
    EditText etDepth;
    EditText etRadius;
    EditText etMoney;
    Toolbar toolbar;
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.isMetric = false;
        this.balls = 0.0D;
        this.cost = 0.0D;
        this.efficiency = 64;
        this.EasyMode = false;
        etArea = (EditText)findViewById(R.id.decimalArea);
        etArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                SetCount();
            }
        });
        etDepth = (EditText)findViewById(R.id.decimalDepth);
        etDepth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                SetCount();
            }
        });
        etRadius = (EditText)findViewById(R.id.decimalRadius);
        etRadius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SetCount();
            }
        });
        etMoney = (EditText)findViewById(R.id.decimalMoney);
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                SetCount();
            }
        });
        //fix for linear layout issue
        float density = getResources().getDisplayMetrics().density;
        if (density <= 1.5f){
            LinearLayout llswitch = (LinearLayout)findViewById(R.id.llSwitch);
            llswitch.setOrientation(LinearLayout.VERTICAL);
        }
        //metric switch watcher
        Switch swmetric = (Switch)findViewById(R.id.switchMetric);
        swmetric.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MetricCheck();
                SetCount();
            }
        });
        //easy switch watcher
        Switch sweasy = (Switch)findViewById(R.id.switchEasy);
        sweasy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EasySwitch();
            }
        });
        //bar watcher
        SeekBar skpacking = (SeekBar)findViewById(R.id.seekEfficiency);
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
        //
    }

    private void MetricCheck()
    {
        Switch swmetric = (Switch)findViewById(R.id.switchMetric);
        TextView money = (TextView)findViewById(R.id.txtunitmoney);
        TextView unitsmall = (TextView)findViewById(R.id.txtunitsmall);
        TextView unitsquared = (TextView)findViewById(R.id.txtunitSquared);
        TextView unitstd = (TextView)findViewById(R.id.txtunitstd);
        EditText et = (EditText)findViewById(R.id.decimalRadius);
        //is metric
        if (swmetric.isChecked())
        {
            this.isMetric = true;
            money.setText(R.string.CommieCash);
            unitsmall.setText(R.string.centimeter);
            unitsquared.setText(R.string.MeterSquared);
            unitstd.setText(R.string.Meter);
            if (EasyMode) {
                et.setText("7.6");
            }
        } else {
            this.isMetric = false;
            money.setText(R.string.muricadollars);
            unitsmall.setText(R.string.Inchs);
            unitsquared.setText(R.string.FeetSquared);
            unitstd.setText(R.string.Feet);
            if (EasyMode) {
                et.setText("1.675");
            }
        }
    }

    private void EasySwitch()
    {
        Switch easyswitch = (Switch)findViewById(R.id.switchEasy);
        LinearLayout llEfficiency = (LinearLayout)findViewById(R.id.llEfficiency);
        LinearLayout llRadius = (LinearLayout)findViewById(R.id.llRadius);

        if (easyswitch.isChecked())
        {
            efficiency = 64;
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
           String strarea = etArea.getText().toString();
           String strdepth = etDepth.getText().toString();
           String strradius = etRadius.getText().toString();
           String strmoney = etMoney.getText().toString();
           String textresult = "";
           TextView finaltext = (TextView)findViewById(R.id.txtResult);
           if (strarea != null || strdepth != null || strradius != null || strmoney != null){
               if (!strarea.isEmpty() && !strdepth.isEmpty() && !strradius.isEmpty()&&
                       !strmoney.isEmpty()) {
                   Double area = Double.parseDouble(strarea);
                   Double depth = Double.parseDouble(strdepth);
                   Double radius = Double.parseDouble(strradius);
                   Double money = Double.parseDouble(strmoney);
                   if(area > 0D && depth >0 && radius >0 && money >0){
                       if (isMetric){
                           //MetricEstimate(efficiency, area, depth, radius, money);
                           EstimateBalls(efficiency, area, depth, radius, money,cubcmpercubm);
                       } else {
                           EstimateBalls(efficiency, area, depth, radius, money,cubinpercubft);
                       }

                       Resources res = getResources();
                       textresult = String.format(res.getString(R.string.formatedresult),
                               round(balls,2),round(cost,2));
                   }
               }
           }
        finaltext.setText(textresult);
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


    public void EstimateBalls(double efficiency, double footage, double depth, double radius,
                         double price, BigDecimal measurementscale)
    {
        //BigDecimal cubcmpercubm = BigDecimal.valueOf(Math.pow(10, 6));
        efficiency = efficiency/100;
        BigDecimal wholevolume = BigDecimal.valueOf(footage*depth);
        BigDecimal usablevolume =wholevolume.multiply(BigDecimal.valueOf(efficiency));
//        BigDecimal ballvolume = BigDecimal.valueOf(4/3 * Math.PI * Math.pow(radius, 3) / 1
//                / cubcmpercubm);
        BigDecimal ballvolume = BigDecimal.valueOf(4).divide(BigDecimal.valueOf(3),10,
                RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(Math.PI)).multiply(
                BigDecimal.valueOf(Math.pow(radius,3))).divide(BigDecimal.valueOf(1),10,
                RoundingMode.HALF_UP).divide
                (measurementscale,10,RoundingMode.HALF_UP);
        this.balls = usablevolume.divide(ballvolume,10,RoundingMode.HALF_UP).doubleValue();
        //this.balls = Math.floor(usablevolume/ballvolume);
        this.cost = Math.ceil(balls*price*100)/100;
    }
}
