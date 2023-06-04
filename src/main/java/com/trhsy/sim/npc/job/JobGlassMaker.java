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
 * @ClassName JobGlassMaker
 * @Description todo 玻璃制造商
 * @Author TRHSY
 * @Date 2023/6/416:29
 **/
public class JobGlassMaker extends Job{

    public JobGlassMaker(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //玻璃制造商
        this.jobName = I18n.format("container.sim.Vocation17");
        //去上班
        this.addJobTask(new JobTaskIdle(this, 200L, I18n.format("container.sim.job.builder_Arrived")));
        //要收集的物品
        List<Block> colItems = new ArrayList();
        //沙子
        colItems.add(Blocks.SAND);
        List<Block> toMineItems = new ArrayList();
        V3 v3=new V3(pos.getX(),pos.getY(),pos.getZ());
        //寻找沙子
        this.folk.setStatus(I18n.format("container.sim.GOTOSANDBLOCK"));
        this.addJobTask(new JobTaskSearchForBlock(this, 120000, colItems,true,v3,30,false,toMineItems));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation17");
    }
}
