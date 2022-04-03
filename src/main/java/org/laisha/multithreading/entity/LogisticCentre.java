package org.laisha.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.laisha.multithreading.exception.ProjectException;
import org.laisha.multithreading.reader.impl.ReaderFromFileImpl;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticCentre {

    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_FILE_PATH_FOR_DATA = "data\\centre_default_data.txt";
    private static final int CAPACITY_LIST_INDEX = 0;
    private static final int CURRENT_LOADING_LIST_INDEX = 1;
    private static final int TERMINAL_QUANTITY_LIST_INDEX = 2;
    private static final long UNLOADING_TIME_ONE_OF_CAPACITY = 3;

    private static LogisticCentre instance;
    private static AtomicBoolean isCreatedInstance = new AtomicBoolean(false);
    private static ReaderFromFileImpl reader = ReaderFromFileImpl.getInstance();
    private static ReentrantLock lock = new ReentrantLock();
    private static String filePathForCentreDatabase;
    private final Deque<Terminal> terminals = new ArrayDeque<>();
    private Semaphore nonPriorityTruckSemaphore = new Semaphore(1, true);
    private Semaphore priorityTruckSemaphore;
    private AtomicInteger currentLoading = new AtomicInteger();
    private int capacity;
    private int terminalQuantity;

    public LogisticCentre() {

        if (filePathForCentreDatabase == null) {
            filePathForCentreDatabase = DEFAULT_FILE_PATH_FOR_DATA;
        }
        List<String> centreDatabaseList;
        try {
            centreDatabaseList = reader.readStringListFromFile(filePathForCentreDatabase);
        } catch (ProjectException e) {
            logger.log(Level.ERROR, "The logistic centre structure could not be" +
                    "created. Further work is impossible.");
            throw new RuntimeException("Work is impossible without the structure of " +
                    "the logistic centre.");
        }
        capacity = Integer.parseInt(centreDatabaseList.get(CAPACITY_LIST_INDEX));
        currentLoading.set(Integer.parseInt(centreDatabaseList.get(CURRENT_LOADING_LIST_INDEX)));
        terminalQuantity = Integer.parseInt(centreDatabaseList.get(TERMINAL_QUANTITY_LIST_INDEX));
        for (int i = 0; i < terminalQuantity; i++) {
            terminals.add(new Terminal());
        }
        priorityTruckSemaphore = new Semaphore(terminalQuantity, true);
    }

    public static LogisticCentre getInstance() {

        if (!isCreatedInstance.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new LogisticCentre();
                    isCreatedInstance.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Terminal takeTerminal(TruckType type) {

        try {
            if (!type.equals(TruckType.REFRIGERATOR)) {
                nonPriorityTruckSemaphore.acquire();
                while (priorityTruckSemaphore.availablePermits() < 1) {
                    TimeUnit.MILLISECONDS.sleep(UNLOADING_TIME_ONE_OF_CAPACITY);
                }
                nonPriorityTruckSemaphore.release();
            }
            priorityTruckSemaphore.acquire();
        } catch (InterruptedException e) {
            logger.log(Level.WARN, "The thread \"{}\" was interrupted while waiting. {}",
                    Thread.currentThread().getName(), e);
            Thread.currentThread().interrupt();
        }
        Terminal currentTerminal;
        lock.lock();
        try {
            currentTerminal = terminals.poll();
            logger.log(Level.DEBUG, "The thread \"{}\" taken the terminal id = {}.",
                    Thread.currentThread().getName(), currentTerminal.getTerminalId());
        } finally {
            lock.unlock();
        }
        return currentTerminal;
    }

    public void releaseTerminal(Terminal terminal) {

        lock.lock();
        try {
            terminals.add(terminal);
            logger.log(Level.DEBUG, "The thread \"{}\" released the terminal id = {}.",
                    Thread.currentThread().getName(), terminal.getTerminalId());
        } finally {
            lock.unlock();
        }
        priorityTruckSemaphore.release();
    }

    public void storeCargoInCentre(int cargoQuantity) {
        currentLoading.getAndAdd(cargoQuantity);
    }

    public void unloadCargoFromCentre(int cargoQuantity) {
        currentLoading.set(currentLoading.get() - cargoQuantity);
    }

    public AtomicInteger getCurrentLoading() {
        return currentLoading;
    }

    public void setCurrentLoading(AtomicInteger currentLoading) {
        this.currentLoading = currentLoading;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Deque<Terminal> getTerminals() {
        return terminals;
    }
}
