package org.example.exceptions;

public class AutomatonFileIOException extends Exception {
    public AutomatonFileIOException(String msg) {
        super("Failed to read/write the automata files!\n" +
                "Please check the directory name and the containing files.\n" +
                "The input automaton file names must be in this format having a constraint automaton structure:\n" +
                "'CA-[i].json' where i >= 1.\n" +
                "Details:\n" + msg);
    }
}
