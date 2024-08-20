package org.example.exceptions;

public class TestcaseFilesReadingFailedException extends Exception {
    public TestcaseFilesReadingFailedException() {
        super("Failed to read the testcase files!\n" +
                "Please check the directory name and the containing files." +
                "\nThe input automaton file names must be in this format: 'automaton-[i].json' where i > 1.");
    }
}
