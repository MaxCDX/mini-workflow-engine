package com.max.javaengine.workflow.executor.impl;

import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;
import com.max.javaengine.workflow.executor.AbstractNodeExecutor;

import java.util.HashMap;
import java.util.Map;

public class EndNodeExecutor extends AbstractNodeExecutor {
    @Override
    public NodeType getSupportedType() {
        return NodeType.END;
    }

    @Override
    protected NodeRunResult doExecute(NodeExecutionContext context) {
        Map<String, Object> outputs = new HashMap<>();
        outputs.put("content", resolveFinalContent(context.getInputs()));
        return success(outputs);
    }

    private String resolveFinalContent(Map<String, Object> inputs) {
        Object response = inputs.get("response");
        if (response != null) {
            return String.valueOf(response);
        }

        if (inputs.size() == 1) {
            return String.valueOf(inputs.values().iterator().next());
        }

        return String.valueOf(inputs);
    }
}
