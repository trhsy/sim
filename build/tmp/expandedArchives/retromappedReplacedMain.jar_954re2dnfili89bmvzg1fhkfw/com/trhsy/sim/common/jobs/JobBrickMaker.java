package com.trhsy.sim.common.jobs;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;

import java.io.Serializable;
import java.util.ArrayList;

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
    private transient ArrayList<IInventory> factoryChests = new ArrayList();
    private transient TileEntityFurnace factoryFurnace = null;
    private long lastGotocmd = 0L;
    private int gotoCount = 0;

    public JobBrickMaker() {
    }

    public JobBrickMaker(FolkData folk) {
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
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSimReloaded.isDayTime()) {
            this.theStage = Stage.IDLE;
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

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
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
    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker1");
    }

    private void stageScanForClay() {
        if (this.theFolk.statusText.contains(I18n.func_135052_a("container.sim.Arrived")) || this.theFolk.statusText.contains(I18n.func_135052_a("container.sim.brick"))) {
            this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker2");
        }

        try {
            this.blockOfClay = findClosestBlockType(this.theFolk.employedAt, Blocks.field_150435_aG, 80, true);
            if (this.blockOfClay == null) {
                this.theStage = Stage.USEFURNACE;
                return;
            }

            this.theStage = Stage.GOTOCLAYBLOCK;
        } catch (Exception var2) {
        }

    }

    private void stageGotoClayBlock() {
        try {
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.field_70733_aJ = 0.0F;
            }

            this.theFolk.updateLocationFromEntity();
            double dist = (double)this.theFolk.location.getDistanceTo(this.blockOfClay);
            if (dist > 4.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.stayPut = false;
                this.theFolk.gotoXYZ(this.blockOfClay, (GotoMethod)null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
            }

            this.theStage = Stage.COLLECTCLAY;
        } catch (Exception var3) {
        }

    }

    private void stageCollectClay() {
        this.runDelay = 1000;
        this.theFolk.isWorking = true;
        this.theFolk.updateLocationFromEntity();
        double dist = (double)this.theFolk.location.getDistanceTo(this.blockOfClay);
        if (dist > 6.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
            this.theFolk.gotoXYZ(this.blockOfClay, (GotoMethod)null);
            this.theFolk.stayPut = false;
            this.lastGotocmd = System.currentTimeMillis();
            ++this.gotoCount;
            if (this.gotoCount > 2) {
                this.gotoCount = 0;
                V3 bs = this.blockOfClay.clone();
                /*Double var5 = bs.y;
                Double var6 = bs.y = bs.y + 1.0;*/
                bs=new V3(bs.x-1.0,bs.y+ 1.0,bs.z,bs.theDimension);
                this.theFolk.beamMeTo(bs);
            }

        } else if (!(dist > 6.0)) {
            try {
                if (dist < 6.0) {
                }

                this.gotoCount = 0;
                BlockPos blockPos=new BlockPos(this.blockOfClay.x.intValue(), this.blockOfClay.y.intValue(), this.blockOfClay.z.intValue());
                this.jobWorld.func_180501_a(blockPos,Blocks.field_150350_a.func_176223_P(),3);
                this.mc.field_71441_e.func_72980_b(this.blockOfClay.x, this.blockOfClay.y, this.blockOfClay.z, "step.sand", 1.0F, 1.0F, false);
                this.theFolk.inventory.add(new ItemStack(Item.func_150898_a(Blocks.field_150435_aG), 1));
                this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker3") + this.theFolk.inventory.size();
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits = (float)((double)var10000.credits - 0.012D);
                if (this.theFolk.inventory.size() < 64) {
                    this.theStage = Stage.SCANFORCLAY;
                } else {
                    this.theStage = Stage.RETURNCLAY;
                    this.step = 1;
                }
            } catch (Exception var7) {
            }

        }
    }

    private void stageReturnClay() {
        this.theFolk.isWorking = false;

        try {
            if (this.step == 1) {
                V3 adj = this.theFolk.employedAt.clone();
                /*Double var3 = adj.y;
                Double var4 = adj.y = adj.y + 1.0;*/
                adj=new V3(adj.x-1.0,adj.y+1.0,adj.z,adj.theDimension);
                this.theFolk.gotoXYZ(adj, (GotoMethod)null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 4.0) {
                    this.theFolk.stayPut = true;
                    this.step = 3;
                } else if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
                }
            } else if (this.step == 3) {
                this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                this.openCloseChest((IInventory)this.factoryChests.get(0), 1000);
                boolean placed = this.inventoriesTransferFromFolk(this.theFolk.inventory, this.factoryChests, (ItemStack)null);
                this.theStage = Stage.USEFURNACE;
                this.step = 1;
            }
        } catch (Exception var5) {
        }

    }

    private void stageUseFurnace() {
        this.factoryFurnace = this.findFurnace(this.theFolk.employedAt);
        this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.factoryFurnace == null) {
            ModSimReloaded.sendChat(this.theFolk.name + "："+I18n.func_135052_a("container.sim.JobBrickMaker4"));
        } else {
            ItemStack currentClay;
            ItemStack gotFuel;
            if (this.step == 1) {
                this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker5");
                currentClay = this.factoryFurnace.func_70301_a(1);
                gotFuel = null;
                if (currentClay == null) {
                    gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.field_151044_h, 64), false, false, new ItemStack(Items.field_151044_h, 64));
                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.field_151129_at, 1), false, false, new ItemStack(Items.field_151129_at, 1));
                    }

                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.func_150898_a(Blocks.field_150364_r), 64), false, false, new ItemStack(Item.func_150898_a(Blocks.field_150364_r), 64));
                    }

                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.func_150898_a(Blocks.field_150344_f), 64), false, false, new ItemStack(Item.func_150898_a(Blocks.field_150344_f), 1));
                    }

                    if (gotFuel == null) {
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.func_135052_a("container.sim.JobBrickMaker6"));
                        this.theStage = Stage.SCANFORCLAY;
                        this.step = 1;
                        return;
                    }

                    this.factoryFurnace.func_70299_a(1, gotFuel);
                    this.step = 2;
                    return;
                }

                this.step = 2;
            } else if (this.step == 2) {
                this.theFolk.statusText =I18n.func_135052_a("container.sim.JobBrickMaker7");
                if (this.factoryFurnace != null) {
                    currentClay = this.factoryFurnace.func_70301_a(0);
                    gotFuel = null;
                    if (currentClay == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.func_150898_a(Blocks.field_150435_aG), 64), false, false, new ItemStack(Blocks.field_150435_aG, 64));
                        if (gotFuel != null) {
                            this.factoryFurnace.func_70299_a(0, gotFuel);
                        }

                        this.step = 3;
                        return;
                    }

                    gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Item.func_150898_a(Blocks.field_150435_aG), 64 - currentClay.field_77994_a), false, false, new ItemStack(Blocks.field_150435_aG, 64 - currentClay.field_77994_a));
                    if (gotFuel != null) {
                        currentClay.field_77994_a += gotFuel.field_77994_a;
                        this.factoryFurnace.func_70299_a(0, currentClay);
                    }

                    this.step = 3;
                    return;
                }
            } else if (this.step == 3) {
                currentClay = this.factoryFurnace.func_70301_a(2);
                if (currentClay != null) {
                    this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker8");
                    this.inventoriesPut(this.factoryChests, currentClay, true);
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits = (float)((double)var10000.credits - 0.005D * (double)currentClay.field_77994_a);
                    this.factoryFurnace.func_70299_a(2, (ItemStack)null);
                } else {
                    this.theFolk.statusText = I18n.func_135052_a("container.sim.JobBrickMaker9");
                }

                this.theStage = Stage.SCANFORCLAY;
            }

        }
    }

    @Override
    public void onArrivedAtWork() {
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.cheese_maker.the_factory");
            this.theStage = Stage.USEFURNACE;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }



}
