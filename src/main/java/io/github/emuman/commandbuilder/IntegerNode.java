package io.github.emuman.commandbuilder;

import com.sun.istack.internal.Nullable;
import io.github.emuman.commandbuilder.exceptions.CommandStructureException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class IntegerNode extends NodeBase {

    private int lowerBound;
    private int upperBound;

    /**
     * A node for parsing an integer out of an argument
     *
     * @param name the name of the node
     * @param lowerBound the lowest integer that can be provided (inclusive)
     * @param upperBound the highest integer that can be provided (inclusive)
     */
    public IntegerNode(String name, int lowerBound, int upperBound) {
        super(name);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public void onExecute(String[] args, Map<String, Object> values, CommandTraceLog traceLog) throws CommandStructureException {
        if (args.length == 0) {
            addTraceLogData(traceLog, CommandTraceLog.ReturnCode.MISSING_ARGUMENT, null);
            return;
        }
        int value = 0;
        try {
            value = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            addTraceLogData(traceLog, CommandTraceLog.ReturnCode.INVALID_ARGUMENT, null);
            traceLog.setMessage(getName() + " must be a valid integer");
            return;
        }
        // if variable is not inside the given bounds
        if (!(value >= lowerBound && value <= upperBound)) {
            addTraceLogData(traceLog, CommandTraceLog.ReturnCode.ARGUMENT_NOT_IN_BOUNDS, null);
            traceLog.setMessage(getName() + " must be in between " + lowerBound + " and " + upperBound + " (inclusive)");
            return;
        }
        addTraceLogData(traceLog, CommandTraceLog.ReturnCode.SUCCESS, value);
        values.put(getName(), value);
        if (getNodes().size() == 0) {
            throw new CommandStructureException("IntegerNode must point towards one other node");
        }
        getNodes().get(0).onExecute(Arrays.copyOfRange(args, 1, args.length), values, traceLog);
    }

    @Override
    public NodeBase createCopy(String name) {
        return new IntegerNode(name, lowerBound, upperBound);
    }

    public void addTraceLogData(CommandTraceLog traceLog, CommandTraceLog.ReturnCode code, @Nullable Integer value) {
        if (code == CommandTraceLog.ReturnCode.SUCCESS) {
            traceLog.addTrace(value.toString());
        } else {
            traceLog.addTrace("<" + getName() + ">");
            traceLog.setReturnCode(code);
        }
    }

}
