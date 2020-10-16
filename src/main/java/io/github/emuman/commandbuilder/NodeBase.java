package io.github.emuman.commandbuilder;

import io.github.emuman.commandbuilder.exceptions.CommandStructureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class NodeBase {

    private String name;
    private List<NodeBase> nodes;

    public NodeBase(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
    }

    /**
     *
     *
     * @param args the arguments to pass into the command tree
     * @param values the values that have been collected
     * @return the tracelog containing information about execution
     */
    public CommandTraceLog run(String[] args, Map<String, Object> values) throws CommandStructureException {
        CommandTraceLog traceLog = new CommandTraceLog();
        onExecute(args, values, traceLog);
        return traceLog;
    }

    public abstract void onExecute(String[] args, Map<String, Object> values, CommandTraceLog traceLog) throws CommandStructureException;

    public abstract NodeBase createCopy(String name);

    public NodeBase createCopy() {
        return createCopy(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NodeBase> getNodes() {
        return nodes;
    }

    public NodeBase addNode(NodeBase node) {
        nodes.add(node);
        return this;
    }

    public String toString() {
        return name;
    }

}
