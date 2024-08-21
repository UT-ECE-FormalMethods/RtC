package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.ops.SingleJoin;
import org.example.ops.MultiJoin;
import org.example.utils.AutomatonUtils;
import org.example.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ObjectMapper mapper = new ObjectMapper();
        FileUtils fileUtils = new FileUtils(mapper);
        AutomatonUtils automatonUtils = new AutomatonUtils();
        SingleJoin automataOps = new SingleJoin(automatonUtils);
        MultiJoin multiJoin = new MultiJoin(automataOps, fileUtils);
        System.out.println(automatonUtils.transitionsIntersectionExists(new ArrayList<>(List.of("a", "b")), new ArrayList<>(List.of("b", "d"))));

        try {
            ConstraintAutomaton automaton_1 = fileUtils.readAutomatonFromFile("src/main/resources/testcases/7/automaton-1.json");
            ConstraintAutomaton automaton_2 = fileUtils.readAutomatonFromFile("src/main/resources/testcases/7/automaton-2.json");
            ConstraintAutomaton automaton_3 = fileUtils.readAutomatonFromFile("src/main/resources/testcases/7/automaton-3.json");
            ConstraintAutomaton automaton_4 = fileUtils.readAutomatonFromFile("src/main/resources/testcases/7/automaton-4.json");

//            ArrayList<ConstraintAutomaton> automatonList = new ArrayList<>(List.of(automaton_1, automaton_2, automaton_3, automaton_4));
            ArrayList<ConstraintAutomaton> automatonList = new ArrayList<>(List.of(automaton_3, automaton_4, automaton_2, automaton_1));


            ConstraintAutomaton result = multiJoin.joinWithNoHeuristic(automatonList);
            fileUtils.writeAutomatonToFile("src/main/resources/testcases/7/result-order-2.json", result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}