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
    public FolkData theFolk = null;
    public Stage theStage;
    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    private transient List<IInventory> millChests = new CopyOnWriteArrayList();
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
            if (this.theStage == null) {
                this.theStage = Stage.IDLE;
            }

            if (this.theFolk != null) {
                if (this.theFolk.destination == null) {
                    this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
                }

            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobLumberjack出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            if (!ModSimReloaded.isDayTime()) {
                if (!theFolk.isNightOwl()) {
                    //闲置
                    this.theStage = Stage.IDLE;
                    return;
                }
            }

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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("JobLumberjack-onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 寻找树
     */
    private void stageScanForTree() {
        try {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.isWorking = false;
            V3 searchXYZ = null;
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

            V3 searchpos;
            if (ts != null) {
                searchpos = ts.clone();
            } else {
                searchpos = this.theFolk.location.clone();
            }

            this.foundWoodAt = findClosestBlockType(searchpos, Blocks.log, ConfigLoader.configLumberArea, false);
            if (this.foundWoodAt == null) {
                ModSimReloaded.sendChat(this.theFolk.name + I18n.format("container.sim.job.lumberjack.farmer.wood"));
                this.theFolk.selfFire();
            }
            this.foundWoodAt.theDimension = this.jobWorld.provider.getDimensionId();
            this.theStage = Stage.GOTOTREE;
            this.onRoute = false;

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageScanForTree出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 去书旁边
     */
    private void stageGotoTree() {
        try {
            this.theFolk.isWorking = false;
            if (!this.onRoute) {
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Going");
                this.theFolk.gotoXYZ(this.foundWoodAt, null);
                this.startedGoing = System.currentTimeMillis();
                this.onRoute = true;
            } else {
                if (this.theFolk.gotoMethod == GotoMethod.WALK) {
                    this.theFolk.updateLocationFromEntity();
                }

                double dist = (double) this.theFolk.location.getDistanceTo(this.foundWoodAt);
                if (dist < 7) {
                    this.theStage = Stage.CHOPPINGTREE;
                    this.theFolk.stayPut = true;
                    this.step = 1;
                } else {
                    if (this.theFolk.destination == null && this.theFolk.theEntity != null) {
                    }

                    if (System.currentTimeMillis() - this.startedGoing > 25000L) {
                        this.theStage = Stage.CHOPPINGTREE;
                        this.theFolk.stayPut = true;
                        this.theFolk.destination = null;
                        this.step = 1;
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageGotoTree出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 砍树阶段
     */
    private void stageChoppingTree() {
        try {
            int i;
            int l;
            if (this.step == 1) {
                this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Choppy");
                this.theFolk.isWorking = true;

                for (i = 0; i < 20; i++) {
                    l = (int)this.foundWoodAt.x;
                    int y = (int)(this.foundWoodAt.y - 1);
                    int z = (int)this.foundWoodAt.z;
                    if (this.jobWorld == null) {
                        this.theFolk.selfFire();
                        return;
                    }
                    ;
                    if (this.jobWorld.getBlockState(new BlockPos(l, y, z)).getBlock() != Blocks.log) {
                        break;
                    }

                    this.foundWoodAt.y = (double) y;
                }

                this.step = 2;
            } else if (this.step == 2) {

                if (this.jobWorld.getBlockState(new BlockPos(this.foundWoodAt.x, this.foundWoodAt.y, this.foundWoodAt.z)).getBlock() == Blocks.log) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            isChopping = true;

                            for (int d = 0; d < 12; ++d) {
                                try {
                                    mc.theWorld.playSound(theFolk.location.x, theFolk.location.y, theFolk.location.z, "step.wood", 1, 1, false);
                                } catch (Exception e) {
                                }

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
                        }
                    });
                    t.start();
                    this.step = 3;
                } else {
                    this.step = 4;
                }
            } else {
                int count;
                if (this.step == 3) {
                    if (this.isChopping) {
                        return;
                    }

                    List<ItemStack> log = this.translateBlockWhenMined(this.jobWorld, this.foundWoodAt);
                    BlockPos blockPos1 = new BlockPos(this.foundWoodAt.x, this.foundWoodAt.y, this.foundWoodAt.z);
                    this.jobWorld.setBlockState(blockPos1, Blocks.air.getDefaultState(), 3);
                    if (log != null) {
                        for (l = 0; l < log.size(); ++l) {
                            ItemStack isl = (ItemStack) log.get(l);
                            this.theFolk.getVillagerInventory().setInventorySlotContents(l, isl);
                        }
                    }

                    count = this.getInventoryCount(this.theFolk, Blocks.log);
                    this.theFolk.statusText = I18n.format("container.sim.job.lumberjack.farmer.Got") + count + I18n.format("container.sim.job.lumberjack.farmer.logs_so_far");
                    this.theFolk.stayPut = false;
                    this.foundWoodAt.y = this.foundWoodAt.y + 1;
                    this.step = 2;
                } else if (this.step == 4) {
                    if (this.theFolk.isSpawned()) {
                        count = this.getInventoryCount(this.theFolk, Blocks.sapling);
                        if (count > 0) {
                            for (i = 0; i < this.theFolk.getVillagerInventory().getSizeInventory(); i++) {
                                ItemStack fis = (ItemStack) this.theFolk.getVillagerInventory().getStackInSlot(i);
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageChoppingTree出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("stageReturnWood出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
                this.theFolk.gotoXYZ(this.theFolk.employedAt, null);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onArrivedAtWork出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("pickUpSaplings出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void plantSapling(Block is) {
        try {
            if (this.theFolk.isSpawned()) {

                if (this.jobWorld.getBlockState(new BlockPos((int) this.theFolk.theEntity.posX, (int) this.theFolk.theEntity.posY, (int) this.theFolk.theEntity.posZ)).getBlock() == null) {
                    BlockPos blockPos1 = new BlockPos((int) this.theFolk.theEntity.posX, (int) this.theFolk.theEntity.posY, (int) this.theFolk.theEntity.posZ);
                    this.jobWorld.setBlockState(blockPos1, is.getDefaultState());
                }
            } else {
                BlockPos blockPos1 = new BlockPos(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z);
                this.jobWorld.setBlockState(blockPos1, Blocks.sapling.getDefaultState(), 3);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("plantSapling出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

}

