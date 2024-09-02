package org.example.ops;

import org.example.constraintAutomaton.AutomatonHeuristic;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.exceptions.AutomatonListSizeLowerThanTwoException;
import org.example.exceptions.WrongHeuristicTypeSelectionException;
import org.example.utils.FileUtils;
import org.example.utils.HeuristicUtils;

import java.util.*;

public class MultiJoin {
    private final SingleJoin singleJoin;
    private final FileUtils fileUtils;
    private final HeuristicUtils heuristicUtils;

    public MultiJoin(SingleJoin automatonJoin, FileUtils fileUtils, HeuristicUtils heuristicUtils) {
        this.singleJoin = automatonJoin;
        this.fileUtils = fileUtils;
        this.heuristicUtils = heuristicUtils;
    }

    public ConstraintAutomaton joinWithNoHeuristic(ArrayList<ConstraintAutomaton> automatonList, boolean shuffle, boolean logExecutionTime, String testCaseDirectoryName) {
        if(shuffle)
            Collections.shuffle(automatonList);

        Deque<ConstraintAutomaton> deque = new LinkedList<>(automatonList);
        long startTime = System.currentTimeMillis();

        while (deque.size() > 1) {
            ConstraintAutomaton firstAutomaton = deque.pollFirst();
            ConstraintAutomaton secondAutomaton = deque.pollFirst();
            System.out.println("queue size: " + deque.size() + ", first autom no. of states: " + firstAutomaton.getStates().size() + ", second autom no. of states: " + secondAutomaton.getStates().size());
            ConstraintAutomaton joinedAutomaton = singleJoin.joinAutomata(firstAutomaton, secondAutomaton);
            deque.addFirst(joinedAutomaton);
        }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Execution time: " + duration + " milliseconds");
        if(logExecutionTime)
            fileUtils.logExecutionTime(duration, "src/main/resources/testcases/" + testCaseDirectoryName + "/iteration_results.txt");

        return deque.getFirst();
    }

    public ConstraintAutomaton joinWithInternalFieldHeuristic(ArrayList<ConstraintAutomaton> automatonList, int heuristicType, boolean logExecutionTime, String testCaseDirectoryName) throws AutomatonListSizeLowerThanTwoException, WrongHeuristicTypeSelectionException {
        if(automatonList.size() < 2)
            throw new AutomatonListSizeLowerThanTwoException();

        long totalJoiningTime = 0;

        ArrayList<AutomatonHeuristic> automata = heuristicUtils.createAutomataHeuristic(automatonList);
        PriorityQueue<AutomatonHeuristic> minHeap = new PriorityQueue<>(Comparator.comparingDouble(ah -> {
            try {
                return heuristicUtils.getInternalFieldAsHeuristic(ah, heuristicType);
            } catch (WrongHeuristicTypeSelectionException e) {
                throw new RuntimeException(e);
            }
        }));
        minHeap.addAll(automata);

        while (minHeap.size() > 1) {
            AutomatonHeuristic automatonHeuristic_1 = minHeap.poll();
            AutomatonHeuristic automatonHeuristic_2 = minHeap.poll();
            System.out.println("Selecting " + automatonHeuristic_1.getAutomaton().getId() + " and " + automatonHeuristic_2.getAutomaton().getId() + " for joining");
            long startTime = System.currentTimeMillis();
            ConstraintAutomaton joinedAutomaton = singleJoin.joinAutomata(automatonHeuristic_1.getAutomaton(), automatonHeuristic_2.getAutomaton());
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            totalJoiningTime += duration;
            minHeap.add(heuristicUtils.createAutomatonHeuristic(joinedAutomaton));
        }

        System.out.println("Execution time: " + totalJoiningTime + " milliseconds");
        if(logExecutionTime)
            fileUtils.logExecutionTime(totalJoiningTime, "src/main/resources/testcases/" + testCaseDirectoryName + "/iteration_results.txt");

        return minHeap.poll().getAutomaton();
    }

    public ConstraintAutomaton joinWithRelationalFieldHeuristic(ArrayList<ConstraintAutomaton> automatonList, int heuristicType, boolean logExecutionTime, String testCaseDirectoryName) throws AutomatonListSizeLowerThanTwoException, WrongHeuristicTypeSelectionException {
        if(automatonList.size() < 2)
            throw new AutomatonListSizeLowerThanTwoException();

        ArrayList<AutomatonHeuristic> automata = heuristicUtils.createAutomataHeuristic(automatonList);
        long totalJoiningTime = 0;

        while (automata.size() > 1) {
            int minDisparity = Integer.MAX_VALUE;
            int index1 = -1;
            int index2 = -1;

            for (int i = 0; i < automata.size(); i++) {
                for (int j = i + 1; j < automata.size(); j++) {
                    int disparity = Math.abs(heuristicUtils.getRelationalHeuristicValue(automata.get(i), automata.get(j), heuristicType));
                    if (disparity < minDisparity) {
                        minDisparity = disparity;
                        index1 = i;
                        index2 = j;
                    }
                }
            }

            AutomatonHeuristic automatonHeuristic1 = automata.get(index1);
            AutomatonHeuristic automatonHeuristic2 = automata.get(index2);

            long startTime = System.currentTimeMillis();
            ConstraintAutomaton joinedAutomaton = singleJoin.joinAutomata(automatonHeuristic1.getAutomaton(), automatonHeuristic2.getAutomaton());
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            totalJoiningTime += duration;

            automata.set(index1, heuristicUtils.createAutomatonHeuristic(joinedAutomaton));
            automata.remove(index2);
        }

        System.out.println("total execution time: " + totalJoiningTime + " ms");
        if(logExecutionTime)
            fileUtils.logExecutionTime(totalJoiningTime, "src/main/resources/testcases/" + testCaseDirectoryName + "/iteration_results.txt");
        return automata.get(0).getAutomaton();
    }

    public ConstraintAutomaton multiJoinAutomata(ArrayList<ConstraintAutomaton> automatonList, int heuristicType, boolean logExecutionTime, String testCaseDirectoryName, boolean shuffleForNormalJoin) throws AutomatonListSizeLowerThanTwoException, WrongHeuristicTypeSelectionException {
        if(heuristicType == 0)
            return joinWithNoHeuristic(automatonList, shuffleForNormalJoin, logExecutionTime, testCaseDirectoryName);
        else if(heuristicType >= 0 && heuristicType <= 3)
            return joinWithInternalFieldHeuristic(automatonList, heuristicType, logExecutionTime, testCaseDirectoryName);
        else if (heuristicType >= 4)
            return joinWithRelationalFieldHeuristic(automatonList, heuristicType, logExecutionTime, testCaseDirectoryName);
        else
            throw new WrongHeuristicTypeSelectionException();
    }
}
