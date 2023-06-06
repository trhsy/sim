package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPatrol;
import com.trhsy.sim.npc.task.JobTaskSearchForBlock;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobSoldier
 * @Description todo 士兵
 * @Author TRHSY
 * @Date 2023/4/1517:34
 **/
public class JobSoldier extends Job{

    public JobSoldier(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(ItemLoader.tinSword);
        //士兵
        this.jobName = I18n.format("container.sim.Vocation7");

    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.atWork) {
            if (this.stage == -1) {
                this.stage = 0;
            } else if (this.stage == 0) {
                this.stage = 1;
                //去上班
                this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
            } else if (this.stage == 1) {
                this.addJobTask(new JobTaskPatrol(this, -1L));
                this.stage = 2;
            }else{
                if (this.jobTasks.size() > 0&&this.currentTask==null) {
                    this.currentTask = (JobTask) this.jobTasks.get(0);
                    this.currentTask.begin();
                }
            }
        }
    }
    @Override
    public String toString() {
        return this.jobName;
    }
}
