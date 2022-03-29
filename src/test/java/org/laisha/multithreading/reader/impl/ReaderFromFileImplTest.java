package org.laisha.multithreading.reader.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.laisha.multithreading.exception.ProjectException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderFromFileImplTest {

    static final String TEST_DATA_FILE_PATH = "data/test_data.txt";
    static final String TEST_EMPTY_FILE_FILEPATH = "data/test_empty_file.txt";
    static final int TEST_DATA_FILE_STRING_QUANTITY = 3;
    static ReaderFromFileImpl reader;

    @BeforeAll
    static void setUpClass() {
        reader = ReaderFromFileImpl.getInstance();
    }

    @AfterAll
    static void tearDownClass() {
        reader = null;
    }

    @ParameterizedTest
    @DisplayName("Reading from valid test file.")
    @ValueSource(strings = {TEST_DATA_FILE_PATH})
    void readTextFromFilePositiveTest(String filepath) {

        List<String> stringList = new ArrayList<>();
        try {
            stringList = reader.readStringListFromFile(filepath);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        int expected = TEST_DATA_FILE_STRING_QUANTITY;
        int actual = stringList.size();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @DisplayName("Test catches an exception when filepath is null or empty string.")
    @NullAndEmptySource
    void readTextFromFileFirstNegativeTest(String filepath) {

        String expectedExceptionMessage = "Working with file is impossible.";
        String actualExceptionMessage = null;
        try {
            reader.readStringListFromFile(filepath);
        } catch (ProjectException e) {
            actualExceptionMessage = e.getMessage();
        }
        assertEquals(expectedExceptionMessage, actualExceptionMessage);
    }

    @ParameterizedTest
    @DisplayName("Test catches an exception when file is empty.")
    @ValueSource(strings = {TEST_EMPTY_FILE_FILEPATH})
    void readTextFromFileSecondNegativeTest(String filepath) {

        String expectedExceptionMessage = "File \"" + filepath + "\" " +
                "is empty. Reading from the file is impossible.";
        String actualExceptionMessage = null;
        try {
            reader.readStringListFromFile(filepath);
        } catch (ProjectException e) {
            actualExceptionMessage = e.getMessage();
        }
        assertEquals(expectedExceptionMessage, actualExceptionMessage);
    }
}