package org.example.constraintAutomaton.transition;

import org.example.constraintAutomaton.constraint.Constraint;
import java.util.List;

public class Transition {
    private String source;
    private String target;
    private List<String> label;
    private List<Constraint> constraints;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
}