package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.MiningBox;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ========================================
 *
 * @ClassName JobMiner
 * @Description todo 矿工
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:52
 * ========================================
 **/
public class JobMiner extends Job implements Serializable {

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient int step = 1;
    private long timeSinceLastChestFullMessage = 0L;
    transient Long timeSinceLastGoto = 0L;
    transient ArrayList<IInventory> miningChests = null;
    String mineDir = "";
    String mineHorizontalDir = "";
    V3 vNextMineableBlock = null;
    transient boolean swingToggle = true;
    private boolean isChestsFull = false;
    private MiningBox theMiningBox;
    private String lastMinedBlockName = "";

    public JobMiner() {
    }

    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }

    public JobMiner(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            this.theStage = Stage.IDLE;
        }

        this.theMiningBox = MiningBox.getMiningBlockByBoxXYZ(folk.employedAt);
        if (this.theFolk.destination == null) {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

        if (this.theMiningBox == null) {
            ModSim.sendChat(I18n.format("container.sim.job.miner.farmer.There") + this.theFolk.name + I18n.format("container.sim.job.miner.farmer.using"));
            this.theFolk.selfFire();
        } else {
            if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                V3 xyz = this.theMiningBox.location;
                int mbX = xyz.x.intValue();
                int mbZ = xyz.z.intValue();
                xyz = this.theMiningBox.marker1XYZ;
                int mX = xyz.x.intValue();
                int mZ = xyz.z.intValue();
                if (mbX < mX) {
                    this.mineHorizontalDir = "+x";
                } else if (mbX > mX) {
                    this.mineHorizontalDir = "-x";
                } else if (mbZ < mZ) {
                    this.mineHorizontalDir = "+z";
                } else if (mbZ > mZ) {
                    this.mineHorizontalDir = "-z";
                }
            }

        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        try {
            if (!ModSim.isDayTime()) {
                this.theStage = Stage.IDLE;
                this.theFolk.action = FolkAction.WANDER;
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Finished");
                this.theFolk.isWorking = false;
                return;
            }

            super.onUpdateGoingToWork(this.theFolk);
        } catch (Exception var2) {
        }

        if (this.theStage == Stage.WAITINGFORCHEST) {
            this.runDelay = 6000;
        }

        if (this.theStage == Stage.BEAMINGDOWN) {
            this.runDelay = 4000;
        }

        if (this.theStage == Stage.MINING) {
            this.runDelay = (int)(2000.0F / this.theFolk.levelMiner);
            if (ModSim.gameMode == GameMode.CREATIVE) {
                this.runDelay = 10;
            }
        }

        if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
            this.timeSinceLastRun = System.currentTimeMillis();
            if (this.theFolk.vocation != Vocation.MINER) {
                this.theFolk.selfFire();
            } else {
                if (this.theStage == Stage.IDLE && ModSim.isDayTime()) {
                    this.theStage = Stage.WAITINGFORCHEST;
                } else if (this.theStage == Stage.WAITINGFORCHEST) {
                    this.stageWaitingForChest();
                } else if (this.theStage == Stage.BEAMINGDOWN) {
                    this.stageBeamingDown();
                } else if (this.theStage == Stage.MINING) {
                    this.stageMining();
                } else if (this.theStage == Stage.BEAMINGUP) {
                    this.stageBeamingUp();
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
            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Arrived");
            this.step = 1;
            this.theStage = Stage.WAITINGFORCHEST;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

    private void stageWaitingForChest() {
        this.theFolk.stayPut = true;
        this.theFolk.isWorking = false;
        if (this.isChestsFull) {
            if (System.currentTimeMillis() - this.timeSinceLastChestFullMessage > 120000L) {
                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.stopped"));
                this.timeSinceLastChestFullMessage = System.currentTimeMillis();
            }

            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.All");
            this.miningChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            ItemStack is = new ItemStack(Blocks.dirt, 1);
            Boolean placedOk = this.inventoriesPut(this.miningChests, is, true);
            if (placedOk) {
                inventoriesGet(this.miningChests, is, false, false);
                this.theFolk.inventory.clear();
                this.isChestsFull = false;
                this.theStage = Stage.BEAMINGDOWN;
                this.setNextMineableBlock();
            }
        } else {
            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Checking");
            this.miningChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.miningChests.size() == 0) {
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.mining");
            } else {
                this.theStage = Stage.BEAMINGDOWN;
                if (this.theFolk.theEntity != null) {
                    try {
                        if (this.theFolk.gender == 0) {
                            this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readym", 1.0F, 1.0F, false);
                        } else {
                            this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readyf", 1.0F, 1.0F, false);
                        }
                    } catch (Exception var3) {
                    }
                }
            }
        }

    }

    private void stageBeamingDown() {
        if (this.vNextMineableBlock == null) {
            this.setNextMineableBlock();
        }

        if (this.vNextMineableBlock != null) {
            this.theFolk.updateLocationFromEntity();
            if (this.theFolk.location.getDistanceTo(this.vNextMineableBlock) >= 10 && !(this.vNextMineableBlock.y <= 20.0D)) {
                if (this.step == 1) {
                    if (this.theFolk.beamingTo == null) {
                        this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Beam");
                        if (this.theFolk.theEntity != null) {
                            if (this.theFolk.gender == 0) {
                                this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":beamm", 1.0F, 1.0F, false);
                            } else {
                                this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":beamf", 1.0F, 1.0F, false);
                            }
                        }

                        this.theFolk.stayPut = true;
                        this.theFolk.beamMeTo(this.vNextMineableBlock.clone());
                        this.step = 2;
                    }
                } else if (this.step == 2 && this.theFolk.destination == null) {
                    this.theStage = Stage.MINING;
                    return;
                }

            } else {
                this.theStage = Stage.MINING;
                this.theFolk.stayPut = true;
            }
        }
    }

    private void stageBeamingUp() {
        this.theFolk.beamMeTo(this.theFolk.employedAt.clone());
        this.theStage = Stage.IDLE;
        this.theFolk.action = FolkAction.WANDER;
        ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.finished"));
        this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1.0F, 1.0F, false);
    }

    private void setNextMineableBlock() {
        int xxx = 0;
        int yyy = 0;
        int zzz = 0;
        V3 m1 = this.theMiningBox.marker1XYZ;
        V3 m2 = this.theMiningBox.marker2XYZ;
        V3 m3 = this.theMiningBox.marker3XYZ;
        if (m1 == null) {
            ModSim.sendChat(I18n.format("container.sim.job.miner.farmer.markers"));
        } else {
            boolean ltrCount;
            int xo;
            int zo;
            int l;
            int ltr;
            int meta;
            if (this.mineHorizontalDir.contentEquals("")) {
                int mx = m1.x.intValue();
                int my = m1.y.intValue() - 1;
                int mz = m1.z.intValue();
                if (m2.x.intValue() == m1.x.intValue()) {
                    if (m2.z.intValue() > mz) {
                        this.mineDir = "z+";
                    } else {
                        this.mineDir = "z-";
                    }
                } else if (m2.z.intValue() == m1.z.intValue()) {
                    if (m2.x.intValue() > mx) {
                        this.mineDir = "x+";
                    } else {
                        this.mineDir = "x-";
                    }
                }

                Block id = null;
                //int idmeta = false;
                //int ftbCount = false;
                ltrCount = false;
                xo = 0;
                zo = 0;
                if (m1.x.intValue() == m2.x.intValue()) {
                    meta = Math.abs(m2.z.intValue() - m1.z.intValue()) - 1;
                } else {
                    meta = Math.abs(m2.x.intValue() - m1.x.intValue()) - 1;
                }

                int ftbCount;
                if (m1.x.intValue() == m3.x.intValue()) {
                    ftbCount = Math.abs(m3.z.intValue() - m1.z.intValue()) - 1;
                } else {
                    ftbCount = Math.abs(m3.x.intValue() - m1.x.intValue()) - 1;
                }

                label187:
                for(l = my; l > 0; --l) {
                    for(int ftb = 0; ftb <= ftbCount; ++ftb) {
                        for(ltr = 1; ltr <= meta; ++ltr) {
                            if (this.mineDir.contentEquals("x+")) {
                                xo = ltr;
                                zo = -ftb;
                            } else if (this.mineDir.contentEquals("x-")) {
                                xo = -ltr;
                                zo = ftb;
                            } else if (this.mineDir.contentEquals("z+")) {
                                xo = ftb;
                                zo = ltr;
                            } else if (this.mineDir.contentEquals("z-")) {
                                xo = -ftb;
                                zo = -ltr;
                            }

                            xxx = mx + xo;
                            yyy = l;
                            zzz = mz + zo;
                            id = this.jobWorld.getBlock(xxx, l, zzz);
                            this.jobWorld.getBlockMetadata(xxx, l, zzz);
                            if (id == Blocks.bedrock) {
                                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.bedrock"));
                                this.theFolk.beamMeTo(this.theFolk.employedAt);
                                this.theFolk.selfFire();
                                return;
                            }

                            try {
                                if (id != null && id != Blocks.water && id != Blocks.water && id != Blocks.lava && id != Blocks.lava && !id.toString().toLowerCase().contains("oil")) {
                                    break label187;
                                }
                            } catch (Exception var26) {
                                break label187;
                            }
                        }
                    }
                }

                try {
                    this.vNextMineableBlock = new V3((double)xxx, (double)yyy, (double)zzz, this.theFolk.employedAt.theDimension);
                } catch (Exception var25) {
                }
            } else {
                V3 vMine = new V3(m1.x, m1.y, m1.z, this.theFolk.employedAt.theDimension);
                Double var32;
                Double var34;
                if (this.mineHorizontalDir.contentEquals("+x")) {
                    var32 = vMine.x;
                    var34 = vMine.x = vMine.x + 1.0D;
                } else if (this.mineHorizontalDir.contentEquals("-x")) {
                    var32 = vMine.x;
                    var34 = vMine.x = vMine.x - 1.0D;
                } else if (this.mineHorizontalDir.contentEquals("+z")) {
                    var32 = vMine.z;
                    var34 = vMine.z = vMine.z + 1.0D;
                } else if (this.mineHorizontalDir.contentEquals("-z")) {
                    var32 = vMine.z;
                    var34 = vMine.z = vMine.z - 1.0D;
                }

                V3 vMineable = new V3();
                Block id = null;
                ltrCount = false;
                xo = 0;
                //int yo = false;
                l = 0;
                boolean flagFound = false;

                label240:
                for(ltr = 0; ltr < 1024; ++ltr) {
                    for(int btt = 0; btt < this.theMiningBox.size; ++btt) {
                        for(int ltt = 0; ltt < this.theMiningBox.size; ++ltt) {
                            if (this.mineHorizontalDir.contentEquals("+x")) {
                                xo = ltr;
                                l = ltt;
                            } else if (this.mineHorizontalDir.contentEquals("-x")) {
                                xo = -ltr;
                                l = -ltt;
                            } else if (this.mineHorizontalDir.contentEquals("+z")) {
                                xo = -ltr;
                                l = ltt;
                            } else if (this.mineHorizontalDir.contentEquals("-z")) {
                                xo = ltr;
                                l = -ltt;
                            }

                            zo = btt;

                            try {
                                if (this.theFolk.employedAt == null) {
                                    this.theStage = Stage.IDLE;
                                    return;
                                }

                                vMineable = new V3(vMine.x + (double)xo, vMine.y + (double)zo, vMine.z + (double)l, this.theFolk.employedAt.theDimension);
                                id = this.jobWorld.getBlock(vMineable.x.intValue(), vMineable.y.intValue(), vMineable.z.intValue());
                                meta = this.jobWorld.getBlockMetadata(vMineable.x.intValue(), vMineable.y.intValue(), vMineable.z.intValue());
                                if (ltr % 10 == 0 && (double)btt == Math.floor((double)(this.theMiningBox.size / 2)) && ltr == 0) {
                                    V3 lightbox = vMineable.clone();
                                    Double var23;
                                    Double var24;
                                    if (this.mineHorizontalDir.contentEquals("+x")) {
                                        var23 = lightbox.z;
                                        var24 = lightbox.z = lightbox.z - 1.0D;
                                    } else if (this.mineHorizontalDir.contentEquals("-x")) {
                                        var23 = lightbox.z;
                                        var24 = lightbox.z = lightbox.z + 1.0D;
                                    } else if (this.mineHorizontalDir.contentEquals("+z")) {
                                        var23 = lightbox.x;
                                        var24 = lightbox.x = lightbox.x + 1.0D;
                                    } else if (this.mineHorizontalDir.contentEquals("-z")) {
                                        var23 = lightbox.x;
                                        var24 = lightbox.x = lightbox.x - 1.0D;
                                    }

                                    Block lbid = this.jobWorld.getBlock(lightbox.x.intValue(), lightbox.y.intValue(), lightbox.z.intValue());
                                    if (this.miningChests.size() > 0 && lbid != BlockLoader.blockLightBox) {
                                        ItemStack light = null;

                                        for (int lightmeta = 0; light == null && lightmeta < 8; ++lightmeta) {
                                            light = inventoriesGet(this.miningChests, new ItemStack(BlockLoader.blockLightBox, 1, lightmeta), false, true);
                                        }

                                        if (light != null) {
                                            ModSim.log.info("灯箱放置在 " + lightbox.toString());
                                            this.jobWorld.setBlock(lightbox.x.intValue(), lightbox.y.intValue(), lightbox.z.intValue(), BlockLoader.blockLightBox, light.getMetadata(), 3);
                                        }
                                    }
                                }
                            } catch (Exception var27) {
                                var27.printStackTrace();
                            }

                            if (id == Blocks.bedrock) {
                                ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.retired"));
                                this.theFolk.beamMeTo(this.theFolk.employedAt);
                                this.theFolk.selfFire();
                                return;
                            }

                            try {
                                if (id != null && id != Blocks.water && id != Blocks.water && id != Blocks.lava && id != Blocks.lava && !id.toString().toLowerCase().contains("oil")) {
                                    flagFound = true;
                                    break label240;
                                }
                            } catch (Exception var28) {
                                flagFound = true;
                                break label240;
                            }
                        }
                    }
                }

                if (!flagFound) {
                    ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.horizontal"));
                    this.theFolk.beamMeTo(this.theFolk.employedAt);
                    this.theFolk.isWorking = false;
                    this.theFolk.selfFire();
                    return;
                }

                this.vNextMineableBlock = vMineable.clone();
            }

        }
    }

    private void stageMining() {
        this.theFolk.isWorking = true;
        if (this.theFolk.theEntity != null) {
            this.theFolk.theEntity.dimension = this.theFolk.employedAt.theDimension;
        } else {
            this.theFolk.location.theDimension = this.theFolk.employedAt.theDimension;
        }

        this.theFolk.action = FolkAction.ATWORK;
        if (this.theFolk.isSpawned() && System.currentTimeMillis() - this.timeSinceLastGoto > 7000L) {
            this.theFolk.updateLocationFromEntity();
            if (this.theFolk.location.y - this.vNextMineableBlock.y > 4.0D) {
                this.vNextMineableBlock.doNotTimeout = false;
                if (this.vNextMineableBlock.y > 20.0D) {
                    this.theFolk.gotoXYZ(this.vNextMineableBlock, GotoMethod.BEAM);
                } else {
                    this.theFolk.stayPut = true;
                }
            } else {
                this.vNextMineableBlock.doNotTimeout = true;
                if (this.vNextMineableBlock.y > 20.0D) {
                    this.theFolk.stayPut = false;
                    this.theFolk.gotoXYZ(this.vNextMineableBlock, GotoMethod.WALK);
                } else {
                    this.theFolk.stayPut = true;
                }
            }

            this.timeSinceLastGoto = System.currentTimeMillis();
            this.theFolk.timeStartedGotoing = System.currentTimeMillis();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int d = 0; d < 5; ++d) {
                    try {
                        jobWorld.playSound(vNextMineableBlock.x, vNextMineableBlock.y, vNextMineableBlock.z, "dig.stone", 1.0F, 1.0F, false);
                    } catch (Exception var4) {
                    }

                    try {
                        Thread.sleep(100L);
                    } catch (Exception var3) {
                    }
                }

            }
        });
        t.start();
        if (this.lastMinedBlockName.contentEquals("")) {
            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Diggy");
        }

        Block id = null;
        //int idmeta = false;
        id = this.jobWorld.getBlock(this.vNextMineableBlock.x.intValue(), this.vNextMineableBlock.y.intValue(), this.vNextMineableBlock.z.intValue());
        int idmeta = this.jobWorld.getBlockMetadata(this.vNextMineableBlock.x.intValue(), this.vNextMineableBlock.y.intValue(), this.vNextMineableBlock.z.intValue());
        if (this.jobWorld != null) {
            ArrayList<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, this.vNextMineableBlock);
            this.jobWorld.setBlock(this.vNextMineableBlock.x.intValue(), this.vNextMineableBlock.y.intValue(), this.vNextMineableBlock.z.intValue(), Blocks.air, 0, 3);
            if (this.theFolk.theEntity != null) {
                try {
                    this.mc.theWorld.spawnParticle("explode", (double) this.vNextMineableBlock.x.intValue(), (double) this.vNextMineableBlock.y.intValue(), (double) this.vNextMineableBlock.z.intValue(), 0.10000000149011612D, 0.30000001192092896D, 0.0D);
                    this.mc.theWorld.spawnParticle("explode", (double) this.vNextMineableBlock.x.intValue(), (double) this.vNextMineableBlock.y.intValue(), (double) this.vNextMineableBlock.z.intValue(), 0.0D, 0.20000000298023224D, 0.0D);
                    this.mc.theWorld.spawnParticle("explode", (double) this.vNextMineableBlock.x.intValue(), (double) this.vNextMineableBlock.y.intValue(), (double) this.vNextMineableBlock.z.intValue(), 0.0D, 0.10000000149011612D, 0.10000000149011612D);
                } catch (Exception var9) {
                }
            }

            if (ModSim.gameMode != GameMode.CREATIVE) {
                GameStates var10000 = ModSim.states;
                var10000.credits -= 0.012F;
                int b4 = (int) Math.floor((double) this.theFolk.levelMiner);
                if (this.theFolk.levelMiner < 10.0F) {
                    FolkData var16 = this.theFolk;
                    var16.levelMiner = (float) ((double) var16.levelMiner + 0.001D / (double) b4);
                }

                int aft = (int) Math.floor((double) this.theFolk.levelMiner);
                if (b4 != aft) {
                    ModSim.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.levelled") + aft);
                }
            } else {
                this.theFolk.levelMiner = 10.0F;
            }

            if (this.theFolk.employedAt != null) {
                if (this.theFolk.employedAt.y - this.vNextMineableBlock.y > 3.0D) {
                    if (this.theMiningBox.addGlassCover && this.mineHorizontalDir.contentEquals("")) {
                        Block gid = this.jobWorld.getBlock(this.vNextMineableBlock.x.intValue(), this.theFolk.employedAt.y.intValue(), this.vNextMineableBlock.z.intValue());
                        if (gid == null && this.miningChests.size() > 0) {
                            ItemStack glass = inventoriesGet(this.miningChests, new ItemStack(Blocks.glass, 1), false, false);
                            if (glass != null) {
                                this.jobWorld.setBlock(this.vNextMineableBlock.x.intValue(), this.theFolk.employedAt.y.intValue(), this.vNextMineableBlock.z.intValue(), Blocks.glass, 0, 3);
                            }
                        }
                    } else {
                        this.jobWorld.setBlock(this.vNextMineableBlock.x.intValue(), this.theFolk.employedAt.y.intValue(), this.vNextMineableBlock.z.intValue(), Blocks.air, 0, 3);
                    }
                }

                boolean keep = false;
                if (this.theMiningBox.discards == 0) {
                    keep = true;
                }

                if (this.theMiningBox.discards == 1) {
                    if (id != Blocks.dirt && id != Blocks.grass) {
                        keep = true;
                    } else {
                        keep = false;
                    }
                }

                if (this.theMiningBox.discards == 2) {
                    if (id != Blocks.dirt && id != Blocks.grass && id != Blocks.stone && id != Blocks.cobblestone) {
                        keep = true;
                    } else {
                        keep = false;
                    }
                }

                if (this.theMiningBox.discards == 3) {
                    if (id != Blocks.dirt && id != Blocks.grass && id != Blocks.sand) {
                        keep = true;
                    } else {
                        keep = false;
                    }
                }

                if (this.theMiningBox.discards == 4) {
                    if (id != Blocks.dirt && id != Blocks.grass && id != Blocks.stone && id != Blocks.cobblestone && id != Blocks.sand) {
                        keep = true;
                    } else {
                        keep = false;
                    }
                }

                try {
                    if (id == Blocks.water || id == Blocks.water || id == Blocks.lava || id == Blocks.lava || id == Blocks.tallgrass || id.toString().toLowerCase().contains("oil")) {
                        keep = false;
                    }
                } catch (Exception var10) {
                    keep = false;
                }

                boolean placedOk = true;
                if (keep && minedStacks != null) {
                    this.miningChests = Job.inventoriesFindClosest(this.theFolk.employedAt, 5);

                    for(int s = 0; s < minedStacks.size(); ++s) {
                        ItemStack stack = (ItemStack)minedStacks.get(s);
                        if (stack != null) {
                            this.lastMinedBlockName = stack.getDisplayName();
                            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Diggy") + this.lastMinedBlockName + "!";
                            placedOk = this.inventoriesPut(this.miningChests, stack, false);
                        }
                    }
                }

                if (!placedOk) {
                    this.isChestsFull = true;
                    this.theStage = Stage.WAITINGFORCHEST;
                    this.theFolk.inventory.clear();
                    this.theFolk.inventory.add(new ItemStack(id, idmeta, 1));
                }

                this.setNextMineableBlock();
            }
        }
    }

}

