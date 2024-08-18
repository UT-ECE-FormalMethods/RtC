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
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/resources/testcases/4/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/resources/testcases/4/automaton-2.json"), ConstraintAutomaton.class);
//            ConstraintAutomaton automaton_3 = mapper.readValue(new File("src/main/resources/testcases/3/automaton-3.json"), ConstraintAutomaton.class);

            ConstraintAutomaton joinedAutomaton = automataOps.joinAutomata(automaton_1, automaton_2);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/4/result.json"), joinedAutomaton);

//            ConstraintAutomaton joinedAutomaton_2 = automataOps.joinAutomata(automaton_3, automaton_1);
//            ConstraintAutomaton finalAutomaton_2 = automataOps.joinAutomata(joinedAutomaton_2, automaton_2);
//
//            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/3/result-2.json"), finalAutomaton_2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}