package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSearchForBlock;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobBrickMaker
 * @Description todo 板砖工匠
 * @Author TRHSY
 * @Date 2023/6/416:29
 **/
public class JobBrickMaker extends Job{

    public JobBrickMaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //板砖工匠
        this.jobName = I18n.format("container.sim.Vocation25");
        //去上班
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
        //要收集的物品
        List<Block> colItems = new ArrayList();
        //黏土
        colItems.add(Blocks.CLAY);
        List<Block> toMineItems = new ArrayList();
        V3 v3=new V3(pos.getX(),pos.getY(),pos.getZ());
        //寻找黏土
        //去寻找
        this.folk.setStatus(I18n.format("container.sim.GOTOCLAYBLOCK"));
        this.addJobTask(new JobTaskSearchForBlock(this, 120000, colItems,true,v3,30,false,toMineItems));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation25");
    }
}
