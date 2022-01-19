package com.dataputt.model;

public interface PuttingModel {
    public void setCalibrationMode(boolean enabled);
    public boolean getCalibrationMode();
    public int getCalibrationPutts();
    public int getNextStation();
    public void makePutt();
    public void missPutt();
    public double probability(double meters);
    public int getPuttsAttempted();
    public int getPuttsMade();
    public Units getUnits();
    public int getNumStations();
}
