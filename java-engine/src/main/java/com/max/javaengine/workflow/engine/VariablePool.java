package com.max.javaengine.workflow.engine;

import java.util.HashMap;
import java.util.Map;

public class VariablePool {
    private final Map<String, Map<String, Object>> variables = new HashMap<>();

    public void set(String nodeId, String outputName, Object value) {
        variables.computeIfAbsent(nodeId, ignored -> new HashMap<>()).put(outputName, value);
    }

    public Object get(String nodeId, String outputName) {
        Map<String, Object> nodeOutputs = variables.get(nodeId);
        if (nodeOutputs == null) {
            return null;
        }
        return nodeOutputs.get(outputName);
    }

    public Map<String, Object> get(String nodeId) {
        Map<String, Object> nodeOutputs = variables.get(nodeId);
        if (nodeOutputs == null) {
            return Map.of();
        }
        return nodeOutputs;
    }

    public void clear() {
        variables.clear();
    }
}
