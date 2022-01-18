package com.dataputt.model;

import java.io.Serializable;

public class Units implements Serializable {
    private static final double feetPerMeter = 3.280839895;
    private final double metersPerStation;
    private final double station1;

    public Units(double metersPerStation, double station1) {
        this.metersPerStation = metersPerStation;
        this.station1 = station1;
    }

    public static double feetToMeters(double feet) {
        return feet / feetPerMeter;
    }

    public static double metersToFeet(double meters) {
        return meters * feetPerMeter;
    }

    public double stationsToMeters(double stations) {
        return (stations * metersPerStation) + station1 - metersPerStation;
    }

    public double metersToStations(double meters) {
        return (meters - station1 + metersPerStation) / metersPerStation;
    }

    public double stationsToFeet(double stations) {
        return metersToFeet(stationsToMeters(stations));
    }

    public double feetToStations(double feet) {
        return metersToStations(feetToMeters(feet));
    }
}
