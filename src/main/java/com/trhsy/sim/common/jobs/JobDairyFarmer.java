package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobDairyFarmer
 * @Description todo 奶农
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:47
 * ========================================
 **/
public class JobDairyFarmer extends Job {
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private CopyOnWriteArrayList<IInventory> farmChests = new CopyOnWriteArrayList();
    private String[] cowNames = new String[6];

    public JobDairyFarmer(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobDairyFarmer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void createCowNames() {
        try {
            for(int i = 0; i < 6; i++) {
                this.cowNames[i] = FolkData.generateName(1, true, "");
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("createCowNames出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onUpdate() {
        try {
            if (this.cowNames[0] == null || this.cowNames[0].contentEquals("")) {
                this.createCowNames();
            }

            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.WAITINGFORMILKING) {
                this.runDelay = 40000;
            } else {
                this.runDelay = 10000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATFARM) {
                        this.stageArrived();
                    } else if (this.theStage == Stage.WAITINGFORMILKING) {
                        this.stageWaiting();
                    } else if (this.theStage == Stage.MILKING) {
                        this.stageMilking();
                    } else if (this.theStage == Stage.STORINGMILK) {
                        this.stageStoringMilk();
                    } else if (this.theStage == Stage.CANTWORK) {
                        this.stageCantWork();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobDairyFarmer-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageArrived() {
        try {
            this.vocation = this.theFolk.vocation;
            this.theStage = Stage.WAITINGFORMILKING;
            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Warming");
            //int count = false;
            int count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityCow.class);
            if (count < 6) {
                this.spawnCows(this.theFolk.employedAt, 6 - count);
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageArrived出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageWaiting() {
        try {
            this.theFolk.updateLocationFromEntity();
            double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist > 10) {
                this.theFolk.beamMeTo(this.theFolk.employedAt);
            }

            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Sterilizing");
            this.theStage = Stage.MILKING;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageWaiting出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageMilking() {
        try {
            Random rand = new Random();
            int c = rand.nextInt(6);
            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Milking") + this.cowNames[c] + I18n.format("container.sim.job.dairy.farmer.the_cow");
            this.theStage = Stage.STORINGMILK;
            this.theFolk.isWorking = true;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageMilking出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageStoringMilk() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Storing");
            this.theFolk.isWorking = false;
            this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.farmChests.size() > 0) {
                boolean ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.milk_bucket, 1), true);
                if (!ok) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.dairy.farmer.dairy"));
                    this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.chests");
                    this.theStage = Stage.CANTWORK;
                } else {
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= 0.05F;
                    this.theStage = Stage.WAITINGFORMILKING;
                }
            } else {
                this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Who");
                this.theStage = Stage.CANTWORK;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageStoringMilk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageCantWork() {
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Arrived");
                this.theStage = Stage.ARRIVEDATFARM;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    private void spawnCows(V3 controlBox, int count) {
        EntityAnimal newAnimal = null;
        try {
            for(int c = 1; c <= count; ++c) {
                newAnimal = new EntityCow(this.jobWorld);
                newAnimal.setLocationAndAngles(controlBox.x + 1, controlBox.y + 1, controlBox.z, 0.0F, 0.0F);
                if (!this.jobWorld.isRemote) {
                    this.jobWorld.spawnEntityInWorld(newAnimal);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("spawnCows出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}

