package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskCourier
 * @Description: 去取件
 * @date 2023/08/15 下午 4:06
 */
public class JobTaskCourier extends JobTask {
    //步
    public int step = 1;
    public String status;

    public JobTaskCourier(Job j, long ms, String status) {
        super(j, ms);
        this.status = status;
    }
    @Override
    public void onTaskBegin() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onTaskComplete() {

    }
}
