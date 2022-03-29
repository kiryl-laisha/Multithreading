package org.laisha.multithreading.reader.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.laisha.multithreading.exception.ProjectException;
import org.laisha.multithreading.reader.ReaderFromFile;
import org.laisha.multithreading.validator.FilePathValidator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReaderFromFileImpl implements ReaderFromFile {

    private static final Logger logger = LogManager.getLogger();
    private static final ReaderFromFileImpl instance = new ReaderFromFileImpl();
    private static final String WINDOWS_DIRECTORY_SEPARATOR = "\\";
    private static final String UNIX_DIRECTORY_SEPARATOR = "/";

    private ReaderFromFileImpl() {
    }

    public static ReaderFromFileImpl getInstance() {
        return instance;
    }

    @Override
    public List<String> readStringListFromFile(String filepath)
            throws ProjectException {

        logger.log(Level.DEBUG, "The file path is \"{}\".", filepath);
        Path path = defineFilePathForData(filepath);
        List<String> stringListFromFile;
        try (Stream<String> stringStream = Files.newBufferedReader(path).lines()) {
            stringListFromFile = stringStream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new ProjectException("Reading from the file for the file path \""
                    + filepath + "\" is not available, ", e);
        }
        logger.log(Level.DEBUG, "The file for the file path \"{}\" has read successfully.\n" +
                "{} string(s) have been read.", filepath, stringListFromFile.size());
        return stringListFromFile;
    }

    private Path defineFilePathForData(String filepath) throws ProjectException {

        FilePathValidator validator = FilePathValidator.getInstance();
        boolean isFilePathValid = validator.validateFilePath(filepath);
        if (!isFilePathValid) {
            throw new ProjectException("Working with file is impossible.");
        }
        filepath = filepath.replace(WINDOWS_DIRECTORY_SEPARATOR, UNIX_DIRECTORY_SEPARATOR);
        URL dataFileUrl = getClass().getClassLoader().getResource(filepath);
        if (dataFileUrl == null) {
            throw new ProjectException("URL for the file path \"" + filepath + "\" could not " +
                    "be found. Reading from the file is impossible.");
        }
        File dataFile = new File(dataFileUrl.getFile());
        if (dataFile.length() == 0) {
            throw new ProjectException("File \"" + filepath + "\" " +
                    "is empty. Reading from the file is impossible.");
        }
        return Paths.get(dataFile.getAbsolutePath());
    }
}