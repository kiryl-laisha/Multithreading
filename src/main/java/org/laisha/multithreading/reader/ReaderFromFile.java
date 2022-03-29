package org.laisha.multithreading.reader;

import org.laisha.multithreading.exception.ProjectException;

import java.util.List;

public interface ReaderFromFile {

    List<String> readStringListFromFile(String filePath) throws ProjectException;
}
