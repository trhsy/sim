package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobTaskProduceItem
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/1521:15
 **/
public class JobTaskProduceItem extends JobTask {
    Item produce;
    List<ItemStack> requirements;
    transient long timeToPlace = 0L;
    String status;
    /**
     * @Author fan
     * @Description //TODO
     * @Date 21:20 2022/11/16
     * @Param [j, ms, prod, req, stage, status]工作，耗时，成品，消耗品，状态字段
     * @return
     **/
    public JobTaskProduceItem(Job j, long ms, Item prod, List<ItemStack> req, String status) {
        super(j, ms);
        this.produce = prod;
        this.requirements = req;
        this.status = status;
    }

    public JobTaskProduceItem(Job j, long ms, Item prod, ItemStack req, String status) {
        super(j, ms);
        this.produce = prod;
        this.requirements = new ArrayList();
        this.requirements.add(req);
        this.status = status;
    }

    public void onTaskBegin() {
    }

    public void onUpdate() {
        if (this.job.folk.isAtLocation(this.job.workPlace)) {
            this.folk.setStatus(this.status);
            if (this.timeToPlace == 0L) {
                this.timeToPlace = System.currentTimeMillis();
                return;
            }

            if (System.currentTimeMillis() - this.timeToPlace < 10000L) {
                return;
            }

            List<IInventory> invs = this.job.inventoriesFindClosest(this.job.workPlace, 5);

            int maxProd;
            int i;
            int j;
            for(maxProd = 0; maxProd < invs.size(); ++maxProd) {
                IInventory iInventory=invs.get(maxProd);
                for(i = 0; i < iInventory.getSizeInventory(); ++i) {
                    ItemStack itemStack= iInventory.getStackInSlot(i);
                    for(j = 0; j < this.requirements.size(); ++j) {
                        ItemStack itemStacks=this.requirements.get(j);
                        if (itemStack!=null&&itemStack.isItemEqual(itemStacks)) {
                            this.folk.addToInventory(itemStack);
                            iInventory.removeStackFromSlot(i);
                            break;
                        }
                    }
                }
            }

            maxProd = -1;

            for(i = 0; i < this.folk.inventory.size(); ++i) {
                ItemStack itemStack=this.folk.inventory.get(i);
                for(j = 0; j < this.requirements.size(); ++j) {
                    ItemStack requirementsStack=this.requirements.get(j);
                    if (itemStack.isItemEqual(this.requirements.get(j))) {
                        int div = Math.floorDiv(itemStack.stackSize, requirementsStack.stackSize);
                        if (maxProd == -1 || maxProd > div) {
                            maxProd = div;
                        }
                    }
                }
            }

            ModSimLoader.log.info("制作 " + maxProd + " " + this.produce.getUnlocalizedName());
            if (maxProd > 0) {
                this.job.placeInJobChest(new ItemStack(this.produce, maxProd));
                ModSimLoader.addMoney(-0.2F * (float)maxProd);
            }

            this.completeTask();
        } else {
            //返回工作岗位
            this.folk.setStatus(I18n.format("container.sim.job_task_Returning_to_work"));
            if (!this.job.folk.entity.isMoving() && this.job.folk.entity.getNavigator().getPath() == null) {
                this.job.folk.forceMoveToXYZ(this.job.workPlace);
            }
        }

    }

    public void onTaskComplete() {
    }
}