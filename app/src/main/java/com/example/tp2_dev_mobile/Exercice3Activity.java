package com.example.tp2_dev_mobile;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Exercice3Activity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accel;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    View layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex3);

        layout = findViewById(R.id.color_layout);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        boolean supported = sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        if (!supported) {
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            Toast.makeText(getApplicationContext(), "ERREUR : Il n'y a pas d'acceléromètre", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent event) {
        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        float total_accel = Math.abs(linear_acceleration[0]) + Math.abs(linear_acceleration[1]) + Math.abs(linear_acceleration[2]);
        int color = Color.GREEN;

        float step = accel.getMaximumRange() / 3;
        if(total_accel <= step) {
            color = Color.GREEN;
        } else if(total_accel <= 2*step && total_accel >= step) {
            color = Color.YELLOW;
        } else  {
            color = Color.RED;
        }
        layout.setBackgroundColor(color);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
