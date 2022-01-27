package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.entity.FarmingBox;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobCropFarmer
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:46
 * ========================================
 **/
public class JobCropFarmer extends Job implements Serializable {
    private static final long serialVersionUID = -1177112214234279141L;
    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient boolean doneSomeWork = false;
    private transient FarmingBox farmingBlock = null;
    private transient ArrayList<IInventory> farmingChests = new ArrayList();
    private transient String farmDir = "";
    private transient int ftbCount = 0;
    private transient int ltrCount = 0;
    private transient int xo = 0;
    private transient int zo = 0;
    private transient Block id = null;
    private transient int mx;
    private transient int my;
    private transient int mz;
    private transient int xxx = 0;
    private transient int yyy = 0;
    private transient int zzz = 0;
    private transient int ftb = 1;
    private transient int ltr = -1;
    private transient int meta = 0;
    private transient long lastFarmCycle = 0L;
    private transient long lastCustomHarvest = 0L;
    private transient int rowCounter = 0;

    public JobCropFarmer() {
    }

    public JobCropFarmer(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

            this.farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(folk.employedAt);
            this.runDelay = 1000;
        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }

    @Override
    public void onUpdate() {
        if (ModSimukraft.theFarmingBoxes.size() != 0) {
            super.onUpdate();
            if (!ModSimukraft.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.CHECKINGFORCHESTS) {
                this.runDelay = 1000;
            }

            if (this.theStage == Stage.HARVEST || this.theStage == Stage.HOELAND || this.theStage == Stage.PLANTSEEDS) {
                this.runDelay = 500;
            }

            if (this.theStage == Stage.HANGOUT) {
                if (this.step == 1) {
                    this.runDelay = 1000;
                } else {
                    this.runDelay = 60000;
                }
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimukraft.isDayTime()) {
                    if (this.theStage == Stage.ARRIVEDATFARM) {
                        this.theStage = Stage.CHECKINGFORCHESTS;
                    } else if (this.theStage == Stage.CHECKINGFORCHESTS) {
                        this.stageCheckingForChests();
                    } else if (this.theStage == Stage.HARVEST) {
                        this.stageHarvest();
                    } else if (this.theStage == Stage.HOELAND) {
                        this.stageHoeland();
                    } else if (this.theStage == Stage.PLANTSEEDS) {
                        this.stagePlantSeeds();
                    } else if (this.theStage == Stage.HANGOUT) {
                        this.stageHangout();
                    }
                }

            }
        }
    }

    public void stageCheckingForChests() {
        if (this.farmingChests.isEmpty()) {
            this.farmingChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        }

        ModSimukraft.log.info("JobCropFarmer: found " + this.farmingChests.size() + " chests at the farm");
        this.theFolk.stayPut = true;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 3) {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

        if (this.farmingChests.isEmpty()) {
            this.theFolk.statusText = "Please place at least one chest near the farming box";
        } else {
            this.theStage = Stage.HARVEST;
            this.step = 1;
            this.theFolk.stayPut = true;
            if (this.theFolk.gender == 0) {
                this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, "satscapesimukraft:readym", 1.0F, 1.0F, false);
            } else {
                this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, "satscapesimukraft:readyf", 1.0F, 1.0F, false);
            }
        }

    }

    private void setupFarming() {
        this.ftb = 0;
        this.ltr = -1;
        if (this.farmingBlock == null) {
            ModSimukraft.log.warning("JobCropFarmer: FarmingBlock is null - not there or not found?!");
        } else {
            V3 m1 = this.farmingBlock.marker1XYZ;
            V3 m2 = this.farmingBlock.marker2XYZ;
            V3 m3 = this.farmingBlock.marker3XYZ;
            if (this.farmingBlock.marker1XYZ == null) {
                ModSimukraft.log.warning("JobCropFarmer: FarmingBlock's markers are null");
            } else {
                try {
                    this.mx = m1.x.intValue();
                    this.my = m1.y.intValue() - 1;
                    this.mz = m1.z.intValue();
                    int m2x = m2.x.intValue();
                    int m1x = m1.x.intValue();
                    int m2z = m2.z.intValue();
                    int m1z = m1.z.intValue();
                    if (m2x == m1x) {
                        if (m2z > this.mz) {
                            this.farmDir = "z+";
                        } else {
                            this.farmDir = "z-";
                        }
                    } else if (m2z == m1z) {
                        if (m2x > this.mx) {
                            this.farmDir = "x+";
                        } else {
                            this.farmDir = "x-";
                        }
                    }

                    this.ltrCount = this.farmingBlock.getSizeWidth();
                    this.ftbCount = this.farmingBlock.getSizeLength();
                } catch (Exception var8) {
                }

            }
        }
    }

    private boolean setXYZ() {
        boolean ret = false;
        ++this.ltr;
        if (this.ltr > this.ltrCount + 1) {
            this.ltr = 0;
            ++this.ftb;
            if (this.ftb > this.ftbCount + 1) {
                ret = true;
            }
        }

        if (this.farmDir.contentEquals("x+")) {
            this.xo = this.ltr;
            this.zo = -this.ftb;
        } else if (this.farmDir.contentEquals("x-")) {
            this.xo = -this.ltr;
            this.zo = this.ftb;
        } else if (this.farmDir.contentEquals("z+")) {
            this.xo = this.ftb;
            this.zo = this.ltr;
        } else if (this.farmDir.contentEquals("z-")) {
            this.xo = -this.ftb;
            this.zo = -this.ltr;
        }

        try {
            this.xxx = this.mx + this.xo;
            this.yyy = this.farmingBlock.location.y.intValue();
            this.zzz = this.mz + this.zo;
            return ret;
        } catch (Exception var3) {
            ModSimukraft.sendChat("There was a problem with " + this.theFolk.name + "'s farming box, please replace it");
            this.theFolk.selfFire();
            return false;
        }
    }

    public void stageHarvest() {
        if (this.farmingBlock == null || this.farmingBlock.farmType == null) {
            ModSimukraft.sendChat("There's a problem with a farming block, please re-make it");
            if (this.theFolk != null) {
                this.theFolk.selfFire();
            }
        }

        if (this.step == 1) {
            this.setupFarming();
            this.theFolk.statusText = "Harvesting";
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist > 3) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

            this.step = 2;
            this.theFolk.isWorking = true;
        } else if (this.step == 2) {
            boolean done = this.setXYZ();
            boolean hasHarvest = false;
            if (done) {
                this.theStage = Stage.HOELAND;
                this.step = 1;
                this.theFolk.isWorking = false;
                this.lastCustomHarvest = System.currentTimeMillis();
                return;
            }

            while(true) {
                while(!hasHarvest) {
                    this.id = this.jobWorld.getBlock(this.xxx, this.yyy, this.zzz);
                    this.meta = this.jobWorld.func_72805_g(this.xxx, this.yyy, this.zzz);

                    try {
                        if (!this.theFolk.isSpawned() && this.farmingBlock.farmType != FarmType.CUSTOM && this.farmingBlock.farmType != FarmType.SUGAR && this.farmingBlock.farmType != FarmType.CACTUS && this.meta < 7) {
                            ++this.meta;
                            this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, this.id, this.meta, 3);
                        }
                    } catch (Exception var8) {
                    }

                    boolean canHarvest = false;
                    V3 harvestBlock = new V3((double)this.xxx, (double)this.yyy, (double)this.zzz, this.jobWorld.field_73011_w.field_76574_g);
                    ArrayList<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, harvestBlock);
                    if (this.farmingBlock.farmType != FarmType.SUGAR && this.farmingBlock.farmType != FarmType.CACTUS) {
                        if (this.id == Blocks.field_150440_ba || this.id == Blocks.field_150423_aK || this.farmingBlock.farmType == FarmType.CUSTOM || this.meta >= 7) {
                            if (this.id != Blocks.field_150393_bb && this.id != Blocks.field_150394_bc) {
                                canHarvest = true;
                            }

                            if (this.id == null) {
                                canHarvest = false;
                            }
                        }
                    } else {
                        Block sid1 = this.jobWorld.getBlock(this.xxx, this.yyy + 1, this.zzz);
                        Block sid2 = this.jobWorld.getBlock(this.xxx, this.yyy + 2, this.zzz);
                        if (sid1 == Blocks.field_150436_aH && sid2 == Blocks.field_150436_aH) {
                            canHarvest = true;
                        }

                        if (sid1 == Blocks.field_150434_aF && sid2 == Blocks.field_150434_aF) {
                            canHarvest = true;
                        }
                    }

                    if (canHarvest) {
                        if (this.farmingBlock.farmType != FarmType.SUGAR && this.farmingBlock.farmType != FarmType.CACTUS) {
                            if (this.farmingBlock.farmType == FarmType.CUSTOM) {
                                if (System.currentTimeMillis() - this.lastCustomHarvest < 3600000L) {
                                    this.theStage = Stage.HOELAND;
                                    this.step = 1;
                                    this.theFolk.isWorking = false;
                                    return;
                                }

                                this.jobWorld.breakBlock(this.xxx, this.yyy, this.zzz, true);
                                this.pickUpDroppedCrops(harvestBlock);
                            } else {
                                if (minedStacks != null) {
                                    this.farmingChests = inventoriesFindClosest(this.theFolk.employedAt, 5);

                                    for(int s = 0; s < minedStacks.size(); ++s) {
                                        ItemStack stack = (ItemStack)minedStacks.get(s);
                                        if (stack != null) {
                                            this.inventoriesPut(this.farmingChests, stack, false);
                                        }
                                    }
                                }

                                this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, this.id, 0, 3);
                            }
                        } else {
                            this.farmingChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                            this.jobWorld.setBlock(this.xxx, this.yyy + 1, this.zzz, this.id, 0, 3);
                            this.jobWorld.setBlock(this.xxx, this.yyy + 2, this.zzz, this.id, 0, 3);
                            if (this.farmingBlock.farmType == FarmType.SUGAR) {
                                this.inventoriesPut(this.farmingChests, new ItemStack(Items.field_151120_aE, 2), false);
                            } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                                this.inventoriesPut(this.farmingChests, new ItemStack(Blocks.field_150434_aF, 2), false);
                            }
                        }

                        if (this.farmingBlock.farmType != FarmType.CUSTOM) {
                        }

                        GameStates var10000 = ModSimukraft.states;
                        var10000.credits -= 0.02F;
                        this.doneSomeWork = true;
                        hasHarvest = true;
                    } else {
                        hasHarvest = false;
                        done = this.setXYZ();
                        if (done) {
                            this.theStage = Stage.HOELAND;
                            this.step = 1;
                            this.theFolk.isWorking = false;
                            return;
                        }
                    }
                }

                return;
            }
        }

    }

    private void pickUpDroppedCrops(V3 v3center) {
        if (this.theFolk.theEntity != null) {
            List list1 = this.jobWorld.func_72839_b(this.theFolk.theEntity, AxisAlignedBB.func_72330_a(v3center.x, v3center.y, v3center.z, v3center.x + 1.0D, v3center.y + 1.0D, v3center.z + 1.0D).func_72314_b(3.0D, 2.0D, 3.0D));
            Iterator iterator1 = list1.iterator();
            if (!list1.isEmpty()) {
                while(iterator1.hasNext()) {
                    Entity entity1 = (Entity)iterator1.next();
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = entityitem.func_92059_d();

                        try {
                            ItemFood food = (ItemFood)is.func_77973_b();
                            if (food != null) {
                                boolean ok = this.inventoriesPut(this.farmingChests, is, false);
                                if (ok) {
                                    entityitem.func_70106_y();
                                }
                            }
                        } catch (Exception var9) {
                        }
                    }
                }
            }

        }
    }

    public void stageHoeland() {
        if (this.step == 1) {
            this.setupFarming();
            this.theFolk.statusText = "Tilling the land";
            this.theFolk.stayPut = true;
            this.theFolk.action = FolkAction.ATWORK;
            this.step = 2;
            this.theFolk.isWorking = true;
            this.rowCounter = 0;
        } else if (this.step == 2) {
            boolean done = false;
            boolean hasTilled = false;

            while(!hasTilled && !done) {
                done = this.setXYZ();
                if (done) {
                    this.theStage = Stage.PLANTSEEDS;
                    this.step = 1;
                    return;
                }

                this.id = this.jobWorld.getBlock(this.xxx, this.yyy - 1, this.zzz);
                this.meta = this.mc.field_71441_e.func_72805_g(this.xxx, this.yyy - 1, this.zzz);
                GameStates var10000;
                if (this.farmingBlock.farmType == FarmType.SUGAR) {
                    this.theFolk.statusText = "preparing the land";
                    if (this.rowCounter % 3 != 0 && (this.rowCounter + 1) % 3 != 0) {
                        if ((this.rowCounter + 2) % 3 == 0 && this.id != Blocks.water) {
                            this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.water, 0, 3);
                            hasTilled = true;
                            var10000 = ModSimukraft.states;
                            var10000.credits -= 0.01F;
                        }
                    } else if (this.id != Blocks.field_150346_d && this.id != Blocks.field_150349_c) {
                        this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150346_d, 0, 3);
                        this.jobWorld.playSound((double)this.xxx, (double)(this.yyy - 1), (double)this.zzz, Blocks.field_150349_c.field_149762_H.func_150498_e(), 1.0F, 1.0F, false);
                        hasTilled = true;
                        var10000 = ModSimukraft.states;
                        var10000.credits -= 0.01F;
                    }

                    ++this.rowCounter;
                    if (this.rowCounter > this.farmingBlock.getSizeWidth() + 1) {
                        this.rowCounter = 0;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                    this.theFolk.statusText = "preparing the land";
                    if ((this.xxx + this.zzz) % 2 == 0 && this.id != Blocks.field_150354_m) {
                        this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150354_m, 0, 3);
                        hasTilled = true;
                        var10000 = ModSimukraft.states;
                        var10000.credits -= 0.01F;
                    }
                } else if ((this.id == Blocks.field_150349_c || this.id == Blocks.field_150346_d) && ((this.farmingBlock.farmType == FarmType.MELON || this.farmingBlock.farmType == FarmType.PUMPKIN) && (this.ftb % 4 == 0 || this.ftb % 4 == 1) || this.farmingBlock.farmType == FarmType.WHEAT || this.farmingBlock.farmType == FarmType.CARROT || this.farmingBlock.farmType == FarmType.POTATO || this.farmingBlock.farmType == FarmType.CUSTOM)) {
                    this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                    this.jobWorld.playSound((double)this.xxx, (double)(this.yyy - 1), (double)this.zzz, Blocks.field_150349_c.field_149762_H.func_150498_e(), 1.0F, 1.0F, false);
                    hasTilled = true;
                    var10000 = ModSimukraft.states;
                    var10000.credits -= 0.01F;
                }

                if (done) {
                    this.theStage = Stage.PLANTSEEDS;
                    this.step = 1;
                    this.theFolk.isWorking = false;
                    return;
                }
            }
        }

    }

    public void stagePlantSeeds() {
        if (this.step == 1) {
            this.setupFarming();
            this.theFolk.stayPut = true;
            this.theFolk.action = FolkAction.ATWORK;
            this.step = 2;
            this.theFolk.isWorking = true;
        } else if (this.step == 2) {
            boolean done = false;
            boolean hasSown = false;

            while(true) {
                Block gid;
                Block aid;
                do {
                    do {
                        if (hasSown || done) {
                            if (done) {
                                this.theStage = Stage.HANGOUT;
                                this.theFolk.statusText = "Relaxing at the farm";
                                this.step = 1;
                                this.theFolk.isWorking = false;
                                this.inventoriesTransferFromFolk(this.theFolk.inventory, this.farmingChests, (ItemStack)null);
                                return;
                            }

                            return;
                        }

                        done = this.setXYZ();
                        if (done) {
                            this.theStage = Stage.HANGOUT;
                            this.theFolk.statusText = "Relaxing at the farm";
                            this.step = 1;
                            return;
                        }

                        gid = this.jobWorld.getBlock(this.xxx, this.yyy - 1, this.zzz);
                        aid = this.jobWorld.getBlock(this.xxx, this.yyy, this.zzz);
                    } while(gid != Blocks.field_150354_m && gid != Blocks.field_150349_c && gid != Blocks.field_150346_d && gid != Blocks.field_150458_ak);
                } while(aid != null);

                try {
                    if (this.farmingBlock.farmType != FarmType.CUSTOM) {
                        this.theFolk.statusText = "Planting " + this.farmingBlock.farmType.toString() + " seeds";
                    }
                } catch (Exception var10) {
                }

                ItemStack seed;
                if (this.farmingBlock.farmType == FarmType.WHEAT) {
                    if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151014_N, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = "No more wheat seeds to plant";
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }

                    this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                    this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150464_aj, 0, 3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.PUMPKIN) {
                    if (this.ftb % 4 == 0 || this.ftb % 4 == 1) {
                        if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151080_bb, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = "I need more pumpkin seeds!";
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }

                        this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                        this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150393_bb, 0, 3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.MELON) {
                    if (this.ftb % 4 == 0 || this.ftb % 4 == 1) {
                        if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151081_bc, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = "I need more melon seeds!";
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }

                        this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                        this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150394_bc, 0, 3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CARROT) {
                    if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151172_bF, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = "I need more carrots to plant!";
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }

                    this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                    this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150459_bM, 0, 3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.POTATO) {
                    if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151174_bG, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = "I need more potatoes to plant!";
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }

                    this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                    this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150469_bN, 0, 3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.SUGAR) {
                    Block cid = this.jobWorld.getBlock(this.xxx, this.yyy - 1, this.zzz);
                    if (cid == Blocks.field_150346_d || cid == Blocks.field_150349_c || cid == Blocks.field_150354_m) {
                        if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                            ItemStack seed = inventoriesGet(this.farmingChests, new ItemStack(Items.field_151120_aE, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = "No more sugar cane to plant";
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }

                        this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150436_aH, 0, 3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                    if ((this.xxx + this.zzz) % 2 == 0) {
                        if (ModSimukraft.gameMode != GameMode.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Blocks.field_150434_aF, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = "No more cactus to plant";
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }

                        this.jobWorld.setBlock(this.xxx, this.yyy, this.zzz, Blocks.field_150434_aF, 0, 3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CUSTOM) {
                    label171:
                    for(int ch = 0; ch < this.farmingChests.size(); ++ch) {
                        IInventory chest = (IInventory)this.farmingChests.get(ch);

                        for(int g = 0; g < chest.func_70302_i_(); ++g) {
                            ItemStack chestStack = chest.func_70301_a(g);
                            if (chestStack != null) {
                                this.theFolk.statusText = "Planting " + chestStack.func_82833_r();
                                ItemStack seed = inventoriesGet(this.farmingChests, new ItemStack(chestStack.func_77973_b(), 1), false, false);
                                if (seed != null) {
                                    this.jobWorld.setBlock(this.xxx, this.yyy - 1, this.zzz, Blocks.field_150458_ak, 0, 3);
                                    hasSown = seed.func_77973_b().func_77648_a(seed, this.mc.thePlayer, this.jobWorld, this.xxx, this.yyy - 1, this.zzz, 1, 0.0F, 0.0F, 0.0F);
                                    if (!hasSown) {
                                        this.theFolk.inventory.add(seed);
                                    }
                                    break label171;
                                }
                            }
                        }
                    }
                }

                if (hasSown) {
                    this.jobWorld.playSound((double)this.xxx, (double)this.yyy, (double)this.zzz, Blocks.field_150349_c.field_149762_H.func_150498_e(), 1.0F, 1.0F, false);
                    GameStates var10000 = ModSimukraft.states;
                    var10000.credits -= 0.01F;
                    this.doneSomeWork = true;
                }
            }
        }

    }

    public void stageHangout() {
        if (this.step == 1) {
            this.lastFarmCycle = System.currentTimeMillis();
            this.step = 2;
            this.theFolk.isWorking = false;
        } else if (this.step == 2) {
            Random ra = new Random();
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist > 3) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

            int r = ra.nextInt(10);
            if (r == 0) {
                if (ModSimukraft.gameMode == GameMode.HARDCORE) {
                    this.theFolk.statusText = "Wow, Hardcore mode is really hard!";
                } else {
                    this.theFolk.statusText = "Posting a picture of my farm on Facebook";
                }
            } else if (r == 1) {
                this.theFolk.statusText = "Checking the weather forecast";
            } else if (r == 2) {
                this.theFolk.statusText = "Wishing I had a tractor";
            } else if (r == 3) {
                this.theFolk.statusText = "Having a break";
            } else if (r == 4) {
                this.theFolk.statusText = "Cleaning dirt off my Hoe";
            } else if (r == 5) {
                this.theFolk.statusText = "Sharpening my Hoe";
            } else if (r == 6) {
                this.theFolk.statusText = "Eating my lunch";
            } else if (r == 7) {
                this.theFolk.statusText = "Reticulating my splines";
            } else if (r == 8) {
                this.theFolk.statusText = "Relaxing for a while";
            } else if (r == 9) {
                this.theFolk.statusText = "Wishing I was playing Minecraft";
            }

            if (System.currentTimeMillis() - this.lastFarmCycle > 180000L) {
                this.theStage = Stage.HARVEST;
                this.step = 1;
                return;
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
            this.theFolk.statusText = "Arrived on the farm";
            this.theStage = Stage.ARRIVEDATFARM;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

