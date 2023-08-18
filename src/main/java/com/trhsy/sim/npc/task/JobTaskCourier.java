package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import com.trhsy.sim.util.Courier;
import com.trhsy.sim.util.CourierTask;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskCourier
 * @Description: 去取件
 * @date 2023/08/15 下午 4:06
 */
public class JobTaskCourier extends JobTask {
    //步
    public int step = 1;
    //状态
    public String status;
    //第几个快递任务
    public int currentTask;
    //提货地点
    private transient V3 pickup;
    //卸货地点
    private transient V3 dropoff;
    private transient List<IInventory> chests = new CopyOnWriteArrayList();

    public JobTaskCourier(Job j, long ms, String status) {
        super(j, ms);
        this.status = status;
        this.currentTask = 0;
        this.step = 0;
    }

    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
        goingToPickup();
    }

    @Override
    public void onUpdate() {
        goingToPickup();
    }

    private void goingToPickup() {
        if (this.step == 0) {
            if (ModSimLoader.theCourierPoints.size() > 0) {
                Courier courier = ModSimLoader.theCourierPoints.get(this.currentTask);
                this.pickup = courier.loc;
                this.dropoff = this.job.workPlace;
                if (this.pickup != null) {
                    //我在去的路上 1 1 1 (提取)
                    this.job.folk.setStatus(I18n.format("container.sim.job.courier.On_my") + this.pickup.toString() + I18n.format("container.sim.job.courier.pick_up"));
                    this.job.folk.forceMoveToXYZNoWarp(this.pickup);
                    if (this.job.folk.isAtLocation(this.pickup, 2)) {
                        this.step = 1;
                    }
                }
            } else {
                this.job.folk.setStatus(I18n.format("container.sim.job.courier.deliveries"));
            }
        } else if (this.step == 1) {
            //在建筑内
            List<IInventory> iInventories = this.job.inventoriesFindClosest(this.pickup, 5);
            if (iInventories != null && iInventories.size() > 0) {
                //取东西
                this.job.folk.setStatus(I18n.format("container.sim.job.courier.Picking"));
                this.job.inventoriesTransferToFolk(iInventories);
                //this.completed = true;
                this.step = 2;
            } else {
                ModSimLoader.log.warn("JobCourier: StagePickingup() 拾取时没有宝箱：" + this.pickup.toString() + "，移除任务。");
                ++this.currentTask;
                if (this.currentTask >= ModSimLoader.theCourierTask.size()) {
                    this.completed = true;
                }
            }
        } else if (this.step == 2) {
            if (this.dropoff == null) {
                this.job.folk.setStatus(I18n.format("container.sim.job.courier.On_my") + this.pickup.toString() + I18n.format("container.sim.job.courier.pick_up"));
                this.job.folk.forceMoveToXYZNoWarp(this.dropoff);
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.job.folk.setStatus(I18n.format("container.sim.job.courier.Dropping"));
            for (ItemStack itemStack : this.job.folk.inventory) {
                this.job.folk.inventory.remove(itemStack);
                this.job.placeInJobChest(itemStack);
            }
            this.step = 0;
        }

    }

    @Override
    public void onTaskComplete() {

    }
}
