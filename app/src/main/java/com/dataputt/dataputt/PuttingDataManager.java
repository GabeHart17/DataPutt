package com.dataputt.dataputt;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.dataputt.model.NormalDistributionPuttingModel;
import com.dataputt.model.PuttingModel;
import com.dataputt.model.StatisticalPuttingModel;
import com.dataputt.model.Units;

public class PuttingDataManager {
    private final String SAVE_FILE_NAME = "manager_saved_putting_model.dppm";  // dppm extension for DataPutt Putting Model
    private StatisticalPuttingModel puttingModel;
    private Context context;

    public PuttingDataManager(Context context) {
        this.context = context;
        this.puttingModel = new NormalDistributionPuttingModel(Units.feetToMeters(3), Units.feetToMeters(12), 0.75, 10, 0.15, 50);
    }

    public PuttingModel getPuttingModel() {
        return new PuttingModelWrapper();
    }

    private void sync() {
        try (ObjectOutputStream os = new ObjectOutputStream(context.openFileOutput(SAVE_FILE_NAME, context.MODE_PRIVATE))) {
            os.writeObject(puttingModel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class PuttingModelWrapper implements PuttingModel {
        public void setCalibrationMode(boolean enabled) {
            puttingModel.setCalibrationMode(enabled);
            sync();
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
            sync();
        }
        public void missPutt() {
            puttingModel.missPutt();
            sync();
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
