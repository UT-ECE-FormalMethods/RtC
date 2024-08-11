package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AutomataJoin {
    public ConstraintAutomaton joinAutomata(ConstraintAutomaton automaton_1, ConstraintAutomaton automaton_2) {
        ConstraintAutomaton joinedAutomaton = new ConstraintAutomaton();

        List<State> states1 = automaton_1.getStates();
        List<State> states2 = automaton_2.getStates();

        List<State> joinedStates = new ArrayList<>();
        List<Transition> joinedTransitions = new ArrayList<>();
        List<String> joinedAlphabet = new ArrayList<>(automaton_1.getAlphabet());

        for (String symbol : automaton_2.getAlphabet()) {
            if (!joinedAlphabet.contains(symbol)) {
                joinedAlphabet.add(symbol); // the total sets of alphabets in total transitions
            }
        }
        // Create joined states
        for (State state1 : states1) {
            for (State state2 : states2) {
                State joinedState = new State();
                joinedState.setId(state1.getId() + "_" + state2.getId());
                joinedState.setInitial(state1.isInitial() && state2.isInitial());
                joinedStates.add(joinedState);
                List<String> stateComposition = new ArrayList<>(Arrays.asList(state1.getId(), state2.getId()));
                joinedState.setComposition(stateComposition);
            }
        }

        for (State state1 : joinedStates) {
            for (State state2 : joinedStates) {
                if (!Objects.equals(state1.getId(), state2.getId())) {
//                    System.out.println(state1.getId() + " vs " + state2.getId());
                    if((Objects.equals(state1.getComposition().get(0), state2.getComposition().get(0))) || (Objects.equals(state1.getComposition().get(1), state2.getComposition().get(1)))) {
                        System.out.println("Protocol 2 for: " + state1.getId() + " with " + state2.getId());
                    }
                    else {
                        System.out.println("Protocol 1 for: " + state1.getId() + " with " + state2.getId());
                    }
                }
            }
        }

        // Create joined transitions
        for (Transition transition1 : automaton_1.getTransitions()) {
            for (Transition transition2 : automaton_2.getTransitions()) {
                List<String> commonLabels = new ArrayList<>(transition1.getLabel());
                commonLabels.retainAll(transition2.getLabel());
                System.out.println(commonLabels);

                if (!commonLabels.isEmpty()) {
                    Transition joinedTransition = new Transition();
                    joinedTransition.setSource(transition1.getSource() + "_" + transition2.getSource());
                    joinedTransition.setTarget(transition1.getTarget() + "_" + transition2.getTarget());
                    joinedTransition.setLabel(commonLabels);

                    List<Constraint> joinedConstraints = new ArrayList<>(transition1.getConstraints());
                    joinedConstraints.addAll(transition2.getConstraints());
                    joinedTransition.setConstraints(joinedConstraints);

                    joinedTransitions.add(joinedTransition);
                }
            }
        }

        joinedAutomaton.setStates(joinedStates);
        joinedAutomaton.setTransitions(joinedTransitions);
        joinedAutomaton.setAlphabet(joinedAlphabet);

        return joinedAutomaton;
    }
}
