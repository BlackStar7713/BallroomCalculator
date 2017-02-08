package com.vibbix.ballroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * The about menu activity
 */
public class activity_about extends AppCompatActivity {
    //region binded views
    @BindView(R.id.versionnumber)
    TextView version;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_nightmode)
    AppCompatSpinner nightmode;
    @BindString(R.string.versionstr)
    String versionfmt;
    @BindString(R.string.link_finn)
    String finnlink;
    @BindString(R.string.link_berge)
    String bergelink;
    @BindArray(R.array.spinner_nightmode_items)
    String[] nightmodeItems;
    @BindString(R.string.prefs_nightmode)
    String prefsNightmode;
    @BindString(R.string.prefs_name)
    String prefsName;
    //endregion

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        String versionno = "ERROR";
        try {
            versionno = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.version.setText(String.format(versionfmt, versionno));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_nightmode_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nightmode.setAdapter(adapter);
        loadNightMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_finn:
                Intent finnIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(this.finnlink));
                startActivity(finnIntent);
                return true;
            case R.id.action_berge:
                Intent bergeIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(this.bergelink));
                startActivity(bergeIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemSelected(value = R.id.spinner_nightmode, callback = OnItemSelected.Callback.ITEM_SELECTED)
    void selectNightMode(int position) {
        SharedPreferences settings = getSharedPreferences(prefsName, 0);
        SharedPreferences.Editor edit = settings.edit();
        if (position == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
            edit.putString(prefsNightmode, nightmodeItems[0]);
        } else if (position == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            edit.putString(prefsNightmode, nightmodeItems[1]);
        } else if (position == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            edit.putString(prefsNightmode, nightmodeItems[2]);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            edit.putString(prefsNightmode, nightmodeItems[3]);
        }
        edit.apply();
        this.getDelegate().applyDayNight();
    }

    private void loadNightMode() {
        SharedPreferences settings = getSharedPreferences(prefsName, 0);
        String mode = settings.getString(this.prefsNightmode, "Auto");
        if (mode.equals("Auto"))
            this.nightmode.setSelection(0, true);
        else if (mode.equals("Day"))
            this.nightmode.setSelection(1, true);
        else if (mode.equals("Night"))
            this.nightmode.setSelection(2, true);
        else if (mode.equals("Follow"))
            this.nightmode.setSelection(3, true);
    }


}
