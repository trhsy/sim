package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobBurgersWaiter
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:44
 * ========================================
 **/
public class JobBurgersWaiter extends Job {
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public int runDelay = 1000;
    private long timeSinceLastRun = 0L;
    private Building theStore = null;

    public JobBurgersWaiter(FolkData folk) {
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
        if (this.theStore == null) {
            this.theStore = Building.getBuilding(this.theFolk.employedAt);
        }

        if (this.theStore != null) {
            if (!ModSim.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.ARRIVEDATSTORE) {
                this.theFolk.action = FolkAction.ATWORK;
                this.runDelay = 11000;
            } else if (this.theStage == Stage.SERVING) {
                this.runDelay = 45000;
            } else {
                this.runDelay = 5000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATSTORE) {
                        this.theStage = Stage.SERVING;
                        this.theFolk.statusText = "Serving customers";
                    } else if (this.theStage == Stage.SERVING) {
                        this.stageServing();
                    }
                }

                if (!ModSim.isDayTime()) {
                    this.theStage = Stage.IDLE;
                }

                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }

    private void stageServing() {
        ArrayList<V3> serve = this.theStore.getSpecialBlocks(2);
        if (!serve.isEmpty()) {
            ArrayList<IInventory> theChests = inventoriesFindClosest((V3)serve.get(0), 3);
            if (!theChests.isEmpty()) {
                this.theFolk.gotoXYZ((V3)serve.get(0), (GotoMethod)null);

                try {
                    this.theFolk.destination.destinationAcc = 0.3D;
                } catch (Exception var6) {
                }

                ItemStack is = inventoriesGet(theChests, (ItemStack)null, true, false);
                if (is == null) {
                    this.theFolk.statusText = "Wishing we had more customers";
                } else {
                    if (is.getItem() == ModSim.itemFood) {
                        is = new ItemStack(is.getItem(), 1, is.getMetadata());
                        inventoriesGet(theChests, is, false, true);
                        this.theFolk.statusText = "Just sold " + is.getDisplayName();
                        int r = (new Random()).nextInt(ModSim.theFolks.size() - 1);
                        FolkData folk = (FolkData) ModSim.theFolks.get(r);
                        if (folk.levelFood < 10) {
                            ++folk.levelFood;
                        }

                        folk.saveThisFolk();
                        ModSim.log.info("JobBurgersWaiter: Just fed " + folk.name);
                        GameStates var10000 = ModSim.states;
                        var10000.credits = (float)((double)var10000.credits - 0.45D);
                    } else {
                        this.theFolk.statusText = "Who put " + is.getDisplayName() + " in my chest, folks can't eat that!";
                    }

                }
            }
        }
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = "Arrived at the store";
            this.theStage = Stage.ARRIVEDATSTORE;
            ArrayList<V3> back = this.theStore.getSpecialBlocks(2);
            if (!back.isEmpty()) {
                this.theFolk.gotoXYZ((V3)back.get(0), (GotoMethod)null);
            }
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

}

