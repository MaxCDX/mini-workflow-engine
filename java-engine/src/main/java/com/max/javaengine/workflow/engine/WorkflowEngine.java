package com.max.javaengine.workflow.engine;

import com.max.javaengine.workflow.domain.Edge;
import com.max.javaengine.workflow.domain.Node;
import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeExecutionStatus;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;
import com.max.javaengine.workflow.domain.WorkflowDSL;
import com.max.javaengine.workflow.executor.NodeExecutor;
import com.max.javaengine.workflow.executor.impl.EndNodeExecutor;
import com.max.javaengine.workflow.executor.impl.LlmNodeExecutor;
import com.max.javaengine.workflow.executor.impl.StartNodeExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowEngine {
    private final Map<NodeType, NodeExecutor> executors = new HashMap<>();

    public WorkflowEngine() {
        this(List.of(
                new StartNodeExecutor(),
                new LlmNodeExecutor(),
                new EndNodeExecutor()
        ));
    }

    public WorkflowEngine(List<NodeExecutor> executors) {
        for (NodeExecutor executor : executors) {
            this.executors.put(executor.getSupportedType(), executor);
        }
    }

    public NodeRunResult execute(WorkflowDSL workflowDSL, Map<String, Object> initialInputs) {
        validateWorkflow(workflowDSL);

        VariablePool variablePool = new VariablePool();
        variablePool.clear();

        Node currentNode = findStartNode(workflowDSL);
        Map<String, Object> currentInputs = initialInputs == null
                ? new HashMap<>()
                : new HashMap<>(initialInputs);

        while (currentNode != null) {
            NodeExecutor executor = findExecutor(currentNode.getType());

            NodeExecutionContext context = new NodeExecutionContext();
            context.setNode(currentNode);
            context.setInputs(currentInputs);

            NodeRunResult result = executor.execute(context);
            storeOutputs(variablePool, currentNode.getId(), result.getOutputs());

            if (result.getStatus() == NodeExecutionStatus.FAILED) {
                return result;
            }

            if (currentNode.getType() == NodeType.END) {
                return result;
            }

            Node nextNode = findNextNode(workflowDSL, currentNode);
            if (nextNode == null) {
                return failure("Workflow ended before reaching END node");
            }

            currentInputs = new HashMap<>(variablePool.get(currentNode.getId()));
            currentNode = nextNode;
        }

        return failure("Workflow did not execute any nodes");
    }

    private void validateWorkflow(WorkflowDSL workflowDSL) {
        if (workflowDSL == null) {
            throw new IllegalArgumentException("Workflow DSL must not be null");
        }
        if (workflowDSL.getNodes() == null || workflowDSL.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Workflow must contain nodes");
        }
        if (workflowDSL.getEdges() == null || workflowDSL.getEdges().isEmpty()) {
            throw new IllegalArgumentException("Workflow must contain edges");
        }
    }

    private Node findStartNode(WorkflowDSL workflowDSL) {
        return workflowDSL.getNodes().stream()
                .filter(node -> node.getType() == NodeType.START)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Workflow must contain a START node"));
    }

    private NodeExecutor findExecutor(NodeType nodeType) {
        NodeExecutor executor = executors.get(nodeType);
        if (executor == null) {
            throw new IllegalArgumentException("No executor found for node type: " + nodeType);
        }
        return executor;
    }

    private Node findNextNode(WorkflowDSL workflowDSL, Node currentNode) {
        Edge nextEdge = workflowDSL.getEdges().stream()
                .filter(edge -> currentNode.getId().equals(edge.getSourceNodeId()))
                .findFirst()
                .orElse(null);

        if (nextEdge == null) {
            return null;
        }

        return workflowDSL.getNodes().stream()
                .filter(node -> node.getId().equals(nextEdge.getTargetNodeId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Target node not found: " + nextEdge.getTargetNodeId()
                ));
    }

    private void storeOutputs(VariablePool variablePool, String nodeId, Map<String, Object> outputs) {
        for (Map.Entry<String, Object> entry : outputs.entrySet()) {
            variablePool.set(nodeId, entry.getKey(), entry.getValue());
        }
    }

    private NodeRunResult failure(String errorMessage) {
        NodeRunResult result = new NodeRunResult();
        result.setStatus(NodeExecutionStatus.FAILED);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
