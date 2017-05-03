package com.eduparra.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
    }

    public void onClick(View view)
    {
        ReadingSensors.terminate();
        stopService(new Intent(this,ReadingSensors.class));
        startActivity(new Intent(this, MainActivity.class));
    }
}
