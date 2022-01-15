package com.dataputt.model;

import java.util.Random;

public class LinearInterpolationPuttingModel extends PuttingModel {
    private final double averagingDecayRate;
    private double[] stations;  // element 0 is station 1
    private int nextStation;
    private final double freqStdev;
    private final Random rng = new Random();

    // averaging decay rate is weight of newest value in decaying average between 0 and 1
    public LinearInterpolationPuttingModel(
            double stationSeparation, double station1, double targetFreq,
            double[] stationFreqs, double averagingDecayRate, double freqStdev) {
        super(stationSeparation, station1, targetFreq);
        this.averagingDecayRate = averagingDecayRate;
        stations = stationFreqs.clone();
        newNextStation();
        this.freqStdev = freqStdev;
    }

    private void newNextStation() {
        double p = rng.nextGaussian() * freqStdev + targetFreq;
        p = Math.min(1, Math.max(0, p));
        int bestIdx = 0;
        for (int i = 1; i < stations.length; i++) {
            if (Math.abs(stations[i] - p) < Math.abs(stations[bestIdx] - p)) {
                bestIdx = i;
            }
        }
        nextStation = bestIdx + 1;
    }

    public int getNextStation() {
        return nextStation;
    }

    @Override
    public void makePutt() {
        stations[nextStation - 1] *= 1 - averagingDecayRate;
        stations[nextStation - 1] += averagingDecayRate;
        newNextStation();
        super.makePutt();
    }

    @Override
    public void missPutt() {
        stations[nextStation - 1] *= 1 - averagingDecayRate;
        newNextStation();
        super.missPutt();
    }

    public double probability(double meters) {
        double asStations = units.metersToStations(meters);
        int nearStation = (int) Math.round(Math.floor(asStations));
        int farStation = (int) Math.round(Math.ceil(asStations));
        double p = -1;
        if (farStation > stations.length) {
            p = stations[stations.length - 1];
        } else if (nearStation < 1) {
            p = stations[0];
        } else {
            double pNear = stations[nearStation - 1];
            double pFar = stations[farStation - 1];
            p = pNear + (pFar - pNear) * (asStations - nearStation);
        }
        return p;
    }
}
