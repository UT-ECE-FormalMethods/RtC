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
        return (joinedTransition.getSource() + "_" + joinedTransition.getLabel() + "_" + joinedTransition.getTarget());
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
            if (transition.getLabel() != null) {
                uniqueLabelsSet.addAll(transition.getLabel());
            }
        }
        return new ArrayList<>(uniqueLabelsSet);
    }

    public boolean intersectionsAreEqual(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        List<String> sortedList1 = new ArrayList<>(list1);
        List<String> sortedList2 = new ArrayList<>(list2);
        Collections.sort(sortedList1);
        Collections.sort(sortedList2);

        return sortedList1.equals(sortedList2);
    }

    public static ConstraintAutomaton removeUnreachableStates(ConstraintAutomaton automaton) {
        Set<String> reachableStates = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<State> states = automaton.getStates();
        List<Transition> transitions = automaton.getTransitions();

        // in this case we consider that there might be more than one reachable state but in our testcases this won't happen
        for (State state : states) {
            if (state.isInitial()) {
                queue.add(state.getId());
                reachableStates.add(state.getId());
            }
        }

        // using BFS to find reachable states
        while (!queue.isEmpty()) {
            String currentState = queue.poll();
            for (Transition transition : transitions) {
                if (transition.getSource().equals(currentState) && !reachableStates.contains(transition.getTarget())) {
                    reachableStates.add(transition.getTarget());
                    queue.add(transition.getTarget());
                }
            }
        }

        // saving reachable states
        List<State> newStates = new ArrayList<>();
        for (State state : states) {
            if (reachableStates.contains(state.getId())) {
                newStates.add(state);
            }
        }

        // keeping transitions that are only between the reachable states.
        List<Transition> newTransitions = new ArrayList<>();
        for (Transition transition : transitions) {
            if (reachableStates.contains(transition.getSource()) && reachableStates.contains(transition.getTarget())) {
                newTransitions.add(transition);
            }
        }

        ConstraintAutomaton newAutomaton = new ConstraintAutomaton();
        newAutomaton.setId(automaton.getId());
        newAutomaton.setStates(newStates);
        newAutomaton.setTransitions(newTransitions);
        newAutomaton.setAlphabet(automaton.getAlphabet());

        return newAutomaton;

        // Note: this code removes the states that are unreachable from the starting/initial state.
        // An unreachable state might actually have a transition to another state but still that state might be unreachable from the starting state
        // for example in this constraint automaton:
        // A --label_1--> B (A is initial state)
        // C --label_2--> B
        // state C has a transition to state B but is still unreachable from the initial state A
        // therefore, it is removed from the CA along with its transition to B
        // this code uses bfs to find states that are reachable from the initial state and then filters the automaton based on those states and removes the redundant states & transitions

    }

    public void printAutomatonDetails(ConstraintAutomaton automaton, boolean showAllDetails) {
        System.out.println("CA name: " + automaton.getId());
        System.out.println("Total number of states: " + automaton.getStates().size());
        System.out.println("Total number of transitions: " + automaton.getTransitions().size());
        System.out.println("Total transition alphabets: " + automaton.getAlphabet().size());
        if(showAllDetails) {
            System.out.println("States:");
            for (State state : automaton.getStates())
                System.out.println("{Id:" + state.getId() + ", initial: " + state.isInitial() + "}");
            System.out.println("Transitions:");
            for (Transition transition : automaton.getTransitions())
                System.out.println("{Source:" + transition.getSource() + ", Target: " + transition.getTarget() + ", Label: " + transition.getLabel() + "}");
            System.out.println(automaton.getAlphabet());
        }
    }

}
