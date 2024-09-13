package org.example.exceptions;

public class JoinOperationFailedException extends Exception {
    public JoinOperationFailedException(String msg) {
        super("Join operation failed\nDetails: " + msg);
    }
}
