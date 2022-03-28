package org.laisha.multithreading.util;

public class EntityIdGenerator {

    private static long terminalId = 0;
    private static long truckId = 1000;

    private EntityIdGenerator() {
    }

    public static long generateTruckId() {
        return truckId++;
    }

    public static long generateTerminalId() {
        return terminalId++;
    }
}
