package io.github.emuman.cbtest;

import io.github.emuman.commandbuilder.*;
import io.github.emuman.commandbuilder.exceptions.CommandStructureException;

import java.util.Arrays;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) throws CommandStructureException {
        TestClass testClass = new TestClass();

        int radius = 0;
        int frequency = 0;
        int duration = 0;

        BranchNode argParse = new BranchNode("test");

        EndNode startEnd = new EndNode("start", (values -> {System.out.println("Starting fireworks...");}));

        EndNode setEnd = new EndNode("setfinal", (values -> {
            if (values.get("set").equals("radius")) {
                testClass.setRadius((Integer) values.get("radius"));
            } else if (values.get("set").equals("frequency")) {
                testClass.setFrequency((Integer) values.get("frequency"));
            } else if (values.get("set").equals("duration")) {
                testClass.setDuration((Integer) values.get("duration"));
            }
        }));

        EndNode getEnd = new EndNode("getfinal", (values -> {
            if (values.get("get").equals("radius")) {
                System.out.println("Radius: " + testClass.getRadius());
            } else if (values.get("get").equals("frequency")) {
                System.out.println("Frequency: " + testClass.getFrequency());
            } else if (values.get("get").equals("duration")) {
                System.out.println("Duration: " + testClass.getDuration());
            }
        }));

        BranchNode setValueChoices = (BranchNode) new BranchNode("set")
        .addNode(new IntegerNode("radius", 0, 256).addNode(setEnd))
        .addNode(new IntegerNode("frequency", 1, 1000).addNode(setEnd))
        .addNode(new IntegerNode("duration", 0, 6000).addNode(setEnd));


        SingleStringNode getValueChoices = new SingleStringNode(
                "get", Arrays.asList("radius", "frequency", "duration"));
        getValueChoices.addNode(getEnd);


        argParse.addNode(setValueChoices).addNode(getValueChoices).addNode(startEnd);

        System.out.println(argParse.run(new String[] {"set"}));
        System.out.println(argParse.run(new String[] {"bruh"}));
        System.out.println(argParse.run(new String[] {"get"}));
        System.out.println(argParse.run(new String[] {}));
        System.out.println(argParse.run(new String[] {"set", "radius"}));
        System.out.println(argParse.run(new String[] {"get", "radius"}));
        System.out.println(argParse.run(new String[] {"set", "radius", "40"}));
        System.out.println(argParse.run(new String[] {"get", "radius"}));
        System.out.println(argParse.run(new String[] {"start"}));


        /* ------------------------------------ */
        System.out.println("\n--------------\n");

        NodeBase repeat = new SingleStringNode("word")
                .addNode(new IntegerNode("amount", 1, 10)
                .addNode(new EndNode("end", (values -> {
                    for (int i = 0; i < (int) values.get("amount"); i++)
                        System.out.println(values.get("word"));
                }))));


        System.out.println(repeat.run(new String[] {"hello", "5"}));
        System.out.println(repeat.run(new String[] {"hello", "12"}));
        System.out.println(repeat.run(new String[] {"hello"}));
        System.out.println(repeat.run(new String[] {"hello", "e"}));
    }

}
