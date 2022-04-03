package org.laisha.multithreading.entity;

import org.laisha.multithreading.util.EntityIdGenerator;

public class Truck extends Thread {

    private final long truckId;
    private TruckType type;
    private int currentLoading;
    private int maxCapacity;

    public Truck(TruckType type, int currentLoading, int maxCapacity) {

        this.truckId = EntityIdGenerator.generateTruckId();
        this.type = type;
        this.currentLoading = currentLoading;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void run() {

        LogisticCentre centre = LogisticCentre.getInstance();
        Terminal currentTerminal = centre.takeTerminal(type);
        if (currentLoading == 0) {
            currentTerminal.loadTruck(this);
        } else {
            currentTerminal.unloadTruck(this);
        }
        centre.releaseTerminal(currentTerminal);
    }

    public long getTruckId() {
        return truckId;
    }

    public TruckType getType() {
        return type;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    public int getCurrentLoading() {
        return currentLoading;
    }

    public void setCurrentLoading(int currentLoading) {
        this.currentLoading = currentLoading;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Truck truck = (Truck) obj;
        if (truckId != truck.truckId) {
            return false;
        }
        if (currentLoading != truck.currentLoading) {
            return false;
        }
        if (maxCapacity != truck.maxCapacity) {
            return false;
        }
        return type == truck.type;
    }

    @Override
    public int hashCode() {

        int result = (int) (truckId ^ (truckId >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + currentLoading;
        result = 31 * result + maxCapacity;
        return result;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("truckId=").append(truckId);
        sb.append(", type=").append(type);
        sb.append(", currentCapacity=").append(currentLoading);
        sb.append(", maxCapacity=").append(maxCapacity);
        sb.append('}');
        return sb.toString();
    }
}
