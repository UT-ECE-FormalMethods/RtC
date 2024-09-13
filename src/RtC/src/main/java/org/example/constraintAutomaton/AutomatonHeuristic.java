package org.example.constraintAutomaton;

public class AutomatonHeuristic {
    private ConstraintAutomaton automaton;
    private int transitionCount;
    private double transitionDensity;
    private int statesCount;
    private double tranStateHarmonicMean;
    private int tranStateSum;

    public AutomatonHeuristic(ConstraintAutomaton automaton) {
        this.automaton = automaton;
        this.transitionCount = automaton.getTransitions().size();
        this.statesCount = automaton.getStates().size();
        this.transitionDensity = (double) this.transitionCount / this.statesCount;
        this.tranStateHarmonicMean = (double) (2 * (this.transitionCount * this.statesCount)) / (this.transitionCount + this.statesCount);
        this.tranStateSum = automaton.getStates().size() + automaton.getTransitions().size();
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

    public double getTranStateHarmonicMean() {
        return tranStateHarmonicMean;
    }

    public void setTranStateHarmonicMean(double tranStateHarmonicMean) {
        this.tranStateHarmonicMean = tranStateHarmonicMean;
    }

    public int getTranStateSum() {
        return tranStateSum;
    }

    public void setTranStateSum(int tranStateSum) {
        this.tranStateSum = tranStateSum;
    }
}
