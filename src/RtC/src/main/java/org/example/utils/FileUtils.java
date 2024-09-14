package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.exceptions.AutomatonFileIOException;
import org.example.exceptions.ExecutionTimeLoggingException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtils {
    private final ObjectMapper objectMapper;

    public FileUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ArrayList<ConstraintAutomaton> readConstraintAutomataFromTestcases(String directory) throws AutomatonFileIOException {
        ArrayList<ConstraintAutomaton> automataList = new ArrayList<>();

        try {
            File dir = new File(directory);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((d, name) -> name.matches("automaton-\\d+\\.json"));
                if (files != null) {
                    Arrays.sort(files, (f1, f2) -> {
                        int num1 = Integer.parseInt(f1.getName().replaceAll("\\D+", ""));
                        int num2 = Integer.parseInt(f2.getName().replaceAll("\\D+", ""));
                        return Integer.compare(num1, num2);
                    });
                    for (File file : files) {
                        ConstraintAutomaton automaton = objectMapper.readValue(file, ConstraintAutomaton.class);
                        automataList.add(automaton);
                    }
                }
            }
            return automataList;
        }
        catch (IOException e) {
            throw new AutomatonFileIOException(e.getMessage());
        }
    }

    public ConstraintAutomaton readAutomatonFromFile(String filename) throws AutomatonFileIOException {
        try {
            return objectMapper.readValue(new File(filename), ConstraintAutomaton.class);
        }
        catch (IOException e) {
            throw new AutomatonFileIOException(e.getMessage());
        }
    }

    public void writeAutomatonToFile(String filename, ConstraintAutomaton automaton) throws AutomatonFileIOException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), automaton);
        }
        catch (IOException e) {
            throw new AutomatonFileIOException(e.getMessage());
        }
    }

    public void logExecutionTime(long timeElapsed, String filename) throws ExecutionTimeLoggingException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write("Task executed in " + timeElapsed + " ms\n");
        } catch (IOException e) {
            throw new ExecutionTimeLoggingException(e.getMessage());
        }
    }
}
