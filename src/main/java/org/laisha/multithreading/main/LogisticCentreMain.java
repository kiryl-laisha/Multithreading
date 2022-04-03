package org.laisha.multithreading.main;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.laisha.multithreading.entity.LogisticCentreLoadingController;
import org.laisha.multithreading.entity.Truck;
import org.laisha.multithreading.exception.ProjectException;
import org.laisha.multithreading.parser.impl.TruckParserImpl;
import org.laisha.multithreading.reader.impl.ReaderFromFileImpl;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogisticCentreMain {

    private static final Logger logger = LogManager.getLogger();
    private static final ReaderFromFileImpl reader = ReaderFromFileImpl.getInstance();
    private static final TruckParserImpl parser = TruckParserImpl.getInstance();
    private static final String TRUCK_DATABASE_FILE_PATH = "data/trucks_database.txt";
    private static final int TIME_PERIOD = 1000;

    public static void main(String[] args) {

        List<String> truckDatabase;
        try {
            truckDatabase = reader.readStringListFromFile(TRUCK_DATABASE_FILE_PATH);
        } catch (ProjectException e) {
            logger.log(Level.ERROR, "The truck database could not be " +
                    "created. Further work is impossible.");
            throw new RuntimeException("Work is impossible without the truck database.");
        }
        List<Truck> trucks = parser.parseTruckDatabase(truckDatabase);
        Timer timer = new Timer();
        LogisticCentreLoadingController controller = new LogisticCentreLoadingController();
        timer.schedule(controller, TIME_PERIOD, TIME_PERIOD);
        ExecutorService executorService = Executors.newFixedThreadPool(trucks.size());
        trucks.forEach(executorService::execute);
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(TIME_PERIOD);
            } catch (InterruptedException e) {
                logger.log(Level.WARN, "The thread \"{}\" was interrupted while sleeping. {}",
                        Thread.currentThread().getName(), e);
                Thread.currentThread().interrupt();
            }
        }
        logger.log(Level.INFO, "Logistic centre has ended its working. " +
                "Loading controller balance is {}.", controller.getControllerBalance());
        controller.cancel();
    }
}
