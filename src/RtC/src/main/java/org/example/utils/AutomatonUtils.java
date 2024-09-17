package org.example.utils;

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
        Set<String> set1 = new HashSet<>(automaton1TransitionLabels);
        Set<String> set2 = new HashSet<>(automaton2Alphabet);
        set1.retainAll(set2);
        return !set1.isEmpty();
    }

    public ArrayList<String> getTransitionLabelsIntersection(List<String> automaton1TransitionLabels, List<String> automaton2Alphabet) {
        ArrayList<String> intersection = new ArrayList<>(automaton1TransitionLabels);
        intersection.retainAll(automaton2Alphabet);
        return intersection;
    }

    public ArrayList<String> getTransitionLabelsUnion(List<String> automaton1TransitionLabels, List<String> automaton2TransitionLabels) {
        Set<String> unionSet = new HashSet<>(automaton1TransitionLabels);
        unionSet.addAll(automaton2TransitionLabels);
        return new ArrayList<>(unionSet);
    }

    public String getJoinedAutomatonId(String automatonId_1, String automatonId_2) {
        return ("(" + automatonId_1 + " X " + automatonId_2 + ")");
    }
}
