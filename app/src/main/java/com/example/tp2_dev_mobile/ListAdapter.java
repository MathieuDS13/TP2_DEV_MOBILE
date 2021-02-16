package com.example.tp2_dev_mobile;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class ListAdapter extends ArrayAdapter<Sensor> {

    public ListAdapter(@NonNull Context context, List<Sensor> sensors) {
        super(context, 0, sensors);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_sensor, parent, false);
        }

        SensorHolder sensorHolder = (SensorHolder) convertView.getTag();
        if (sensorHolder == null) {
            sensorHolder = new SensorHolder();
            sensorHolder.sensorName = convertView.findViewById(R.id.sensor_name);
        }

        Sensor sensor = getItem(position);
        sensorHolder.sensorName.setText(sensor.getName());

        return convertView;
    }

    private class SensorHolder {
        public TextView sensorName;
    }
}
