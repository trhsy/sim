package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;

/**
 * @ClassName JobTaskIdle
 * @Description todo 闲置工作任务
 * @Author TRHSY
 * @Date 2022/11/1520:16
 **/
public class JobTaskIdle extends JobTask {
    public transient long delayTime;
    String status;

    public JobTaskIdle(Job j, long ms, String status) {
        super(j, ms);
        this.delayTime = ms;
        this.status = status;
    }
    @Override
    public void onTaskComplete() {
    }
    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
    }
    @Override
    public void onUpdate() {
    }
}