package com.example.tp2_dev_mobile;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Exercice6Activity extends AppCompatActivity implements SensorEventListener {

    ImageView pic;
    TextView state_str;
    SensorManager manager;
    Sensor proximity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ex6);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximity = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        state_str = findViewById(R.id.proximity_state);
        pic = findViewById(R.id.dog_pic);


        if (proximity == null) {
            Toast.makeText(getApplicationContext(), "Pas de capteur de proximité", Toast.LENGTH_LONG).show();
            manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        } else {
            manager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] <= proximity.getMaximumRange() - 1.0) {
            state_str.setText("Trop proche !");
            pic.setImageResource(R.drawable.near_dog);
        } else {
            state_str.setText("Good boy!");
            pic.setImageResource(R.drawable.far_dog);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
}
