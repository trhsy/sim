package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.job.Job;
import com.trhsy.sim.util.Courier;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

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
    public long timeSinceArrival;
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
        if(System.currentTimeMillis()-this.timeSinceArrival>1000){
            goingToPickup();
        }
    }

    private void goingToPickup() {
        try{
        this.timeSinceArrival=System.currentTimeMillis();
        if (this.step == 0) {
            if (ModSimLoader.theCourierPoints.size() > 0) {
                Courier courier = ModSimLoader.theCourierPoints.get(this.currentTask);
                this.pickup = courier.loc;
                this.dropoff = this.job.workPlace;
                if (this.pickup != null) {
                    //我在去的路上 1 1 1 (提取)
                    this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.courier.On_my",new Object[0]).getUnformattedText() + this.pickup.toString() + new TextComponentTranslation("container.sim.job.courier.pick_up",new Object[0]).getUnformattedText());
                    this.job.folk.forceMoveToXYZ(this.pickup);
                    if (this.job.folk.isAtLocation(this.pickup, 2)) {
                        this.step = 1;
                    }
                }
            } else {
                this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.courier.deliveries",new Object[0]).getUnformattedText());
            }
        } else if (this.step == 1) {
            //在建筑内
            List<IInventory> iInventories = this.job.inventoriesFindClosest(this.pickup, 5);
            if (iInventories != null && iInventories.size() > 0) {
                //取东西
                this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.courier.Picking",new Object[0]).getUnformattedText());
                this.job.inventoriesTransferToFolks(iInventories);
                //this.completed = true;
                this.step = 2;
            } else {
                ModSimLoader.log.warn("JobCourier: StagePickingup() 拾取时没有宝箱：" + this.pickup.toString() + "，移除任务。");
                if (this.currentTask >= ModSimLoader.theCourierPoints.size()&&ModSimLoader.theCourierPoints.size()>0) {
//                    ModSimLoader.theCourierPoints.remove(this.currentTask);
                    this.completed = true;
                }
//                ++this.currentTask;
                this.step = 2;
            }
        } else if (this.step == 2) {
            if (this.dropoff != null) {
                this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.courier.On_my",new Object[0]).getUnformattedText() + this.pickup.toString() + new TextComponentTranslation("container.sim.job.courier.pick_up",new Object[0]).getUnformattedText());
                V3 v3=this.dropoff;
                this.folk.forceMoveToXYZ(v3);
                this.step = 3;
            }
        } else if (this.step == 3) {
            this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.courier.Dropping",new Object[0]).getUnformattedText());
            for (ItemStack itemStack : this.job.folk.inventory) {
                if(itemStack!=null){
                    this.job.folk.inventory.remove(itemStack);
                    this.job.placeInJobChest(itemStack);
                }
            }
            if(this.currentTask==0){
                ModSimLoader.theCourierPoints.remove(this.currentTask);
            }else{
                ModSimLoader.theCourierPoints.remove(this.currentTask-1);
            }
            this.step = 0;
        }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobTaskCourier-goingToPickup出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public void onTaskComplete() {

    }
}
