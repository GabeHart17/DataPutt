/*
 Putting is modeled as having a normally distributed angle off of the basket's center line.
 The parameters of this normal distribution are assumed to be independent of distance.
 The probability of making a put at a given distance depends on the size of the basket, which is
 2*atan(w/2d) where w is the distance to the basket and d is the distance. Putting distances are
 large enough and basket sizes small enough that small angle approximations can be used, meaning
 that the probability of making a put is the probability that the putt's angle off the center line
 is less than w/d. That is, p = normCDF(-w/d,w/d,mu=0,sigma). We then have that
 invErf(p) = k*(w/d) for some constant k. Thus, d*invErf(p) is constant for all distances. This
 constant will be called the errorConstant. At a given distance d, p = erf(errorConstant/d).
 The distance d at which a putt will have probability p is d = errorConstant/invErf(p).
 */

package com.dataputt.model;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.distribution.NormalDistribution;


public class NormalDistributionPuttingModel extends PuttingModel {
    private final double freqStdev;
    private final Random rng = new Random();
    private final NormalDistribution freqDist;
    private final ArrayList<PuttingStation> stations = new ArrayList<>();
    private final int calibrationPuttsNeeded;
    private PuttingStation nextStation;

    public NormalDistributionPuttingModel(double stationSeparation, double station1, double targetFreq,
                                          int numStations, double freqStdev, int calibrationPutts) {
        super(stationSeparation, station1, targetFreq);
        this.freqStdev = freqStdev;
        this.freqDist = new NormalDistribution(targetFreq, freqStdev);
        this.calibrationPuttsNeeded = calibrationPutts;
        for (int i = 0; i < numStations; i++) {
            stations.add(new PuttingStation(i+1,
                    this.units.stationsToMeters(i+1), 100));
        }
        nextStation = stations.get(0);
    }

    private double computeErrorConstant() {
        double acc = 0;
        for (PuttingStation s : stations) {
            acc += Erf.erfInv(s.laplaceSuccessionProbability()) * s.getDistance();
        }
        return acc / stations.size();
    }

    private void generateNextStation() {
        if (calibrationMode) {
            // uniform random station
            nextStation = stations.get(rng.nextInt(stations.size()));
            if (calibrationPutts >= calibrationPuttsNeeded) setCalibrationMode(false);
        } else {
            double ec = computeErrorConstant();
            double freq = Math.max(0.0, freqDist.sample());
            double targetDistance = ec / Erf.erfInv(freq);
            // select outside or inside target depending on whether rate is above or below desired
            double currentRate = Integer.valueOf(puttsMade).doubleValue() / Integer.valueOf(puttsAttempted).doubleValue();
            double distDirectionMultiplier = currentRate < targetFreq ? -1 : 1;
            PuttingStation best = stations.get(0);
            for (PuttingStation s : stations) {
                double dist = (s.getDistance() - targetDistance) * distDirectionMultiplier;
                if (dist > 0 && dist < Math.abs(best.getDistance() - targetDistance)) {
                    best = s;
                }
            }
            if ((best.getDistance() - targetDistance) * distDirectionMultiplier > 0) {
                nextStation = best;
            } else if (distDirectionMultiplier < 0) {
                nextStation = stations.get(0);
            } else {
                nextStation = stations.get(stations.size() - 1);
            }
        }
    }

    public int getNextStation() {
        return nextStation.getStationNumber();
    }

    @Override
    public void makePutt() {
        super.makePutt();
        nextStation.putt(true);
        generateNextStation();
    }

    @Override
    public void missPutt() {
        super.missPutt();
        nextStation.putt(false);
        generateNextStation();
    }

    @Override
    public double probability(double meters) {
        double ec = computeErrorConstant();
        return Erf.erf(ec / meters);
    }

    private static class PuttingStation {
        private final int stationNumber;
        private final double distance;
        private final int maxPutts;
        private LinkedList<Boolean> putts = new LinkedList<>();
        private int totalMade = 0;

        public PuttingStation(int stationNumber, double distance, int maxPutts) {
            this.stationNumber = stationNumber;
            this.distance = distance;
            this.maxPutts = maxPutts;
        }

        public void putt(Boolean made) {
            putts.addFirst(made);
            if (made) totalMade++;
            if (putts.size() > maxPutts) {
                if (putts.removeLast()) totalMade--;
            }
        }

        public int getStationNumber() {
            return stationNumber;
        }

        public double getDistance() {
            return distance;
        }

        public int getTotalPutts() {
            return putts.size();
        }

        public int getTotalMade() {
            return totalMade;
        }

        public double getProportionMade() {
            if (putts.size() == 0) return 0.5;  // uniform distribution when now data
            return Integer.valueOf(totalMade).doubleValue() / Integer.valueOf(putts.size()).doubleValue();
        }

        public double laplaceSuccessionProbability() {
            return Integer.valueOf(totalMade + 1).doubleValue() / Integer.valueOf(putts.size() + 2).doubleValue();
        }
    }
}
