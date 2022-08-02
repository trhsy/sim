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
import com.trhsy.sim.common.core.entity.functionality.MiningBox;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public FolkData theFolk =new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient int step = 1;
    private long timeSinceLastChestFullMessage = 0L;
    transient Long timeSinceLastGoto = 0L;
    transient List<IInventory> miningChests = null;
    String mineDir = "";
    //如果它是空的，则作为一个标志，表示我们正在垂直挖掘，或者水平方向+x-x+z-z
    String mineHorizontalDir = "";
    V3 vNextMineableBlock = null;
    transient boolean swingToggle = true;
    private boolean isChestsFull = false;
    private MiningBox theMiningBox;
    private String lastMinedBlockName = "";

    public JobMiner() {
    }

    /**
     * 重置工作
     */
    @Override
    public void resetJob() {
        try {
            this.theStage = Stage.IDLE;
            this.theFolk.isWorking = false;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("resetJob出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 职业矿工
     *
     * @param folk
     */
    public JobMiner(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            this.theMiningBox = MiningBox.getMiningBlockByBoxXYZ(folk.employedAt);
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
            /**
             * 看看我们是不是在水平挖掘
             */
            if (this.theMiningBox == null) {
                //采矿箱有个问题
                ModSimReloaded.sendChat(I18n.format("container.sim.job.miner.farmer.There") + this.theFolk.name + I18n.format("container.sim.job.miner.farmer.using"));
                this.theFolk.selfFire();
            } else {
                if (this.theMiningBox.marker1XYZ != null && this.theMiningBox.marker2XYZ == null) {
                    V3 xyz = this.theMiningBox.location;
                    int mbX = (int) xyz.x;
                    int mbZ = (int) xyz.z;
                    xyz = this.theMiningBox.marker1XYZ;
                    int mX = (int) xyz.x;
                    int mZ = (int) xyz.z;
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobMiner出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
                this.theFolk.action = FolkAction.WANDER;//游荡
                //今天的工作做完了
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Finished");
                this.theFolk.isWorking = false;
                return;
            }

            super.onUpdateGoingToWork(this.theFolk);
            //等待箱子
            if (this.theStage == Stage.WAITINGFORCHEST) {
                this.runDelay = 6000;
            }

            if (this.theStage == Stage.BEAMINGDOWN) {
                this.runDelay = 4000;
            }
            //挖矿
            if (this.theStage == Stage.MINING) {
                this.runDelay = (int) (2000.0F / this.theFolk.levelMiner);
                //是创造模式
                if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                    this.runDelay = 10;
                }
            }

            if (System.currentTimeMillis() - this.timeSinceLastRun < (long) this.runDelay) {
                return;
            }
            this.timeSinceLastRun = System.currentTimeMillis();
            //挖矿
            if (this.theFolk.vocation != Vocation.MINER) {
                this.theFolk.selfFire();
                return;
            }
            if (this.theStage == Stage.IDLE && ModSimReloaded.isDayTime()) {
                this.theStage = Stage.WAITINGFORCHEST;//等待箱子
            } else if (this.theStage == Stage.WAITINGFORCHEST) {
                this.stageWaitingForChest();
            } else if (this.theStage == Stage.BEAMINGDOWN) {
                this.stageBeamingDown();
            } else if (this.theStage == Stage.MINING) {
                this.stageMining();
            } else if (this.theStage == Stage.BEAMINGUP) {
                this.stageBeamingUp();
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobMiner-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 这可能需要在所有子类中保持一致？
     */
    @Override
    public void onArrivedAtWork() {
        try {
            int dist = 0;
            dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                //来到矿区
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Arrived");
                this.step = 1;
                this.theStage = Stage.WAITINGFORCHEST;
            } else {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    private void stageWaitingForChest() {
        try {
            this.theFolk.stayPut = true;
            this.theFolk.isWorking = false;
            if (this.isChestsFull) {
                if (System.currentTimeMillis() - this.timeSinceLastChestFullMessage > 120000L) {
                    //箱子已经装，满
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.stopped"));
                    this.timeSinceLastChestFullMessage = System.currentTimeMillis();
                }
                //所有的箱子都满了，请清空或添加更多的箱子
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.All");
                this.miningChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                ItemStack is = new ItemStack(Blocks.dirt, 1);
                Boolean placedOk = this.inventoriesPut(this.miningChests, is, true);
                if (placedOk) {
                    inventoriesGet(this.miningChests, is, false, false);
                    this.theFolk.getVillagerInventory().clear();
                    this.isChestsFull = false;
                    this.theStage = Stage.BEAMINGDOWN;
                    this.setNextMineableBlock();
                }
            } else {
                //检查储物箱......
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Checking");
                //检查主标记旁边的箱子
                this.miningChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                if (this.miningChests.size() == 0) {
                    //至少有一个附近的矿业盒
                    this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.mining");
                } else {
                    this.theStage = Stage.BEAMINGDOWN;
                    if (this.theFolk.theEntity != null) {
                        try {
                            if (this.theFolk.gender == 0) {
                                this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readym", 1, 1, false);
                            } else {
                                this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readyf", 1, 1, false);
                            }
                        } catch (Exception e) {
                            //切换维度时，playSound可以进行NPE
                        }
                    }
                }
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageWaitingForChest出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void stageBeamingDown() {
        try {
            if (this.vNextMineableBlock == null) {
                this.setNextMineableBlock();
            }

            if (this.vNextMineableBlock != null) {
                this.theFolk.updateLocationFromEntity();
                if (this.theFolk.location.getDistanceTo(this.vNextMineableBlock) < 10 || !(this.vNextMineableBlock.y <= 20)) {
                    this.theStage = Stage.MINING;
                    this.theFolk.stayPut = true;
                    return;
                } else {
                    if (this.step == 1) {
                        if (this.theFolk.beamingTo == null) {
                            //Beam me down, Scotty!
                            this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Beam");
                            if (this.theFolk.theEntity != null) {
                                if (this.theFolk.gender == 0) {
                                    this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":beamm", 1, 1, false);
                                } else {
                                    this.mc.theWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":beamf", 1, 1, false);
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
                }
            } else {
                return;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageBeamingDown出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 永远不要击中这个，需要在日落前把它们发射回来
     */
    private void stageBeamingUp() {
        try {
            this.theFolk.beamMeTo(this.theFolk.employedAt.clone());
            this.theStage = Stage.IDLE;
            this.theFolk.action = FolkAction.WANDER;
            //他们已经完成了挖掘矿井
            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.finished"));
            this.mc.theWorld.playSound(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, ModSim.MODID + ":cash", 1, 1, false);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageBeamingUp出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 设置下一个可开采区块 找到下一个可开采区块，并将其位置设置为类字段V3，以便对其进行开采
     */
    private void setNextMineableBlock() {
        try {
            int mx, my, mz;
            int xxx = 0, yyy = 0, zzz = 0;
            V3 m1 = this.theMiningBox.marker1XYZ;//三个标记棒位置
            V3 m2 = this.theMiningBox.marker2XYZ;
            V3 m3 = this.theMiningBox.marker3XYZ;
            if (m1 == null) {
                //矿井中遇到了一个问题。
                ModSimReloaded.sendChat(I18n.format("container.sim.job.miner.farmer.markers"));
                return;
            } else {
                if (this.mineHorizontalDir.contentEquals("")) {
                    mx = (int)m1.x;
                    my = (int) (m1.y - 1);
                    mz = (int)m1.z;
                    if (m2.x == m1.x) {
                        if (m2.z > mz) {
                            this.mineDir = "z+";
                        } else {
                            this.mineDir = "z-";
                        }
                    } else if (m2.z == m1.z) {
                        if (m2.x > mx) {
                            this.mineDir = "x+";
                        } else {
                            this.mineDir = "x-";
                        }
                    }

                    Block id = null;
                    int ftbCount = 0, ltrCount = 0;
                    int xo = 0, zo = 0;
                    if (m1.x == m2.x) {
                        ltrCount = (int) (Math.abs(m2.z - m1.z) - 1);
                    } else {
                        ltrCount = (int) (Math.abs(m2.x - m1.x) - 1);
                    }

                    if (m1.x == m3.x) {
                        ftbCount = (int) (Math.abs(m3.z - m1.z) - 1);
                    } else {
                        ftbCount = (int) (Math.abs(m3.x - m1.x) - 1);
                    }

                    gotABlock:
                    for (int l = my; l > 0; --l) {
                        for (int ftb = 0; ftb <= ftbCount; ftb++) {
                            for (int ltr = 1; ltr <= ltrCount; ltr++) {
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

                                id = this.jobWorld.getBlockState(new BlockPos(xxx, yyy, zzz)).getBlock();
                                if (id == Blocks.bedrock) {
                                    //因为该矿井现已达到基岩，从矿井底部返回地面。
                                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.bedrock"));
                                    this.theFolk.beamMeTo(this.theFolk.employedAt);
                                    this.theFolk.selfFire();
                                    return;
                                }

                                try {
                                    Block block = id;
                                    if (id != Blocks.air && id != Blocks.water && id != Blocks.water && id != Blocks.lava && id != Blocks.lava && !block.toString().toLowerCase().contains("oil")) {
                                        break gotABlock;
                                    }
                                } catch (Exception e) {
                                    break gotABlock;
                                }
                            }
                        }
                    }

                    try {
                        this.vNextMineableBlock = new V3((double) xxx, (double) yyy, (double) zzz, this.theFolk.employedAt.theDimension);
                    } catch (Exception e) {
                    }
                } else {
                    V3 vMine = new V3(m1.x, m1.y, m1.z, this.theFolk.employedAt.theDimension);

                    if (this.mineHorizontalDir.contentEquals("+x")) {
                        vMine.x++;
                        //vMine = new V3(vMine.x + 1, vMine.y, vMine.z, vMine.theDimension);
                    } else if (this.mineHorizontalDir.contentEquals("-x")) {
                        vMine.x--;
                        // vMine = new V3(vMine.x - 1, vMine.y, vMine.z, vMine.theDimension);
                    } else if (this.mineHorizontalDir.contentEquals("+z")) {
                        vMine.z++;
                        // vMine = new V3(vMine.x, vMine.y, vMine.z + 1, vMine.theDimension);
                    } else if (this.mineHorizontalDir.contentEquals("-z")) {
                        vMine.z--;
                        //vMine = new V3(vMine.x, vMine.y, vMine.z - 1, vMine.theDimension);
                    }

                    V3 vMineable = null;
                    Block id = null;
                    int meta = 0, xo = 0, yo = 0, zo = 0;
                    boolean flagFound = false;

                    gotABlock2:
                    for (int ftb = 0; ftb < 1024; ++ftb) {
                        for (int btt = 0; btt < this.theMiningBox.size; btt++) {
                            for (int ltr = 0; ltr < this.theMiningBox.size; ltr++) {
                                if (this.mineHorizontalDir.contentEquals("+x")) {
                                    xo = ftb;
                                    zo = ltr;
                                } else if (this.mineHorizontalDir.contentEquals("-x")) {
                                    xo = -ftb;
                                    zo = -ltr;
                                } else if (this.mineHorizontalDir.contentEquals("+z")) {
                                    xo = -ltr;
                                    zo = ftb;
                                } else if (this.mineHorizontalDir.contentEquals("-z")) {
                                    xo = ltr;
                                    zo = -ftb;
                                }

                                yo = btt;

                                try {
                                    if (this.theFolk.employedAt == null) {
                                        this.theStage = Stage.IDLE;
                                        return;
                                    }

                                    vMineable = new V3(vMine.x + (double) xo, vMine.y + (double) yo, vMine.z + (double) zo, this.theFolk.employedAt.theDimension);

                                    id = this.jobWorld.getBlockState(new BlockPos(vMineable.x, vMineable.y, vMineable.z)).getBlock();
                                    if (ftb % 10 == 0 && (double) btt == Math.floor((double) (this.theMiningBox.size / 2)) && ltr == 0) {
                                        V3 lightbox = vMineable.clone();

                                        if (this.mineHorizontalDir.contentEquals("+x")) {
                                            lightbox.z--;
                                            //lightbox = new V3(lightbox.x, lightbox.y, lightbox.z - 1, lightbox.theDimension);
                                        } else if (this.mineHorizontalDir.contentEquals("-x")) {
                                            lightbox.z++;
                                            //lightbox = new V3(lightbox.x, lightbox.y, lightbox.z + 1, lightbox.theDimension);
                                        } else if (this.mineHorizontalDir.contentEquals("+z")) {
                                            lightbox.x++;
                                            // lightbox = new V3(lightbox.x + 1, lightbox.y, lightbox.z, lightbox.theDimension);
                                        } else if (this.mineHorizontalDir.contentEquals("-z")) {
                                            lightbox.x--;
                                            //lightbox = new V3(lightbox.x - 1, lightbox.y, lightbox.z, lightbox.theDimension);
                                        }

                                        Block lbid = this.jobWorld.getBlockState(new BlockPos(lightbox.x, lightbox.y, lightbox.z)).getBlock();
                                        if (this.miningChests.size() > 0 && lbid != BlockLoader.blockLightBox) {
                                            ItemStack light = null;

                                            for (int lightmeta = 0; light == null && lightmeta < 8; ++lightmeta) {
                                                light = inventoriesGet(this.miningChests, new ItemStack(BlockLoader.blockLightBox, 1, lightmeta), false, true);
                                            }

                                            if (light != null) {
                                                ModSimReloaded.log.info("灯箱放置在 " + lightbox.toString());
                                                BlockPos blockPos1 = new BlockPos(lightbox.x, lightbox.y, lightbox.z);
                                                this.jobWorld.setBlockState(blockPos1, BlockLoader.blockLightBox.getDefaultState(), 3);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    //var27.printStackTrace();
                                }

                                if (id == Blocks.bedrock) {
                                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.retired"));
                                    this.theFolk.beamMeTo(this.theFolk.employedAt);
                                    this.theFolk.selfFire();
                                    return;
                                }

                                try {
                                    Block block = id;
                                    if (id != null && id != Blocks.water && id != Blocks.water && id != Blocks.lava && id != Blocks.lava && !id.toString().toLowerCase().contains("oil")) {
                                        flagFound = true;
                                        break gotABlock2;
                                    }
                                } catch (Exception e) {
                                    flagFound = true;
                                    break gotABlock2;
                                }
                            }
                        }
                    }

                    if (!flagFound) {
                        //因为水平矿井已达到1公里极限。如果你需要一个较长的矿井，则重新设置一个挖掘点。
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.horizontal"));
                        this.theFolk.beamMeTo(this.theFolk.employedAt);
                        this.theFolk.isWorking = false;
                        this.theFolk.selfFire();
                        return;
                    }

                    this.vNextMineableBlock = vMineable.clone();
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setNextMineableBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    /**
     * 挖矿
     */
    private void stageMining() {
        try {
            this.theFolk.isWorking = true;
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.dimension = this.theFolk.employedAt.theDimension;
            } else {
                this.theFolk.location.theDimension = this.theFolk.employedAt.theDimension;
            }
            //到达工作地点
            this.theFolk.action = FolkAction.ATWORK;
            //是孕期
            if (this.theFolk.isSpawned() && System.currentTimeMillis() - this.timeSinceLastGoto > 7000L) {
                this.theFolk.updateLocationFromEntity();//从实体更新位置
                if (this.theFolk.location.y - this.vNextMineableBlock.y > 4) {
                    this.vNextMineableBlock.doNotTimeout = false;
                    if (this.vNextMineableBlock.y > 20) {
                        this.theFolk.gotoXYZ(this.vNextMineableBlock, null);
                    } else {
                        this.theFolk.stayPut = true;
                    }
                } else {
                    this.vNextMineableBlock.doNotTimeout = true;
                    if (this.vNextMineableBlock.y > 20) {
                        this.theFolk.stayPut = false;
                        this.theFolk.gotoXYZ(this.vNextMineableBlock, null);//行走
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
                    for (int d = 0; d < 5; ++d) {
                        try {
                            JobMiner.this.jobWorld.playSound(vNextMineableBlock.x, vNextMineableBlock.y, vNextMineableBlock.z, "dig.stone", 1, 1, false);
                        } catch (Exception e) {
                        }

                        try {
                            Thread.sleep(100L);
                        } catch (Exception e) {
                        }
                    }

                }
            });
            t.start();
            if (this.lastMinedBlockName.contentEquals("")) {
                //挖掘挖掘
                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Diggy");
            }
            BlockPos blockPos = new BlockPos(this.vNextMineableBlock.x, this.vNextMineableBlock.y, this.vNextMineableBlock.z);
            Block blockid = this.jobWorld.getBlockState(blockPos).getBlock();
            int id = Block.getIdFromBlock(blockid);

            int idmeta = blockid.getMetaFromState(this.jobWorld.getBlockState(new BlockPos(blockPos)));
            if (this.jobWorld != null) {
                List<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, this.vNextMineableBlock);//开采时平移块体
                BlockPos blockPos1 = new BlockPos(this.vNextMineableBlock.x, this.vNextMineableBlock.y, this.vNextMineableBlock.z);
                this.jobWorld.setBlockState(blockPos1, Blocks.air.getDefaultState(), 3);
                if (this.theFolk.theEntity != null) {
                    try {
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) this.vNextMineableBlock.x, (double) this.vNextMineableBlock.y, (double) this.vNextMineableBlock.z, 0.1f, 0.3f, 0);
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) this.vNextMineableBlock.x, (double) this.vNextMineableBlock.y, (double) this.vNextMineableBlock.z, 0, 0.2f, 0);
                        this.mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) this.vNextMineableBlock.x, (double) this.vNextMineableBlock.y, (double) this.vNextMineableBlock.z, 0, 0.1f, 0.1f);
                    } catch (Exception e) {
                    }
                }

                if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= 0.012F;
                    int b4 = (int) Math.floor((double) this.theFolk.levelMiner);
                    if (this.theFolk.levelMiner < 10.0F) {//挖掘等级小于10加等级
                        FolkData var16 = this.theFolk;
                        this.theFolk.levelMiner += (0.001 / b4);
                    }

                    int aft = (int) Math.floor((double) this.theFolk.levelMiner);
                    if (b4 != aft) {
                        //刚刚矿工等级提升了
                        ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.miner.farmer.levelled") + aft);
                    }
                } else {
                    this.theFolk.levelMiner = 10.0F;
                }

                if (this.theFolk.employedAt != null) {//要去的地方不是空
                    if (this.theFolk.employedAt.y - this.vNextMineableBlock.y > 3) {
                        if (this.theMiningBox.addGlassCover && this.mineHorizontalDir.contentEquals("")) {//加上玻璃

                            Block gid = this.jobWorld.getBlockState(new BlockPos(this.vNextMineableBlock.x, this.theFolk.employedAt.y, this.vNextMineableBlock.z)).getBlock();
                            if (gid == null && this.miningChests.size() > 0) {
                                ItemStack glass = inventoriesGet(this.miningChests, new ItemStack(Blocks.glass, 1), false, false);
                                if (glass != null) {
                                    BlockPos blockPos2 = new BlockPos(this.vNextMineableBlock.x, this.theFolk.employedAt.y, this.vNextMineableBlock.z);
                                    this.jobWorld.setBlockState(blockPos2, Blocks.glass.getDefaultState(), 3);
                                }
                            }
                        } else {
                            BlockPos blockPos2 = new BlockPos(this.vNextMineableBlock.x, this.theFolk.employedAt.y, this.vNextMineableBlock.z);
                            this.jobWorld.setBlockState(blockPos2, Blocks.air.getDefaultState(), 2);
                        }
                    }
                    //看看我们是想保留还是放弃这个街区
                    boolean keep = false;
                    if (this.theMiningBox.discards == 0) {
                        keep = true;//保留
                    }

                    if (this.theMiningBox.discards == 1) {
                        if (id != Block.getIdFromBlock(Blocks.dirt) && id != Block.getIdFromBlock(Blocks.grass)) {
                            keep = true;
                        } else {
                            keep = false;
                        }
                    }

                    if (this.theMiningBox.discards == 2) {
                        if (id != Block.getIdFromBlock(Blocks.dirt) || id != Block.getIdFromBlock(Blocks.grass) || id != Block.getIdFromBlock(Blocks.stone) || id != Block.getIdFromBlock(Blocks.cobblestone)) {
                            keep = true;
                        } else {
                            keep = false;
                        }
                    }

                    if (this.theMiningBox.discards == 3) {
                        if (id != Block.getIdFromBlock(Blocks.dirt) || id != Block.getIdFromBlock(Blocks.grass) || id != Block.getIdFromBlock(Blocks.sand)) {
                            keep = true;
                        } else {
                            keep = false;
                        }
                    }

                    if (this.theMiningBox.discards == 4) {
                        if (id != Block.getIdFromBlock(Blocks.dirt) || id != Block.getIdFromBlock(Blocks.grass) || id != Block.getIdFromBlock(Blocks.stone) || id != Block.getIdFromBlock(Blocks.cobblestone) || id != Block.getIdFromBlock(Blocks.sand)) {
                            keep = true;
                        } else {
                            keep = false;
                        }
                    }

                    try {
                        Block block = Block.getBlockById(id);
                        if (id == Block.getIdFromBlock(Blocks.water) || id == Block.getIdFromBlock(Blocks.water) || id == Block.getIdFromBlock(Blocks.lava) || id == Block.getIdFromBlock(Blocks.lava) || id == Block.getIdFromBlock(Blocks.tallgrass) || block.toString().toLowerCase().contains("oil")) {
                            keep = false;
                        }
                    } catch (Exception e) {
                        keep = false;
                    }

                    boolean placedOk = true;
                    if (keep && minedStacks != null) {
                        this.miningChests = Job.inventoriesFindClosest(this.theFolk.employedAt, 5);//最近的箱子

                        for (int s = 0; s < minedStacks.size(); ++s) {
                            ItemStack stack = (ItemStack) minedStacks.get(s);
                            if (stack != null) {
                                this.lastMinedBlockName = stack.getDisplayName();
                                //挖掘挖掘
                                this.theFolk.statusText = I18n.format("container.sim.job.miner.farmer.Diggy") + this.lastMinedBlockName + "!";
                                placedOk = this.inventoriesPut(this.miningChests, stack, false);
                            }
                        }
                    }

                    if (!placedOk) {
                        this.isChestsFull = true;
                        this.theStage = Stage.WAITINGFORCHEST;
                        this.theFolk.getVillagerInventory().clear();
                        this.theFolk.getVillagerInventory().setInventorySlotContents(0, new ItemStack(Block.getBlockById(id), idmeta, 1));
                    }

                    this.setNextMineableBlock();
                } else {
                    return;
                }
            } else {
                return;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageMining出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

}

