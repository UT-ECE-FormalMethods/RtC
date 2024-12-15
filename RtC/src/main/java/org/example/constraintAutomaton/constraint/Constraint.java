package org.example.constraintAutomaton.constraint;

import java.util.List;

public class Constraint {
    private String type;
    private List<String> channels;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
