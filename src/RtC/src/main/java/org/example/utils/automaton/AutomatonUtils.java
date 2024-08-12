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

    public boolean transitionsIntersectionExists(Transition automaton1Transition, List<String> transitionAlphabet) {
        return new HashSet<>(transitionAlphabet).containsAll(automaton1Transition.getLabel()); //using hashset for performance boosting
    }

    public ArrayList<String> getTransitionLabelsIntersection(Transition transition, List<String> transition2Alphabet) {
        ArrayList<String> intersection = new ArrayList<>(transition.getLabel());
        intersection.retainAll(transition2Alphabet);
        return intersection;
    }

    public ArrayList<String> getTransitionLabelsUnion(List<String> transition1Labels, List<String> transition2Labels) {
        Set<String> unionSet = new HashSet<>(transition1Labels);
        unionSet.addAll(transition2Labels);
        return new ArrayList<>(unionSet);
    }
}
