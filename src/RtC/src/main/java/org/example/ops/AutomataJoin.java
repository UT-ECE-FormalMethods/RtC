package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.constraint.Constraint;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;
import org.example.utils.automaton.AutomatonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AutomataJoin {
    public ConstraintAutomaton joinAutomata(ConstraintAutomaton automaton_1, ConstraintAutomaton automaton_2) {
        ConstraintAutomaton joinedAutomaton = new ConstraintAutomaton();
        AutomatonUtils automatonUtils = new AutomatonUtils();

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
                    if ((Objects.equals(state1.getComposition().get(0), state2.getComposition().get(0)))) {
                        Transition transition = automatonUtils.getAutomatonTransition(automaton_2.getTransitions(), state1.getComposition().get(1), state2.getComposition().get(1));
                        if(transition != null) {
                            System.out.println(state1.getId() + " -> " + state2.getId());
                            boolean intersectionExists = automatonUtils.transitionsIntersectionExists(transition.getLabel(), automaton_1.getAlphabet());
                            if(!intersectionExists) {
                                Transition newTransition = new Transition();
                                newTransition.setSource(state1.getId());
                                newTransition.setTarget(state2.getId());
                                newTransition.setLabel(transition.getLabel());
                                newTransition.setConstraints(new ArrayList<>());
                                joinedTransitions.add(newTransition);
                            }
                        }
                    }

                    else if ((Objects.equals(state1.getComposition().get(1), state2.getComposition().get(1)))) {
                        Transition transition = automatonUtils.getAutomatonTransition(automaton_1.getTransitions(), state1.getComposition().get(0), state2.getComposition().get(0));
                        if(transition != null) {
//                            System.out.println(state1.getId() + " -> " + state2.getId());
                            boolean intersectionExists = automatonUtils.transitionsIntersectionExists(transition.getLabel(), automaton_2.getAlphabet());
                            if(!intersectionExists) {
                                Transition newTransition = new Transition();
                                newTransition.setSource(state1.getId());
                                newTransition.setTarget(state2.getId());
                                newTransition.setLabel(transition.getLabel());
                                newTransition.setConstraints(new ArrayList<>());
                                joinedTransitions.add(newTransition);
                            }
                        }
                    }

                    else {
//                        System.out.println(state1.getId() + " -> " + state2.getId());
                        Transition transition1 = automatonUtils.getAutomatonTransition(automaton_1.getTransitions(), state1.getComposition().get(0), state2.getComposition().get(0));
                        Transition transition2 = automatonUtils.getAutomatonTransition(automaton_2.getTransitions(), state1.getComposition().get(1), state2.getComposition().get(1));
                        if(transition1 != null && transition2 != null) { // check this !!!
                            ArrayList<String> intersection1 = automatonUtils.getTransitionLabelsIntersection(transition1.getLabel(), automaton_2.getAlphabet());
                            ArrayList<String> intersection2 = automatonUtils.getTransitionLabelsIntersection(transition2.getLabel(), automaton_1.getAlphabet());
                            if (intersection1.equals(intersection2)) {
                                Transition newTransition = new Transition();
                                newTransition.setSource(state1.getId());
                                newTransition.setTarget(state2.getId());
                                newTransition.setLabel(automatonUtils.getTransitionLabelsUnion(transition1.getLabel(), transition2.getLabel()));
                                newTransition.setConstraints(new ArrayList<>());
                                joinedTransitions.add(newTransition);
                            }
                        }
                    }
                }
            }
        }

        for(State state: joinedStates) {
            state.setComposition(new ArrayList<>(List.of(state.getId())));
        }

        joinedAutomaton.setStates(joinedStates);
        joinedAutomaton.setTransitions(joinedTransitions);
        joinedAutomaton.setAlphabet(joinedAlphabet);

        return joinedAutomaton;
    }
}
