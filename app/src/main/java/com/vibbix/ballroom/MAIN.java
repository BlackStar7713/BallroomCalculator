package com.vibbix.ballroom;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
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


public class MAIN extends Activity {
    public double balls;
    public double cost;
    public int efficiency;
    public boolean isMetric;
    public boolean EasyMode;
    //views
    EditText etArea;
    EditText etDepth;
    EditText etRadius;
    EditText etMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isMetric = false;
        this.balls = 0.0D;
        this.cost = 0.0D;
        this.efficiency = 64;
        this.EasyMode = false;
        setContentView(R.layout.activity_main);
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
                           MetricEstimate(efficiency, area, depth, radius, money);
                       } else {
                           ImperialEstimate(efficiency, area, depth, radius, money);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ImperialEstimate(double efficiency, double footage, double depth, double radius,
                               double price)
    {
        double cubinpercubft = 1728D;
        efficiency = efficiency/100;
        double wholevolume = footage*depth;
        double usablevolume = wholevolume * efficiency;
        double ballvolume = 4/3 * Math.PI * Math.pow(radius, 3) / cubinpercubft;
        this.balls = Math.floor(usablevolume/ballvolume);
        this.cost = Math.ceil(balls*price*100)/100;
    }

    public void MetricEstimate(double efficiency, double footage, double depth, double radius,
                               double price)
    {
        double cubcmpercubm = Math.pow(10, 6);
        efficiency = efficiency/100;
        double wholevolume = footage*depth;
        double usablevolume = wholevolume * efficiency;
        double ballvolume = 4/3 * Math.PI * Math.pow(radius, 3) / 1 / cubcmpercubm;
        this.balls = Math.floor(usablevolume/ballvolume);
        this.cost = Math.ceil(balls*price*100)/100;
    }
}
