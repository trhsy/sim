package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskChopTrees;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPlaceInChest;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import com.trhsy.sim.task.JobTask;
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
                //生成鸡
                this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_chicken"), EntityChicken.class, 5000L));
                this.stage = 2;
            } else if (this.stage == 2) {
                //照料鸡
                this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_chicken")));
                this.stage = 3;
            } else if (this.stage == 3) {
                //往工作项放东西
                this.addJobTask(new JobTaskPlaceInChest(this, 10000L, new ItemStack(Items.EGG, new Random().nextInt(5) + 1)));
                this.stage = 4;
            }else if (this.stage == 4) {
                //照料鸡
                this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_chicken")));
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
        return I18n.format("container.sim.Vocation3");
    }
}
