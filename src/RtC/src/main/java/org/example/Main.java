package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;
import org.example.ops.AutomataJoin;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ObjectMapper mapper =new ObjectMapper();
        AutomataJoin automataOps = new AutomataJoin();

        try {
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/java/org/example/assets/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/java/org/example/assets/automaton-2.json"), ConstraintAutomaton.class);

            ConstraintAutomaton joinedAutomaton = automataOps.joinAutomata(automaton_1, automaton_2);

//            System.out.println("States:");
//            for (State state : joinedAutomaton.getStates()) {
//                System.out.println("ID: " + state.getId() + ", Initial: " + state.isInitial());
//                System.out.print("State composition: ");
//                for(String item: state.getComposition()) {
//                    System.out.print(item + " ");
//                }
//                System.out.println("\n");
//            }

//            System.out.println("\nTransitions:");
//            for (Transition transition : joinedAutomaton.getTransitions()) {
//                System.out.println("Source: " + transition.getSource() + ", Target: " + transition.getTarget() + ", Label: " + transition.getLabel());
//                for (Constraint constraint : transition.getConstraints()) {
//                    System.out.println("  Constraint Type: " + constraint.getType() + ", Channels: " + constraint.getChannels());
//                }
//            }

//            System.out.println("\nAlphabet: " + joinedAutomaton.getAlphabet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}