package org.example.ops;

import org.example.constraintAutomaton.ConstraintAutomaton;
import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;
import org.example.exceptions.JoinOperationFailedException;
import org.example.utils.AutomatonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SingleJoin {
    private final AutomatonUtils automatonUtils;

    public SingleJoin(AutomatonUtils automatonUtils) {
        this.automatonUtils = automatonUtils;
    }

    public ConstraintAutomaton joinAutomata(ConstraintAutomaton automaton_1, ConstraintAutomaton automaton_2) throws JoinOperationFailedException {
        ConstraintAutomaton joinedAutomaton = new ConstraintAutomaton();
        joinedAutomaton.setId(automatonUtils.getJoinedAutomatonId(automaton_1.getId(), automaton_2.getId()));
        List<State> states1 = automaton_1.getStates();
        List<State> states2 = automaton_2.getStates();

        List<State> joinedStates = new ArrayList<>();
        List<Transition> joinedTransitions = new ArrayList<>();
        List<String> joinedAlphabet = new ArrayList<>(automaton_1.getAlphabet());

        try {
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
                        //e.g. this if is for the case of transition from p1_p2 -> p1_q2 (first p1 is same)
                        if ((Objects.equals(state1.getComposition().get(0), state2.getComposition().get(0)))) {
                            Transition transition = automatonUtils.getAutomatonTransition(automaton_2.getTransitions(), state1.getComposition().get(1), state2.getComposition().get(1));
                            if (transition != null) {

                                boolean intersectionExists = automatonUtils.transitionsIntersectionExists(transition.getLabel(), automaton_1.getAlphabet());
                                if (!intersectionExists) {
                                    Transition newTransition = new Transition();
                                    newTransition.setSource(state1.getId());
                                    newTransition.setTarget(state2.getId());
                                    newTransition.setLabel(transition.getLabel());
                                    newTransition.setConstraints(new ArrayList<>());
                                    joinedTransitions.add(newTransition);
                                }
                            }
                        }

                        //e.g. this if is for the case of transition from q1_q2 -> p1_q2 (second q2 is same)
                        else if ((Objects.equals(state1.getComposition().get(1), state2.getComposition().get(1)))) {

                            Transition transition = automatonUtils.getAutomatonTransition(automaton_1.getTransitions(), state1.getComposition().get(0), state2.getComposition().get(0));
                            if (transition != null) {
                                boolean intersectionExists = automatonUtils.transitionsIntersectionExists(transition.getLabel(), automaton_2.getAlphabet());
                                if (!intersectionExists) {
                                    Transition newTransition = new Transition();
                                    newTransition.setSource(state1.getId());
                                    newTransition.setTarget(state2.getId());
                                    newTransition.setLabel(transition.getLabel());
                                    newTransition.setConstraints(new ArrayList<>());
                                    joinedTransitions.add(newTransition);
                                }
                            }
                        }

                        // this case is for when neither the first nor the second are the same (e.g. q1_p2 -> p1_q2)
                        else {
                            Transition transition1 = automatonUtils.getAutomatonTransition(automaton_1.getTransitions(), state1.getComposition().get(0), state2.getComposition().get(0));
                            Transition transition2 = automatonUtils.getAutomatonTransition(automaton_2.getTransitions(), state1.getComposition().get(1), state2.getComposition().get(1));
                            if (transition1 != null && transition2 != null) { // check this !!!
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

            for (State state : joinedStates) {
                state.setComposition(new ArrayList<>(List.of(state.getId())));
            }

            joinedAutomaton.setStates(joinedStates);
            joinedAutomaton.setTransitions(joinedTransitions);
            joinedAutomaton.setAlphabet(joinedAlphabet);

            return joinedAutomaton;
        }
        catch (Exception e) {
            throw new JoinOperationFailedException(e.getMessage());
        }
    }

}
