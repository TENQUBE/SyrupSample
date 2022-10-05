package com.tenqube.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tenqube.sample.syrup.main.VisualActivity;

import timber.log.Timber;

public class VisualSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main_test);
        checkPermission();
        findViewById(R.id.theme).setOnClickListener(v -> startTheme());
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(VisualSampleActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(VisualSampleActivity.this,
                    Manifest.permission.READ_SMS
            )) {

            } else {

                ActivityCompat.requestPermissions(VisualSampleActivity.this,
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        0);

            }
        }
    }

    private void startTheme() {
        startActivity(new Intent(this, VisualActivity.class));
    }
}