package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.utils.FileUtils;
import org.example.utils.HeuristicUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

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

    public ConstraintAutomaton joinWithInternalFieldHeuristic(ArrayList<ConstraintAutomaton> automatonList) {
        //todo
        return new ConstraintAutomaton();
    }
}
