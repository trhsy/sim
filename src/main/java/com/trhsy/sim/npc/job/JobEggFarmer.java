package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPlaceInChest;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.util.Random;

/**
 * @ClassName JobEggFarmer
 * @Description todo 鸡蛋农
 * @Author TRHSY
 * @Date 2023/4/89:01
 **/
public class JobEggFarmer extends Job{
    public JobEggFarmer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        //鸡蛋农
        this.jobName = I18n.format("container.sim.Vocation3");
        //去上班
        this.addJobTask(new JobTaskIdle(this, 5000L, I18n.format("container.sim.job.builder_Arrived")));
        //生成鸡
        this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_chicken"), EntityChicken.class, 5000L));
        //照料鸡
        this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_chicken")));
        //往工作项放东西
        this.addJobTask(new JobTaskPlaceInChest(this, 10000L, new ItemStack(Items.EGG, new Random().nextInt(5) + 1)));
        //照料鸡
        this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_chicken")));
    }

    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation3");
    }
}
