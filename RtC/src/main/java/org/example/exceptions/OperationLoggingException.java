package org.example.exceptions;

public class OperationLoggingException extends Exception {
    public OperationLoggingException(String msg) {
        super("Failed to log operation details!\n" +
                "Details:\n" + msg);
    }
}
