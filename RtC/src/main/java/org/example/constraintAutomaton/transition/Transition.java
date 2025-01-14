package org.example.constraintAutomaton.transition;

import org.example.constraintAutomaton.constraint.Constraint;
import java.util.List;

public class Transition {
    private String source;
    private String target;
    private List<String> labels;
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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }
}