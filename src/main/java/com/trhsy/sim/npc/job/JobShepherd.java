package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskHarvestAnimal;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskPatrol;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import com.trhsy.sim.task.JobTask;
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
        //手持羊毛
        folk.holding = new ItemStack(Blocks.WOOL);
        //牧羊人
        this.jobName = I18n.format("container.sim.Vocation8");
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
                //生成羊
                this.addJobTask(new JobTaskSpawnLivestock(this, I18n.format("container.sim.job_Livestock_sheep"), EntitySheep.class, 5000L));
                this.stage = 2;
            }else if (this.stage == 2) {
                //照料羊
                this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_sheep")));
                this.stage = 3;
            }else if (this.stage == 3) {
                //生成羊
                this.addJobTask(new JobTaskHarvestAnimal(this, 10000L, I18n.format("container.sim.job_Livestock_sheep"), new ItemStack(Blocks.WOOL, 3), true, "Shearing sheep"));
                this.stage = 4;
            }else if (this.stage == 4) {
                //生成羊
                this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+I18n.format("container.sim.job_Livestock_sheep")));
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
        return this.jobName;
    }
}
