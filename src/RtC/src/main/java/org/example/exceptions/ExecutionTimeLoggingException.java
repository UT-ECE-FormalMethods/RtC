package org.example.exceptions;

public class ExecutionTimeLoggingException extends Exception {
    public ExecutionTimeLoggingException(String msg) {
        super("Failed to write operation execution time\n" +
                "Details: " + msg);
    }
}
