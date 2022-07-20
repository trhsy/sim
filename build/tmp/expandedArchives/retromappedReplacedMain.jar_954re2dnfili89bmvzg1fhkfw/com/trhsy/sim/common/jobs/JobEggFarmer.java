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
 * @Description todo 蛋农
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
        if (!ModSimReloaded.isDayTime()) {
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
    }

    private void stageArrived() {
        this.vocation = this.theFolk.vocation;
        this.theStage = Stage.FEEDINGCHICKENS;
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.Feeding");
        //int count = false;
        int count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityChicken.class);
        if (count < 6) {
            this.spawnHens(this.theFolk.employedAt, 6 - count);
        }

    }

    private void stageWaiting() {
        this.theFolk.updateLocationFromEntity();
        double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 10) {
            this.theFolk.beamMeTo(this.theFolk.employedAt);
        }

        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.Raking");
        this.theStage = Stage.COLLECTINGEGGS;
    }

    private void stageCollectingEggs() {
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.Collecting");
        this.theStage = Stage.STORINGEGGS;
        this.theFolk.isWorking = true;
    }

    private void stageStoringEggs() {
        Random rand = new Random();
        int c = rand.nextInt(7);
        this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.Storing");
        this.theFolk.isWorking = false;
        this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.farmChests.size() > 0) {
            boolean ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.field_151110_aK, c + 1, 0), true);
            if (!ok) {
                ModSimReloaded.sendChat(this.theFolk.name + I18n.func_135052_a("container.sim.job.egg.farmer.chests_eggs"));
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.chests_full");
                this.theStage = Stage.CANTWORK;
            } else {
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits -= 0.05F;
                this.theStage = Stage.FEEDINGCHICKENS;
            }
        } else {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.egg_chests");
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
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.egg.farmer.Arrived");
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
            newAnimal.func_70012_b(controlBox.x + 1, controlBox.y + 1, controlBox.z, 0.0F, 0.0F);
            if (!this.jobWorld.field_72995_K) {
                this.jobWorld.func_72838_d(newAnimal);
            }
        }

    }

}

