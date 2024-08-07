package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ObjectMapper mapper =new ObjectMapper();

        try {
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/java/org/example/assets/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/java/org/example/assets/automaton-2.json"), ConstraintAutomaton.class);

            System.out.println("States:");
            for (State state : automaton_1.getStates()) {
                System.out.println("ID: " + state.getId() + ", Initial: " + state.isInitial());
                for(String item: state.getComposition()) {
                    System.out.println(item);
                }
            }

            System.out.println("\nTransitions:");
            for (Transition transition : automaton_1.getTransitions()) {
                System.out.println("Source: " + transition.getSource() + ", Target: " + transition.getTarget() + ", Label: " + transition.getLabel());
                for (Constraint constraint : transition.getConstraints()) {
                    System.out.println("  Constraint Type: " + constraint.getType() + ", Channels: " + constraint.getChannels());
                }
            }

            System.out.println("\nAlphabet: " + automaton_1.getAlphabet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}