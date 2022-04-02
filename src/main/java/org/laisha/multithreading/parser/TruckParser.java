package org.laisha.multithreading.parser;

import org.laisha.multithreading.entity.Truck;

import java.util.List;

public interface TruckParser {

    List<Truck> parseTruckDatabase(List<String> truckDatabase);
}
