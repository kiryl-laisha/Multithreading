package org.laisha.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.laisha.multithreading.util.EntityIdGenerator;

import java.util.concurrent.TimeUnit;

public class Terminal {

    private static final Logger logger = LogManager.getLogger();
    private final long terminalId;
    private static final long LOADING_TIME_ONE_OF_CAPACITY = 15;
    private static final long UNLOADING_TIME_ONE_OF_CAPACITY = 10;
    private static final int LOADING_OF_UNLOADED_TRUCK = 0;
    public static LogisticCentre centre;

    public Terminal() {
        this.terminalId = EntityIdGenerator.generateTerminalId();
    }

    public void loadTruck(Truck truck) {

        do {
            if (centre.getCurrentLoading() >= truck.getMaxCapacity()) {
                centre.unloadCargoFromCentre(truck.getMaxCapacity());
                waitProcessTime(truck.getMaxCapacity() * LOADING_TIME_ONE_OF_CAPACITY);
                truck.setCurrentLoading(truck.getMaxCapacity());
                break;
            } else {
                waitProcessTime(UNLOADING_TIME_ONE_OF_CAPACITY);
            }
        } while (true);
        logger.log(Level.DEBUG, "The truck id = {} is loaded. Quantity of cargo " +
                "is {}.", truck.getTruckId(), truck.getCurrentLoading());
    }

    public void unloadTruck(Truck truck) {

        do {
            if (centre.getCurrentLoading() + truck.getCurrentLoading()
                    <= centre.getCapacity()) {
                centre.storeCargoInCentre(truck.getCurrentLoading());
                waitProcessTime(truck.getCurrentLoading() * UNLOADING_TIME_ONE_OF_CAPACITY);
                truck.setCurrentLoading(LOADING_OF_UNLOADED_TRUCK);
                break;
            } else {
                waitProcessTime(LOADING_TIME_ONE_OF_CAPACITY);
            }
        } while (true);
        logger.log(Level.DEBUG, "The truck id = {} is unloaded. Quantity of unloaded " +
                "cargo is {}.", truck.getTruckId(), truck.getMaxCapacity());
    }

    public long getTerminalId() {
        return terminalId;
    }

    private void waitProcessTime(long processTime) {

        try {
            TimeUnit.MILLISECONDS.sleep(processTime);
        } catch (InterruptedException e) {
            logger.log(Level.WARN, "The thread \"{}\" was interrupted while sleeping. {}.",
                    Thread.currentThread().getName(), e);
            Thread.currentThread().interrupt();
        }
    }
}
