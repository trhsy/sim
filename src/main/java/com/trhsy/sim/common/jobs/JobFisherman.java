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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobFisherman
 * @Description todo 渔夫
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:48
 * ========================================
 **/
public class JobFisherman extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207254191941L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient long timeSinceLastCaughtFish = 0L;
    private transient int fishCount = 0;
    private transient CopyOnWriteArrayList<IInventory> dockChests = new CopyOnWriteArrayList();

    public JobFisherman(FolkData folk) {
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobFisherman出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            if (this.theStage == Stage.CAUGHTFISH) {
                this.runDelay = 20000 + (new Random()).nextInt(20000);
            } else {
                this.runDelay = 2000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATDOCK) {
                        this.stageArrived();
                    } else if (this.theStage == Stage.FISHING) {
                        this.stageFishing();
                    } else if (this.theStage == Stage.CAUGHTFISH) {
                        this.stageCaughtFish();
                    } else if (this.theStage == Stage.SELLINGFISH) {
                        this.stageSellingFish();
                    } else if (this.theStage == Stage.CANTWORK) {
                        this.stageCantWork();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobFisherman-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageArrived() {
        try {
            V3 water = Job.findClosestBlockType(this.theFolk.employedAt, Blocks.water, 5, false);
            if (water == null) {
                this.theStage = Stage.CANTWORK;
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.fisherman.farmer.Fisherman"));
            } else {
                this.theStage = Stage.FISHING;
                this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.Casting");
                this.fishCount = 0;
                if (!this.jobWorld.isRemote) {
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageArrived出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.round");
    }

    private void stageFishing() {
        try {
            this.theFolk.updateLocationFromEntity();
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 3) {
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.Fishing");
                if (System.currentTimeMillis() - this.timeSinceLastCaughtFish > 50000L) {
                    this.theStage = Stage.CAUGHTFISH;
                }

                if (this.mc.getIntegratedServer().worldServers[0].getWorldTime() % 24000L > 11980L) {
                    this.theStage = Stage.SELLINGFISH;
                    this.step = 1;
                }

            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageFishing出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageCaughtFish() {
        try {
            this.timeSinceLastCaughtFish = System.currentTimeMillis();
            this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.Caught");
            ++this.fishCount;
            GameStates var10000 = ModSimReloaded.states;
            var10000.credits -= 0.02F;
            this.dockChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
            if (this.dockChests.size() == 0) {
                this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.someone");
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.fisherman.farmer.dock"));
                if (this.theFolk.theEntity != null) {
                    this.theFolk.theEntity.dropItem(Items.fish, 1);
                }
            } else {
                this.inventoriesPut(this.dockChests, new ItemStack(Items.fish, 1), true);
            }

            this.theStage = Stage.FISHING;
            this.theFolk.updateLocationFromEntity();
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 3) {
                this.theFolk.stayPut = true;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCaughtFish出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageSellingFish() {
        try {
            if (this.mc.getIntegratedServer().worldServers[0].getWorldTime() % 24000L < 11600L) {
                this.theStage = Stage.IDLE;
            } else {
                this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.caught_done") + this.fishCount + I18n.format("container.sim.job.fisherman.farmer.fish");
                if (this.step == 1) {
                    //int sell = false;
                    ItemStack fishStack = null;
                    if (ModSimReloaded.theFolks.size() > 1) {
                        int sell = ModSimReloaded.theFolks.size() + 1;
                        this.dockChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
                        fishStack = inventoriesGet(this.dockChests, new ItemStack(Items.fish, sell), false, false);
                    }

                    if (fishStack == null) {
                        this.theStage = Stage.IDLE;
                        return;
                    }

                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.fisherman.farmer.caughts") + this.fishCount + I18n.format("container.sim.job.fisherman.farmer.and_has") + fishStack.stackSize + I18n.format("container.sim.job.fisherman.farmer.to_folks"));

                    for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                        FolkData folk = (FolkData) ModSimReloaded.theFolks.get(f);
                        if (fishStack.stackSize > 0) {
                            folk.levelFood = 10;
                            --fishStack.stackSize;
                        }
                    }

                    this.step = 2;
                } else if (this.step == 2) {
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageSellingFish出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.fisherman.farmer.Arrived");
                this.theStage = Stage.ARRIVEDATDOCK;
                this.timeSinceLastCaughtFish = System.currentTimeMillis();
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void resetJob() {
        try {
            this.theStage = Stage.IDLE;
            this.fishCount = 0;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("resetJob出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

}

