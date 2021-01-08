package io.github.emuman.commandbuilder;

import java.util.Map;
import java.util.function.Consumer;

public class EndNode extends NodeBase {

    private final Consumer<Map<String, Object>> function;

    public EndNode(String name, Consumer<Map<String, Object>> function) {
        super(name);
        this.function = function;
    }

    @Override
    public void onExecute(String[] args, Map<String, Object> values, CommandTraceLog traceLog) {
        if (args.length != 0) {
            traceLog.setReturnCode(CommandTraceLog.ReturnCode.EXTRA_ARGUMENT);
        }
        function.accept(values);
        traceLog.setReturnCode(CommandTraceLog.ReturnCode.SUCCESS);
    }

    @Override
    public NodeBase createCopy(String name) {
        return new EndNode(name, function);
    }

}
