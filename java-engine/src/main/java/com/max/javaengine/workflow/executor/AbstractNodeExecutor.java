package com.max.javaengine.workflow.executor;

import com.max.javaengine.workflow.domain.Node;
import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeExecutionStatus;
import com.max.javaengine.workflow.domain.NodeRunResult;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNodeExecutor implements NodeExecutor {
    @Override
    public final NodeRunResult execute(NodeExecutionContext context) {
        if (context == null || context.getNode() == null) {
            return failure("Node execution context is missing");
        }

        try {
            return doExecute(context);
        } catch (Exception exception) {
            return failure(exception.getMessage());
        }
    }

    protected abstract NodeRunResult doExecute(NodeExecutionContext context);

    protected NodeRunResult success(Map<String, Object> outputs) {
        NodeRunResult result = new NodeRunResult();
        result.setStatus(NodeExecutionStatus.SUCCESS);
        result.setOutputs(outputs);
        return result;
    }

    protected NodeRunResult failure(String errorMessage) {
        NodeRunResult result = new NodeRunResult();
        result.setStatus(NodeExecutionStatus.FAILED);
        result.setErrorMessage(errorMessage);
        result.setOutputs(new HashMap<>());
        return result;
    }

    protected String getStringConfig(Node node, String key) {
        Object value = node.getConfig().get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}
