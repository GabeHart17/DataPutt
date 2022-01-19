package com.dataputt.dataputt;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.dataputt.dataputt.databinding.FragmentStatsBinding;
import com.dataputt.model.PuttingModel;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    private PuttingModel puttingModel;

    private Random rng = new Random();

    public StatsFragment() {
        super(R.layout.fragment_putting);
    }

    private void drawAccuracyGraph(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint pBlack = new Paint();
        pBlack.setColor(Color.BLACK);
        pBlack.setStrokeWidth(4);
        int margin = 50;
        canvas.drawLine(margin, height - margin, width, height - margin, pBlack);
        canvas.drawLine(margin, 0, margin, height - margin, pBlack);
        Paint pBlue = new Paint();
        pBlue.setColor(Color.BLUE);
        pBlue.setStrokeWidth(4);
        double perStation = Integer.valueOf(width - margin).doubleValue() / puttingModel.getNextStation();
        for (int i = margin; i < width; i++) {
            double rate = puttingModel.probability(puttingModel.getUnits().stationsToMeters((i - margin) / perStation));
            double scale = Integer.valueOf(height - margin).doubleValue();
            canvas.drawPoint(i, height - (float) (scale * rate) - margin, pBlue);
        }
//        double rate = Math.abs(rng.nextDouble());
//        rate -= Math.floor(rate);
//        for (int i = margin; i < width; i++) {
//            double scale = Integer.valueOf(height - margin).doubleValue() / 100;
//            double y = scale * rate * 100;
//            canvas.drawPoint(i, (float) (height - y - margin), pBlue);
//        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            puttingModel = ((MainActivity) context).getPuttingModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer attempted = puttingModel.getPuttsAttempted();
        binding.statsPuttsAttempted.setText("Putts attempted: " + attempted.toString());
        Integer made = puttingModel.getPuttsMade();
        binding.statsPuttsMade.setText("Putts made: " + made.toString());
        double proportion = made.doubleValue() / attempted.doubleValue();
        binding.statsPercentMade.setText("Percent made: " + Long.valueOf(Math.round(proportion * 100)).toString());
        binding.statsAccuracyGraph.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                drawAccuracyGraph(surfaceHolder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                drawAccuracyGraph(surfaceHolder);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}