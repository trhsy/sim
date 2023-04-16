package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskChopTrees;
import com.trhsy.sim.npc.task.JobTaskIdle;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobLumberjack
 * @Description todo 伐木工
 * @Author TRHSY
 * @Date 2023/4/1517:33
 **/
public class JobLumberjack extends Job {
    List<Block> wood = new ArrayList();
    List<Block> toMine = new ArrayList();

    public JobLumberjack(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding=new ItemStack(ItemLoader.tinAxe);
        this.jobName = I18n.format("container.sim.Vocation2");
        this.wood.add(Blocks.LOG);
        this.wood.add(Blocks.LOG2);
        //去上班
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
        this.addJobTask(new JobTaskChopTrees(this, -1L, this.workPlace, 30));
    }

    public String toString() {
        return this.jobName;
    }
}
