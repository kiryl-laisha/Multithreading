package org.laisha.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class LogisticCentreLoadingController extends TimerTask {

    private static final Logger logger = LogManager.getLogger();
    private static final LogisticCentre centre = LogisticCentre.getInstance();
    private static final int MIN_PERMISSIBLE_LOADING_PERCENT = 20;
    private static final int MAX_PERMISSIBLE_LOADING_PERCENT = 80;
    private static final int CONTROLLER_ACTION_PERCENT = 5;
    private int controllerBalance = 0;

    @Override
    public void run() {

        logger.log(Level.DEBUG, "Loading controller has been started.");
        int capacity = centre.getCapacity();
        int currentLoading = centre.getCurrentLoading();
        while (currentLoading < capacity * MIN_PERMISSIBLE_LOADING_PERCENT / 100) {
            int storedCargo = capacity * CONTROLLER_ACTION_PERCENT / 100;
            centre.storeCargoInCentre(storedCargo);
            controllerBalance = controllerBalance - storedCargo;
            currentLoading = centre.getCurrentLoading();
            logger.log(Level.DEBUG, "The logistic centre has been stored by {}.",
                    storedCargo);
        }
        while (currentLoading > capacity * MAX_PERMISSIBLE_LOADING_PERCENT / 100) {
            int unloadedCargo = capacity * CONTROLLER_ACTION_PERCENT / 100;
            centre.unloadCargoFromCentre(unloadedCargo);
            controllerBalance = controllerBalance + unloadedCargo;
            currentLoading = centre.getCurrentLoading();
            logger.log(Level.DEBUG, "The logistic centre has been unloaded by {}.",
                    unloadedCargo);
        }
        logger.log(Level.DEBUG, "Loading controller has been finished. " +
                "Loading controller balance is {}.", controllerBalance);
    }

    public int getControllerBalance() {
        return controllerBalance;
    }
}
