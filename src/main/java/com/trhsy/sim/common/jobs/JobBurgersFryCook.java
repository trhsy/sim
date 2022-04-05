package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.Building;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ItemLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobBurgersFryCook
 * @Description todo 厨师
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:40
 * ========================================
 **/
public class JobBurgersFryCook extends Job {
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public int runDelay = 1000;
    private long timeSinceLastRun = 0L;
    private Building theStore = null;
    private ItemStack isMakeFood = null;
    private int tryMeta = 3;

    public JobBurgersFryCook(FolkData folk) {
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
            } else if (this.theStage == Stage.NOINGREDIANTS) {
                this.runDelay = 30000;
            } else if (this.theStage == Stage.MAKEFOOD) {
                this.runDelay = 15000;
            } else {
                this.runDelay = 5000;
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATSTORE) {
                        this.theStage = Stage.MAKEFOOD;
                    } else if (this.theStage == Stage.MAKEFOOD) {
                        this.stageMakeFood();
                    } else if (this.theStage == Stage.NOINGREDIANTS) {
                        this.stageNoIngrediants();
                    }
                }

                if (!ModSim.isDayTime()) {
                    this.theStage = Stage.IDLE;
                }

                this.timeSinceLastRun = System.currentTimeMillis();
            }
        }
    }
    /**
     * @Author fan
     * @Description //TODO 没有配料
     * @Date 16:31 2022/4/5
     * @Param []
     * @return void
     **/
    private void stageNoIngrediants() {
        this.theFolk.statusText = I18n.format("container.sim.job.Arrived_ingrediants");
        this.theStage = Stage.MAKEFOOD;
        this.step = 1;
    }

    private void stageMakeFood() {
        ArrayList<V3> ch = this.theStore.getSpecialBlocks(0);
        if (ch.isEmpty()) {
            this.theStage = Stage.NOINGREDIANTS;
        } else {
            ArrayList<IInventory> chestsIn = inventoriesFindClosest((V3)ch.get(0), 3);
            if (chestsIn.isEmpty()) {
                this.theStage = Stage.NOINGREDIANTS;
            } else {
                ArrayList<V3> ch2 = this.theStore.getSpecialBlocks(2);
                if (ch.isEmpty()) {
                    this.theStage = Stage.NOINGREDIANTS;
                } else {
                    ArrayList<IInventory> chestsOut = inventoriesFindClosest((V3)ch2.get(0), 3);
                    if (chestsIn.isEmpty()) {
                        this.theStage = Stage.NOINGREDIANTS;
                    } else {
                        ArrayList<V3> back = this.theStore.getSpecialBlocks(1);
                        if (!back.isEmpty()) {
                            this.theFolk.gotoXYZ((V3)back.get(0), (GotoMethod)null);

                            try {
                                this.theFolk.destination.destinationAcc = 0.3D;
                            } catch (Exception var7) {
                            }
                        }

                        if (this.step == 1) {
                            int c;
                            if (this.tryMeta == 3) {
                                c = this.getItemCountInChests(chestsIn, new ItemStack(ItemLoader.itemFoods, 1, 0), true);
                                if (c == 0) {
                                    this.tryMeta = 1;
                                    return;
                                }

                                c = this.getItemCountInChests(chestsIn, new ItemStack(Items.beef, 1), false);
                                if (c == 0) {
                                    this.tryMeta = 2;
                                    return;
                                }

                                c = this.getItemCountInChests(chestsIn, new ItemStack(Items.bread, 1), false);
                                if (c == 0) {
                                    this.tryMeta = 2;
                                    return;
                                }

                                this.isMakeFood = new ItemStack(ItemLoader.itemFoods, 1, 3);
                                this.step = 2;
                                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_Cooking");
                            } else if (this.tryMeta == 1) {
                                c = this.getItemCountInChests(chestsIn, new ItemStack(Items.beef, 1), false);
                                if (c == 0) {
                                    this.tryMeta = 2;
                                    return;
                                }

                                c = this.getItemCountInChests(chestsIn, new ItemStack(Items.bread, 1), false);
                                if (c == 0) {
                                    this.tryMeta = 2;
                                    return;
                                }

                                this.isMakeFood = new ItemStack(ItemLoader.itemFoods, 1, 1);
                                this.step = 2;
                                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_Hamburger");
                            } else if (this.tryMeta == 2) {
                                c = this.getItemCountInChests(chestsIn, new ItemStack(Items.potato), false);
                                if (c == 0) {
                                    this.tryMeta = 3;
                                    return;
                                }

                                this.isMakeFood = new ItemStack(ItemLoader.itemFoods, 1, 2);
                                this.step = 2;
                                this.theFolk.statusText = I18n.format("container.sim.job.Arrived_Fries");
                            }

                            if (this.step == 1) {
                                this.theStage = Stage.NOINGREDIANTS;
                            } else {
                                this.theFolk.isWorking = true;
                            }
                        } else if (this.step == 2) {
                            if (this.isMakeFood.getMetadata() == 3) {
                                inventoriesGet(chestsIn, new ItemStack(ItemLoader.itemFoods, 1, 0), false, true);
                                inventoriesGet(chestsIn, new ItemStack(Items.bread, 1), false, false);
                                inventoriesGet(chestsIn, new ItemStack(Items.beef, 1), false, false);
                                this.tryMeta = 1;
                            } else if (this.isMakeFood.getMetadata() == 1) {
                                inventoriesGet(chestsIn, new ItemStack(Items.bread, 1), false, false);
                                inventoriesGet(chestsIn, new ItemStack(Items.beef, 1), false, false);
                                this.tryMeta = 2;
                            } else if (this.isMakeFood.getMetadata() == 2) {
                                inventoriesGet(chestsIn, new ItemStack(Items.potato, 1), false, false);
                                this.tryMeta = 3;
                            }

                            this.inventoriesPut(chestsOut, this.isMakeFood, true);
                            this.theFolk.isWorking = false;
                            this.step = 1;
                            this.theFolk.statusText = I18n.format("container.sim.job.Arrived_Checking_Ingrediants");
                            GameStates var10000 = ModSim.states;
                            var10000.credits = (float)((double)var10000.credits - 0.45D);
                        }

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
            this.theFolk.statusText = I18n.format("container.sim.job.Arrived_at_the_store");
            this.theStage = Stage.ARRIVEDATSTORE;
            ArrayList<V3> back = this.theStore.getSpecialBlocks(1);
            if (!back.isEmpty()) {
                this.theFolk.gotoXYZ((V3)back.get(0), (GotoMethod)null);
                this.step = 1;
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

