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
        ObjectMapper mapper = new ObjectMapper();
        AutomataJoin automataOps = new AutomataJoin();

        try {
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/resources/testcases/3/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/resources/testcases/3/automaton-2.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_3 = mapper.readValue(new File("src/main/resources/testcases/3/automaton-3.json"), ConstraintAutomaton.class);

            ConstraintAutomaton joinedAutomaton = automataOps.joinAutomata(automaton_1, automaton_2);
            ConstraintAutomaton finalAutomaton = automataOps.joinAutomata(joinedAutomaton, automaton_3);

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/3/result-cp.json"), finalAutomaton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}