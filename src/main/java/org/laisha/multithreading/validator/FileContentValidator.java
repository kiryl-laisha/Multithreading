package org.laisha.multithreading.validator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FileContentValidator {

    private static final Logger logger = LogManager.getLogger();
    private static final FileContentValidator instance = new FileContentValidator();
    private static final String INTEGER_REGEX = "^\\s*\\d+\\s*$";

    private FileContentValidator() {
    }

    public static FileContentValidator getInstance() {
        return instance;
    }

    public final boolean validateLogisticCentreDataFile(List<String> dataFile) {

        if (dataFile == null) {
            logger.log(Level.WARN, "The provided list is null.");
            return false;
        }
        if (dataFile.size() != 3) {
            logger.log(Level.WARN, "The file contains incorrect number of " +
                    "data for logistic centre parameters.");
            return false;
        }
        boolean isCorrectData = dataFile.stream()
                .allMatch(x -> x.matches(INTEGER_REGEX));
        if (!isCorrectData) {
            logger.log(Level.WARN, "The file contains incorrect data for " +
                    "logistic centre parameters.");
            return false;
        }
        return true;
    }

    public final boolean validateTruckDataFile(List<String> dataFile) {

        if (dataFile == null) {
            logger.log(Level.WARN, "The provided list is null.");
            return false;
        }
        if (dataFile.size() != 2) {
            logger.log(Level.WARN, "The file contains incorrect number of " +
                    "data for logistic centre parameters.");
            return false;
        }
        boolean isCorrectData = dataFile.stream()
                .allMatch(x -> x.matches(INTEGER_REGEX));
        if (!isCorrectData) {
            logger.log(Level.WARN, "The file contains incorrect data for " +
                    "logistic centre parameters.");
            return false;
        }
        return true;
    }
}

