package com.max.javaengine.workflow.executor.impl;

import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;
import com.max.javaengine.workflow.executor.AbstractNodeExecutor;

import java.util.HashMap;
import java.util.Map;

public class StartNodeExecutor extends AbstractNodeExecutor {
    @Override
    public NodeType getSupportedType() {
        return NodeType.START;
    }

    @Override
    protected NodeRunResult doExecute(NodeExecutionContext context) {
        Map<String, Object> outputs = new HashMap<>(context.getInputs());
        return success(outputs);
    }
}
