package com.max.javaengine.workflow.domain;

import java.util.HashMap;
import java.util.Map;

public class NodeRunResult {
    private NodeExecutionStatus status;
    private Map<String, Object> outputs = new HashMap<>();
    private String errorMessage;

    public NodeExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(NodeExecutionStatus status) {
        this.status = status;
    }

    public Map<String, Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, Object> outputs) {
        this.outputs = outputs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
