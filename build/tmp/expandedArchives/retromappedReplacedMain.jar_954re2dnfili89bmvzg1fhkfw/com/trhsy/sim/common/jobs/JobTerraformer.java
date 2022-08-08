package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameMode;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName JobTerraformer
 * @Description todo 地形改造者
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:55
 * ========================================
 **/
public class JobTerraformer extends Job {
    public Vocation vocation = null;
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient TerraformerType theType;
    private transient int radius;
    private transient List<IInventory> constructorChests = new CopyOnWriteArrayList();
    private transient int totalBlockCount = 0;
    private transient int counter = 0;
    private transient int buckets = 0;

    public JobTerraformer() {
    }

    public JobTerraformer(FolkData folk) {
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobTerraformer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void resetJob() {
        try {
            this.theStage = Stage.IDLE;
            this.theFolk.isWorking = false;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("resetJob出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
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
            //等待资源
            if (this.theStage == Stage.WAITINGFORRESOURCES) {
                this.runDelay = 4000;
            }
            //正在进行
            if (this.theStage == Stage.INPROGRESS) {
                //创造模式
                if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                    this.runDelay = 1;
                } else {
                    this.runDelay = 300;
                }
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    //等待资源
                    if (this.theStage == Stage.WAITINGFORRESOURCES) {
                        this.stageWaitingForResources();
                        //正在进行
                    } else if (this.theStage == Stage.INPROGRESS) {
                        this.stageInProgress();
                        //完成
                    } else if (this.theStage == Stage.COMPLETE) {
                        this.stageComplete();
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobTerraformer-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 等待资源阶段
     * @Date 22:23 2022/7/9
     * @Param []
     **/
    private void stageWaitingForResources() {
        try {
            this.theFolk.isWorking = false;
            //检查地形资源...
            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.former.Checking");
            this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.step == 1) {
                if (this.constructorChests.isEmpty()) {
                    //请将至少一个箱子放在箱子附近。
                    this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.Please");
                } else {
                    //地形成型器类型
                    this.theType = this.theFolk.terraformerType;
                    //地形形成器半径
                    this.radius = this.theFolk.terraformerRadius;
                    if (this.theType == null) {
                        this.step = 4;
                    } else {
                        this.step = 2;
                        ((IInventory) this.constructorChests.get(0)).func_174889_b(mc.field_71439_g);
                    }
                }
            } else if (this.step == 2) {
                ((IInventory) this.constructorChests.get(0)).func_174886_c(mc.field_71439_g);
                this.theStage = Stage.INPROGRESS;
                this.step = 1;
            } else if (this.step == 3) {
                if (this.vocation != Vocation.TERRAFORMER) {
                    this.theFolk.selfFire();
                    return;
                }

                this.step = 4;
                this.theStage = Stage.INPROGRESS;
            } else if (this.step == 4) {
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.choose");
                this.step = 1;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("地形规划师等待资源出错了:" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageInProgress() {
        try {
            this.theFolk.isWorking = true;
            Random rand = new Random();
            boolean hasPlacedTree = false;
            ItemStack is = null;
            this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            V3 v;
            if (this.step == 1) {
                CopyOnWriteArrayList blockIDs;
                //填海
                if (this.theType == TerraformerType.WATERTODIRT) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150355_j);
                    blockIDs.add(Blocks.field_150355_j);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, true, false);
                    //植树
                } else if (this.theType == TerraformerType.NATURE) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150346_d);
                    blockIDs.add(Blocks.field_150349_c);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, true, true, false);
                    //割草
                } else if (this.theType == TerraformerType.LAWNMOWER) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150329_H);
                    blockIDs.add(Blocks.field_150328_O);
                    blockIDs.add(Blocks.field_150327_N);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, true, false);
                    //铺平
                } else if (this.theType == TerraformerType.FLATTENIZER) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150349_c);
                    blockIDs.add(Blocks.field_150346_d);
                    blockIDs.add(Blocks.field_150329_H);
                    blockIDs.add(Blocks.field_150348_b);
                    blockIDs.add(Blocks.field_150354_m);
                    blockIDs.add(Blocks.field_150322_A);
                    blockIDs.add(Blocks.field_150351_n);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, false, false);
                    //单层泥土
                } else if (this.theType == TerraformerType.VALUEPACK) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150350_a);
                    blockIDs.add(Blocks.field_150329_H);
                    blockIDs.add(Blocks.field_150328_O);
                    blockIDs.add(Blocks.field_150327_N);
                    v = new V3(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b - 1, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, true);
                    //放冰
                } else if (this.theType == TerraformerType.GLACIAL) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150350_a);
                    blockIDs.add(Blocks.field_150329_H);
                    blockIDs.add(Blocks.field_150355_j);
                    blockIDs.add(Blocks.field_150355_j);
                    v = new V3(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, true, true, false);
                    //放水
                } else if (this.theType == TerraformerType.MOISTURIZER) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150353_l);
                    blockIDs.add(Blocks.field_150353_l);
                    v = new V3(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                    //放岩浆
                } else if (this.theType == TerraformerType.THERMALIZER) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150353_l);
                    v = new V3(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                    //除冰
                } else if (this.theType == TerraformerType.DEICER) {
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.field_150433_aE);
                    v = new V3(this.theFolk.employedAt.field_72450_a, this.theFolk.employedAt.field_72448_b, this.theFolk.employedAt.field_72449_c, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                }

                this.step = 2;
            } else if (this.step == 2) {
                //扫描地形...
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.Scanning");
            } else if (this.step == 3) {
                this.totalBlockCount = this.closestBlocks.size();
                if (this.totalBlockCount == 0) {
                    //没有什么要地球化的！
                    this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.Nothing");
                    ModSimReloaded.sendChat(I18n.func_135052_a("container.sim.job.terra.farmer.terraformed"));
                    this.theFolk.selfFire();
                    return;
                }

                this.step = 4;
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.process");
            } else if (this.step == 4) {
                //int count = false;
                ItemStack gotDirt;
                if (this.theType == TerraformerType.WATERTODIRT) {
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Blocks.field_150346_d, 1), false, false);
                        if (gotDirt == null) {
                            this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.dirt");
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            this.step = 1;
                            return;
                        }
                    }
                } else if (this.theType == TerraformerType.NATURE) {
                    ++this.counter;
                    hasPlacedTree = false;
                    if (this.counter % 15 == 0) {
                        hasPlacedTree = true;
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            is = inventoriesGet(this.constructorChests, (ItemStack) null, true, false);
                            if (is == null) {
                                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.saplings");
                                this.theStage = Stage.WAITINGFORRESOURCES;
                                this.step = 1;
                                return;
                            }
                        } else {
                            is = new ItemStack(Blocks.field_150345_g, 1, (new Random()).nextInt(4));
                        }
                    }
                } else if (this.theType != TerraformerType.LAWNMOWER && this.theType != TerraformerType.DEICER && this.theType != TerraformerType.FLATTENIZER) {
                    if (this.theType == TerraformerType.VALUEPACK) {
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Blocks.field_150346_d, 1), false, false);
                            if (gotDirt == null) {
                                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.dirt");
                                this.theStage = Stage.WAITINGFORRESOURCES;
                                this.step = 1;
                                return;
                            }
                        }
                    } else if (this.theType != TerraformerType.GLACIAL && this.theType != TerraformerType.MOISTURIZER) {
                        if (this.theType == TerraformerType.THERMALIZER && GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Items.field_151133_ar, 1), false, false);
                            if (gotDirt == null) {
                                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.buckets");
                                this.theStage = Stage.WAITINGFORRESOURCES;
                                this.step = 1;
                                return;
                            }
                        }
                    } else if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        if (this.buckets == 0) {
                            gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Items.field_151131_as, 1), false, false);
                            if (gotDirt == null) {
                                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.water");
                                this.theStage = Stage.WAITINGFORRESOURCES;
                                this.step = 1;
                                return;
                            }

                            this.inventoriesPut(this.constructorChests, new ItemStack(Items.field_151133_ar, 1), true);
                            this.buckets = 1000;
                        } else {
                            --this.buckets;
                        }
                    }
                }

                Double x = (double) this.totalBlockCount;
                Double y = (double) this.closestBlocks.size();
                Double percent = (x - y) / x;
                percent = percent * 100;
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.Terraforming") + ", " + percent + " % " + I18n.func_135052_a("container.sim.job.terra.farmer.complete");
                v = (V3) this.closestBlocks.get(0);
                GameStates var10000;
                if (this.theType == TerraformerType.WATERTODIRT) {
                    BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                    this.jobWorld.func_180501_a(blockPos2, Blocks.field_150346_d.func_176223_P(), 3);
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        var10000 = ModSimReloaded.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.009D);
                    }
                } else if (this.theType == TerraformerType.NATURE) {
                    if (hasPlacedTree) {
                        BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b + 1, v.field_72449_c);
                        this.jobWorld.func_180501_a(blockPos2, Blocks.field_150345_g.func_176223_P(), 3);
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            var10000 = ModSimReloaded.states;
                            var10000.credits = (float) ((double) var10000.credits - 0.009D);
                        }

                        this.runDelay = 500;
                    } else {
                        int r = rand.nextInt(10);
                        if (r == 2) {
                            BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b + 1, v.field_72449_c);
                            this.jobWorld.func_180501_a(blockPos2, Blocks.field_150328_O.func_176223_P(), 3);
                            this.jobWorld.func_175689_h(blockPos2);
                        } else if (r == 5) {
                            BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b + 1, v.field_72449_c);
                            this.jobWorld.func_180501_a(blockPos2, Blocks.field_150327_N.func_176223_P(), 3);
                        }

                        this.runDelay = 50;
                    }
                } else {
                    List minedStacks;
                    int s;
                    ItemStack stack;
                    if (this.theType == TerraformerType.LAWNMOWER) {
                        minedStacks = this.translateBlockWhenMined(this.jobWorld, v);
                        if (minedStacks != null) {
                            for (s = 0; s < minedStacks.size(); ++s) {
                                stack = (ItemStack) minedStacks.get(s);
                                if (stack != null) {
                                    this.inventoriesPut(this.constructorChests, stack, false);
                                }
                            }
                        }

                        if (this.mc.field_71441_e.field_72995_K) {
                            BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                            this.jobWorld.func_180501_a(blockPos2, Blocks.field_150350_a.func_176223_P(), 3);
                            var10000 = ModSimReloaded.states;
                            var10000.credits = (float) ((double) var10000.credits - 0.009D);
                        }
                    } else if (this.theType != TerraformerType.FLATTENIZER) {
                        if (this.theType == TerraformerType.VALUEPACK) {
                            if (this.mc.field_71441_e.field_72995_K) {
                                BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                                this.jobWorld.func_180501_a(blockPos2, Blocks.field_150346_d.func_176223_P(), 3);
                                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                    var10000 = ModSimReloaded.states;
                                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                }
                            }
                        } else if (this.theType == TerraformerType.GLACIAL) {

                            Block blockId = this.jobWorld.func_180495_p(new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c)).func_177230_c();
                            if (blockId != null && blockId != Blocks.field_150329_H) {
                                if ((blockId == Blocks.field_150355_j || blockId == Blocks.field_150355_j) && this.mc.field_71441_e.field_72995_K) {
                                    BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                                    this.jobWorld.func_180501_a(blockPos2, Blocks.field_150432_aD.func_176223_P(), 3);
                                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                        var10000 = ModSimReloaded.states;
                                        var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                    }
                                }
                            } else {

                                Block idBelow = this.jobWorld.func_180495_p(new BlockPos(v.field_72450_a, v.field_72448_b - 1, v.field_72449_c)).func_177230_c();
                                if (idBelow != null && idBelow != Blocks.field_150432_aD && idBelow != Blocks.field_150355_j && idBelow != Blocks.field_150355_j && idBelow != Blocks.field_150433_aE && this.mc.field_71441_e.field_72995_K) {
                                    BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                                    this.jobWorld.func_180501_a(blockPos2, Blocks.field_150433_aE.func_176223_P(), 3);
                                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                        var10000 = ModSimReloaded.states;
                                        var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                    }
                                }
                            }
                        } else if (this.theType == TerraformerType.MOISTURIZER) {
                            if (this.mc.field_71441_e.field_72995_K) {
                                BlockPos blockPos = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                                this.jobWorld.func_180501_a(blockPos, Blocks.field_150343_Z.func_176223_P(), 3);
                                this.jobWorld.func_175689_h(blockPos);
                                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                    var10000 = ModSimReloaded.states;
                                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                }
                            }
                        } else if (this.theType == TerraformerType.THERMALIZER) {
                            if (this.mc.field_71441_e.field_72995_K) {
                                BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                                this.jobWorld.func_180501_a(blockPos2, Blocks.field_150350_a.func_176223_P(), 3);
                                this.jobWorld.func_175689_h(blockPos2);
                                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                    var10000 = ModSimReloaded.states;
                                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                }

                                this.inventoriesPut(this.constructorChests, new ItemStack(Items.field_151129_at, 1), false);
                            }
                        } else if (this.theType == TerraformerType.DEICER && this.mc.field_71441_e.field_72995_K) {
                            BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                            this.jobWorld.func_180501_a(blockPos2, Blocks.field_150349_c.func_176223_P(), 3);
                            this.jobWorld.func_175689_h(blockPos2);
                            ++this.counter;
                            if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                var10000 = ModSimReloaded.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }

                            if (this.counter % 4 == 0) {
                                this.inventoriesPut(this.constructorChests, new ItemStack(Blocks.field_150433_aE, 1, 0), false);
                            }
                        }
                    } else {
                        minedStacks = this.translateBlockWhenMined(this.jobWorld, v);
                        if (minedStacks != null) {
                            for (s = 0; s < minedStacks.size(); ++s) {
                                stack = (ItemStack) minedStacks.get(s);
                                if (stack != null) {
                                    this.inventoriesPut(this.constructorChests, stack, false);
                                }
                            }
                        }

                        if (this.mc.field_71441_e.field_72995_K) {
                            BlockPos blockPos2 = new BlockPos(v.field_72450_a, v.field_72448_b, v.field_72449_c);
                            this.jobWorld.func_180501_a(blockPos2, Blocks.field_150350_a.func_176223_P(), 3);
                            if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                                var10000 = ModSimReloaded.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }
                        }
                    }
                }

                this.closestBlocks.remove(0);
                if (this.closestBlocks.size() == 0) {
                    this.theStage = Stage.COMPLETE;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageInProgress出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageComplete() {
        try {
            this.theFolk.isWorking = false;
            ModSimReloaded.sendChat(this.theFolk.name + I18n.func_135052_a("container.sim.job.terra.farmer.has_completed"));
            this.mc.field_71441_e.func_72980_b(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u, this.mc.field_71439_g.field_70161_v, ModSim.MODID + ":cash", 1, 1, false);
            this.theFolk.stayPut = false;
            this.theFolk.terraformerRadius = 1;
            this.theFolk.terraformerType = null;
            this.theFolk.selfFire();
            this.theStage = Stage.IDLE;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageComplete出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.func_135052_a("container.sim.job.terra.farmer.Arrived");
                this.theStage = Stage.WAITINGFORRESOURCES;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }


}

