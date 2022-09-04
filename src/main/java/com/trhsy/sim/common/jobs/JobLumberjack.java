package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.core.entity.Building;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.GameStates;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.core.entity.enums.FolkAction;
import com.trhsy.sim.common.core.entity.enums.GotoMethod;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ========================================
 *
 * @ClassName JobLumberjack
 * @Description todo 伐木工人
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:51
 * ========================================
 **/
public class JobLumberjack extends Job implements Serializable {
    private static final long serialVersionUID = -1177112207904887741L;
    //职业
    public Vocation vocation = null;
    public FolkData theFolk = new FolkData();
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient List<IInventory> millChests = new CopyOnWriteArrayList();
    //木材
    private transient V3 foundWoodAt = null;
    private transient Building lumbermill = null;
    private transient long startedGoing = 0L;
    public transient boolean isChopping = false;
    private transient float pay = 0.0F;
    private transient boolean onRoute = false;

    public JobLumberjack() {
    }

    public JobLumberjack(FolkData folk) {
        try {
            this.theFolk = folk;
            //如果当前状态为空则闲置
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }
            //找不到npc
            if (this.theFolk != null) {
                //npc 目的地为空
                if (this.theFolk.destination == null) {
                    //去其雇佣地
                    V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord + 1, this.theFolk.employedAt.zCoord);
                    this.theFolk.gotoXYZ(v3, null);
                    //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("JobLumberjack出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    /**
     * 重置工作
     */
    @Override
    public void resetJob() {
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }

    @Override
    public void onUpdate() {
        try {
            super.onUpdate();
            //夜晚
            if (!ModSimReloaded.isDayTime()) {
                //不是夜猫子
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }
            //去工作
            super.onUpdateGoingToWork(this.theFolk);
            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long) this.runDelay) {
                this.timeSinceLastRun = System.currentTimeMillis();
                //闲置寻找树
                if (this.theStage == Stage.IDLE && ModSimReloaded.isDayTime()) {
                    this.theStage = Stage.SCANFORTREE;
                    //抵达伐木场
                } else if (this.theStage == Stage.ARRIVEDATMILL) {
                    this.theStage = Stage.SCANFORTREE;
                    //寻找树
                } else if (this.theStage == Stage.SCANFORTREE) {
                    this.stageScanForTree();
                    //去到树旁边
                } else if (this.theStage == Stage.GOTOTREE) {
                    this.pickUpSaplings();
                    this.stageGotoTree();
                    //砍树
                } else if (this.theStage == Stage.CHOPPINGTREE) {
                    this.stageChoppingTree();
                    this.pickUpSaplings();
                    //返回树
                } else if (this.theStage == Stage.RETURNWOOD) {
                    this.stageReturnWood();
                    this.pickUpSaplings();
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("JobLumberjack-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 寻找树
     */
    private void stageScanForTree() {
        try {
            //在工作
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.isWorking = false;
            V3 searchXYZ = null;
            //伐木工
            this.lumbermill = Building.getBuilding(this.theFolk.employedAt);
            V3 ts = null;
            if (this.lumbermill.lumbermillMarker != null) {
                searchXYZ = this.lumbermill.lumbermillMarker;
            } else if (this.theFolk.employedAt != null) {
                searchXYZ = this.theFolk.employedAt.clone();
            } else {
                searchXYZ = this.theFolk.location.clone();
            }

            ts = searchXYZ.clone();

            if (ts == null) {
                ts= this.theFolk.location.clone();
            }
            //查找最近范围的树
            this.foundWoodAt = findClosestBlockType(ts, Blocks.log, ConfigLoader.configLumberArea, false);
            //没找到
            if (this.foundWoodAt == null) {
                //获得最近箱子
                this.millChests = inventoriesFindClosest(this.theFolk.employedAt, 6);
                //获得箱子库存
                int dist = this.getInventoryCount(this.theFolk, Blocks.log);
                if(dist>0){
                    //将物品从NPC转移到箱子
                    this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.millChests, new ItemStack(Blocks.log));
                    this.pay = (float) dist * 0.03F;
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= this.pay;
                    //已交付
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.delivered") + dist + I18n.format("container.sim.job.lumberjack.farmer.lumbermill"));
                }

                //将树苗从箱子转移到NPC
                boolean flg=this.inventoriesTransferToFolk(this.theFolk.getVillagerInventory(), this.millChests, new ItemStack(Blocks.sapling),Blocks.sapling);
                if(flg){
                    this.step =4;
                    this.foundWoodAt = findClosestBlockType(ts, Blocks.sapling, ConfigLoader.configLumberArea, false);
                    if(this.foundWoodAt ==null){
                    V3 v=new V3(this.theFolk.location.xCoord+5,this.theFolk.location.yCoord,this.theFolk.location.zCoord+5);
                    this.foundWoodAt=v;
                    }
                    this.theStage = Stage.GOTOTREE;
                    this.onRoute = false;
                    //树苗
                    /*int count = this.getInventoryCount(this.theFolk, Blocks.sapling);
                    if (count > 0) {
                        for (int i = 0; i < this.theFolk.getVillagerInventory().getSizeInventory(); i++) {
                            this.theFolk.gotoXYZ(v, null);
                            ItemStack fis = this.theFolk.getVillagerInventory().getStackInSlot(i);
                            if (fis != null && Block.getBlockFromItem(fis.getItem()) == Blocks.sapling) {
                                this.theFolk.getVillagerInventory().removeStackFromSlot(i);
                                this.plantSapling(Block.getBlockFromItem(fis.getItem()));
                                break;
                            }
                        }
                    }*/
                    return;
                }else{
                    //在该地区找不到任何木材,你能放一些树苗到箱子里吗？
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.wood"));
                    this.theFolk.selfFire();
//                    return;
                }
            }else{
                this.theStage = Stage.RETURNWOOD;
                this.step = 1;
            }
            this.foundWoodAt.theDimension = this.jobWorld.provider.getDimensionId();
            this.theStage = Stage.GOTOTREE;
            this.onRoute = false;

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageScanForTree出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 去树旁边
     */
    private void stageGotoTree() {
        try {
            this.theFolk.isWorking = false;
            if (!this.onRoute) {
                //去砍树...
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Going");
                this.theFolk.gotoXYZ(this.foundWoodAt, null);
                this.startedGoing = System.currentTimeMillis();
                this.onRoute = true;
            } else {
                //走过去
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }
                //距离树多远
                double dist = this.theFolk.location.getDistanceTo(this.foundWoodAt);
                if (dist < 7) {
                    //砍树
                    this.theStage = Stage.CHOPPINGTREE;
                    this.theFolk.stayPut = true;
                    this.step = 1;
                } else {
                    if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                        //去砍树...
                        this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Going");
                        this.theFolk.gotoXYZ(this.foundWoodAt, null);
                        this.startedGoing = System.currentTimeMillis();
                        this.onRoute = true;
                    }
                    if (System.currentTimeMillis() - this.startedGoing > 25000L) {
                        //砍树
                        this.theStage = Stage.CHOPPINGTREE;
                        this.theFolk.stayPut = true;
                        this.theFolk.destination = null;
                        this.step = 1;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageGotoTree出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 砍树阶段
     */
    private void stageChoppingTree() {
        try {
            int count;
            Block block = this.jobWorld.getBlockState(new BlockPos(foundWoodAt.xCoord, foundWoodAt.yCoord, foundWoodAt.zCoord)).getBlock();
            if (this.step == 1) {
                //砍树砍树
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Choppy");
                this.theFolk.isWorking = true;
                //找到行李箱的底部
                for (int i = 0; i < 20; i++) {
                    int x = (int) this.foundWoodAt.xCoord;
                    int y = (int) (this.foundWoodAt.yCoord - 0);
                    int z = (int) this.foundWoodAt.zCoord;
                    if (this.jobWorld == null) {
                        this.theFolk.selfFire();
                        return;
                    }
                    block = this.jobWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != Blocks.log || block != Blocks.log2) {
                        break;
                    } else {
                        this.foundWoodAt = new V3(this.foundWoodAt.xCoord, y, this.foundWoodAt.zCoord);
                    }
                }

                this.step = 2;
            } else if (this.step == 2) {
                block = this.jobWorld.getBlockState(new BlockPos(this.foundWoodAt.xCoord, this.foundWoodAt.yCoord, this.foundWoodAt.zCoord)).getBlock();
                if (block == Blocks.log || block == Blocks.log2) {
                    ThreadPoolExecutor threadPoolExecutor = ModSimReloaded.threadPoolExecutor;
                    threadPoolExecutor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                isChopping = true;
                                for (int d = 0; d < 12; d++) {
                                    mc.theWorld.playSound(theFolk.location.xCoord, theFolk.location.yCoord, theFolk.location.zCoord, "step.wood", 1, 1, false);
                                    if (theFolk.theEntity != null) {
                                        theFolk.theEntity.swingProgress = 0.3F;

                                        try {
                                            Thread.sleep(100L);
                                        } catch (Exception e) {
                                        }

                                        theFolk.theEntity.swingProgress = 0.7F;

                                        try {
                                            Thread.sleep(100L);
                                        } catch (Exception e) {
                                        }
                                    }
                                }

                                isChopping = false;
                            } catch (Exception e) {
                            }
                        }
                    });
                    //threadPoolExecutor.shutdown();
                    this.step = 3;
                } else {
                    this.step = 4;
                }
            } else {
                if (this.step == 3) {
                    if (this.isChopping) {
                        return;
                    }

                    List<ItemStack> log = this.translateBlockWhenMined(this.jobWorld, this.foundWoodAt);
                    BlockPos blockPos1 = new BlockPos(this.foundWoodAt.xCoord, this.foundWoodAt.yCoord, this.foundWoodAt.zCoord);
                    this.jobWorld.setBlockState(blockPos1, Blocks.air.getDefaultState(), 3);
                    if (log != null) {
                        for (int l = 0; l < log.size(); ++l) {
                            ItemStack isl = log.get(l);
                            this.theFolk.getVillagerInventory().func_174894_a(isl);
                            //this.theFolk.getVillagerInventory().setInventorySlotContents(l, isl);
                        }
                    }

                    count = this.getInventoryCount(this.theFolk, Blocks.log);
                    //到目前为止拿到
                    this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Got") + count + I18n.format("container.sim.job.lumberjack.farmer.logs_so_far");
                    this.theFolk.stayPut = false;
                    this.foundWoodAt = new V3(this.foundWoodAt.xCoord, this.foundWoodAt.yCoord + 1, this.foundWoodAt.zCoord);
                    //this.foundWoodAt.yCoord = this.foundWoodAt.yCoord + 1;
                    this.step = 2;
                } else if (this.step == 4) {
                    if (this.theFolk.isSpawned()) {
                        //树苗
                        count = this.getInventoryCount(this.theFolk, Blocks.sapling);
                        if (count > 0) {
                            for (int i = 0; i < this.theFolk.getVillagerInventory().getSizeInventory(); i++) {
                                ItemStack fis = this.theFolk.getVillagerInventory().getStackInSlot(i);
                                if (fis != null && Block.getBlockFromItem(fis.getItem()) == Blocks.sapling) {
                                    this.theFolk.getVillagerInventory().removeStackFromSlot(i);
                                    this.plantSapling(Block.getBlockFromItem(fis.getItem()));
                                    break;
                                }
                            }
                        }
                    } else {
                        this.plantSapling(Blocks.sapling);
                    }

                    count = this.getInventoryCount(this.theFolk, Blocks.log);
                    if (count < 12) {
                        this.theStage = Stage.SCANFORTREE;
                    } else {
                        this.theStage = Stage.RETURNWOOD;
                        this.step = 1;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageChoppingTree出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 返回木材
     */
    private void stageReturnWood() {
        try {
            this.theFolk.isWorking = false;
            if (this.step == 1) {
                //将木材送回伐木场箱子
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Delivering");
                V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord + 1, this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                this.step = 2;
            } else {
                if (this.step == 2) {
                    if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                        this.theFolk.updateLocationFromEntity();
                    }
                    //获取距离
                    int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
                    if (dist <= 1) {
                        this.step = 3;
                    } else if (this.theFolk.destination == null && this.theFolk.theEntity != null) {

                    }
                } else if (this.step == 3) {
                    this.theFolk.stayPut = true;
                    //获得箱子库存
                    int dist = this.getInventoryCount(this.theFolk, Blocks.log);
                    //获得最近箱子
                    this.millChests = inventoriesFindClosest(this.theFolk.employedAt, 6);
                    //将物品从NPC转移到箱子
                    this.inventoriesTransferFromFolk(this.theFolk.getVillagerInventory(), this.millChests, new ItemStack(Blocks.log));
                    this.pay = (float) dist * 0.03F;
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= this.pay;
                    //已交付
                    ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.delivered") + dist + I18n.format("container.sim.job.lumberjack.farmer.lumbermill"));
                    this.theStage = Stage.SCANFORTREE;
                    this.step = 1;
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("stageReturnWood出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

    /**
     * 到达工作地点
     */
    @Override
    public void onArrivedAtWork() {
        try {
            int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
            if (dist <= 1) {
                this.theFolk.action = FolkAction.ATWORK;
                this.theFolk.stayPut = true;
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.a_lumberjack");
                this.theStage = Stage.ARRIVEDATMILL;
            } else {
                V3 v3 = new V3(this.theFolk.employedAt.xCoord, this.theFolk.employedAt.yCoord + 1, this.theFolk.employedAt.zCoord);
                this.theFolk.gotoXYZ(v3, null);
                //this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 捡起树苗
     */
    private void pickUpSaplings() {
        try {
            if (this.theFolk.isSpawned()) {
                List<Entity> list1 = this.jobWorld.getEntitiesWithinAABBExcludingEntity(this.theFolk.theEntity, new AxisAlignedBB(this.theFolk.theEntity.posX, this.theFolk.theEntity.posY, this.theFolk.theEntity.posZ, this.theFolk.theEntity.posX + 1, this.theFolk.theEntity.posY + 1, this.theFolk.theEntity.posZ + 1).expand(3, 4, 3));
                if (!list1.isEmpty()) {
                    for (Entity entity : list1) {
                        if (entity instanceof EntityItem) {
                            EntityItem entityitem = (EntityItem) entity;
                            ItemStack is = entityitem.getEntityItem();
                            Item ID = is.getItem();
                            if (ID == Item.getItemFromBlock(Blocks.sapling)) {
                                this.theFolk.getVillagerInventory().setInventorySlotContents(0, new ItemStack(Blocks.sapling, is.getMetadata(), 1));
                                entityitem.setDead();
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("pickUpSaplings出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    private void plantSapling(Block is) {
        try {
            if (this.theFolk.isSpawned()) {
                Block block=this.jobWorld.getBlockState(new BlockPos((int) this.theFolk.theEntity.posX, (int) this.theFolk.theEntity.posY, (int) this.theFolk.theEntity.posZ)).getBlock();
                if(block==Blocks.dirt){
                    BlockPos blockPos1 = new BlockPos((int) this.theFolk.theEntity.posX, (int) this.theFolk.theEntity.posY, (int) this.theFolk.theEntity.posZ);
                    this.jobWorld.setBlockState(blockPos1, is.getDefaultState());
                }else{
                    plantSapling(is);
                }
            } else {
                BlockPos blockPos1 = new BlockPos(this.theFolk.location.xCoord, this.theFolk.location.yCoord, this.theFolk.location.zCoord);
                this.jobWorld.setBlockState(blockPos1, Blocks.sapling.getDefaultState(), 3);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimReloaded.log.error("plantSapling出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }


    }

}

