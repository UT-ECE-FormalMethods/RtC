package org.example.exceptions;

public class WrongHeuristicTypeSelectionException extends Exception {
    public WrongHeuristicTypeSelectionException() {
        super("Wrong heuristic selected! Please select again.");
    }
}
