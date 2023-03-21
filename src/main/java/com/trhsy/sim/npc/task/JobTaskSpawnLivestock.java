package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskSpawnLivestock
 * @Description: 生成畜牧
 * @date 2023/3/21 15:57
 */
public class JobTaskSpawnLivestock extends JobTask {

    public String livestockName;
    public  Class livestockClass;

    public JobTaskSpawnLivestock(Job job, String livestockName, Class livestockClass, long ms) {
        super(job, ms);
        this.livestockName = livestockName;
        this.livestockClass = livestockClass;
    }

    /**
     * 开始任务
     */
    @Override
    public void onTaskBegin() {
        this.folk.setStatus(I18n.format("container.sim.job_task_Spawn1"));
        ModSimLoader.log.info("开始繁殖牲畜");
        //开始检测当前地方的实体动物
        int animalCount = this.getAnimalsInPen(this.job.workPlace, this.livestockClass).size();
        ModSimLoader.log.info( "检测到 "+animalCount + " " + this.livestockName);
        if (animalCount < 6) {
        }
        ModSimLoader.log.info("生成 " + (6 - animalCount) + " 以上");
        this.spawnAnimals(this.job.workPlace, this.livestockName, 6 - animalCount);
    }

    /**
     * 实体动物
     * @param controlBox
     * @param animal
     * @return
     */
    public List<EntityAnimal> getAnimalsInPen(V3 controlBox, Class animal) {
        List<EntityAnimal> list = this.folk.entity.worldObj.getEntitiesWithinAABB(animal, new AxisAlignedBB(controlBox.x - 5.0D, controlBox.y, controlBox.z - 5.0D, controlBox.x + 5.0D, controlBox.y + 2.0D, controlBox.z + 5.0D));
        return list;
    }

    private void spawnAnimals(V3 controlBox, String animal, int count) {
        EntityAnimal newAnimal = null;

        for(int c = 1; c <= count; ++c) {
            if (animal.contentEquals(I18n.format("container.sim.job_Livestock_pig"))) {
                newAnimal = new EntityPig(this.job.jobWorld);
            } else if (animal.contentEquals(I18n.format("container.sim.job_Livestock_cow"))) {
                newAnimal = new EntityCow(this.job.jobWorld);
            } else if (animal.contentEquals(I18n.format("container.sim.job_Livestock_chicken"))) {
                newAnimal = new EntityChicken(this.job.jobWorld);
            } else if (animal.contentEquals(I18n.format("container.sim.job_Livestock_sheep"))) {
                newAnimal = new EntitySheep(this.job.jobWorld);
            }else if (animal.contentEquals(I18n.format("container.sim.job_Livestock_rabbit"))) {
                newAnimal = new EntityRabbit(this.job.jobWorld);
            }

            ((EntityAnimal)newAnimal).setLocationAndAngles(controlBox.x, controlBox.y + 1.0D, controlBox.z, 0.0F, 0.0F);
            if (!this.job.jobWorld.isRemote) {
                this.job.jobWorld.spawnEntityInWorld(newAnimal);
            }
        }

    }

    /**
     * 更新任务
     */
    @Override
    public void onUpdate() {

    }

    /**
     * 完成任务
     */
    @Override
    public void onTaskComplete() {

    }
}
