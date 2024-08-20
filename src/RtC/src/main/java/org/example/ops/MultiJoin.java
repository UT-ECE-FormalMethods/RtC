package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.utils.AutomatonUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class MultiJoin {
    private final AutomataJoin automatonJoin;

    public MultiJoin(AutomataJoin automatonJoin) {
        this.automatonJoin = automatonJoin;
    }

    public ConstraintAutomaton joinWithNoHeuristic(ArrayList<ConstraintAutomaton> automatonList) {
        Deque<ConstraintAutomaton> deque = new LinkedList<>(automatonList);

        while (deque.size() > 1) {
            ConstraintAutomaton firstAutomaton = deque.pollFirst();
            ConstraintAutomaton secondAutomaton = deque.pollFirst();

            ConstraintAutomaton joinedAutomaton = automatonJoin.joinAutomata(firstAutomaton, secondAutomaton);
            deque.addFirst(joinedAutomaton);
        }

        return deque.getFirst();
    }
}
