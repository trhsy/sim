package com.trhsy.sim.api.buildcraft.api.tablet;

public class TabletTicker {
    private final float tickTime;
    private float time = 0.0F;
    private int ticked = 0;

    public TabletTicker(float tickTime) {
        this.tickTime = tickTime;
    }

    public void add(float time) {
        for(this.time += time; this.time >= this.tickTime; ++this.ticked) {
            this.time -= this.tickTime;
        }

    }

    public int getTicks() {
        return this.ticked;
    }

    public boolean tick() {
        boolean oldTicked = this.ticked > 0;
        this.ticked = 0;
        return oldTicked;
    }

    public void reset() {
        this.ticked = 0;
        this.time = 0.0F;
    }
}
