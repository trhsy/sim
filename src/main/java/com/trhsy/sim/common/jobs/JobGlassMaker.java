package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public FolkData theFolk = new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    //找到沙子地点
    private transient V3 blockOfSand = null;
    private transient List<IInventory> factoryChests = new CopyOnWriteArrayList();
    private transient TileEntityFurnace factoryFurnace = null;
    private long lastGotocmd = 0L;
    private int gotoCount = 0;

    public JobGlassMaker() {
    }

    public JobGlassMaker(FolkData folk) {
        try {
            this.theFolk = folk;
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord+0.5, this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                    //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("JobGlassMaker出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
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
                this.theStage = Stage.SCANFORSAND;
            } else {
                if (this.theStage != Stage.COLLECTSAND && this.theStage != Stage.GOTOSANDBLOCK && this.theStage != Stage.SCANFORSAND) {
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
                        //扫描沙子
                        if (this.theStage == Stage.SCANFORSAND) {
                            this.stageScanForSand();
                            //去寻找沙子
                        } else if (this.theStage == Stage.GOTOSANDBLOCK) {
                            this.stageGotoSandBlock();
                            //收集沙子
                        } else if (this.theStage == Stage.COLLECTSAND) {
                            this.stageCollectSand();
                            //返回沙子
                        } else if (this.theStage == Stage.RETURNSAND) {
                            this.stageReturnSand();
                            //使用熔炉
                        } else if (this.theStage == Stage.USEFURNACE) {
                            this.stageUseFurnace();
                            //不能工作
                        } else if (this.theStage == Stage.CANTWORK) {
                            this.stageCantWork();
                        }
                    }

                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("JobGlassMaker-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    //这里没有沙子？
    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.There");
    }

    private void stageScanForSand() {
        try {

            //去挖掘一些沙子
            if (this.theFolk.statusText.contains(I18n.format("container.sim.Arrived")) || this.theFolk.statusText.contains(I18n.format("container.sim.glass"))) {
                //要去挖更多的沙子
                this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Going");
            }
            //找到80个格子内的沙子
            this.blockOfSand = findClosestBlockType(this.theFolk.employedAt, Blocks.sand, 80, true);
            if (this.blockOfSand == null) {
                ItemStack itemStack= this.theFolk.getVillagerInventory().getStackInSlot(0);
                if(itemStack!=null&&itemStack.stackSize>0){
                    this.theStage = Stage.RETURNSAND;
                    this.step = 1;
                    return;
                }
                ItemStack gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.sand, 64), false, false, new ItemStack(Blocks.sand, 64));
                if (gotFuel != null) {
                    //this.factoryFurnace.setInventorySlotContents(0, gotFuel);
                    this.theStage = Stage.USEFURNACE;
                    return;
                }

                this.theStage = Stage.CANTWORK;
                return;
            }

            this.theStage = Stage.GOTOSANDBLOCK;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageScanForSand出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 去沙子旁边
     * @Date 22:16 2022/9/1
     * @Param []
     **/
    private void stageGotoSandBlock() {
        try {

            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.swingProgress = 0.0F;
            }

            this.theFolk.updateLocationFromEntity();
            double dist = (double) this.theFolk.location.getDistanceTo(this.blockOfSand);
            if (dist > 4 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.stayPut = false;
                this.theFolk.gotoXYZ(this.blockOfSand, null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
            }

            this.theStage = Stage.COLLECTSAND;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageGotoSandBlock出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 收集沙子
     * @Date 22:17 2022/9/1
     * @Param []
     **/
    private void stageCollectSand() {
        try {
            //停顿一秒
            this.runDelay = 1000;
            //在工作中
            this.theFolk.isWorking = true;
            //更新NPC位置
            this.theFolk.updateLocationFromEntity();
            //NPC距离沙子距离
            double dist = this.theFolk.location.getDistanceTo(this.blockOfSand);
            //大于6 去到沙子旁边
            if (dist > 6 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.gotoXYZ(this.blockOfSand, null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
                ++this.gotoCount;
                if (this.gotoCount > 2) {
                    this.gotoCount = 0;
                    V3 bs = this.blockOfSand.clone();
                    bs = new V3(bs.xCoord, bs.yCoord+0.5, bs.zCoord, bs.theDimension);
                    this.theFolk.beamMeTo(bs);
                }

            }
            if (dist < 6) {
                theFolk.stayPut=true;
            }

            this.gotoCount = 0;
            BlockPos blockPos1 = new BlockPos(this.blockOfSand.xCoord, this.blockOfSand.yCoord, this.blockOfSand.zCoord);
            this.jobWorld.setBlockState(blockPos1, Blocks.air.getDefaultState(), 3);
            //this.mc.theWorld.playSound(this.blockOfSand.xCoord, this.blockOfSand.yCoord, this.blockOfSand.zCoord, "step.sand", 1, 1, false);
            this.theFolk.getVillagerInventory().func_174894_a(new ItemStack(Blocks.sand, 1));
            //我得到沙子惹！
            this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Diggy") + this.theFolk.getVillagerInventory().getStackInSlot(0).stackSize;
            GameStates var10000 = ModSimReloaded.states;
            var10000.credits = (float) ((double) var10000.credits - 0.012D);
            if (this.theFolk.getVillagerInventory().getStackInSlot(0).stackSize < 64) {
                this.theStage = Stage.SCANFORSAND;
            } else {
                this.theStage = Stage.RETURNSAND;
                this.step = 1;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("JobGlassMaker-stageCollectSand出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    /**
     * @Author fan
     * @Description //TODO 返回沙子
     * @Date 23:06 2022/9/1
     * @Param []
     * @return void
     **/
    private void stageReturnSand() {
        this.theFolk.isWorking = false;

        try {
            if (this.step == 1) {
                V3 adj = this.theFolk.employedAt.clone();
                adj = new V3(adj.xCoord, adj.yCoord+0.5, adj.zCoord, adj.theDimension);
                this.theFolk.gotoXYZ(adj, null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 4) {
                    this.theFolk.stayPut = true;
                    this.step = 3;
                } else if (this.theFolk.destination == null) {
                    V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord+0.5, this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                    //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }
            } else if (this.step == 3) {
                this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                this.openCloseChest(this.factoryChests.get(0), 1000);
                boolean placed = this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.factoryChests,  null);
                this.theStage = Stage.USEFURNACE;
                this.step = 1;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageReturnSand出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }
    /**
     * @Author fan
     * @Description //TODO 使用熔炉
     * @Date 20:53 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageUseFurnace() {

        try {
            this.factoryFurnace = this.findFurnace(this.theFolk.employedAt);
            this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.factoryFurnace == null) {
                //我的炉子不见了
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.glass.farmer.Where"));
            } else {
                ItemStack currentSand;
                ItemStack gotFuel;
                if (this.step == 1) {
                    //检查炉子燃料
                    this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Checking");
                    //沙子
                    currentSand = this.factoryFurnace.getStackInSlot(1);
                    if (currentSand == null) {
                        //煤炭
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.coal, 64), false, false, new ItemStack(Items.coal, 64));
                        //熔岩桶
                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.lava_bucket, 1), false, false, new ItemStack(Items.lava_bucket, 1));
                        }
                        //木材
                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.log, 64), false, false, new ItemStack(Blocks.log, 64));
                        }
                        //木板
                        if (gotFuel == null) {
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Blocks.planks, 64), false, false, new ItemStack(Blocks.planks, 1));
                        }

                        if (gotFuel == null) {
                            //(玻璃制造商) 我的的炉子没有任何燃料
                            ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.glass.farmer.furnace"));
                            this.theStage = Stage.SCANFORSAND;
                            this.step = 1;
                            return;
                        }
                        //将燃料放到熔炉
                        this.factoryFurnace.setInventorySlotContents(1, gotFuel);
                        this.step = 2;
                        return;
                    }

                    this.step = 2;
                } else if (this.step == 2) {
                    //往炉子里加沙子
                    this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Adding");
                    if (this.factoryFurnace != null) {
                        currentSand = this.factoryFurnace.getStackInSlot(0);
                        if (currentSand == null) {
                            //取箱子的沙子
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
                    //获得玻璃
                    currentSand = this.factoryFurnace.getStackInSlot(2);
                    if (currentSand != null) {
                        //将玻璃放入仓库
                        this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Putting");
                        this.inventoriesPut(this.factoryChests, currentSand, true);
                        GameStates var10000 = ModSimReloaded.states;
                        var10000.credits = (float) ((double) var10000.credits - 0.005D * (double) currentSand.stackSize);
                        this.factoryFurnace.setInventorySlotContents(2, null);
                    } else {
                        //没有制造玻璃
                        this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.glass");
                    }
                    this.theStage = Stage.SCANFORSAND;
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageUseFurnace出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.glass.farmer.Arrived");
                this.theStage = Stage.USEFURNACE;
            } else {
                V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord+0.5, this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

}

