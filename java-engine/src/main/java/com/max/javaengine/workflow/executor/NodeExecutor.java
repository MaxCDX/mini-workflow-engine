package com.max.javaengine.workflow.executor;

import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;

public interface NodeExecutor {
    NodeType getSupportedType();

    NodeRunResult execute(NodeExecutionContext context);
}
