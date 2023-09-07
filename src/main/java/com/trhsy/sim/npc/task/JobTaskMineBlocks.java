package com.trhsy.sim.npc.task;

import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobTaskMineBlocks
 * @Description todo 开采方块
 * @Author TRHSY
 * @Date 2023/6/416:24
 **/
public class JobTaskMineBlocks extends JobTask {
    List<BlockPos> toMine = new ArrayList();
    V3 startPoint;
    int x;
    int z;

    public JobTaskMineBlocks(Job j, long ms, V3 startPoint, int x, int z) {
        super(j, ms);
        this.startPoint = startPoint;
        this.x = x;
        this.z = z;
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
