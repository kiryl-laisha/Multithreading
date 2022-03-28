package org.laisha.multithreading.entity;

import java.lang.reflect.GenericArrayType;
import java.util.PrimitiveIterator;

public class Truck implements Runnable {

    private long truckId;
    private int capacity;
    private TruckType type;
    private TruckAssigment assigment;

    public enum TruckAssigment {
        LOADING, UNLOADING
    }

    public Truck(long truckId, int capacity, TruckType type, TruckAssigment assigment) {
        this.truckId = truckId;
        this.capacity = capacity;
        this.type = type;
        this.assigment = assigment;
    }

    @Override

    public void run() {

    }
}
