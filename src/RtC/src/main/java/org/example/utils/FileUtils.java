package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    private final ObjectMapper objectMapper;

    public FileUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ArrayList<ConstraintAutomaton> readConstraintAutomataFromTestcases(String directory) {
        ArrayList<ConstraintAutomaton> automataList = new ArrayList<>();

        //todo

        return automataList;
    }

    public ConstraintAutomaton readAutomatonFromFile(String filename) throws IOException {
        return objectMapper.readValue(new File(filename), ConstraintAutomaton.class);
    }

    public void writeAutomatonToFile(String filename, ConstraintAutomaton automaton) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), automaton);
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
