package org.laisha.multithreading.entity;

import org.laisha.multithreading.util.EntityIdGenerator;

public class Truck implements Runnable {

    private final long truckId;
    private TruckType type;
    private int capacity;

    public Truck(TruckType type, int capacity) {

        this.truckId = EntityIdGenerator.generateTruckId();
        this.type = type;
        this.capacity = capacity;
    }

    @Override
    public void run() {

        LogisticCentre centre = LogisticCentre.getInstance();
        Terminal currentTerminal = centre.takeTerminal(type);
        if (capacity == 0) {
            currentTerminal.loadTruck(this);
        } else {
            currentTerminal. unloadTruck(this);
        }
    }

    public long getTruckId() {
        return truckId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TruckType getType() {
        return type;
    }

    public void setType(TruckType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() !=  obj.getClass()) {
            return false;
        }
        Truck truck = (Truck) obj;
        if (truckId != truck.truckId) {
            return false;
        }
        if (capacity != truck.capacity) {
            return false;
        }
        return type == truck.type;
    }

    @Override
    public int hashCode() {

        int result = (int) (truckId ^ (truckId >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + capacity;
        return result;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("truckId=").append(truckId);
        sb.append(", type=").append(type);
        sb.append(", capacity=").append(capacity);
        sb.append('}');
        return sb.toString();
    }
}
