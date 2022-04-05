package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

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
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient TerraformerType theType;
    private transient int radius;
    private transient ArrayList<IInventory> constructorChests = new ArrayList();
    private transient int totalBlockCount = 0;
    private transient int counter = 0;
    private transient int buckets = 0;

    public JobTerraformer() {
    }

    public JobTerraformer(FolkData folk) {
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
        this.theFolk.isWorking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!ModSim.isDayTime()) {
            this.theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(this.theFolk);
        if (this.theStage == Stage.WAITINGFORRESOURCES) {
            this.runDelay = 4000;
        }

        if (this.theStage == Stage.INPROGRESS) {
            if (ModSim.gameMode == GameMode.CREATIVE) {
                this.runDelay = 1;
            } else {
                this.runDelay = 300;
            }
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theStage != Stage.IDLE || !ModSim.isDayTime()) {
                if (this.theStage == Stage.WAITINGFORRESOURCES) {
                    this.stageWaitingForResources();
                } else if (this.theStage == Stage.INPROGRESS) {
                    this.stageInProgress();
                } else if (this.theStage == Stage.COMPLETE) {
                    this.stageComplete();
                }
            }

        }
    }

    private void stageWaitingForResources() {
        this.theFolk.isWorking = false;
        this.theFolk.statusText = I18n.format("container.sim.job.terra.former.Checking");
        this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.step == 1) {
            if (this.constructorChests.isEmpty()) {
                this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.Please");
            } else {
                this.theType = this.theFolk.terraformerType;
                this.radius = this.theFolk.terraformerRadius;
                if (this.theType == null) {
                    this.step = 4;
                } else {
                    this.step = 2;
                    ((IInventory)this.constructorChests.get(0)).openChest();
                }
            }
        } else if (this.step == 2) {
            ((IInventory)this.constructorChests.get(0)).closeChest();
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
            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.choose");
            this.step = 1;
        }

    }

    private void stageInProgress() {
        this.theFolk.isWorking = true;
        Random rand = new Random();
        boolean hasPlacedTree = false;
        ItemStack is = null;
        this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        if (this.step == 1) {
            ArrayList blockIDs;
            if (this.theType == TerraformerType.WATERTODIRT) {
                blockIDs = new ArrayList();
                blockIDs.add(Blocks.water);
                blockIDs.add(Blocks.water);
                this.closestBlocks = null;
                this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, true, false);
            } else if (this.theType == TerraformerType.NATURE) {
                blockIDs = new ArrayList();
                blockIDs.add(Blocks.dirt);
                blockIDs.add(Blocks.grass);
                this.closestBlocks = null;
                this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, true, true, false);
            } else if (this.theType == TerraformerType.LAWNMOWER) {
                blockIDs = new ArrayList();
                blockIDs.add(Blocks.tallgrass);
                blockIDs.add(Blocks.red_flower);
                blockIDs.add(Blocks.yellow_flower);
                this.closestBlocks = null;
                this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, true, false);
            } else if (this.theType == TerraformerType.FLATTENIZER) {
                blockIDs = new ArrayList();
                blockIDs.add(Blocks.grass);
                blockIDs.add(Blocks.dirt);
                blockIDs.add(Blocks.tallgrass);
                blockIDs.add(Blocks.stone);
                blockIDs.add(Blocks.sand);
                blockIDs.add(Blocks.sandstone);
                blockIDs.add(Blocks.gravel);
                this.closestBlocks = null;
                this.setClosestBlocksOfType(this.theFolk.employedAt, blockIDs, this.radius, false, false, false);
            } else {
                V3 v;
                if (this.theType == TerraformerType.VALUEPACK) {
                    blockIDs = new ArrayList();
                    blockIDs.add(Blocks.air);
                    blockIDs.add(Blocks.tallgrass);
                    blockIDs.add(Blocks.red_flower);
                    blockIDs.add(Blocks.yellow_flower);
                    v = new V3(this.theFolk.employedAt.x, this.theFolk.employedAt.y - 1.0D, this.theFolk.employedAt.z, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, true);
                } else if (this.theType == TerraformerType.GLACIAL) {
                    blockIDs = new ArrayList();
                    blockIDs.add(Blocks.air);
                    blockIDs.add(Blocks.tallgrass);
                    blockIDs.add(Blocks.water);
                    blockIDs.add(Blocks.water);
                    v = new V3(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, true, true, false);
                } else if (this.theType == TerraformerType.MOISTURIZER) {
                    blockIDs = new ArrayList();
                    blockIDs.add(Blocks.lava);
                    blockIDs.add(Blocks.lava);
                    v = new V3(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                } else if (this.theType == TerraformerType.THERMALIZER) {
                    blockIDs = new ArrayList();
                    blockIDs.add(Blocks.lava);
                    v = new V3(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                } else if (this.theType == TerraformerType.DEICER) {
                    blockIDs = new ArrayList();
                    blockIDs.add(Blocks.snow);
                    v = new V3(this.theFolk.employedAt.x, this.theFolk.employedAt.y, this.theFolk.employedAt.z, this.theFolk.employedAt.theDimension);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(v, blockIDs, this.radius, false, true, false);
                }
            }

            this.step = 2;
        } else if (this.step == 2) {
            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.Scanning");
        } else if (this.step == 3) {
            this.totalBlockCount = this.closestBlocks.size();
            if (this.totalBlockCount == 0) {
                this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.Nothing");
                ModSim.sendChat(I18n.format("container.sim.job.terra.farmer.terraformed"));
                this.theFolk.selfFire();
                return;
            }

            this.step = 4;
            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.process");
        } else if (this.step == 4) {
            //int count = false;
            ItemStack gotDirt;
            if (this.theType == TerraformerType.WATERTODIRT) {
                if (ModSim.gameMode != GameMode.CREATIVE) {
                    gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Blocks.dirt, 1), false, false);
                    if (gotDirt == null) {
                        this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.dirt");
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
                    if (ModSim.gameMode != GameMode.CREATIVE) {
                        is = inventoriesGet(this.constructorChests, (ItemStack) null, true, false);
                        if (is == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.saplings");
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            this.step = 1;
                            return;
                        }
                    } else {
                        is = new ItemStack(Blocks.sapling, 1, (new Random()).nextInt(4));
                    }
                }
            } else if (this.theType != TerraformerType.LAWNMOWER && this.theType != TerraformerType.DEICER && this.theType != TerraformerType.FLATTENIZER) {
                if (this.theType == TerraformerType.VALUEPACK) {
                    if (ModSim.gameMode != GameMode.CREATIVE) {
                        gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Blocks.dirt, 1), false, false);
                        if (gotDirt == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.dirt");
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            this.step = 1;
                            return;
                        }
                    }
                } else if (this.theType != TerraformerType.GLACIAL && this.theType != TerraformerType.MOISTURIZER) {
                    if (this.theType == TerraformerType.THERMALIZER && ModSim.gameMode != GameMode.CREATIVE) {
                        gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Items.bucket, 1), false, false);
                        if (gotDirt == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.buckets");
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            this.step = 1;
                            return;
                        }
                    }
                } else if (ModSim.gameMode != GameMode.CREATIVE) {
                    if (this.buckets == 0) {
                        gotDirt = inventoriesGet(this.constructorChests, new ItemStack(Items.water_bucket, 1), false, false);
                        if (gotDirt == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.water");
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            this.step = 1;
                            return;
                        }

                        this.inventoriesPut(this.constructorChests, new ItemStack(Items.bucket, 1), true);
                        this.buckets = 1000;
                    } else {
                        --this.buckets;
                    }
                }
            }

            Double x = (double)this.totalBlockCount;
            Double y = (double)this.closestBlocks.size();
            Double percent = (x - y) / x;
            percent = percent * 100.0D;
            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.Terraforming") + ", " + percent.intValue() + " % " + I18n.format("container.sim.job.terra.farmer.complete");
            V3 v = (V3)this.closestBlocks.get(0);
            GameStates var10000;
            if (this.theType == TerraformerType.WATERTODIRT) {
                this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.dirt, 0, 3);
                if (ModSim.gameMode != GameMode.CREATIVE) {
                    var10000 = ModSim.states;
                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                }
            } else if (this.theType == TerraformerType.NATURE) {
                if (hasPlacedTree) {
                    this.jobWorld.setBlock(v.x.intValue(), v.y.intValue() + 1, v.z.intValue(), Blocks.sapling, is.getMetadata(), 3);
                    if (ModSim.gameMode != GameMode.CREATIVE) {
                        var10000 = ModSim.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.009D);
                    }

                    this.runDelay = 500;
                } else {
                    int r = rand.nextInt(10);
                    if (r == 2) {
                        this.jobWorld.setBlock(v.x.intValue(), v.y.intValue() + 1, v.z.intValue(), Blocks.red_flower, 0, 3);
                        this.jobWorld.markBlockForUpdate(v.x.intValue(), v.y.intValue() + 1, v.z.intValue());
                    } else if (r == 5) {
                        this.jobWorld.setBlock(v.x.intValue(), v.y.intValue() + 1, v.z.intValue(), Blocks.yellow_flower, 0, 3);
                    }

                    this.runDelay = 50;
                }
            } else {
                ArrayList minedStacks;
                int s;
                ItemStack stack;
                if (this.theType == TerraformerType.LAWNMOWER) {
                    minedStacks = this.translateBlockWhenMined(this.jobWorld, v);
                    if (minedStacks != null) {
                        for(s = 0; s < minedStacks.size(); ++s) {
                            stack = (ItemStack)minedStacks.get(s);
                            if (stack != null) {
                                this.inventoriesPut(this.constructorChests, stack, false);
                            }
                        }
                    }

                    if (this.mc.theWorld.isRemote) {
                        this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.air, 0, 3);
                        var10000 = ModSim.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.009D);
                    }
                } else if (this.theType != TerraformerType.FLATTENIZER) {
                    if (this.theType == TerraformerType.VALUEPACK) {
                        if (this.mc.theWorld.isRemote) {
                            this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.dirt, 0, 3);
                            if (ModSim.gameMode != GameMode.CREATIVE) {
                                var10000 = ModSim.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }
                        }
                    } else if (this.theType == TerraformerType.GLACIAL) {
                        Block blockId = this.jobWorld.getBlock(v.x.intValue(), v.y.intValue(), v.z.intValue());
                        if (blockId != null && blockId != Blocks.tallgrass) {
                            if ((blockId == Blocks.water || blockId == Blocks.water) && this.mc.theWorld.isRemote) {
                                this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.ice, 0, 3);
                                if (ModSim.gameMode != GameMode.CREATIVE) {
                                    var10000 = ModSim.states;
                                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                }
                            }
                        } else {
                            Block idBelow = this.jobWorld.getBlock(v.x.intValue(), v.y.intValue() - 1, v.z.intValue());
                            if (idBelow != null && idBelow != Blocks.ice && idBelow != Blocks.water && idBelow != Blocks.water && idBelow != Blocks.snow && this.mc.theWorld.isRemote) {
                                this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.snow, 0, 3);
                                if (ModSim.gameMode != GameMode.CREATIVE) {
                                    var10000 = ModSim.states;
                                    var10000.credits = (float) ((double) var10000.credits - 0.009D);
                                }
                            }
                        }
                    } else if (this.theType == TerraformerType.MOISTURIZER) {
                        if (this.mc.theWorld.isRemote) {
                            this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.obsidian, 0, 3);
                            this.jobWorld.markBlockForUpdate(v.x.intValue(), v.y.intValue(), v.z.intValue());
                            if (ModSim.gameMode != GameMode.CREATIVE) {
                                var10000 = ModSim.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }
                        }
                    } else if (this.theType == TerraformerType.THERMALIZER) {
                        if (this.mc.theWorld.isRemote) {
                            this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.air, 0, 3);
                            this.jobWorld.markBlockForUpdate(v.x.intValue(), v.y.intValue(), v.z.intValue());
                            if (ModSim.gameMode != GameMode.CREATIVE) {
                                var10000 = ModSim.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }

                            this.inventoriesPut(this.constructorChests, new ItemStack(Items.lava_bucket, 1), false);
                        }
                    } else if (this.theType == TerraformerType.DEICER && this.mc.theWorld.isRemote) {
                        this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.grass, 0, 3);
                        this.jobWorld.markBlockForUpdate(v.x.intValue(), v.y.intValue(), v.z.intValue());
                        ++this.counter;
                        if (ModSim.gameMode != GameMode.CREATIVE) {
                            var10000 = ModSim.states;
                            var10000.credits = (float) ((double) var10000.credits - 0.009D);
                        }

                        if (this.counter % 4 == 0) {
                            this.inventoriesPut(this.constructorChests, new ItemStack(Blocks.snow, 1, 0), false);
                        }
                    }
                } else {
                    minedStacks = this.translateBlockWhenMined(this.jobWorld, v);
                    if (minedStacks != null) {
                        for(s = 0; s < minedStacks.size(); ++s) {
                            stack = (ItemStack)minedStacks.get(s);
                            if (stack != null) {
                                this.inventoriesPut(this.constructorChests, stack, false);
                            }
                        }
                    }

                    try {
                        if (this.mc.theWorld.isRemote) {
                            this.jobWorld.setBlock(v.x.intValue(), v.y.intValue(), v.z.intValue(), Blocks.air, 0, 3);
                            if (ModSim.gameMode != GameMode.CREATIVE) {
                                var10000 = ModSim.states;
                                var10000.credits = (float) ((double) var10000.credits - 0.009D);
                            }
                        }
                    } catch (Exception var12) {
                        var12.printStackTrace();
                    }
                }
            }

            this.closestBlocks.remove(0);
            if (this.closestBlocks.size() == 0) {
                this.theStage = Stage.COMPLETE;
            }
        }

    }

    private void stageComplete() {
        this.theFolk.isWorking = false;
        ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.terra.farmer.has_completed"));
        this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
        this.theFolk.stayPut = false;
        this.theFolk.terraformerRadius = 1;
        this.theFolk.terraformerType = null;
        this.theFolk.selfFire();
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.terra.farmer.Arrived");
            this.theStage = Stage.WAITINGFORRESOURCES;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }


}

