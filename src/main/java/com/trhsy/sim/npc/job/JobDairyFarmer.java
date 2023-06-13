package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.*;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.util.Random;

/**
 * @ClassName JobDairyFarmer
 * @Description todo 牛奶农的工作
 * @Author TRHSY
 * @Date 2023/4/517:40
 **/
public class JobDairyFarmer extends Job{

    public JobDairyFarmer(NpcData folk, BlockPos pos, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(Items.MILK_BUCKET);
        //牛奶农
        this.jobName = I18n.format("container.sim.Vocation20");
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
                //生成 牛
                this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_cow"), EntityCow.class, 5000L));
                this.stage = 2;
            } else if (this.stage == 2) {
                //照料牛
                this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_cow")));
                this.stage = 3;
            } else if (this.stage == 3) {
                // 挤牛奶
                this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, I18n.format("container.sim.job_Livestock_cow"), new ItemStack(Items.MILK_BUCKET, 1), false, I18n.format("container.sim.MILKING")));
                this.stage = 4;
            }else if (this.stage == 4) {
                //照料牛
                this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_cow")));
                this.stage = 5;
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
        //牛奶农
        return this.jobName;
    }
}
