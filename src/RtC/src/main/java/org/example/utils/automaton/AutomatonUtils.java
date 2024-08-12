package org.example.utils.automaton;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.transition.Transition;

import java.util.*;

public class AutomatonUtils {
    public Transition getAutomatonTransition(List<Transition> automatonTransitions, String sourceState, String targetState) {
        for (Transition transition: automatonTransitions) {
            if (Objects.equals(transition.getSource(), sourceState) && Objects.equals(transition.getTarget(), targetState)) {
                return transition;
            }
        }
        return null;
    }

    public boolean transitionsIntersectionExists(List<String> automaton1TransitionLabels, List<String> automaton2Alphabet) {
        return new HashSet<>(automaton2Alphabet).containsAll(automaton1TransitionLabels); //using hashset for performance boosting
    }

    public ArrayList<String> getTransitionLabelsIntersection(List<String> automaton1TransitionLabels, List<String> automaton2Alphabet) {
        ArrayList<String> intersection = new ArrayList<>(automaton1TransitionLabels);
        intersection.retainAll(automaton2Alphabet);
        return intersection;
    }

    public ArrayList<String> getTransitionLabelsUnion(List<String> automaton1TransitionLabels, List<String> automaton2TransitionLabels) {
        Set<String> unionSet = new HashSet<>(automaton1TransitionLabels);
        unionSet.addAll(automaton1TransitionLabels);
        return new ArrayList<>(unionSet);
    }
}
