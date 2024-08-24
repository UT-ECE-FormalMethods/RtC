package org.example.exceptions;

public class AutomatonListSizeLowerThanTwoException extends Exception {
    public AutomatonListSizeLowerThanTwoException() {
        super("Automaton list for joining operation ust have at least two members!");
    }
}
