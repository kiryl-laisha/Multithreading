package org.laisha.multithreading.exception;

public class ProjectException extends Exception {

    public ProjectException() {
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }
} 