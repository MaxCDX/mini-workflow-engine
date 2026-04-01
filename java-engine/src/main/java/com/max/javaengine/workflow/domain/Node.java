package com.max.javaengine.workflow.domain;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String id;
    private NodeType type;
    private Map<String, Object> config = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
