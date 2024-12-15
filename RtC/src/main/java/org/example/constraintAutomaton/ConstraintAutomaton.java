package org.example.constraintAutomaton;

import org.example.constraintAutomaton.state.State;
import org.example.constraintAutomaton.transition.Transition;

import java.util.List;

public class ConstraintAutomaton {
    private String id;
    private List<State> states;
    private List<Transition> transitions;
    private List<String> alphabet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }
}