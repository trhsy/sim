package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobBuilder
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:37
 * ========================================
 **/
public class JobBuilder extends Job implements Serializable {
    private static final long serialVersionUID = -1177665807904279141L;
    //阶段
    public Stage theStage;
    //实体人数据
    public FolkData theFolk = null;
    //职业
    public Vocation vocation = null;
    public int runDelay = 1000;
    public long timeSinceLastRun = 0L;
    private transient ArrayList<IInventory> constructorChests = new ArrayList();
    //建筑物
    private transient Building theBuilding = null;
    private transient EntityConBox theConBox = null;
    private transient long lastNotifiedOfMaterials = 0L;
    private transient long soundLastPlayed = 0L;
    int l = 0;
    int ftb = 0;
    int ltr = 0;
    int xo = 0;
    int zo = 0;
    int acount = 0;
    int cx;
    int cy;
    int cz;
    int ex;
    int ey;
    int ez;
    int bx = 0;
    int by = 0;
    int bz = 0;

    public JobBuilder() {
    }

    public JobBuilder(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }

            this.theBuilding = this.theFolk.theBuilding;
        }
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
    }

    @Override
    public void onUpdate() {
        if (this.theFolk != null) {
            super.onUpdate();
            if (!ModSim.isDayTime()) {
                this.theStage = Stage.IDLE;
            }

            super.onUpdateGoingToWork(this.theFolk);
            if (this.theStage == Stage.WAITINGFORRESOURCES) {
                this.runDelay = 3000;
                if (this.theBuilding != null) {
                }
            }

            if (this.theStage == Stage.INPROGRESS && this.step == 1) {
                this.runDelay = (int)(2000.0F / this.theFolk.levelBuilder);
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                if (this.theFolk.theirJob != null && this.theFolk.vocation != Vocation.BUILDER) {
                    this.theFolk.selfFire();
                } else {
                    this.theFolk.updateLocationFromEntity();
                    int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                    if (dist <= 3 && this.theStage == Stage.WORKERASSIGNED) {
                        this.theFolk.action = FolkAction.ATWORK;
                        this.theFolk.statusText = I18n.format("container.sim.job.builder_Arrived");
                        this.theStage = Stage.BLUEPRINT;
                    }

                    if (dist < 10 && this.theStage == Stage.WORKERASSIGNED && this.theFolk.destination == null) {
                        this.theFolk.action = FolkAction.ATWORK;
                        this.theFolk.statusText = I18n.format("container.sim.job.builder_Arrived");
                        this.theStage = Stage.BLUEPRINT;
                    }

                    if ((this.theStage == Stage.IDLE || this.theStage == Stage.WORKERASSIGNED) && ModSim.isDayTime()) {
                        if (this.theFolk.action != FolkAction.ONWAYTOWORK) {
                            this.theStage = Stage.WORKERASSIGNED;
                        }
                    } else if (this.theStage != Stage.WORKERASSIGNED) {
                        if (this.theStage == Stage.BLUEPRINT) {
                            this.stageBlueprint();
                        } else if (this.theStage == Stage.WAITINGFORRESOURCES) {
                            this.stageWaitingForResources();
                        } else if (this.theStage == Stage.INPROGRESS) {
                            this.stageInProgress();
                        } else if (this.theStage == Stage.COMPLETE) {
                            this.stageComplete();
                        }
                    }

                }
            }
        }
    }

    private void stageBlueprint() {
        this.theBuilding = this.theFolk.theBuilding;
        if (this.theBuilding == null) {
            this.theFolk.statusText = I18n.format("container.sim.job.builder_building");
        } else {
            this.theFolk.statusText = I18n.format("container.sim.job.builder_blueprints");
            this.theFolk.updateLocationFromEntity();
            double dist = (double)this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist < 4.0D) {
                this.theFolk.stayPut = true;
            }

            if (ModSim.configFolkTalking) {
                if (this.theFolk.gender == 0) {
                    this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readym", 1.0F, 1.0F, false);
                } else {
                    this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readyf", 1.0F, 1.0F, false);
                }
            }

            this.theStage = Stage.WAITINGFORRESOURCES;
            this.step = 1;
            if (this.theConBox == null) {
                World world = MinecraftServer.getServer().worldServerForDimension(this.theFolk.location.theDimension);
                this.theConBox = new EntityConBox(world);
                this.theConBox.theFolk = this.theFolk;
                this.theConBox.setLocationAndAngles(this.theFolk.employedAt.x + 2.0D, this.theFolk.employedAt.y, this.theFolk.employedAt.z, 0.0F, 0.0F);
                if (!world.isRemote) {
                    world.spawnEntityInWorld(this.theConBox);
                }
            }
        }

    }

    private void stageWaitingForResources() {
        this.theFolk.isWorking = false;
        int dist;
        if (this.step == 1) {
            this.theFolk.statusText = I18n.format("container.sim.job.builder_Checking");
            this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.constructorChests.size() == 0) {
                this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_block");
            } else {
                try {
                    ((IInventory)this.constructorChests.get(0)).openChest();
                } catch (Exception var2) {
                    ModSim.log.info("JobBuilder:JobBuilder's 的箱子是空的");
                }

                this.step = 2;
            }

            dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist < 5) {
                this.theFolk.stayPut = true;
            }
        } else if (this.step == 2) {
            ((IInventory)this.constructorChests.get(0)).closeChest();
            this.theStage = Stage.INPROGRESS;
            this.step = 1;
        } else if (this.step == 3) {
            if (this.theFolk.vocation != Vocation.BUILDER) {
                this.theFolk.selfFire();
                return;
            }

            this.step = 2;
            this.theStage = Stage.INPROGRESS;
            if (this.theFolk.isSpawned()) {
                this.theFolk.updateLocationFromEntity();
            }

            dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist < 5) {
                this.theFolk.stayPut = true;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }
        }

    }

    private void stageInProgress() {
        Block blockId = null;
        boolean alreadyPlaced = false;
        this.theFolk.updateLocationFromEntity();
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 5 && this.theFolk.destination == null) {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        } else {
            if (this.step == 1) {
                this.cx = this.theFolk.employedAt.x.intValue();
                this.cy = this.theFolk.employedAt.y.intValue();
                this.cz = this.theFolk.employedAt.z.intValue();
                this.ex = this.theFolk.employedAt.x.intValue();
                this.ey = this.theFolk.employedAt.y.intValue();
                this.ez = this.theFolk.employedAt.z.intValue();
                this.bx = this.ex;
                this.by = this.ey;
                this.bz = this.ez;
                if (this.theBuilding.buildDirection.contentEquals("-x")) {
                    this.bx = this.cx + 1;
                } else if (this.theBuilding.buildDirection.contentEquals("+x")) {
                    this.bx = this.cx - 1;
                } else if (this.theBuilding.buildDirection.contentEquals("-z")) {
                    this.bz = this.cz + 1;
                } else {
                    if (!this.theBuilding.buildDirection.contentEquals("+z")) {
                        ;
                        ModSim.sendChat(I18n.format("container.sim.job.builder_constructor_direction"));
                        this.theFolk.selfFire();
                        return;
                    }

                    this.bz = this.cz - 1;
                }

                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_started_building") + this.theBuilding.displayNameWithoutPK);
                this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Building") + this.theBuilding.displayNameWithoutPK;
                if (this.theBuilding == null || this.theBuilding.layerCount == 0) {
                    ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_started_misplaced"));
                    return;
                }

                this.theFolk.stayPut = true;
                if (this.theBuilding == null) {
                    this.theFolk.selfFire();
                    return;
                }

                this.l = 0;
                this.ftb = 0;
                this.ltr = 0;
                this.acount = 0;
                this.step = 2;
                this.theBuilding.blockLocations.clear();
            } else if (this.step == 2) {
                do {
                    this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Building") + this.theBuilding.displayNameWithoutPK;
                    if (this.theBuilding.buildDirection.contentEquals("+z")) {
                        this.xo = this.ltr;
                        this.zo = -this.ftb;
                    } else if (this.theBuilding.buildDirection.contentEquals("-z")) {
                        this.xo = -this.ltr;
                        this.zo = this.ftb;
                    } else if (this.theBuilding.buildDirection.contentEquals("+x")) {
                        this.xo = -this.ftb;
                        this.zo = -this.ltr;
                    } else if (this.theBuilding.buildDirection.contentEquals("-x")) {
                        this.xo = this.ftb;
                        this.zo = this.ltr;
                    }

                    if (this.theBuilding == null) {
                        this.theFolk.selfFire();
                        return;
                    }

                    String[] bl = null;

                    try {
                        bl = this.theBuilding.structure[this.acount].split(":");
                    } catch (Exception var17) {
                        ModSim.log.warn("JobBuilder: 建筑中的空块,改用空气");
                        bl = "0:0".split(":");
                    }

                    blockId = Block.getBlockFromName(bl[0]);
                    ModSim.log.info("***************blockId:" + blockId);
                    int subtype = Integer.parseInt(bl[1]);
                    if (blockId == Blocks.grass) {
                        blockId = Blocks.dirt;
                    }

                    if (this.theBuilding.type.contentEquals("other") && this.acount == 0) {
                        blockId = BlockLoader.blockControlBox;
                        subtype = 2;
                    }

                    if (blockId == BlockLoader.blockControlBox) {
                        try {
                            this.theBuilding.primaryXYZ = new V3((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), this.theFolk.employedAt.theDimension);
                            this.theBuilding.saveThisBuilding();
                        } catch (Exception var16) {
                            ModSim.log.info("JobBuilder:构建为空");
                        }
                    }

                    V3 v3;
                    if (Block.getIdFromBlock(blockId) == 999 && subtype == 999) {
                        this.theBuilding.livingXYZ = new V3((double)(this.bx + this.xo), (double)(this.by + this.l), (double)(this.bz + this.zo), this.theFolk.employedAt.theDimension);
                        blockId = null;
                        subtype = 0;
                    } else if (Block.getIdFromBlock(blockId) == 999 && subtype >= 0 && subtype <= 9) {
                        v3 = new V3((double)(this.bx + this.xo), (double)(this.by + this.l), (double)(this.bz + this.zo), this.theFolk.employedAt.theDimension);
                        v3.meta = subtype;
                        this.theBuilding.blockSpecial.add(v3);
                        blockId = null;
                        subtype = 0;
                    }

                    v3 = null;
                    boolean var7 = false;

                    Block currBlockId;
                    try {
                        currBlockId = this.jobWorld.getBlock(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                        int currBlockMeta = this.jobWorld.getBlockMetadata(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                        if (blockId != currBlockId && (blockId != Blocks.dirt || currBlockId != Blocks.grass) && (blockId != Blocks.grass || currBlockId != Blocks.dirt)) {
                            alreadyPlaced = false;
                        } else {
                            alreadyPlaced = true;
                        }
                    } catch (Exception var20) {
                        this.theFolk.selfFire();
                        return;
                    }

                    String want = "???";
                    ItemStack wantIS = new ItemStack(blockId, 1, 0);
                    if (wantIS != null && wantIS != null) {
                        try {
                            want = wantIS.getDisplayName();
                            ModSim.log.info("*******************ItemStack:" + want);
                            if (blockId != null) {
                                this.theBuilding.blockLocations.add(new V3(this.bx + this.xo, this.by + this.l, this.bz + this.zo, this.theFolk.location.theDimension));
                            }
                        } catch (Exception var15) {
                            want = "?";
                            ModSim.log.info("JobBuilder:wantItemStack 为空, wantIS 为空, blockID=" + blockId);
                        }
                    } else {
                        want = "???";
                    }

                    if (!alreadyPlaced && currBlockId != null) {
                        V3 blockToRemove = new V3(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                        this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                        this.mineBlockIntoChests(this.constructorChests, blockToRemove);
                        this.jobWorld.setBlock(this.bx + this.xo, this.by + this.l, this.bz + this.zo, Blocks.air, 0, 3);
                        this.theFolk.isWorking = true;
                    }

                    if (!alreadyPlaced) {
                        boolean gotBlock = false;
                        boolean requiredBlocks = blockId == Blocks.planks || blockId == Blocks.cobblestone || blockId == Blocks.glass || blockId == Blocks.wool || blockId == Blocks.brick_block || blockId == Blocks.dirt || blockId == Blocks.stonebrick || blockId == Blocks.fence || blockId == Blocks.stone || blockId == Blocks.log;
                        ItemStack got;
                        if (ModSim.gameMode == GameMode.NORMAL) {
                            if (requiredBlocks) {
                                this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                                got = inventoriesGet(this.constructorChests, new ItemStack(blockId, 1, 0), false, false);
                                if (got != null) {
                                    gotBlock = true;
                                } else {
                                    gotBlock = false;
                                }
                            } else {
                                gotBlock = true;
                            }
                        } else if (ModSim.gameMode == GameMode.CREATIVE) {
                            gotBlock = true;
                        } else if (ModSim.gameMode == GameMode.HARDCORE) {
                            if (blockId != null) {
                                if (blockId != Blocks.grass && blockId != Blocks.water && blockId != Blocks.water && blockId != Blocks.lava && blockId != Blocks.lava && blockId != Blocks.wall_sign && blockId != Blocks.cake && blockId != Blocks.stone_slab && blockId != Blocks.wooden_slab && blockId != Blocks.double_wooden_slab && blockId != Blocks.double_stone_slab && blockId != Blocks.farmland && blockId != Blocks.wooden_door && blockId != Blocks.iron_door && blockId != Blocks.bed) {
                                    this.constructorChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                                    got = inventoriesGet(this.constructorChests, new ItemStack(blockId, 1, 0), false, false);
                                    if (got != null) {
                                        gotBlock = true;
                                    } else {
                                        gotBlock = false;
                                    }

                                    if (blockId == BlockLoader.blockControlBox) {
                                        gotBlock = true;
                                    }
                                } else {
                                    gotBlock = true;
                                }
                            } else {
                                gotBlock = true;
                            }
                        }

                        if (!gotBlock) {
                            this.theStage = Stage.WAITINGFORRESOURCES;
                            if (want.toLowerCase().contentEquals("oak wood planks")) {
                                want = "Planks";
                            }

                            if (want.toLowerCase().contentEquals("oak wood")) {
                                want = "Logs";
                            }

                            this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_started_Waiting") + want;
                            if (System.currentTimeMillis() - this.lastNotifiedOfMaterials > (long) (ModSim.configMaterialReminderInterval * 60 * 1000)) {
                                this.lastNotifiedOfMaterials = System.currentTimeMillis();
                                ModSim.sendChat(this.theFolk.name + " ( " + I18n.format("container.sim.job.builder_constructor_started_who's") + this.theFolk.theBuilding.displayNameWithoutPK + ")" + I18n.format("container.sim.job.builder_constructor_started_more") + want);
                            }

                            this.step = 3;
                            return;
                        }

                        try {
                            if (!alreadyPlaced) {
                                try {
                                    if (blockId == BlockLoader.blockControlBox || blockId == BlockLoader.blockControlBox) {
                                        blockId = BlockLoader.blockControlBox;
                                    }

                                    if (blockId == BlockLoader.blockControlBox && this.theBuilding.displayNameWithoutPK.toLowerCase().contentEquals("sim-u-bank")) {
                                        subtype = 1;
                                    }

                                    this.theFolk.stayPut = true;
                                    this.jobWorld.setBlock(this.bx + this.xo, this.by + this.l, this.bz + this.zo, blockId, subtype, 3);
                                    this.jobWorld.markBlockForUpdate(this.bx + this.xo, this.by + this.l, this.bz + this.zo);
                                    int b4 = (int) Math.floor((double) this.theFolk.levelBuilder);
                                    if (this.theFolk.levelBuilder < 10.0F) {
                                        FolkData var10000 = this.theFolk;
                                        var10000.levelBuilder = (float)((double)var10000.levelBuilder + 0.001D / (double)b4);
                                    }

                                    int aft = (int)Math.floor((double)this.theFolk.levelBuilder);
                                    if (b4 != aft) {
                                        ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_levelled") + aft);
                                    }

                                    if (System.currentTimeMillis() - this.soundLastPlayed >= 2000L) {
                                        this.mc.theWorld.playSound((double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), ModSim.MODID + ":construction", 1.0F, 1.0F, false);
                                        this.soundLastPlayed = System.currentTimeMillis();
                                    }

                                    if (this.mc.theWorld.isRemote) {
                                        this.mc.theWorld.spawnParticle("explode", (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0.0D, 0.30000001192092896D, 0.0D);
                                        this.mc.theWorld.spawnParticle("explode", (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0.0D, 0.20000000298023224D, 0.0D);
                                        this.mc.theWorld.spawnParticle("explode", (double) (this.bx + this.xo), (double) (this.by + this.l), (double) (this.bz + this.zo), 0.0D, 0.10000000149011612D, 0.0D);
                                    }

                                    if (blockId != null && ModSim.gameMode != GameMode.CREATIVE) {
                                        GameStates var25 = ModSim.states;
                                        var25.credits -= 0.02F;
                                    }
                                } catch (Exception var18) {
                                    ModSim.log.warn("JobBuilder: 可能不存在的方块（来自其他模组）ID=" + blockId);

                                    try {
                                        this.jobWorld.setBlock(this.bx + this.xo, this.by + this.l, this.bz + this.zo, blockId, 0, 3);
                                    } catch (Exception var14) {
                                        var14.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception var19) {
                            var19.printStackTrace();
                        }
                    }

                    ++this.acount;
                    ++this.ltr;
                    if (this.ltr == this.theBuilding.ltrCount) {
                        this.ltr = 0;
                        ++this.ftb;
                        if (this.ftb == this.theBuilding.ftbCount) {
                            this.ftb = 0;
                            ++this.l;
                            if (this.l == this.theBuilding.layerCount) {
                                this.theStage = Stage.COMPLETE;
                                this.stageComplete();
                                return;
                            }
                        }
                    }

                    if (blockId != null && !alreadyPlaced) {
                        if (ModSim.gameMode == GameMode.CREATIVE) {
                            this.runDelay = 0;
                        } else {
                            this.runDelay = (int) (2000.0F / this.theFolk.levelBuilder);
                        }
                    } else {
                        this.runDelay = 0;
                    }

                    if (this.theFolk.theEntity != null) {
                        this.theFolk.theEntity.swingItem();
                    }
                } while(blockId == null || alreadyPlaced);
            }

        }
    }

    private void stageComplete() {
        this.theFolk.isWorking = false;
        if (this.theBuilding != null) {
            if (this.theBuilding.buildingComplete) {
            }

            if (this.theBuilding != null) {
                this.theBuilding.buildingComplete = true;
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.builder_constructor_completed") + this.theBuilding.displayNameWithoutPK);
                ModSim.proxy.getClientWorld().playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
                this.theBuilding.saveThisBuilding();
                this.theFolk.theBuilding = null;
            } else {
                ModSim.sendChat(I18n.format("container.sim.job.builder_constructor_Error") + this.theFolk.name + I18n.format("container.sim.job.builder_constructor_was_building"));
            }
        }

        if (this.theFolk.theEntity != null) {
            this.theFolk.theEntity.setSneaking(false);
        }

        this.theFolk.stayPut = false;
        this.theFolk.selfFire();
        this.theStage = Stage.IDLE;
        boolean activeBuilders = false;

        int b;
        for (b = 0; b < ModSim.theFolks.size(); ++b) {
            FolkData fd = (FolkData) ModSim.theFolks.get(b);
            if (fd.vocation == Vocation.BUILDER) {
                activeBuilders = true;
            }
        }

        if (!activeBuilders) {
            for (b = 0; b < ModSim.theBuildings.size(); ++b) {
                Building building = (Building) ModSim.theBuildings.get(b);
                building.buildingComplete = true;
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
            this.theFolk.statusText = I18n.format("container.sim.job.builder_constructor_site");
            this.theStage = Stage.BLUEPRINT;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

