package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.CourierTask;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobCourier
 * @Description todo 信使/快递员
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:46
 * ========================================
 **/
public class JobCourier extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207901844141L;

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient ArrayList<CourierTask> courierTasks = new ArrayList();
    private transient ArrayList<IInventory> chests = new ArrayList();
    private transient int currentTask = 0;
    private transient long timeSinceLastCycle = 0L;
    private transient V3 pickup;
    private transient V3 dropoff;
    private transient boolean onRoute = false;

    public JobCourier() {
    }

    public JobCourier(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimReloaded.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.ATDEPOT) {
            this.runDelay = 15000;
        } else {
            this.runDelay = 3000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage == Stage.IDLE && ModSimReloaded.isDayTime()) {
                this.onUpdateGoingToWork(this.theFolk);
            } else if (this.theStage == Stage.ATDEPOT) {
                this.stageAtDepot();
            } else if (this.theStage == Stage.GOINGTOPICKUP) {
                this.stageGoingToPickup();
            } else if (this.theStage == Stage.PICKINGUP) {
                this.stagePickingUp();
            } else if (this.theStage == Stage.GOINGTODROPOFF) {
                this.stageGoingToDropoff();
            } else if (this.theStage == Stage.DROPPINGOFF) {
                this.stageDroppingOff();
            }

        }
    }

    private void stageAtDepot() {
        if (System.currentTimeMillis() - this.timeSinceLastCycle >= 180000L) {
            this.currentTask = 0;
            this.courierTasks.clear();

            for (int t = 0; t < ModSimReloaded.theCourierTasks.size(); ++t) {
                CourierTask task = (CourierTask) ModSimReloaded.theCourierTasks.get(t);
                if (task != null && task.pickup != null && task.folkname.contentEquals(this.theFolk.name)) {
                    try {
                        this.courierTasks.add(task);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
            }

            if (this.courierTasks.size() == 0) {
                this.theFolk.statusText = I18n.format("container.sim.job.courier.deliveries");
            } else {
                this.theStage = Stage.GOINGTOPICKUP;
                this.onRoute = false;
            }
        }
    }

    private void stageGoingToPickup() {
        if (!this.onRoute) {
            CourierTask task = (CourierTask)this.courierTasks.get(this.currentTask);
            this.pickup = task.pickup;
            if (this.pickup != null) {
                this.theFolk.statusText = I18n.format("container.sim.job.courier.On_my") + this.pickup.name + I18n.format("container.sim.job.courier.pick_up");
                V3 d = this.pickup.clone();
                //Double var4 = d.y;
                //Double var5 = d.y = d.y + 1;
                d=new V3(d.x,d.y+1,d.z,d.theDimension);
                this.theFolk.gotoXYZ(d, GotoMethod.BEAM);
                this.onRoute = true;
            } else {
                this.theStage = Stage.IDLE;
            }
        } else {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            double dist = (double)this.theFolk.location.getDistanceTo(this.pickup);
            if (dist < 3) {
                this.theStage = Stage.PICKINGUP;
                this.onRoute = false;
            } else if (this.theFolk.destination == null) {
                this.onRoute = false;
            }
        }

    }

    private void stagePickingUp() {
        CourierTask task = (CourierTask)this.courierTasks.get(this.currentTask);
        V3 pickup = task.pickup;
        this.chests.clear();
        this.chests = inventoriesFindClosest(pickup, 4);
        if (this.chests.size() == 0) {
            ModSimReloaded.log.warn("JobCourier: StagePickingup() 拾取时没有宝箱：" + pickup.name + "，移除任务。");
            ++this.currentTask;
            if (this.currentTask >= this.courierTasks.size()) {
                this.currentTask = 0;
                this.theStage = Stage.IDLE;
            } else {
                this.onRoute = false;
                this.theStage = Stage.GOINGTOPICKUP;
            }
        } else {
            this.theFolk.stayPut = true;
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.statusText = I18n.format("container.sim.job.courier.Picking");
            ModSimReloaded.log.info("JobCourier: pickupStage() " + this.theFolk.name + "(courier)找到 " + this.chests.size() + " 个箱子 " + pickup.name);
            this.inventoriesTransferToFolk(this.theFolk.inventory, this.chests, (ItemStack) null, BlockLoader.blockLightBox);
        }

        if (this.theFolk.inventory.size() == 0) {
            ++this.currentTask;
            if (this.currentTask >= this.courierTasks.size()) {
                this.currentTask = 0;
                this.theStage = Stage.IDLE;
                this.timeSinceLastCycle = System.currentTimeMillis();
            } else {
                this.theStage = Stage.GOINGTOPICKUP;
            }
        } else {
            this.theStage = Stage.GOINGTODROPOFF;
            this.theFolk.statusText = I18n.format("container.sim.job.courier.Going");
            this.onRoute = false;
        }

    }

    private void stageGoingToDropoff() {
        CourierTask task = (CourierTask)this.courierTasks.get(this.currentTask);
        if (task != null && task.dropoff != null) {
            this.dropoff = task.dropoff.clone();
        } else {
            this.theStage = Stage.ATDEPOT;
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            if (task != null) {
                this.courierTasks.remove(task);
            }
        }

        if (this.dropoff == null) {
            this.dropoff = this.theFolk.employedAt;
            this.dropoff.name = I18n.format("container.sim.job.courier.The_depot");
        }

        if (!this.onRoute) {
            this.theFolk.statusText = I18n.format("container.sim.job.courier.On_my") + this.dropoff.name + I18n.format("container.sim.job.courier.drop_off");
            V3 d = this.dropoff.clone();
            d=new V3(d.x,d.y+1,d.z,d.theDimension);
            if (d == null) {
                d = this.theFolk.employedAt.clone();
            }

            Double var4 = d.y;
            Double var5 = d.y = d.y + 1;
            this.theFolk.beamMeTo(d);
            this.theFolk.gotoXYZ(d, GotoMethod.BEAM);
            this.onRoute = true;
        } else {
            if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                this.theFolk.updateLocationFromEntity();
            }

            double dist = (double)this.theFolk.location.getDistanceTo(this.dropoff);
            if (dist < 4) {
                this.theStage = Stage.DROPPINGOFF;
                this.onRoute = false;
            } else if (this.theFolk.destination == null) {
                this.onRoute = false;
            }
        }

    }

    private void stageDroppingOff() {
        CourierTask task = (CourierTask)this.courierTasks.get(this.currentTask);
        V3 dropoff = task.dropoff;
        dropoff=new V3(dropoff.x,dropoff.y+1,dropoff.z,dropoff.theDimension);
        if (dropoff == null) {
            dropoff = this.theFolk.employedAt;
            dropoff.name = I18n.format("container.sim.job.courier.The_depot");
        }

        this.chests.clear();
        this.chests = inventoriesFindClosest(dropoff, 5);
        if (this.chests.size() == 0) {
            ModSimReloaded.log.warn("JobCourierL dropoff() 下车时没有找到箱子");
            ++this.currentTask;
            if (this.currentTask >= this.courierTasks.size()) {
                this.currentTask = 0;
                this.theStage = Stage.IDLE;
            } else {
                this.theStage = Stage.GOINGTOPICKUP;
            }
        } else {
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.courier.Dropping");
            this.theFolk.action = FolkAction.ATWORK;
            ModSimReloaded.log.info("JobCourier: " + this.theFolk.name + " 找到 " + this.chests.size() + " 个箱子 " + dropoff.name);

            while(this.theFolk.inventory.size() > 0) {
                int oldSize = this.theFolk.inventory.size();
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits -= 0.11F;
                ItemStack invItem = (ItemStack)this.theFolk.inventory.get(0);
                if (this.theFolk.inventory.size() > 1) {
                    this.theFolk.statusText = this.theFolk.inventory.size() + I18n.format("container.sim.job.courier.unload");
                } else {
                    this.theFolk.statusText = I18n.format("container.sim.job.courier.Last");
                }

                boolean placed = this.inventoriesTransferFromFolk(this.theFolk.inventory, this.chests, (ItemStack)null);
                if (!placed) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.courier.Courier") + dropoff.name + I18n.format("container.sim.job.courier.because"));
                    break;
                }
            }
        }

        ++this.currentTask;
        if (this.currentTask >= this.courierTasks.size()) {
            this.theFolk.statusText = I18n.format("container.sim.job.courier.task_list");
            this.currentTask = 0;
            this.timeSinceLastCycle = System.currentTimeMillis();
            this.theFolk.stayPut = false;
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            this.theStage = Stage.IDLE;
        } else {
            this.theStage = Stage.GOINGTOPICKUP;
        }

    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.courier.Arrived");
            this.theStage = Stage.ATDEPOT;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

