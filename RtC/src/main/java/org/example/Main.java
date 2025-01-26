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
        String testcaseDirectoryName = "s1-4";
        int testCaseSection = 1;
        int heuristicType = 0;
        String testcaseDirectory = "src/main/resources/testcases/main/section-" + testCaseSection +"/" + testcaseDirectoryName + "/";
        System.out.println("testcase : " + testcaseDirectoryName + ", heuristic type: " + heuristicType);
        try {
            ArrayList<ConstraintAutomaton> automatonTestCaseList = fileUtils.readConstraintAutomataFromTestcases(testcaseDirectory, "CA");
            for(ConstraintAutomaton ca: automatonTestCaseList) {
                ConstraintAutomaton ca_temp = AutomatonUtils.removeUnreachableStates(ca);
                System.out.println("original");
                System.out.println("States: " + ca.getStates().size() + ", Transitions: " + ca.getTransitions().size());
                System.out.println("cleaned");
                System.out.println("States: " + ca_temp.getStates().size() + ", Transitions: " + ca_temp.getTransitions().size());
            }

            ConstraintAutomaton result = multiJoin.multiJoinAutomata(automatonTestCaseList, heuristicType, true, testcaseDirectory + "iteration_results.txt", false);
            result = AutomatonUtils.removeUnreachableStates(result);
            fileUtils.writeAutomatonToFile(testcaseDirectory + "/result-h" + heuristicType + ".json", result);
//            fileUtils.writeAutomatonToFile("src/main/resources/testcases/" + testcaseDirectoryName + "/result-r-u" + heuristicType + ".json", resultRemovedUnreachable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}