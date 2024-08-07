package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // Create joined transitions

        joinedAutomaton.setStates(joinedStates);
        return joinedAutomaton;
    }
}
