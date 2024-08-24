package org.example.utils;

import org.example.constraintAutomaton.AutomatonHeuristic;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.exceptions.WrongHeuristicTypeSelectionException;

import java.util.ArrayList;

public class HeuristicUtils {

    public AutomatonHeuristic createAutomatonHeuristic(ConstraintAutomaton automaton) {
        return new AutomatonHeuristic(automaton);
    }

    public ArrayList<AutomatonHeuristic> createAutomataHeuristic(ArrayList<ConstraintAutomaton> automatonList) {
        ArrayList<AutomatonHeuristic> automataHeuristics = new ArrayList<>();
        for(ConstraintAutomaton constraintAutomaton : automatonList) {
            automataHeuristics.add(createAutomatonHeuristic(constraintAutomaton));
        }
        return automataHeuristics;
    }

    public double getInternalFieldAsHeuristic(AutomatonHeuristic automaton, int heuristicType) throws WrongHeuristicTypeSelectionException {
        return switch (heuristicType) {
            case 1 -> automaton.getTransitionCount();
            case 2 -> automaton.getStatesCount();
            case 3 -> automaton.getTransitionDensity();
            default -> throw new WrongHeuristicTypeSelectionException();
        };
    }
}
