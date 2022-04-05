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
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<IInventory> farmChests = new ArrayList();
    private String[] cowNames = new String[6];

    public JobDairyFarmer(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, GotoMethod.BEAM);
            }

        }
    }

    private void createCowNames() {
        for(int i = 0; i < 6; ++i) {
            this.cowNames[i] = FolkData.generateName(1, true, "");
        }

    }

    @Override
    public void onUpdate() {
        if (this.cowNames[0] == null || this.cowNames[0].contentEquals("")) {
            this.createCowNames();
        }

        super.onUpdate();
        if (!ModSim.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.WAITINGFORMILKING) {
            this.runDelay = 40000;
        } else {
            this.runDelay = 10000;
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
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
    }

    private void stageArrived() {
        this.vocation = this.theFolk.vocation;
        this.theStage = Stage.WAITINGFORMILKING;
        this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Warming");
        //int count = false;
        int count = this.getAnimalCountInPen(this.theFolk.employedAt, EntityCow.class);
        if (count < 6) {
            this.spawnCows(this.theFolk.employedAt, 6 - count);
        }

    }

    private void stageWaiting() {
        this.theFolk.updateLocationFromEntity();
        double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 10.0D) {
            this.theFolk.beamMeTo(this.theFolk.employedAt);
        }

        this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Sterilizing");
        this.theStage = Stage.MILKING;
    }

    private void stageMilking() {
        Random rand = new Random();
        int c = rand.nextInt(6);
        this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Milking") + this.cowNames[c] + I18n.format("container.sim.job.dairy.farmer.the_cow");
        this.theStage = Stage.STORINGMILK;
        this.theFolk.isWorking = true;
    }

    private void stageStoringMilk() {
        this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Storing");
        this.theFolk.isWorking = false;
        this.farmChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.farmChests.size() > 0) {
            boolean ok = this.inventoriesPut(this.farmChests, new ItemStack(Items.milk_bucket, 1), true);
            if (!ok) {
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.dairy.farmer.dairy"));
                this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.chests");
                this.theStage = Stage.CANTWORK;
            } else {
                GameStates var10000 = ModSim.states;
                var10000.credits -= 0.05F;
                this.theStage = Stage.WAITINGFORMILKING;
            }
        } else {
            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Who");
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
            this.theFolk.statusText = I18n.format("container.sim.job.dairy.farmer.Arrived");
            this.theStage = Stage.ARRIVEDATFARM;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    private void spawnCows(V3 controlBox, int count) {
        EntityAnimal newAnimal = null;

        for(int c = 1; c <= count; ++c) {
            newAnimal = new EntityCow(this.jobWorld);
            newAnimal.setLocationAndAngles(controlBox.x + 1.0D, controlBox.y + 1.0D, controlBox.z, 0.0F, 0.0F);
            if (!this.jobWorld.isRemote) {
                this.jobWorld.spawnEntityInWorld(newAnimal);
            }
        }

    }
}

