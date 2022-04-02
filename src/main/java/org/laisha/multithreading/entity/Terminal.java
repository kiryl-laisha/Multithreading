package org.laisha.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.laisha.multithreading.util.EntityIdGenerator;

import java.util.concurrent.TimeUnit;

public class Terminal {

    private static final Logger logger = LogManager.getLogger();
    private static final long LOADING_TIME_ONE_OF_CAPACITY = 5;
    private static final long UNLOADING_TIME_ONE_OF_CAPACITY = 3;
    private final long terminalId;
    private final LogisticCentre centre = LogisticCentre.getInstance();

    public Terminal() {
        this.terminalId = EntityIdGenerator.generateTerminalId();
    }

    public void loadTruck(Truck truck) {

        do {
            if (centre.getCurrentLoading().get() >= truck.getCapacity()) {
                centre.unloadCargoFromCentre(truck.getCapacity());
                waitProcessTime(truck.getCapacity() * LOADING_TIME_ONE_OF_CAPACITY);
                break;
            } else {
                waitProcessTime(UNLOADING_TIME_ONE_OF_CAPACITY);
            }
        } while (true);
        logger.log(Level.DEBUG, "The truck id = {} is loaded. Quantity of cargo " +
                "is {}.", truck.getTruckId(), truck.getCapacity());
    }

    public void unloadTruck(Truck truck) {

        do {
            if (centre.getCurrentLoading().get() + truck.getCapacity() <= centre.getCapacity()) {
                centre.storeCargoInCentre(truck.getCapacity());
                waitProcessTime(truck.getCapacity() * UNLOADING_TIME_ONE_OF_CAPACITY);
                break;
            } else {
                waitProcessTime(LOADING_TIME_ONE_OF_CAPACITY);
            }
        } while (true);
        logger.log(Level.DEBUG, "The truck id = {} is unloaded. Quantity of unloaded " +
                "cargo is {}.", truck.getTruckId(), truck.getCapacity());
    }

    public long getTerminalId() {
        return terminalId;
    }

    private void waitProcessTime(long processTime) {

        try {
            TimeUnit.MILLISECONDS.sleep(processTime);
        } catch (InterruptedException e) {
            logger.log(Level.WARN, "The thread \"{}\" was interrupted while sleeping. {}",
                    Thread.currentThread().getName(), e);
            Thread.currentThread().interrupt();
        }
    }
}
