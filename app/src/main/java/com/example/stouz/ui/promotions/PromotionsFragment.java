package com.example.stouz.ui.promotions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stouz.R;
import com.example.stouz.ShakeDetector;

public class PromotionsFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView promoCodeTextView;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;
    private int progress = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_promotions, container, false);
        progress = 0;
        progressBar = root.findViewById(R.id.progressBar);
        promoCodeTextView = root.findViewById(R.id.promoCodeTextView);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(count -> {
            progress += 20; // Increase progress faster
            progressBar.setProgress(progress);

            if (progress >= 100) {
                progressBar.setVisibility(View.GONE);
                promoCodeTextView.setVisibility(View.VISIBLE);
                promoCodeTextView.setText("Promocyjny super kod");
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}
