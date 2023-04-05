package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskHarvestAnimal;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @ClassName JobShepherd
 * @Description todo 羊毛农
 * @Author TRHSY
 * @Date 2023/4/521:04
 **/
public class JobShepherd extends Job{
    public JobShepherd(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //牧羊人
        this.jobName = I18n.format("container.sim.Vocation8");
        this.addJobTask(new JobTaskIdle(this, 5000L, I18n.format("container.sim.job.builder_Arrived")));
        this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_sheep"), EntitySheep.class, 5000L));
        this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_sheep")));
        this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, I18n.format("container.sim.job_Livestock_sheep"), new ItemStack(Blocks.WOOL, 3), true, "Shearing sheep"));
        this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_sheep")));
    }

    @Override
    public String toString() {
        return this.jobName;
    }
}
