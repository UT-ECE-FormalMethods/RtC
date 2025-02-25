package org.example.exceptions;

public class WrongHeuristicTypeSelectionException extends Exception {
    public WrongHeuristicTypeSelectionException() {
        super("Wrong heuristic selected!\n" +
                "Options are as following:\n" +
                "(0) for {No Heuristic (Incremental)},\n" +
                "(1) for {Min Transitions},\n" +
                "(2) for {Min States},\n" +
                "(3) for {Transition Density},\n" +
                "(4) for {Transition Disparity},\n" +
                "(5) for {State Disparity},\n" +
                "(6) for {Transition and State Harmonic Mean},\n" +
                "(7) for {Transition and State Sum},\n" +
                "(8) for {Max Connectivity}\n");
    }
}
