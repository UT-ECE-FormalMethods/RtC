package org.example.constraintAutomaton;

public class AutomatonHeuristic {
    private ConstraintAutomaton automaton;
    private int transitionCount;
    private double transitionDensity;
    private int statesCount;
    //implementing different heuristic values as fields

    public AutomatonHeuristic(ConstraintAutomaton automaton) {
        this.automaton = automaton;
        this.transitionCount = automaton.getTransitions().size();
        this.statesCount = automaton.getStates().size();
        this.transitionDensity = (double) this.transitionCount / this.statesCount;
    }

    public ConstraintAutomaton getAutomaton() {
        return automaton;
    }

    public void setAutomaton(ConstraintAutomaton automaton) {
        this.automaton = automaton;
    }

    public double getTransitionDensity() {
        return transitionDensity;
    }

    public void setTransitionDensity(double transitionDensity) {
        this.transitionDensity = transitionDensity;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

    public void setTransitionCount(int transitionCount) {
        this.transitionCount = transitionCount;
    }

    public int getStatesCount() {
        return statesCount;
    }

    public void setStatesCount(int statesCount) {
        this.statesCount = statesCount;
    }
}
