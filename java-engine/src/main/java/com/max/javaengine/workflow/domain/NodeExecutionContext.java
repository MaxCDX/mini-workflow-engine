package com.max.javaengine.workflow.domain;

import java.util.HashMap;
import java.util.Map;

public class NodeExecutionContext {
    private Node node;
    private Map<String, Object> inputs = new HashMap<>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs;
    }
}
