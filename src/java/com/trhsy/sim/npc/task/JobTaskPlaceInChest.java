package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

/**
 * @ClassName JobTaskPlaceInChest
 * @Description todo 往箱子里放东西
 * @Author TRHSY
 * @Date 2023/4/89:26
 **/
public class JobTaskPlaceInChest extends JobTask {
    /**
     * @Author fan
     * @Description //TODO 放箱子里的物品
     * @Date 9:27 2023/4/8
     * @Param 
     * @return 
     **/
    ItemStack toPlace;
    /*
     * @Author fan
     * @Description //TODO 放入时间
     * @Date 9:27 2023/4/8
     * @Param 
     * @return 
     **/
    transient long timeToPlace = 0L;
    public JobTaskPlaceInChest(Job j, long ms, ItemStack is) {
        super(j, ms);
        this.toPlace = is;
    }

    @Override
    public void onTaskBegin() {
        this.folk.setStatus(I18n.format("container.sim.job_task_PlaceInChest1"));
    }

    @Override
    public void onUpdate() {
        if (this.job.folk.isAtLocation(this.job.workPlace)) {
            if (this.timeToPlace == 0L) {
                this.timeToPlace = System.currentTimeMillis();
                return;
            }

            if (System.currentTimeMillis() - this.timeToPlace < 10000L) {
                return;
            }

            this.job.placeInJobChest(this.toPlace);
            ModSimLoader.addMoney(0.02F * (float)this.toPlace.stackSize);
            this.completeTask();
        } else if (!this.job.folk.entity.isMoving() && this.job.folk.entity.getNavigator().getPath() == null) {
            if(!this.folk.forceMoveToXYZ(this.job.workPlace)){
                this.folk.forceMoveToXYZNoWarp(this.job.workPlace);
            }
        }
    }

    @Override
    public void onTaskComplete() {

    }
}
