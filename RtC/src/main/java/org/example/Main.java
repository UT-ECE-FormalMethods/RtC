package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.ops.SingleJoin;
import org.example.ops.MultiJoin;
import org.example.utils.AutomatonUtils;
import org.example.utils.FileUtils;
import org.example.utils.HeuristicUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        FileUtils fileUtils = new FileUtils(mapper);
        AutomatonUtils automatonUtils = new AutomatonUtils();
        HeuristicUtils heuristicUtils = new HeuristicUtils(automatonUtils);
        SingleJoin automataOps = new SingleJoin(automatonUtils);
        MultiJoin multiJoin = new MultiJoin(automataOps, fileUtils, heuristicUtils);

        String mainTestcaseDirectory = "src/main/resources/testcases/main/final/";
        String testcaseSize = "X-large";
        String testcasePrefix = "xl";
        String testcaseNumber = "1";
        int heuristicType = 0;

        if (args.length > 0) {
            try {
                heuristicType = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid heuristic type. Using default value: " + heuristicType);
            }
        }

        String testcaseDirectory = mainTestcaseDirectory + testcaseSize + "/" + testcasePrefix + "-" + testcaseNumber;
        System.out.println("testcase : " + testcaseDirectory + "\nheuristic type: " + heuristicType);
        try {
            if(heuristicType != 6) {
                System.out.println("using heuristic type: " + heuristicType);

                ArrayList<ConstraintAutomaton> automatonTestCaseList = fileUtils.readConstraintAutomataFromTestcases(testcaseDirectory, "CA");
                for (ConstraintAutomaton ca : automatonTestCaseList) {
                    ConstraintAutomaton ca_temp = AutomatonUtils.removeUnreachableStates(ca);
                    System.out.println("original");
                    System.out.println("States: " + ca.getStates().size() + ", Transitions: " + ca.getTransitions().size());
                    System.out.println("cleaned");
                    System.out.println("States: " + ca_temp.getStates().size() + ", Transitions: " + ca_temp.getTransitions().size());
                }

                ConstraintAutomaton result = multiJoin.multiJoinAutomata(automatonTestCaseList, heuristicType, true, testcaseDirectory + "/iteration_results.txt", false);
                result = AutomatonUtils.removeUnreachableStates(result);
                fileUtils.writeAutomatonToFile(testcaseDirectory + "/result-h" + heuristicType + ".json", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}