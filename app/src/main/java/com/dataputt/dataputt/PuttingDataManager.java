package com.dataputt.dataputt;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.dataputt.model.NormalDistributionPuttingModel;
import com.dataputt.model.PuttingModel;
import com.dataputt.model.StatisticalPuttingModel;
import com.dataputt.model.Units;

public class PuttingDataManager {
    private final String SAVE_FILE_NAME = "manager_saved_putting_model.dppm";  // dppm extension for DataPutt Putting Model
    private StatisticalPuttingModel puttingModel;
    private Context context;

    public PuttingDataManager(@NonNull Context context) {
        this.context = context;
        Context c = context.getApplicationContext();
        Object obj = null;
        try (ObjectInputStream is = new ObjectInputStream(context.openFileInput(SAVE_FILE_NAME))) {
            obj = is.readObject();
        } catch (FileNotFoundException e) {
            obj = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (obj != null && obj instanceof StatisticalPuttingModel) {
            this.puttingModel = (StatisticalPuttingModel) obj;
        } else {
            this.puttingModel = new NormalDistributionPuttingModel(
                    Units.feetToMeters(3), Units.feetToMeters(12), 10, 0.75, 0.15, 50);
        }
    }

    public PuttingModel getPuttingModel() {
        return new PuttingModelWrapper();
    }

    public void sync() {
        try (ObjectOutputStream os = new ObjectOutputStream(context.openFileOutput(SAVE_FILE_NAME, context.MODE_PRIVATE))) {
            os.writeObject(puttingModel);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

        public int getNumStations() {
            return puttingModel.getNumStations();
        }
    }
}
