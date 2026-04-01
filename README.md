# mini-workflow-engine
A lightweight workflow engine built from scratch with Java and Python versions.

# Mini Workflow Engine

A lightweight workflow engine built from scratch to understand how modern workflow systems (like LangFlow, Airflow, or internal orchestration engines) are designed and executed.

---

## 1. Overview

This project implements a minimal workflow engine that executes workflows defined as a graph-based DSL (Domain Specific Language).

A workflow is represented as a directed graph composed of:
- Nodes (units of execution)
- Edges (connections between nodes)

The system separates **workflow definition** from **workflow execution**, making it easy to extend and reason about.

Core idea:

- DSL defines what the workflow looks like
- Engine controls how the workflow runs
- Executors define what each node does
- VariablePool handles data passing between nodes

---

## 2. Architecture

The system is divided into four main layers:

### 2.1 DSL Layer (Structure Definition)

Responsible for describing the workflow structure.

Key components:
- `WorkflowDSL` → root object (nodes + edges)
- `Node` → represents a step in the workflow
- `Edge` → represents transitions between nodes
- `NodeData` / `Value` → configuration and inputs

Responsibility:
- Define graph structure
- Describe node configuration
- No execution logic

---

### 2.2 Engine Layer (Orchestration)

Responsible for coordinating execution.

Key component:
- `WorkflowEngine`

Responsibility:
- Validate workflow
- Build execution chain from graph
- Determine execution order
- Trigger node execution

Think of it as:
> The conductor of the workflow

---

### 2.3 Executor Layer (Execution Logic)

Responsible for executing specific node types.

Key components:
- `NodeExecutor` (interface)
- `AbstractNodeExecutor` (shared logic)
- Concrete executors:
  - `StartNodeExecutor`
  - `LLMNodeExecutor`
  - `EndNodeExecutor`

Responsibility:
- Execute node-specific logic
- Resolve inputs
- Produce outputs

Design pattern:
> Strategy Pattern (one executor per node type)

---

### 2.4 Runtime Layer (State Management)

Responsible for managing runtime data.

Key components:
- `VariablePool`
- `NodeState`
- `NodeRunResult`

Responsibility:
- Store intermediate results
- Enable data passing between nodes
- Track execution state

---

## 3. Execution Flow

The execution of a workflow follows these steps:

1. Load workflow definition (WorkflowDSL)
2. Initialize VariablePool
3. Find the Start node
4. Execute current node
5. Store outputs into VariablePool
6. Resolve next node via edges
7. Repeat until End node is reached

Simplified:

Start → Node → Node → End

Data flow:

Node A output → VariablePool → Node B input

---

## 4. Core Concepts

### Node
A unit of execution in the workflow.

Examples:
- Start node (entry point)
- LLM node (processing)
- End node (final output)

---

### Edge
Represents the connection between nodes.

Defines execution order and transitions.

---

### DSL (Domain Specific Language)
Defines the workflow structure as data.

- Stored as JSON or objects
- Decoupled from execution

---

### Executor
Responsible for executing a specific node type.

Keeps business logic isolated from the engine.

---

### VariablePool
Shared memory during execution.

- Stores outputs from nodes
- Provides inputs to downstream nodes

---

## 5. Example Workflow

Minimal example:

Start → LLM → End

Execution:

1. Start receives input
2. LLM processes input
3. End returns final result

### Example DSL (JSON)

```json
{
  "nodes": [
    { "id": "start", "type": "START" },
    { "id": "llm", "type": "LLM" },
    { "id": "end", "type": "END" }
  ],
  "edges": [
    { "source": "start", "target": "llm" },
    { "source": "llm", "target": "end" }
  ]
}
```

This JSON represents a minimal workflow graph where:
- Execution starts at `start`
- Flows into `llm`
- Ends at `end`
---

## 6. Design Principles

### Separation of Concerns
- DSL → structure
- Engine → control
- Executor → logic
- Runtime → state

---

### Extensibility
New node types can be added by:
- Implementing a new executor
- Registering it in the engine

---

### Declarative Design
Workflow is defined as data, not hardcoded flow.

---

## 7. Future Improvements

- Parallel execution (DAG support)
- Conditional branching
- Retry / timeout mechanisms
- Persistence layer
- Visualization (graph UI)
- Plugin system