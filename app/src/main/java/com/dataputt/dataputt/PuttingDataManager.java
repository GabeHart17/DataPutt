package com.dataputt.dataputt;

import com.dataputt.model.PuttingModel;
import com.dataputt.model.StatisticalPuttingModel;
import com.dataputt.model.Units;

public class PuttingDataManager {
    private StatisticalPuttingModel puttingModel;

    public PuttingModel getPuttingModel() {
        return new PuttingModelWrapper();
    }

    private class PuttingModelWrapper implements PuttingModel {
        public void setCalibrationMode(boolean enabled) {
            puttingModel.setCalibrationMode(enabled);
        }

        public boolean getCalibrationMode() {
            return puttingModel.getCalibrationMode();
        }

        public int getCalibrationPutts() {
            return puttingModel.getCalibrationPutts();
        }

        public int getNextStation() {
            return puttingModel.getNextStation();
        }

        public void makePutt() {
            puttingModel.makePutt();
        }
        public void missPutt() {
            puttingModel.missPutt();
        }

        public double probability(double meters) {
            return puttingModel.probability(meters);
        }

        public int getPuttsAttempted() {
            return puttingModel.getPuttsAttempted();
        }

        public int getPuttsMade() {
            return puttingModel.getPuttsMade();
        }

        public Units getUnits() {
            return puttingModel.getUnits();
        }
    }
}
