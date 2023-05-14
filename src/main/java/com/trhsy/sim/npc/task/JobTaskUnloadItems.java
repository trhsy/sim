package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobTaskUnloadItems
 * @Description todo 卸载材料
 * @Author TRHSY
 * @Date 2022/11/1521:08
 **/
public class JobTaskUnloadItems extends JobTask {
    public List<ItemStack> collectionItems = new ArrayList();
    transient long timeToUnload = 0L;

    public JobTaskUnloadItems(Job j, long ms, List<ItemStack> collectionItems) {
        super(j, ms);
        this.collectionItems = collectionItems;
    }
    @Override
    public void onTaskBegin() {
        if (this.collectionItems.size() < 1) {
            this.completeTask();
        } else {
            this.job.folk.forceMoveToXYZ(this.job.workPlace);
        }
    }
    @Override
    public void onUpdate() {
        List<ItemStack> toDelete = new ArrayList();
        if (this.job.folk.isAtLocation(this.job.workPlace)) {
            //正在卸载货物
            this.folk.setStatus(I18n.format("container.sim.job_task_Unloading_inventory"));
            if (this.timeToUnload == 0L) {
                this.timeToUnload = System.currentTimeMillis();
                return;
            }

            if (System.currentTimeMillis() - this.timeToUnload < 10000L) {
                return;
            }

            for(int i = 0; i < this.job.folk.inventory.size(); ++i) {
                ItemStack is = this.job.folk.inventory.get(i);
                for(int j = 0; j < this.collectionItems.size(); ++j) {
                    ItemStack collectionItem = this.collectionItems.get(j);
                    if (is.isItemEqual(collectionItem)) {
                        this.job.placeInJobChest(is);
                        if(ModSimLoader.gamemode!=1){
                            ModSimLoader.addMoney(-0.02F * (float)is.stackSize);
                        }
                        toDelete.add(is);
                    }
                }
            }

            for(int i = 0; i < toDelete.size(); ++i) {
                this.job.folk.inventory.remove(toDelete.get(i));
            }

            toDelete.clear();
            this.completeTask();
        } else {
            //返回工作岗位
            this.folk.setStatus(I18n.format("container.sim.job_task_Returning_to_work"));
            if (!this.job.folk.entity.isMoving()) {
                this.job.folk.forceMoveToXYZ(this.job.workPlace);
            }
        }

    }
    @Override
    public void onTaskComplete() {
    }
}