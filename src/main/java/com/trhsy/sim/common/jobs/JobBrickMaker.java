package com.trhsy.sim.common.jobs;

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 砖匠的工作
 */
public class JobBrickMaker extends Job implements Serializable {
    private static final long serialVersionUID = 1177111222904279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient V3 blockOfClay = null;
    private transient List<IInventory> factoryChests = new CopyOnWriteArrayList();
    private transient TileEntityFurnace factoryFurnace = null;
    private long lastGotocmd = 0L;
    private int gotoCount = 0;

    public JobBrickMaker() {
    }

    public JobBrickMaker(FolkData folk) {
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBrickMaker出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
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
            if (this.theStage == Stage.IDLE) {
                this.runDelay = 2000;
                this.theStage = Stage.SCANFORCLAY;
            } else {
                if (this.theStage != Stage.COLLECTCLAY && this.theStage != Stage.GOTOCLAYBLOCK && this.theStage != Stage.SCANFORCLAY) {
                    this.runDelay = 2000;
                } else {
                    this.runDelay = 250;
                }

                if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                    this.timeSinceLastRun = System.currentTimeMillis();
                    if (this.factoryFurnace == null) {
                        this.factoryFurnace = this.findFurnace(this.theFolk.employedAt);
                    }

                    if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                        if (this.theStage == Stage.SCANFORCLAY) {
                            this.stageScanForClay();
                        } else if (this.theStage == Stage.GOTOCLAYBLOCK) {
                            this.stageGotoClayBlock();
                        } else if (this.theStage == Stage.COLLECTCLAY) {
                            this.stageCollectClay();
                        } else if (this.theStage == Stage.RETURNCLAY) {
                            this.stageReturnClay();
                        } else if (this.theStage == Stage.USEFURNACE) {
                            this.stageUseFurnace();
                        } else if (this.theStage == Stage.CANTWORK) {
                            this.stageCantWork();
                        }
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBrickMaker-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker1");
    }

    private void stageScanForClay() {
        try {
            if (this.theFolk.statusText.contains(I18n.format("container.sim.Arrived")) || this.theFolk.statusText.contains(I18n.format("container.sim.brick"))) {
                this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker2");
            }
            this.blockOfClay = findClosestBlockType(this.theFolk.employedAt, Blocks.clay, 80, true);
            if (this.blockOfClay == null) {
                this.theStage = Stage.USEFURNACE;
                return;
            }

            this.theStage = Stage.GOTOCLAYBLOCK;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageScanForClay出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageGotoClayBlock() {
        try {
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.swingProgress = 0.0F;
            }

            this.theFolk.updateLocationFromEntity();
            double dist = (double) this.theFolk.location.getDistanceTo(this.blockOfClay);
            if (dist > 4.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.stayPut = false;
                this.theFolk.gotoXYZ(this.blockOfClay, null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
            }

            this.theStage = Stage.COLLECTCLAY;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGotoClayBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageCollectClay() {
        try {
        this.runDelay = 1000;
        this.theFolk.isWorking = true;
        this.theFolk.updateLocationFromEntity();
        double dist = (double) this.theFolk.location.getDistanceTo(this.blockOfClay);
        if (dist > 6.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
            this.theFolk.gotoXYZ(this.blockOfClay, null);
            this.theFolk.stayPut = false;
            this.lastGotocmd = System.currentTimeMillis();
            ++this.gotoCount;
            if (this.gotoCount > 2) {
                this.gotoCount = 0;
                V3 bs = this.blockOfClay.clone();
                /*Double var5 = bs.y;
                Double var6 = bs.y = bs.y + 1.0;*/
                bs = new V3(bs.x - 1.0, bs.y + 1.0, bs.z, bs.theDimension);
                this.theFolk.beamMeTo(bs);
            }

        } else if (!(dist > 6.0)) {

                if (dist < 6.0) {
                }

                this.gotoCount = 0;
                BlockPos blockPos = new BlockPos(this.blockOfClay.x, this.blockOfClay.y, this.blockOfClay.z);
                this.jobWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
                this.mc.theWorld.playSound(this.blockOfClay.x, this.blockOfClay.y, this.blockOfClay.z, "step.sand", 1, 1, false);
                this.theFolk.getVillagerInventory().setInventorySlotContents(0, new ItemStack(Item.getItemFromBlock(Blocks.clay), 1));
                this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker3") + this.theFolk.getVillagerInventory().getSizeInventory();
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits = (float) ((double) var10000.credits - 0.012D);
                if (this.theFolk.getVillagerInventory().getSizeInventory() < 64) {
                    this.theStage = Stage.SCANFORCLAY;
                } else {
                    this.theStage = Stage.RETURNCLAY;
                    this.step = 1;
                }


        }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCollectClay出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageReturnClay() {
        this.theFolk.isWorking = false;

        try {
            if (this.step == 1) {
                V3 adj = this.theFolk.employedAt.clone();
                /*Double var3 = adj.y;
                Double var4 = adj.y = adj.y + 1.0;*/
                adj = new V3(adj.x - 1.0, adj.y + 1.0, adj.z, adj.theDimension);
                this.theFolk.gotoXYZ(adj, null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = (double) this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 4.0) {
                    this.theFolk.stayPut = true;
                    this.step = 3;
                } else if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }
            } else if (this.step == 3) {
                this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                this.openCloseChest((IInventory) this.factoryChests.get(0), 1000);
                boolean placed = this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.factoryChests, (ItemStack) null);
                this.theStage = Stage.USEFURNACE;
                this.step = 1;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageReturnClay出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageUseFurnace() {
        try {
            this.factoryFurnace = this.findFurnace(this.theFolk.employedAt);
            this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.factoryFurnace == null) {
                ModSimReloaded.sendChat(this.theFolk.name + "：" + I18n.format("container.sim.JobBrickMaker4"));
            } else {
                ItemStack currentClay;
                ItemStack gotFuel;
                if (this.step == 1) {
                    this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker5");
                    currentClay = this.factoryFurnace.getStackInSlot(1);
                    if (currentClay == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.coal, 64), false, false, new ItemStack(Items.coal, 64));
                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.lava_bucket, 1), false, false, new ItemStack(Items.lava_bucket, 1));
                        }

                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.getItemFromBlock(Blocks.log), 64), false, false, new ItemStack(Item.getItemFromBlock(Blocks.log), 64));
                        }

                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.getItemFromBlock(Blocks.planks), 64), false, false, new ItemStack(Item.getItemFromBlock(Blocks.planks), 1));
                        }

                        if (gotFuel == null) {
                            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.JobBrickMaker6"));
                            this.theStage = Stage.SCANFORCLAY;
                            this.step = 1;
                            return;
                        }

                        this.factoryFurnace.setInventorySlotContents(1, gotFuel);
                        this.step = 2;
                        return;
                    }

                    this.step = 2;
                } else if (this.step == 2) {
                    this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker7");
                    if (this.factoryFurnace != null) {
                        currentClay = this.factoryFurnace.getStackInSlot(0);
                        if (currentClay == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.getItemFromBlock(Blocks.clay), 64), false, false, new ItemStack(Blocks.clay, 64));
                            if (gotFuel != null) {
                                this.factoryFurnace.setInventorySlotContents(0, gotFuel);
                            }

                            this.step = 3;
                            return;
                        }

                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.getItemFromBlock(Blocks.clay), 64 - currentClay.stackSize), false, false, new ItemStack(Blocks.clay, 64 - currentClay.stackSize));
                        if (gotFuel != null) {
                            currentClay.stackSize += gotFuel.stackSize;
                            this.factoryFurnace.setInventorySlotContents(0, currentClay);
                        }

                        this.step = 3;
                        return;
                    }
                } else if (this.step == 3) {
                    currentClay = this.factoryFurnace.getStackInSlot(2);
                    if (currentClay != null) {
                        this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker8");
                        this.inventoriesPut(this.factoryChests, currentClay, true);
                        GameStates var10000 = ModSimReloaded.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.005D * (double) currentClay.stackSize);
                        this.factoryFurnace.setInventorySlotContents(2, (ItemStack) null);
                    } else {
                        this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker9");
                    }

                    this.theStage = Stage.SCANFORCLAY;
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageUseFurnace出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.cheese_maker.the_factory");
                this.theStage = Stage.USEFURNACE;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }


}
