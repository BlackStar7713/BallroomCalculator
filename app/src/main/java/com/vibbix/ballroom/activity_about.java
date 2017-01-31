package com.vibbix.ballroom;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * The about menu activity
 */
public class activity_about extends AppCompatActivity {
        TextView version;
        String versionno;
        Toolbar toolbar;
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getVersion();
            version = (TextView)findViewById(R.id.versionnumber);
            String versionfmt = getString(R.string.versionstr);
            version.setText(String.format(versionfmt, versionno));
        }
        private void getVersion(){
            try {
                this.versionno = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
}
