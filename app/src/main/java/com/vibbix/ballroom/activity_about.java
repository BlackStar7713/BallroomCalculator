package com.vibbix.ballroom;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The about menu activity
 */
public class activity_about extends AppCompatActivity {
    @BindView(R.id.versionnumber)
        TextView version;
    @BindView(R.id.toolbar)
        Toolbar toolbar;
    @BindString(R.string.versionstr)
    String versionfmt;
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            ButterKnife.bind(this);
            setSupportActionBar(toolbar);
            String versionno = "ERROR";
            try {
                versionno = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            this.version.setText(String.format(versionfmt, versionno));

        }
}
