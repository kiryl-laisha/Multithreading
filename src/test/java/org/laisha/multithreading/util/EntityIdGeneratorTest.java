package org.laisha.multithreading.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityIdGeneratorTest {

    @Test
    void generateTerminalIdPositiveTest() {

        long expectedId = 1;
        long actualId = EntityIdGenerator.generateTerminalId();
        assertEquals(expectedId, actualId);
    }

    @Test
    void generateTruckIdPositiveTest() {

        long expectedId = 1001;
        long actualId = EntityIdGenerator.generateTruckId();
        assertEquals(expectedId, actualId);
    }
}