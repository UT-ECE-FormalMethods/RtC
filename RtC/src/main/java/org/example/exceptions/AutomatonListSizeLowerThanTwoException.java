package org.example.exceptions;

public class AutomatonListSizeLowerThanTwoException extends Exception {
    public AutomatonListSizeLowerThanTwoException() {
        super("Automaton list for joining operation must have at least two members!");
    }
}
