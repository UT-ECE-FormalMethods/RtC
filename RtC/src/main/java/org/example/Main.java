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
        String testcaseDirectoryName = "27";
        int heuristicType = 9;

        System.out.println("testcase : " + testcaseDirectoryName + ", heuristic type: " + heuristicType);
        try {
            ArrayList<ConstraintAutomaton> automatonTestCaseList = fileUtils.readConstraintAutomataFromTestcases("src/main/resources/testcases/" + testcaseDirectoryName + "/");
            ConstraintAutomaton result = multiJoin.multiJoinAutomata(automatonTestCaseList, heuristicType, true, testcaseDirectoryName, false);
            fileUtils.writeAutomatonToFile("src/main/resources/testcases/" + testcaseDirectoryName + "/result-h" + heuristicType + ".json", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}