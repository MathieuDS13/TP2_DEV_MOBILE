package com.example.tp2_dev_mobile;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Exercice4Activity extends AppCompatActivity implements SensorEventListener {

    private static final float ALPHA = 0.99f ;
    TextView direction;
    SensorManager manager;
    Sensor accel;
    String directionStr;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex4);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        direction = findViewById(R.id.direction_text);

        if (accel == null) {
            direction.setText("Pas d'accèléromètre disponible !");
            manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        } else {
            manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        lowPassFilter(linear_acceleration);

        int max = -1;

        if (Math.abs(linear_acceleration[0]) > Math.abs(linear_acceleration[1]) ) {
            max = 0;
        } else {
            max = 1;
        }


        if (Math.abs(linear_acceleration[max]) < Math.abs(linear_acceleration[2])) {
            max = 2;
        }
        System.out.println(max);
        directionStr ="";
        float precision = 0.60f;
        switch (max) {
            case 0:
                if (linear_acceleration[0] < 0 && linear_acceleration[0] < -precision) {
                    directionStr = "Droite";
                } else if(linear_acceleration[0] > precision) directionStr = "Gauche";
                break;

            case 1:
                if (linear_acceleration[1] < 0 && linear_acceleration[1] < -precision) {
                    directionStr = "Arrière";
                } else if(linear_acceleration[1] > precision) directionStr = "Avant";
                break;
            case 2:
                if (linear_acceleration[2] < 0 && linear_acceleration[2] < -precision) {
                    directionStr = "Bas";
                } else if(linear_acceleration[2] > precision) directionStr = "Haut";
                break;
        }

        this.direction.setText(directionStr);
        //System.out.println("x : " + linear_acceleration[0] + "; y : " + linear_acceleration[1] + "; z : " + linear_acceleration[2] + " Direction : " + directionStr);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void lowPassFilter(float[] input) {
        for ( int i=0; i<input.length; i++ ) {
            linear_acceleration[i] = linear_acceleration[i] + (1- ALPHA) * (input[i] - linear_acceleration[i]);
        }
    }
}
