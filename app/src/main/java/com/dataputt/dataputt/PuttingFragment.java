package com.dataputt.dataputt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataputt.dataputt.databinding.FragmentPuttingBinding;
import com.dataputt.model.LinearInterpolationPuttingModel;
import com.dataputt.model.NormalDistributionPuttingModel;
import com.dataputt.model.PuttingModel;
import com.dataputt.model.Units;

import java.util.Objects;

public class PuttingFragment extends Fragment {

    private FragmentPuttingBinding binding;
    private PuttingModel puttingModel;

    public PuttingFragment() {
        super(R.layout.fragment_putting);
//        double[] inits = new double[10];
//        for (int i = 0; i < 10; i++) inits[i] = 0.5;
//        puttingModel = new LinearInterpolationPuttingModel(
//                Units.feetToMeters(3), Units.feetToMeters(12), 0.75, inits, 0.1, 0.15);
        puttingModel = new NormalDistributionPuttingModel(Units.feetToMeters(3), Units.feetToMeters(12), 0.75, 10, 0.15, 50);
//        puttingModel = ((MainActivity) Objects.requireNonNull(getActivity())).getPuttingModel();
    }

    private void updateStationInfo() {
        int nextStation = puttingModel.getNextStation();
        binding.stationText.setText(Integer.valueOf(nextStation).toString());
        String[] ftNumber = Double.valueOf(puttingModel.units.stationsToFeet(nextStation)).toString().split("\\.");
        binding.stationDistanceText.setText(ftNumber[0] + "." + ftNumber[1].substring(0, Math.min(ftNumber[1].length(), 2)) + " ft");
        if (puttingModel.getCalibrationMode())  {
            binding.puttCounterText.setText("calibration putts: " + Integer.valueOf(puttingModel.getCalibrationPutts()).toString());
        }
        else {
            binding.puttCounterText.setText("putts made/attempted: " +
                    Integer.valueOf(puttingModel.getPuttsMade()).toString() +
                    " / " + Integer.valueOf(puttingModel.getPuttsAttempted()).toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPuttingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.makeButton.setOnClickListener(v -> {
            puttingModel.makePutt();
            updateStationInfo();
        });

        binding.missButton.setOnClickListener(v -> {
            puttingModel.missPutt();
            updateStationInfo();
        });

        updateStationInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}