package com.dataputt.model;

public abstract class StatisticalPuttingModel implements PuttingModel {
    protected final double stationSeparation;
    protected final double station1;
    private final Units units;
    protected final double targetFreq;
    protected int puttsAttempted = 0;
    protected int puttsMade = 0;
    protected boolean calibrationMode = true;
    protected int calibrationPutts = 0;

    public StatisticalPuttingModel(double stationSeparation, double station1, double targetFreq) {
        this.stationSeparation = stationSeparation;
        this.station1 = station1;
        this.units = new Units(stationSeparation, station1);
        this.targetFreq = targetFreq;
    }

    public void setCalibrationMode(boolean enabled) {
        calibrationMode = enabled;
    }

    public boolean getCalibrationMode() {
        return calibrationMode;
    }

    public int getCalibrationPutts() {
        return calibrationPutts;
    }

    public abstract int getNextStation();

    public void makePutt() {
        if (calibrationMode) calibrationPutts++;
        else {
            puttsAttempted++;
            puttsMade++;
        }
    }
    public void missPutt() {
        if (calibrationMode) calibrationPutts++;
        else puttsAttempted++;
    }

    public abstract double probability(double meters);

    public int getPuttsAttempted() {
        return puttsAttempted;
    }

    public int getPuttsMade() {
        return puttsMade;
    }

    public Units getUnits() {
        return units;
    }
}
