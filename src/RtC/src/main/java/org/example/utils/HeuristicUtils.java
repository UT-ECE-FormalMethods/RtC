package org.example.utils;

import org.example.constraintAutomaton.AutomatonHeuristic;
import org.example.constraintAutomaton.ConstraintAutomaton;

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
}
