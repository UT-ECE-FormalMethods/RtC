package org.example.utils;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.state.State;
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

    public String getJoinedTransitionId(Transition joinedTransition) {
        return (joinedTransition.getSource() + "_" + joinedTransition.getLabels() + "_" + joinedTransition.getTarget());
    }

    public List<Transition> getAutomatonTransitionsBySource(ConstraintAutomaton automaton, String stateName, boolean sourceOrTarget) {
        //true for checking source, false for checking target/destination
        List<Transition> filteredTransitions = new ArrayList<>();
        for (Transition transition : automaton.getTransitions()) {
            if (transition.getSource().equals(stateName) && sourceOrTarget)
                filteredTransitions.add(transition);
            if(transition.getTarget().equals(stateName) && !sourceOrTarget)
                filteredTransitions.add(transition);
        }
        return filteredTransitions;
    }

    public HashMap<String, List<Transition>> createStateTransitionMap(ConstraintAutomaton automaton) {
        HashMap<String, List<Transition>> stateTransitionMap = new HashMap<>();

        for (State state : automaton.getStates()) {
            List<Transition> transitionsFromState = getAutomatonTransitionsBySource(automaton, state.getId(), true);
            stateTransitionMap.put(state.getId(), transitionsFromState);
        }

        return stateTransitionMap;
    }

    public ArrayList<String> getAutomatonAlphabetNames(List<Transition> transitions) {
        Set<String> uniqueLabelsSet = new HashSet<>();
        for (Transition transition : transitions) {
            if (transition.getLabels() != null) {
                uniqueLabelsSet.addAll(transition.getLabels());
            }
        }
        return new ArrayList<>(uniqueLabelsSet);
    }
}
