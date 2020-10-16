package io.github.emuman.commandbuilder;

import com.sun.istack.internal.Nullable;
import io.github.emuman.commandbuilder.exceptions.CommandStructureException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SingleStringNode extends NodeBase {

    private List<String> options;

    /**
     * A node for parsing an integer out of an argument
     *
     * @param name the name of the node
     * @param options the list of words that can be passed
     */
    public SingleStringNode(String name, List<String> options) {
        super(name);
        this.options = options;
    }

    /**
     * A node for parsing an integer out of an argument
     *
     * @param name the name of the node
     */
    public SingleStringNode(String name) {
        super(name);
        this.options = null;
    }

    @Override
    public void onExecute(String[] args, Map<String, Object> values, CommandTraceLog traceLog) throws CommandStructureException {
        if (args.length == 0) {
            addTraceLogData(traceLog, CommandTraceLog.ReturnCode.MISSING_ARGUMENT, null);
            return;
        }
        String choice = args[0].toLowerCase();
        // if there are specific options for this string
        if (options != null && options.size() != 0) {
            // make sure variable is in the list of options
            if (!options.contains(choice)) {
                addTraceLogData(traceLog, CommandTraceLog.ReturnCode.INVALID_ARGUMENT, null);
                return;
            }
        }
        addTraceLogData(traceLog, CommandTraceLog.ReturnCode.SUCCESS, choice);
        values.put(getName(), choice);
        if (getNodes().size() == 0) {
            throw new CommandStructureException("SingleStringNode must point towards one other node");
        }
        getNodes().get(0).onExecute(Arrays.copyOfRange(args, 1, args.length), values, traceLog);
    }

    @Override
    public NodeBase createCopy(String name) {
        return new SingleStringNode(name, options);
    }

    public void addTraceLogData(CommandTraceLog traceLog, CommandTraceLog.ReturnCode code, @Nullable String choice) {
        if (code == CommandTraceLog.ReturnCode.SUCCESS) {
            traceLog.addTrace(choice);
        } else {
            if (options == null || options.size() == 0) {
                // there are no options, display name of node
                traceLog.addTrace("<" + getName() + ">");
            } else {
                // there are options to choose from, display that
                traceLog.addTrace("[" + String.join("|", options) + "]");
            }
            traceLog.setReturnCode(code);
        }
    }

}
