package org.example.utils.automaton;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.transition.Transition;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
}
