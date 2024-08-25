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

    public ConstraintAutomaton joinWithNoHeuristic(ArrayList<ConstraintAutomaton> automatonList) {
        //should I shuffle the list first for randomness in join order?
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
        fileUtils.logExecutionTime(duration, "src/main/resources/testcases/7/iteration_results.txt");
        return deque.getFirst();
    }

    public ConstraintAutomaton joinWithInternalFieldHeuristic(ArrayList<ConstraintAutomaton> automatonList, int heuristicType) throws AutomatonListSizeLowerThanTwoException, WrongHeuristicTypeSelectionException {
        if(automatonList.size() < 2)
            throw new AutomatonListSizeLowerThanTwoException();

        ArrayList<AutomatonHeuristic> automata = heuristicUtils.createAutomataHeuristic(automatonList);
        PriorityQueue<AutomatonHeuristic> minHeap = new PriorityQueue<>(Comparator.comparingDouble(ah -> {
            try {
                return heuristicUtils.getInternalFieldAsHeuristic(ah, heuristicType);
            } catch (WrongHeuristicTypeSelectionException e) {
                throw new RuntimeException(e);
            }
        }));
        minHeap.addAll(automata);

        long startTime = System.currentTimeMillis();
        while (minHeap.size() > 1) {
            AutomatonHeuristic automatonHeuristic_1 = minHeap.poll();
            AutomatonHeuristic automatonHeuristic_2 = minHeap.poll();
            System.out.println("Selecting " + automatonHeuristic_1.getAutomaton().getId() + " and " + automatonHeuristic_2.getAutomaton().getId() + " for joining");
            ConstraintAutomaton joinedAutomaton = singleJoin.joinAutomata(automatonHeuristic_1.getAutomaton(), automatonHeuristic_2.getAutomaton());
            minHeap.add(heuristicUtils.createAutomatonHeuristic(joinedAutomaton));
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Execution time: " + duration + " milliseconds");

        return minHeap.poll().getAutomaton();
    }
}
