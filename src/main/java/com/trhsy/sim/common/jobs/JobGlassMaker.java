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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobGlassMaker
 * @Description todo 玻璃制造商
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:49
 * ========================================
 **/
public class JobGlassMaker extends Job implements Serializable {
    private static final long serialVersionUID = 1177111222904279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient V3 blockOfSand = null;
    private transient ArrayList<IInventory> factoryChests = new ArrayList();
    private transient TileEntityFurnace factoryFurnace = null;
    private long lastGotocmd = 0L;
    private int gotoCount = 0;

    public JobGlassMaker() {
    }

    public JobGlassMaker(FolkData folk) {
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
            if (!theFolk.isNightOwl()) {
                //闲置
                this.theStage = Stage.IDLE;
                return;
            }
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.IDLE) {
            this.runDelay = 2000;
            this.theStage = Stage.SCANFORSAND;
        } else {
            if (this.theStage != Stage.COLLECTSAND && this.theStage != Stage.GOTOSANDBLOCK && this.theStage != Stage.SCANFORSAND) {
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
                    if (this.theStage == Stage.SCANFORSAND) {
                        this.stageScanForSand();
                    } else if (this.theStage == Stage.GOTOSANDBLOCK) {
                        this.stageGotoSandBlock();
                    } else if (this.theStage == Stage.COLLECTSAND) {
                        this.stageCollectSand();
                    } else if (this.theStage == Stage.RETURNSAND) {
                        this.stageReturnSand();
                    } else if (this.theStage == Stage.USEFURNACE) {
                        this.stageUseFurnace();
                    } else if (this.theStage == Stage.CANTWORK) {
                        this.stageCantWork();
                    }
                }

            }
        }
    }
    //这里没有沙子？
    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.There");
    }

    private void stageScanForSand() {
        //去挖掘一些沙子
        if (this.theFolk.statusText.contains(I18n.format("container.sim.Arrived")) || this.theFolk.statusText.contains(I18n.format("container.sim.glass"))) {
            this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Going");
        }

        try {
            this.blockOfSand = findClosestBlockType(this.theFolk.employedAt, Blocks.sand, 80, true);
            if (this.blockOfSand == null) {
                this.theStage = Stage.USEFURNACE;
                return;
            }

            this.theStage = Stage.GOTOSANDBLOCK;
        } catch (Exception var2) {
        }

    }

    private void stageGotoSandBlock() {
        try {
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.swingProgress = 0.0F;
            }

            this.theFolk.updateLocationFromEntity();
            double dist = (double)this.theFolk.location.getDistanceTo(this.blockOfSand);
            if (dist > 4 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.stayPut = false;
                this.theFolk.gotoXYZ(this.blockOfSand, (GotoMethod)null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
            }

            this.theStage = Stage.COLLECTSAND;
        } catch (Exception var3) {
        }

    }

    private void stageCollectSand() {
        this.runDelay = 1000;
        this.theFolk.isWorking = true;
        this.theFolk.updateLocationFromEntity();
        double dist = (double)this.theFolk.location.getDistanceTo(this.blockOfSand);
        if (dist > 6 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
            this.theFolk.gotoXYZ(this.blockOfSand, (GotoMethod)null);
            this.theFolk.stayPut = false;
            this.lastGotocmd = System.currentTimeMillis();
            ++this.gotoCount;
            if (this.gotoCount > 2) {
                this.gotoCount = 0;
                V3 bs = this.blockOfSand.clone();
               /* Double var5 = bs.y;
                Double var6 = bs.y = bs.y + 1;*/
                bs=new V3(bs.x-1,bs.y+1,bs.z,bs.theDimension);
                this.theFolk.beamMeTo(bs);
            }

        } else if (!(dist > 6)) {
            try {
                if (dist < 6) {
                }

                this.gotoCount = 0;
                BlockPos blockPos1=new BlockPos(this.blockOfSand.x.intValue(), this.blockOfSand.y.intValue(), this.blockOfSand.z.intValue());
                this.jobWorld.setBlockState(blockPos1,this.blockOfSand.blockID.getDefaultState(),3);
                this.mc.theWorld.playSound(this.blockOfSand.x, this.blockOfSand.y, this.blockOfSand.z, "step.sand", 1.0F, 1.0F, false);
                this.theFolk.getVillagerInventory().setInventorySlotContents(0,new ItemStack(Blocks.sand, 1));
                //我得到沙子惹！
                this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Diggy") + this.theFolk.getVillagerInventory().getSizeInventory();
                GameStates var10000 = ModSimReloaded.states;
                var10000.credits = (float)((double)var10000.credits - 0.012D);
                if (this.theFolk.getVillagerInventory().getSizeInventory() < 64) {
                    this.theStage = Stage.SCANFORSAND;
                } else {
                    this.theStage = Stage.RETURNSAND;
                    this.step = 1;
                }
            } catch (Exception var7) {
            }

        }
    }

    private void stageReturnSand() {
        this.theFolk.isWorking = false;

        try {
            if (this.step == 1) {
                V3 adj = this.theFolk.employedAt.clone();
                /*Double var3 = adj.y;
                Double var4 = adj.y = adj.y + 1;*/
                adj=new V3(adj.x-1,adj.y+ 1,adj.z,adj.theDimension);
                this.theFolk.gotoXYZ(adj, (GotoMethod)null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 4) {
                    this.theFolk.stayPut = true;
                    this.step = 3;
                } else if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
                }
            } else if (this.step == 3) {
                this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                this.openCloseChest((IInventory)this.factoryChests.get(0), 1000);
                boolean placed = this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.factoryChests, (ItemStack)null);
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
            //我的炉子不见了
            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.glass.farmer.Where"));
        } else {
            ItemStack currentSand;
            ItemStack gotFuel;
            if (this.step == 1) {
                this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Checking");
                currentSand = this.factoryFurnace.getStackInSlot(1);
                gotFuel = null;
                if (currentSand == null) {
                    gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.coal, 64), false, false, new ItemStack(Items.coal, 64));
                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.lava_bucket, 1), false, false, new ItemStack(Items.lava_bucket, 1));
                    }

                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.log, 64), false, false, new ItemStack(Blocks.log, 64));
                    }

                    if (gotFuel == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.planks, 64), false, false, new ItemStack(Blocks.planks, 1));
                    }

                    if (gotFuel == null) {
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.glass.farmer.furnace"));
                        this.theStage = Stage.SCANFORSAND;
                        this.step = 1;
                        return;
                    }

                    this.factoryFurnace.setInventorySlotContents(1, gotFuel);
                    this.step = 2;
                    return;
                }

                this.step = 2;
            } else if (this.step == 2) {
                this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Adding");
                if (this.factoryFurnace != null) {
                    currentSand = this.factoryFurnace.getStackInSlot(0);
                    gotFuel = null;
                    if (currentSand == null) {
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.sand, 64), false, false, new ItemStack(Blocks.sand, 64));
                        if (gotFuel != null) {
                            this.factoryFurnace.setInventorySlotContents(0, gotFuel);
                        }

                        this.step = 3;
                        return;
                    }

                    gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.sand, 64 - currentSand.stackSize), false, false, new ItemStack(Blocks.sand, 64 - currentSand.stackSize));
                    if (gotFuel != null) {
                        currentSand.stackSize += gotFuel.stackSize;
                        this.factoryFurnace.setInventorySlotContents(0, currentSand);
                    }

                    this.step = 3;
                    return;
                }
            } else if (this.step == 3) {
                currentSand = this.factoryFurnace.getStackInSlot(2);
                if (currentSand != null) {
                    this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Putting");
                    this.inventoriesPut(this.factoryChests, currentSand, true);
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits = (float)((double)var10000.credits - 0.005D * (double)currentSand.stackSize);
                    this.factoryFurnace.setInventorySlotContents(2, (ItemStack)null);
                } else {
                    this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.glass");
                }

                this.theStage = Stage.SCANFORSAND;
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
            this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Arrived");
            this.theStage = Stage.USEFURNACE;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

