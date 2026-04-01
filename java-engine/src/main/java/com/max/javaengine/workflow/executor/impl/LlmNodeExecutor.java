package com.max.javaengine.workflow.executor.impl;

import com.max.javaengine.workflow.domain.NodeExecutionContext;
import com.max.javaengine.workflow.domain.NodeRunResult;
import com.max.javaengine.workflow.domain.NodeType;
import com.max.javaengine.workflow.executor.AbstractNodeExecutor;

import java.util.HashMap;
import java.util.Map;

public class LlmNodeExecutor extends AbstractNodeExecutor {
    @Override
    public NodeType getSupportedType() {
        return NodeType.LLM;
    }

    @Override
    protected NodeRunResult doExecute(NodeExecutionContext context) {
        String prompt = resolvePrompt(context);
        Map<String, Object> outputs = new HashMap<>();
        outputs.put("response", fakeLlmResponse(prompt));
        return success(outputs);
    }

    private String resolvePrompt(NodeExecutionContext context) {
        Object promptInput = context.getInputs().get("prompt");
        if (promptInput != null) {
            return String.valueOf(promptInput);
        }

        Object userInput = context.getInputs().get("userInput");
        if (userInput != null) {
            return String.valueOf(userInput);
        }

        String configPrompt = getStringConfig(context.getNode(), "prompt");
        if (configPrompt != null) {
            return configPrompt;
        }

        if (!context.getInputs().isEmpty()) {
            return String.valueOf(context.getInputs().values().iterator().next());
        }

        return "";
    }

    private String fakeLlmResponse(String prompt) {
        return "Mock LLM response: " + prompt;
    }
}
