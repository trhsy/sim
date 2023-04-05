package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskHarvestAnimal;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @ClassName JobDairyFarmer
 * @Description todo 牛奶农的工作
 * @Author TRHSY
 * @Date 2023/4/517:40
 **/
public class JobDairyFarmer extends Job{

    public JobDairyFarmer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //牛奶农
        this.jobName = I18n.format("container.sim.Vocation20");
        //去上班
        this.addJobTask(new JobTaskIdle(this, 5000L, I18n.format("container.sim.job.builder_Arrived")));
        //生成 牛
        this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_cow"), EntityCow.class, 5000L));
        //照料牛
        this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_cow")));
        // 挤牛奶
        this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, I18n.format("container.sim.job_Livestock_cow"), new ItemStack(Items.MILK_BUCKET, 1), false, I18n.format("container.sim.MILKING")));
        //照料牛
        this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_cow")));
    }

    @Override
    public String toString() {
        //牛奶农
        return this.jobName;
    }
}
