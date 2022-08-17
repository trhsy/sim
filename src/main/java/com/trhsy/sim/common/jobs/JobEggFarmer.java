package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobEggFarmer
 * @Description todo 蛋农
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:48
 * ========================================
 **/
public class JobEggFarmer extends Job {
    public Vocation vocation = null;
    public Stage theStage;
    public FolkData theFolk=new FolkData();
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private List<IInventory> farmChests = new CopyOnWriteArrayList();

    public JobEggFarmer(FolkData folk) {
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobEggFarmer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.FEEDINGCHICKENS) {
                this.runDelay = 40000;
            } else {
                this.runDelay = 10000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATFARM) {
                        this.stageArrived();
                    } else if (this.theStage == Stage.FEEDINGCHICKENS) {
                        this.stageWaiting();
                    } else if (this.theStage == Stage.COLLECTINGEGGS) {
                        this.stageCollectingEggs();
                    } else if (this.theStage == Stage.STORINGEGGS) {
                        this.stageStoringEggs();
                    } else if (this.theStage == Stage.CANTWORK) {
                        this.stageCantWork();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobEggFarmer-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageArrived() {
        try {
            this.vocation = this.theFolk.vocation;
            this.theStage = Stage.FEEDINGCHICKENS;
            this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.Feeding");
            //int count = false;
            int count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityChicken.class);
            if (count < 6) {
                this.spawnHens(this.theFolk.employedAt, 6 - count);
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

            this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.Raking");
            this.theStage = Stage.COLLECTINGEGGS;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageWaiting出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageCollectingEggs() {
        try {
            this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.Collecting");
            this.theStage = Stage.STORINGEGGS;
            this.theFolk.isWorking = true;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCollectingEggs出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageStoringEggs() {
        try {
            Random rand = new Random();
            int c = rand.nextInt(7);
            this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.Storing");
            this.theFolk.isWorking = false;
            this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.farmChests.size() > 0) {
                boolean ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.egg, c + 1, 0), true);
                if (!ok) {
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.egg.farmer.chests_eggs"));
                    this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.chests_full");
                    this.theStage = Stage.CANTWORK;
                } else {
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= 0.05F;
                    this.theStage = Stage.FEEDINGCHICKENS;
                }
            } else {
                this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.egg_chests");
                this.theStage = Stage.CANTWORK;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageStoringEggs出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
                this.theFolk.statusText = I18n.format("container.sim.job.egg.farmer.Arrived");
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

    private void spawnHens(V3 controlBox, int count) {
        EntityAnimal newAnimal = null;
        try {
            for(int c = 1; c <= count; ++c) {
                newAnimal = new EntityChicken(this.jobWorld);
                newAnimal.setLocationAndAngles(controlBox.xCoord + 1, controlBox.yCoord + 1, controlBox.zCoord, 0.0F, 0.0F);
                if (!this.jobWorld.isRemote) {
                    this.jobWorld.spawnEntityInWorld(newAnimal);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("spawnHens出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}

