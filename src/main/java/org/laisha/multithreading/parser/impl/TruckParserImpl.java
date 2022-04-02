package org.laisha.multithreading.parser.impl;

import org.laisha.multithreading.entity.Truck;
import org.laisha.multithreading.entity.TruckType;
import org.laisha.multithreading.parser.TruckParser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TruckParserImpl implements TruckParser {

    private static final TruckParserImpl instance = new TruckParserImpl();
    private static final String DATA_DELIMITER = "\\s+";

    private TruckParserImpl() {
    }

    public static TruckParserImpl  getInstance(){
        return instance;
    }
    @Override
    public List<Truck> parseTruckDatabase(List<String> truckDatabase) {

        List<Truck> trucks = truckDatabase.stream()
                .map(String::strip)
                .map(x -> Stream.of(x.split(DATA_DELIMITER))
                        .collect(Collectors.toList()))
                .map(t -> new Truck(TruckType.valueOf(t.get(0)),
                        Integer.parseInt(t.get(1)),
                        Integer.parseInt(t.get(2))))
                .collect(Collectors.toList());
        return trucks;
    }
}
