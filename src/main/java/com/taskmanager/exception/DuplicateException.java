package com.taskmanager.exception;

public class DuplicateException extends RuntimeException{

    public DuplicateException() {
    }

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable exception) {
        super(message, exception);
    }
}
