package com.example.tp2_dev_mobile;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Exercice2Activity extends AppCompatActivity {

    Button playButton;
    TextView confirmMsg;
    private SensorManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        confirmMsg = findViewById(R.id.confirm_msg);

        playButton = findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sensor accel = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);

                if(accel == null) {
                    Toast.makeText(getApplicationContext(), "Pas de capteur de gravité trouvé", Toast.LENGTH_LONG).show();
                    accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if(accel == null) {
                        confirmMsg.setText("Aucun accéléromètre de disponible, vous ne pouvez pas jouer à ce jeu !");
                    } else {
                        confirmMsg.setText("Le jeu va utiliser l'acceléromètre' " + accel.getName());
                    }
                } else  {
                    confirmMsg.setText("Le jeu va utiliser le capteur de gravité " + accel.getName());
                }
            }
        });
    }

}
