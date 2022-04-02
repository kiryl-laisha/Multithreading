package org.laisha.multithreading.main;

import org.laisha.multithreading.entity.LogisticCentre;

public class LogisticCentreMain {

    private final static LogisticCentre logisticCentre = LogisticCentre.getInstance();

    public static void main(String[] args) {

        System.out.println(logisticCentre.getTerminals().size());

    }
}
