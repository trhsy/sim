package com.trhsy.sim.npc.job;

import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.task.JobTaskButcherAnimal;
import com.trhsy.sim.npc.task.JobTaskIdle;
import com.trhsy.sim.npc.task.JobTaskSpawnLivestock;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.*;
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
        this.livestockName = livestock;
        //农
        this.jobName = livestock + I18n.format("container.sim.Hire_farmer");
        //猪
        if (this.livestockName == I18n.format("container.sim.job_Livestock_pig")) {
            this.livestockClass = EntityPig.class;
            //牛
        } else if (this.livestockName == I18n.format("container.sim.job_Livestock_cow")) {
            this.livestockClass = EntityCow.class;
            //羊
        } else if (this.livestockName == I18n.format("container.sim.job_Livestock_sheep")) {
            this.livestockClass = EntitySheep.class;
            //鸡
        } else if (this.livestockName == I18n.format("container.sim.job_Livestock_chicken")) {
            this.livestockClass = EntityChicken.class;
            //兔
        }else if (this.livestockName == I18n.format("container.sim.job_Livestock_rabbit")) {
            this.livestockClass = EntityRabbit.class;
        }
        //去上班
        this.addJobTask(new JobTaskIdle(this, 5000L, I18n.format("container.sim.job.builder_Arrived")));

        this.addJobTask(new JobTaskSpawnLivestock(this, this.livestockName, this.livestockClass, 5000L));
        //照料
        this.addJobTask(new JobTaskIdle(this, 180000L, I18n.format("container.sim.job.crop.farmer.Tending1")+" " + this.livestockName + (this.livestockName == "sheep" ? "" : "s")));
        //屠戮畜生
        this.addJobTask(new JobTaskButcherAnimal(this, 10000L));
        this.addJobTask(new JobTaskIdle(this, -1L, I18n.format("container.sim.job.crop.farmer.Tending1")+" " + this.livestockName + (this.livestockName == "sheep" ? "" : "s")));
    }

    @Override
    public String toString() {
        //农民
        return this.jobName;
    }
}
