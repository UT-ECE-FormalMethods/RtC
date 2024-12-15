package org.example.constraintAutomaton.state;

import java.util.List;

public class State {
    private String id;
    private boolean initial;
    private List<String> composition; // for example if state name is q1_q2 then the composition is [q1, q2]

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public List<String> getComposition() {
        return composition;
    }

    public void setComposition(List<String> composition) {
        this.composition = composition;
    }
}
