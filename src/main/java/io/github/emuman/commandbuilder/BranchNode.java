package io.github.emuman.commandbuilder;

import io.github.emuman.commandbuilder.exceptions.CommandStructureException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BranchNode extends NodeBase {

    public BranchNode(String name) {
        super(name);
    }

    @Override
    public void onExecute(String[] args, Map<String, Object> values, CommandTraceLog traceLog) throws CommandStructureException {
        if (args.length == 0) {
            addTraceLogData(traceLog, CommandTraceLog.ReturnCode.MISSING_ARGUMENT, null);
            return;
        }
        if (getNodes().size() == 0) {
            throw new CommandStructureException("BranchNode must have at least one node to follow");
        }
        for (NodeBase node : getNodes()) {
            if (node.getName().equalsIgnoreCase(args[0])) {
                // found the correct node, run it
                addTraceLogData(traceLog, CommandTraceLog.ReturnCode.SUCCESS, node);
                values.put(getName(), node.getName());
                node.onExecute(Arrays.copyOfRange(args, 1, args.length), values, traceLog);
                return;
            }
        }
        // none of the nodes had the name given as an argument
        addTraceLogData(traceLog, CommandTraceLog.ReturnCode.INVALID_ARGUMENT, null);
    }

    @Override
    public NodeBase createCopy(String name) {
        // pretty much useless on this node
        return new BranchNode(name);
    }

    /**
     * Adds information to the tracelog regarding how the execution went
     *
     * @param traceLog the TraceLog object to add data to
     * @param code whether or not it was successful, and if not, what went wrong
     * @param choice the node/branch that was chosen by the argument, if any
     */
    public void addTraceLogData(CommandTraceLog traceLog, CommandTraceLog.ReturnCode code, NodeBase choice) {
        if (code == CommandTraceLog.ReturnCode.SUCCESS) {
            // don't need to update return code, that will be done either at the end or when something does fail
            traceLog.addTrace(choice.getName());
        } else {
            // put a list of possible options in the tracelog, then set the return code
            traceLog.addTrace("[" + getNodes().stream().map(NodeBase::getName).collect(Collectors.joining("|")) + "]");
            traceLog.setReturnCode(code);
        }
    }

}
