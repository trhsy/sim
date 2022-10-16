package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.NpcData;

import java.util.Random;

/**
 * @ClassName Task
 * @Description todo 任务
 * @Author TRHSY
 * @Date 2022/10/1615:47
 **/
public abstract class Task {
    public NpcData folk;
    public transient long deadline;
    public transient long timeSinceLastRun = 0L;
    public boolean hasBegun;
    public boolean completed;
    public boolean interruptSleep;
    public Random rand = new Random();

    public Task(NpcData folk, long ms) {
        this.folk = folk;
        this.deadline = ms;
    }

    public void completeTask() {
        this.onTaskComplete();
        this.completed = true;
        this.folk.nextTask();
    }

    public void failTask(String message) {
        this.onTaskComplete();
        this.completed = true;
        this.folk.nextTask();
    }

    public void begin() {
        this.completed = false;
        this.timeSinceLastRun = System.currentTimeMillis();
        this.onTaskBegin();
    }

    public void update() {
        if (!this.completed) {
            if (this.deadline > 0L && System.currentTimeMillis() - this.timeSinceLastRun > this.deadline) {
                this.completeTask();
            } else {
                this.onUpdate();
            }
        }
    }

    public abstract void onTaskBegin();

    public abstract void onUpdate();

    public abstract void onTaskComplete();
}
