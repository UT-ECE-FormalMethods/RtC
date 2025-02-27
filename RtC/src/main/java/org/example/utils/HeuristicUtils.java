package org.example.utils;

import org.example.constraintAutomaton.AutomatonHeuristic;
import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.exceptions.WrongHeuristicTypeSelectionException;

import java.util.ArrayList;

public class HeuristicUtils {

    private final AutomatonUtils automatonUtils;

    public HeuristicUtils(AutomatonUtils automatonUtils) {
        this.automatonUtils = automatonUtils;
    }

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

    public double getInternalFieldAsHeuristic(AutomatonHeuristic automaton, int heuristicType)
            throws WrongHeuristicTypeSelectionException {
        return switch (heuristicType) {
            case 1 -> automaton.getTransitionCount();
            case 2 -> automaton.getStatesCount();
            case 3 -> automaton.getTransitionDensity();
            case 6 -> automaton.getTranStateHarmonicMean();
            case 7 -> automaton.getTranStateProduct();
            default -> throw new WrongHeuristicTypeSelectionException();
        };
    }

    public double getRelationalHeuristicValue(AutomatonHeuristic automaton_1, AutomatonHeuristic automaton_2, int heuristicType)
            throws WrongHeuristicTypeSelectionException {
        return switch (heuristicType) {
            case 4 -> (automaton_1.getTransitionCount() - automaton_2.getTransitionCount());
            case 5 -> (automaton_1.getStatesCount() - automaton_2.getStatesCount());
            case 8, 9, 10 -> (double) 1 / ((automatonUtils.getTransitionLabelsIntersection(automaton_1.getAutomatonAlphabet(), automaton_2.getAutomatonAlphabet()).size()) + 1);
            default -> throw new WrongHeuristicTypeSelectionException();
        };
    }

}
