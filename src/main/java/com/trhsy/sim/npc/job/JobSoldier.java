package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskPatrol;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        this.addJobTask(new JobTaskPatrol(this, -1L));
    }

    @Override
    public String toString() {
        return this.jobName;
    }
}
