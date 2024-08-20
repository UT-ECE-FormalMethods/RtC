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
        FileUtils fileUtils = new FileUtils();
        AutomatonUtils automatonUtils = new AutomatonUtils();
        SingleJoin automataOps = new SingleJoin(automatonUtils);
        MultiJoin multiJoin = new MultiJoin(automataOps, fileUtils);
        System.out.println(automatonUtils.transitionsIntersectionExists(new ArrayList<>(List.of("a", "b")), new ArrayList<>(List.of("b", "d"))));

        try {
            ConstraintAutomaton automaton_1 = mapper.readValue(new File("src/main/resources/testcases/7/automaton-1.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_2 = mapper.readValue(new File("src/main/resources/testcases/7/automaton-2.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_3 = mapper.readValue(new File("src/main/resources/testcases/7/automaton-3.json"), ConstraintAutomaton.class);
            ConstraintAutomaton automaton_4 = mapper.readValue(new File("src/main/resources/testcases/7/automaton-4.json"), ConstraintAutomaton.class);

//            ArrayList<ConstraintAutomaton> automatonList = new ArrayList<>(List.of(automaton_1, automaton_2, automaton_3, automaton_4));
            ArrayList<ConstraintAutomaton> automatonList = new ArrayList<>(List.of(automaton_3, automaton_4, automaton_2, automaton_1));


            ConstraintAutomaton result = multiJoin.joinWithNoHeuristic(automatonList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/7/result-order-2.json"), result);



//            long startTime2 = System.currentTimeMillis();
//            ConstraintAutomaton result2 = multiJoin.joinWithNoHeuristic(automatonList2);
//            long endTime2 = System.currentTimeMillis();
//            long duration2 = (endTime2 - startTime2);
//
//            System.out.println("Execution time: " + duration2 + " milliseconds");
//            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/5/result-automated.json"), result);
//            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/5/result-automated-2.json"), result2);

//            System.out.println("--------------------------");
//
//            long startTime3 = System.currentTimeMillis();
//            ConstraintAutomaton joinedAutomaton = automataOps.joinAutomata(automaton_1, automaton_3);
//            ConstraintAutomaton finalAutomaton = automataOps.joinAutomata(automaton_2, joinedAutomaton);
//            long endTime3 = System.currentTimeMillis();
//            long duration3 = (endTime3 - startTime3);
//            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/6/result.json"), finalAutomaton);
//            System.out.println("Execution time: " + duration3 + " milliseconds");
//            fileUtils.logExecutionTime(duration3, "src/main/resources/iteration_result_test.txt");

//            long startTime4 = System.currentTimeMillis();
//            ConstraintAutomaton joinedAutomaton2 = automataOps.joinAutomata(automaton_1, automaton_2);
//            ConstraintAutomaton finalAutomaton2 = automataOps.joinAutomata(automaton_3, joinedAutomaton2);
//            long endTime4 = System.currentTimeMillis();
//            long duration4 = (endTime4 - startTime4);
//            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/testcases/5/result-2.json"), finalAutomaton2);
//            System.out.println("Execution time: " + duration4 + " milliseconds");
//            fileUtils.logExecutionTime(duration4, "src/main/resources/iteration_result_test.txt");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}