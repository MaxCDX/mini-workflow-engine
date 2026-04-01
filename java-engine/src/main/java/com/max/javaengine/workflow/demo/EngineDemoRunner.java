package com.max.javaengine.workflow.demo;

import com.max.javaengine.workflow.domain.Edge;
import com.max.javaengine.workflow.domain.Node;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;
import com.max.javaengine.workflow.domain.WorkflowDSL;
import com.max.javaengine.workflow.engine.WorkflowEngine;

import java.util.List;
import java.util.Map;

public class EngineDemoRunner {
    public static void main(String[] args) {
        WorkflowDSL workflowDSL = buildDemoWorkflow();
        Map<String, Object> input = Map.of("userInput", "Hello");

        printWorkflowOverview(workflowDSL, input);

        WorkflowEngine workflowEngine = new WorkflowEngine();
        NodeRunResult result = workflowEngine.execute(workflowDSL, input);

        System.out.println();
        System.out.println("Final result");
        System.out.println("------------");
        System.out.println("Status : " + result.getStatus());
        System.out.println("Outputs: " + result.getOutputs());
        if (result.getErrorMessage() != null) {
            System.out.println("Error  : " + result.getErrorMessage());
        }
    }

    private static WorkflowDSL buildDemoWorkflow() {
        Node startNode = new Node();
        startNode.setId("start-1");
        startNode.setType(NodeType.START);

        Node llmNode = new Node();
        llmNode.setId("llm-1");
        llmNode.setType(NodeType.LLM);

        Node endNode = new Node();
        endNode.setId("end-1");
        endNode.setType(NodeType.END);

        Edge startToLlm = new Edge();
        startToLlm.setSourceNodeId("start-1");
        startToLlm.setTargetNodeId("llm-1");

        Edge llmToEnd = new Edge();
        llmToEnd.setSourceNodeId("llm-1");
        llmToEnd.setTargetNodeId("end-1");

        WorkflowDSL workflowDSL = new WorkflowDSL();
        workflowDSL.setNodes(List.of(startNode, llmNode, endNode));
        workflowDSL.setEdges(List.of(startToLlm, llmToEnd));
        return workflowDSL;
    }

    private static void printWorkflowOverview(WorkflowDSL workflowDSL, Map<String, Object> input) {
        System.out.println("Workflow demo");
        System.out.println("-------------");
        System.out.println("Flow  : Start -> Llm -> End");
        System.out.println("Input : " + input);
        System.out.println("Nodes : " + workflowDSL.getNodes().stream().map(Node::getId).toList());
        System.out.println("Edges : ");
        for (Edge edge : workflowDSL.getEdges()) {
            System.out.println("  " + edge.getSourceNodeId() + " -> " + edge.getTargetNodeId());
        }
        System.out.println();
        System.out.println("Expected data flow");
        System.out.println("------------------");
        System.out.println("Start passes userInput forward");
        System.out.println("Llm turns it into a mock response");
        System.out.println("End returns that response as final content");
    }
}
