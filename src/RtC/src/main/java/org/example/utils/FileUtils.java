package org.example.utils;

import org.example.constraintAutomaton.ConstraintAutomaton;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

    public ArrayList<ConstraintAutomaton> readConstraintAutomataFromTestcases(String directory) {
        ArrayList<ConstraintAutomaton> automataList = new ArrayList<>();

        //todo

        return automataList;
    }

    public void logExecutionTime(long timeElapsed, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Task executed in " + timeElapsed + " ms\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

}
