package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobFisherman
 * @Description todo
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
    private transient ArrayList<IInventory> dockChests = new ArrayList();

    public JobFisherman(FolkData folk) {
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
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimukraft.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.CAUGHTFISH) {
            this.runDelay = 20000 + (new Random()).nextInt(20000);
        } else {
            this.runDelay = 2000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) {
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
    }

    private void stageArrived() {
        V3 water = Job.findClosestBlockType(this.theFolk.employedAt, Blocks.water, 5, false);
        if (water == null) {
            this.theStage = Stage.CANTWORK;
            ModSimukraft.sendChat(this.theFolk.name + " (Fisherman) can't find any fish in the area");
        } else {
            this.theStage = Stage.FISHING;
            this.theFolk.statusText = "Casting out my line";
            this.fishCount = 0;
            if (!this.jobWorld.isRemote) {
            }
        }

    }

    private void stageCantWork() {
        this.theFolk.statusText = "Ain't no fish 'round here!";
    }

    private void stageFishing() {
        this.theFolk.updateLocationFromEntity();
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 3) {
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Fishing";
            if (System.currentTimeMillis() - this.timeSinceLastCaughtFish > 50000L) {
                this.theStage = Stage.CAUGHTFISH;
            }

            if (this.mc.getIntegratedServer().worldServers[0].getWorldTime() % 24000L > 11980L) {
                this.theStage = Stage.SELLINGFISH;
                this.step = 1;
            }

        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }
    }

    private void stageCaughtFish() {
        this.timeSinceLastCaughtFish = System.currentTimeMillis();
        this.theFolk.statusText = "Caught a fish, yay!";
        ++this.fishCount;
        GameStates var10000 = ModSimukraft.states;
        var10000.credits -= 0.02F;
        this.dockChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
        if (this.dockChests.size() == 0) {
            this.theFolk.statusText = "Damn! someone stole my fishing chests!";
            ModSimukraft.sendChat(this.theFolk.name + " (fisherman) can't find any chests at the dock!");
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
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }
    }

    private void stageSellingFish() {
        if (this.mc.getIntegratedServer().worldServers[0].getWorldTime() % 24000L < 11600L) {
            this.theStage = Stage.IDLE;
        } else {
            this.theFolk.statusText = "All done for today, caught " + this.fishCount + " fish!";
            if (this.step == 1) {
                int sell = false;
                ItemStack fishStack = null;
                if (ModSimukraft.theFolks.size() > 1) {
                    int sell = ModSimukraft.theFolks.size() + 1;
                    this.dockChests = inventoriesFindClosest(this.theFolk.employedAt, 4);
                    fishStack = inventoriesGet(this.dockChests, new ItemStack(Items.fish, sell), false, false);
                }

                if (fishStack == null) {
                    this.theStage = Stage.IDLE;
                    return;
                }

                ModSimukraft.sendChat(this.theFolk.name + " caught " + this.fishCount + " fish today and has sold " + fishStack.stackSize + " to folks.");

                for(int f = 0; f < ModSimukraft.theFolks.size(); ++f) {
                    FolkData folk = (FolkData)ModSimukraft.theFolks.get(f);
                    if (fishStack.stackSize > 0) {
                        folk.levelFood = 10;
                        --fishStack.stackSize;
                    }
                }

                this.step = 2;
            } else if (this.step == 2) {
            }

        }
    }

    @Override
    public void onArrivedAtWork() {
        int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the dock";
            this.theStage = Stage.ARRIVEDATDOCK;
            this.timeSinceLastCaughtFish = System.currentTimeMillis();
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.fishCount = 0;
    }

}

