package com.trhsy.sim.common.jobs;

import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ModSimReloaded;
import jdk.nashorn.internal.ir.Block;
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
    public FolkData theFolk =new FolkData();
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
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
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
                //扫描黏土
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
                        //扫描黏土
                        if (this.theStage == Stage.SCANFORCLAY) {
                            this.stageScanForClay();
                            //去找黏土块
                        } else if (this.theStage == Stage.GOTOCLAYBLOCK) {
                            this.stageGotoClayBlock();
                            //收集黏土
                        } else if (this.theStage == Stage.COLLECTCLAY) {
                            this.stageCollectClay();
                            //返回黏土
                        } else if (this.theStage == Stage.RETURNCLAY) {
                            this.stageReturnClay();
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobBrickMaker-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private void stageCantWork() {
        this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker1");
    }
    /**
     * @Author fan
     * @Description //TODO 扫描黏土
     * @Date 20:40 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageScanForClay() {
        try {

            //到达 ||砖
            if (this.theFolk.statusText.contains(I18n.format("container.sim.Arrived")) || this.theFolk.statusText.contains(I18n.format("container.sim.brick"))) {
                //去挖掘一些粘土
                this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker2");
            }
            //寻找黏土
            this.blockOfClay = findClosestBlockType(this.theFolk.employedAt, Blocks.clay, 80, true);
            //没有找到黏土
            if (this.blockOfClay == null) {
                ItemStack itemStack= this.theFolk.getVillagerInventory().getStackInSlot(0);
                if(itemStack!=null&&itemStack.stackSize>0){
                    this.theStage = Stage.RETURNCLAY;
                    this.step = 1;
                    return;
                }
                this.theStage = Stage.CANTWORK;
                return;
            }
            //去找黏土块
            this.theStage = Stage.GOTOCLAYBLOCK;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageScanForClay出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    /**
     * @Author fan
     * @Description //TODO 去找黏土块
     * @Date 20:42 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageGotoClayBlock() {
        try {
            if (this.theFolk.theEntity != null) {
                this.theFolk.theEntity.swingProgress = 0.0F;
            }

            this.theFolk.updateLocationFromEntity();
            //距离黏土多远
            double dist = this.theFolk.location.getDistanceTo(this.blockOfClay);
            if (dist > 4.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
                this.theFolk.stayPut = false;
                this.theFolk.gotoXYZ(this.blockOfClay, null);
                this.theFolk.stayPut = false;
                this.lastGotocmd = System.currentTimeMillis();
            }
            //收集黏土
            this.theStage = Stage.COLLECTCLAY;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGotoClayBlock出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
    /**
     * @Author fan
     * @Description //TODO 收集黏土
     * @Date 20:43 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageCollectClay() {
        try {
        this.runDelay = 1000;
        this.theFolk.isWorking = true;
        this.theFolk.updateLocationFromEntity();
        //距离黏土多远
        double dist = this.theFolk.location.getDistanceTo(this.blockOfClay);
        if (dist > 6.0 && System.currentTimeMillis() - this.lastGotocmd > 10000L) {
            this.theFolk.gotoXYZ(this.blockOfClay, null);
            this.theFolk.stayPut = false;
            this.lastGotocmd = System.currentTimeMillis();
            ++this.gotoCount;
            if (this.gotoCount > 2) {
                this.gotoCount = 0;
                V3 bs = this.blockOfClay.clone();
                bs = new V3(bs.xCoord - 1.0, bs.yCoord+0.5, bs.zCoord, bs.theDimension);
                this.theFolk.beamMeTo(bs);
            }

        } else {
                this.gotoCount = 0;
                //黏土块位置
                BlockPos blockPos = new BlockPos(this.blockOfClay.xCoord, this.blockOfClay.yCoord, this.blockOfClay.zCoord);
                //挖掉
                this.jobWorld.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
                //播放音乐
                //this.mc.theWorld.playSound(this.blockOfClay.xCoord, this.blockOfClay.yCoord, this.blockOfClay.zCoord, "step.sand", 1, 1, false);
                //放到npc箱子里
                this.theFolk.getVillagerInventory().func_174894_a(new ItemStack(Items.clay_ball, 4));
                ItemStack itemStack=this.theFolk.getVillagerInventory().getStackInSlot(0);
                if(itemStack!=null&&itemStack.stackSize>0){
                    //我得到粘土惹
                    this.theFolk.statusText = I18n.format("container.sim.JobBrickMaker3") + itemStack.stackSize;
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits = (float) ((double) var10000.credits - 0.012D);
                    if (this.theFolk.getVillagerInventory().getSizeInventory() < 64) {
                        //扫描黏土
                        this.theStage = Stage.SCANFORCLAY;
                    } else {
                        //返回黏土
                        this.theStage = Stage.RETURNCLAY;
                        this.step = 1;
                    }
                }

        }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageCollectClay出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 返回黏土
     * @Date 20:47 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageReturnClay() {
        this.theFolk.isWorking = false;

        try {
            if (this.step == 1) {
                V3 adj = this.theFolk.employedAt.clone();
                adj = new V3(adj.xCoord - 1.0, adj.yCoord+0.5, adj.zCoord, adj.theDimension);
                this.theFolk.gotoXYZ(adj, null);
                this.step = 2;
            } else if (this.step == 2) {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                if (dist < 4.0) {
                    this.theFolk.stayPut = true;
                    this.step = 3;
                } else if (this.theFolk.destination == null) {
                    V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
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
    /**
     * @Author fan
     * @Description //TODO 使用熔炼
     * @Date 20:58 2022/9/4
     * @Param []
     * @return void
     **/
    private void stageUseFurnace() {
        try {
            this.factoryFurnace = this.findFurnace(this.theFolk.employedAt);
            this.factoryChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
            if (this.factoryFurnace == null) {
                //我的炉子不见了！
                ModSimReloaded.sendChat(this.theFolk.name + "：" + I18n.format("container.sim.JobBrickMaker4"));
            } else {
                ItemStack currentClay;
                ItemStack gotFuel;
                if (this.step == 1) {
                    //检查燃烧炉
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
                            //（板砖厂）炉子里没有任何燃料,请添加燃料谢谢！
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
                            gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.clay_ball, 64), false, false, new ItemStack(Blocks.clay, 64));
                            if (gotFuel != null) {
                                this.factoryFurnace.setInventorySlotContents(0, gotFuel);
                            }

                            this.step = 3;
                            return;
                        }
                        gotFuel = inventoriesGet(this.factoryChests, new ItemStack(Items.clay_ball, 64 - currentClay.stackSize), false, false, new ItemStack(Blocks.clay, 64 - currentClay.stackSize));
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
                V3 v3=new V3(this.theFolk.employedAt.xCoord,this.theFolk.employedAt.yCoord+0.5,this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }


}
