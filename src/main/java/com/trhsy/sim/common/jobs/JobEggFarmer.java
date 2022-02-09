package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobEggFarmer
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:48
 * ========================================
 **/
public class JobEggFarmer extends Job {
    public Vocation vocation = null;
    public Stage theStage;
    public FolkData theFolk;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private ArrayList<IInventory> farmChests = new ArrayList();

    public JobEggFarmer(FolkData folk) {
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
        if (!ModSim.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.FEEDINGCHICKENS) {
            this.runDelay = 40000;
        } else {
            this.runDelay = 10000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
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
    }

    private void stageArrived() {
        this.vocation = this.theFolk.vocation;
        this.theStage = Stage.FEEDINGCHICKENS;
        this.theFolk.statusText = "Feeding Chickens";
        //int count = false;
        int count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityChicken.class);
        if (count < 6) {
            this.spawnHens(this.theFolk.employedAt, 6 - count);
        }

    }

    private void stageWaiting() {
        this.theFolk.updateLocationFromEntity();
        double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 10.0D) {
            this.theFolk.beamMeTo(this.theFolk.employedAt);
        }

        this.theFolk.statusText = "Raking Manure";
        this.theStage = Stage.COLLECTINGEGGS;
    }

    private void stageCollectingEggs() {
        this.theFolk.statusText = "Collecting Eggs";
        this.theStage = Stage.STORINGEGGS;
        this.theFolk.isWorking = true;
    }

    private void stageStoringEggs() {
        Random rand = new Random();
        int c = rand.nextInt(7);
        this.theFolk.statusText = "Storing Eggs in refrigerated chest";
        this.theFolk.isWorking = false;
        this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.farmChests.size() > 0) {
            boolean ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.egg, c + 1, 0), true);
            if (!ok) {
                ModSim.sendChat(this.theFolk.name + "'s egg farm chests are full of eggs!");
                this.theFolk.statusText = "Can't work, the chests are full";
                this.theStage = Stage.CANTWORK;
            } else {
                GameStates var10000 = ModSim.states;
                var10000.credits -= 0.05F;
                this.theStage = Stage.FEEDINGCHICKENS;
            }
        } else {
            this.theFolk.statusText = "Who stole my egg chests!";
            this.theStage = Stage.CANTWORK;
        }

    }

    private void stageCantWork() {
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the farm";
            this.theStage = Stage.ARRIVEDATFARM;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    private void spawnHens(V3 controlBox, int count) {
        EntityAnimal newAnimal = null;

        for(int c = 1; c <= count; ++c) {
            newAnimal = new EntityChicken(this.jobWorld);
            newAnimal.setLocationAndAngles(controlBox.x + 1.0D, controlBox.y + 1.0D, controlBox.z, 0.0F, 0.0F);
            if (!this.jobWorld.isRemote) {
                this.jobWorld.spawnEntityInWorld(newAnimal);
            }
        }

    }

}

