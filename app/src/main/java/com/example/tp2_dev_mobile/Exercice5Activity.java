package com.example.tp2_dev_mobile;

import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Exercice5Activity extends AppCompatActivity implements SensorEventListener {

    TextView flash_state;
    SensorManager manager;
    Sensor accel;
    boolean state = false;
    CameraManager cameraManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private final long SHAKE_DELAY = 1000;
    private final long SHAKE_MAX_DELAY = 1500;
    private final int SHAKE_NEED = 1;
    private long lastShake = 0;
    private int shakeCount = 0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ex5);

        flash_state = findViewById(R.id.flash_state);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        String str = state ? "On" : "Off";
        flash_state.setText(str);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        if (accel == null) {
            flash_state.setText("Pas d'accèléromètre disponible !");
            manager.unregisterListener(this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        } else {
            manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = Math.abs(mAccel * 0.9f + delta); // perform low-cut filter
        System.out.println(shakeCount);

        if (mAccel > 7.5) {
            System.out.println("mAccel : " + mAccel);
            System.out.println("shakeCount" + shakeCount);

            long now = System.currentTimeMillis();


            //Ignore les secousses trop rapprochées
            if (lastShake + SHAKE_DELAY > now) {
                return;
            }

            //Reset le shakeCount après avoir dépassé le délai entre 2 shake
            if (lastShake + SHAKE_MAX_DELAY < now) {
                shakeCount = 0;
            }
            lastShake = now;
            shakeCount++;

            if (shakeCount >= SHAKE_NEED) {
                onShake();
                shakeCount = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onShake() {
        System.out.println("On m'a secoué");
        state = !state;
        String str = state ? "On" : "Off";
        flash_state.setText(str);
        String camID = null;
        try {
            for (String camera : cameraManager.getCameraIdList()) {
                if (cameraManager.getCameraCharacteristics(camera).get(FLASH_INFO_AVAILABLE)) {
                    camID = camera;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraManager.setTorchMode(camID, state);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
