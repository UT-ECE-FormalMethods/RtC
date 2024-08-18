package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;
import org.example.ops.AutomataJoin;
import org.example.utils.automaton.AutomatonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ObjectMapper mapper = new ObjectMapper();
        AutomataJoin automataOps = new AutomataJoin();
        AutomatonUtils automatonUtils = new AutomatonUtils();
        System.out.println(automatonUtils.transitionsIntersectionExists(new ArrayList<>(List.of("a", "b")), new ArrayList<>(List.of("b", "d"))));

        try {
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/resources/testcases/5/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/resources/testcases/5/automaton-2.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_3 = mapper.readValue(new File("src/main/resources/testcases/5/automaton-3.json"), ConstraintAutomaton.class);

            long startTime = System.currentTimeMillis();

            ConstraintAutomaton joinedAutomaton = automataOps.joinAutomata(automaton_1, automaton_3);
            ConstraintAutomaton finalAutomaton = automataOps.joinAutomata(automaton_2, joinedAutomaton);
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/5/result.json"), finalAutomaton);
            System.out.println("Execution time: " + duration + " milliseconds");

            long startTime2 = System.currentTimeMillis();
            ConstraintAutomaton joinedAutomaton2 = automataOps.joinAutomata(automaton_1, automaton_2);
            ConstraintAutomaton finalAutomaton2 = automataOps.joinAutomata(automaton_3, joinedAutomaton2);
            long endTime2 = System.currentTimeMillis();
            long duration2 = (endTime2 - startTime2);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/5/result2.json"), finalAutomaton2);
            System.out.println("Execution time: " + duration2 + " milliseconds");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}