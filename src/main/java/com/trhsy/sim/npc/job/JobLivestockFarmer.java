package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskButcherAnimal;
import com.trhsy.sim.npc.task.JobTaskChopTrees;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.job
 * @ClassName: JobLivestockFarmer
 * @Description:
 * @date 2023/3/21 14:04
 */
public class JobLivestockFarmer extends Job{
    /**
     * 工作名称牲畜名称
     */
    public String livestockName;
    /**
     * 牲畜等级
     */
    public Class livestockClass;

    public JobLivestockFarmer(NpcData folk, BlockPos pos, String livestock, World world) {
        super(folk,pos,world);
        //手持斧子
        folk.holding = new ItemStack(ItemLoader.tinAxe);
        this.livestockName = livestock;
        //猪
        if (this.livestockName.equals(I18n.format("container.sim.job_Livestock_pig"))) {
            this.livestockClass = EntityPig.class;
            //养猪户
            this.jobName = I18n.format("container.sim.Vocation13");
            //牛
        } else if (this.livestockName.equals(I18n.format("container.sim.job_Livestock_cow"))) {
            this.livestockClass = EntityCow.class;
            //养牛户
            this.jobName =I18n.format("container.sim.Vocation12");
            //羊
        } else if (this.livestockName.equals(I18n.format("container.sim.job_Livestock_sheep"))) {
            this.livestockClass = EntitySheep.class;
            //养羊户
            this.jobName = I18n.format("container.sim.Vocation28");
            //鸡
        } else if (this.livestockName.equals(I18n.format("container.sim.job_Livestock_chicken"))) {
            this.livestockClass = EntityChicken.class;
            //养鸡户
            this.jobName = I18n.format("container.sim.Vocation14");
            //兔
        }else if (this.livestockName.equals(I18n.format("container.sim.job_Livestock_rabbit"))) {
            this.livestockClass = EntityRabbit.class;
            //养兔户
            this.jobName =  I18n.format("container.sim.Vocation29");
        }

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
                this.addJobTask(new JobTaskSpawnLivestock(this, this.livestockName, this.livestockClass, 5000L));
                this.stage = 2;
            }else if (this.stage == 2) {
                //照料
                this.addJobTask(new JobTaskIdle(this, 120000L, I18n.format("container.sim.job.crop.farmer.Tending1")+" " + this.livestockName));
                this.stage = 3;
            }else if (this.stage == 3) {
                //屠戮畜生
                this.addJobTask(new JobTaskButcherAnimal(this, 180000L));
                this.stage = 4;
            }else if (this.stage == 4) {
                this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+" " + this.livestockName ));
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
        //农民
        return this.jobName;
    }
}
