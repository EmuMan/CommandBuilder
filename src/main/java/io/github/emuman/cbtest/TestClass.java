package io.github.emuman.cbtest;

import java.util.ArrayList;
import java.util.List;

public class TestClass {

    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int frequency;
    private int duration;

    public TestClass() {
        radius = 50;
        frequency = 3;
        duration = 60;
    }



}
