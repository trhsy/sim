package com.trhsy.sim.common.jobs;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.GameMode;
import com.trhsy.sim.common.entity.GameStates;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.entity.enums.FarmType;
import com.trhsy.sim.common.entity.enums.FolkAction;
import com.trhsy.sim.common.entity.enums.GotoMethod;
import com.trhsy.sim.common.entity.functionality.FarmingBox;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName JobCropFarmer
 * @Description todo 种植业农民
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:46
 * ========================================
 **/
public class JobCropFarmer extends Job implements Serializable {
    private static final long serialVersionUID = -1177112214234279141L;
    //职业
    public Vocation vocation = null;
    //实体人
    public FolkData theFolk = null;
    //状态
    public Stage theStage;

    public transient int runDelay = 1000;
    public transient long timeSinceLastRun = 0L;
    //完成一些工作
    private transient boolean doneSomeWork = false;
    //养殖箱
    private transient FarmingBox farmingBlock = null;

    private transient ArrayList<IInventory> farmingChests = new ArrayList();
    //去哪里
    private transient String farmDir = "";
    //未破坏统计
    private transient int ftbCount = 0;
    //最后统计
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
    //最后一个农业周期
    private transient long lastFarmCycle = 0L;
    //最后的收获
    private transient long lastCustomHarvest = 0L;
    //行计数器
    private transient int rowCounter = 0;

    public JobCropFarmer() {
    }

    /**
     * 工作作物农场主
     * @param folk
     */
    public JobCropFarmer(FolkData folk) {
        this.theFolk = folk;
        if (this.theStage == null) {
            //状态闲置
            this.theStage = Stage.IDLE;
        }

        if (this.theFolk != null) {
            //目的地为空重新设置 为雇佣地
            if (this.theFolk.destination == null) {
                this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
            }
            //设置养殖箱位置
            this.farmingBlock = FarmingBox.getFarmingBlockByBoxXYZ(folk.employedAt);
            //延迟
            this.runDelay = 1000;
        }
    }

    /**
     * 重置工作
     */
    @Override
    public void resetJob() {
        //设置闲置
        this.theStage = Stage.IDLE;
        this.theFolk.isWorking = false;
    }

    /**
     * 更新
     */
    @Override
    public void onUpdate() {
        //养殖箱子不等于1
        if (ModSimReloaded.theFarmingBoxes.size() != 0) {
            super.onUpdate();
            if (!ModSimReloaded.isDayTime()) {
                //闲置
                this.theStage = Stage.IDLE;
            }
            //去上班
            super.onUpdateGoingToWork(this.theFolk);
            //检查箱子
            if (this.theStage == Stage.CHECKINGFORCHESTS) {
                this.runDelay = 1000;
                //延迟
            }
            //收获季节                                                     锄地                              种植种子
            if (this.theStage == Stage.HARVEST || this.theStage == Stage.HOELAND || this.theStage == Stage.PLANTSEEDS) {
                this.runDelay = 500;
                //延迟
            }
            //闲逛
            if (this.theStage == Stage.HANGOUT) {
                //步
                if (this.step == 1) {
                    this.runDelay = 1000;
                    //延迟
                } else {
                    this.runDelay = 60000;
                    //延迟
                }
            }
            //当前时间毫秒- 上次跑步后的时间 》=当前运行延迟
            if (System.currentTimeMillis() - this.timeSinceLastRun >= (long)this.runDelay) {
                //上次跑步后的时间=当前时间毫秒
                this.timeSinceLastRun = System.currentTimeMillis();
                //闲置 或者 晚上
                if (this.theStage != Stage.IDLE || !ModSimReloaded.isDayTime()) {
                    //如果到达农场
                    if (this.theStage == Stage.ARRIVEDATFARM) {
                        //检查箱子
                        this.theStage = Stage.CHECKINGFORCHESTS;
                    } else if (this.theStage == Stage.CHECKINGFORCHESTS) {
                        this.stageCheckingForChests();
                    } else if (this.theStage == Stage.HARVEST) {
                        //收获季节
                        this.stageHarvest();
                    } else if (this.theStage == Stage.HOELAND) {
                        //锄地
                        this.stageHoeland();
                    } else if (this.theStage == Stage.PLANTSEEDS) {
                        //种种子
                        this.stagePlantSeeds();
                    } else if (this.theStage == Stage.HANGOUT) {
                        //闲逛
                        this.stageHangout();
                    }
                }

            }
        }
    }

    /**
     * 检查箱子
     */
    public void stageCheckingForChests() {
        //检查箱子
        if (this.farmingChests.isEmpty()) {
            this.farmingChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
        }

        ModSimReloaded.log.info("JobCropFarmer: 在农场发现 " + this.farmingChests.size() + " 箱子");
        this.theFolk.stayPut = true;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist > 3) {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

        if (this.farmingChests.isEmpty()) {
            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Please");
        } else {
            this.theStage = Stage.HARVEST;
            this.step = 1;
            this.theFolk.stayPut = true;
            if (this.theFolk.gender == 0) {
                this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readym", 1.0F, 1.0F, false);
            } else {
                this.jobWorld.playSound(this.theFolk.location.x, this.theFolk.location.y, this.theFolk.location.z, ModSim.MODID + ":readyf", 1.0F, 1.0F, false);
            }
        }

    }

    /**
     * 设置农业
     */
    private void setupFarming() {
        this.ftb = 0;
        this.ltr = -1;
        if (this.farmingBlock == null) {
            ModSimReloaded.log.warn("JobCropFarmer: FarmingBlock 为空 - 不存在或未找到？！");
        } else {
            V3 m1 = this.farmingBlock.marker1XYZ;
            V3 m2 = this.farmingBlock.marker2XYZ;
            V3 m3 = this.farmingBlock.marker3XYZ;
            if (this.farmingBlock.marker1XYZ == null) {
                ModSimReloaded.log.warn("JobCropFarmer: FarmingBlock 的标记为空");
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

    /**
     * 设置地址
     * @return
     */
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
            ModSimReloaded.sendChat(I18n.format("container.sim.job.crop.farmer.There") + this.theFolk.name + I18n.format("container.sim.job.crop.farmer.farming"));
            this.theFolk.selfFire();
            return false;
        }
    }

    /**
     * 收获
     */
    public void stageHarvest() {
        if (this.farmingBlock == null || this.farmingBlock.farmType == null) {
            ModSimReloaded.sendChat(I18n.format("container.sim.job.crop.farmer.problem"));//农业区出现问题，请重新设置
            if (this.theFolk != null) {
                this.theFolk.selfFire();
            }
        }

        if (this.step == 1) {
            this.setupFarming();
            //收获
            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Harvesting");
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
                //锄地
                this.theStage = Stage.HOELAND;
                this.step = 1;
                this.theFolk.isWorking = false;
                this.lastCustomHarvest = System.currentTimeMillis();
                return;
            }

            while(true) {
                while(!hasHarvest) {//有收获

                    this.id = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy, this.zzz)).getBlock();
                    this.meta = this.id.getMetaFromState(this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy, this.zzz)));

                    try {
                        //实体人没有死亡          不是定制 不是甘蔗不是仙人掌
                        if (!this.theFolk.isSpawned() && this.farmingBlock.farmType != FarmType.CUSTOM && this.farmingBlock.farmType != FarmType.SUGAR && this.farmingBlock.farmType != FarmType.CACTUS && this.meta < 7) {
                            ++this.meta;
                            BlockPos blockPos=new BlockPos(this.xxx, this.yyy, this.zzz);
                            this.jobWorld.setBlockState(blockPos,this.id.getDefaultState(),3);
                        }
                    } catch (Exception var8) {
                    }

                    boolean canHarvest = false;
                    //收获的块
                    V3 harvestBlock = new V3((double)this.xxx, (double)this.yyy, (double)this.zzz, this.jobWorld.provider.getDimensionId());
                    //
                    ArrayList<ItemStack> minedStacks = this.translateBlockWhenMined(this.jobWorld, harvestBlock);
                    //
                    if (this.farmingBlock.farmType != FarmType.SUGAR && this.farmingBlock.farmType != FarmType.CACTUS) {
                        //
                        if (this.id == Blocks.melon_block || this.id == Blocks.pumpkin || this.farmingBlock.farmType == FarmType.CUSTOM || this.meta >= 7) {
                            if (this.id != Blocks.pumpkin_stem && this.id != Blocks.melon_stem) {
                                canHarvest = true;
                            }

                            if (this.id == null) {
                                canHarvest = false;
                            }
                        }
                    } else {

                        Block sid1 = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy + 1, this.zzz)).getBlock();
                        Block sid2 = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy + 2, this.zzz)).getBlock();
                        if (sid1 == Blocks.reeds && sid2 == Blocks.reeds) {
                            canHarvest = true;
                        }

                        if (sid1 == Blocks.cactus && sid2 == Blocks.cactus) {
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

                                this.jobWorld.destroyBlock(new BlockPos(this.xxx, this.yyy, this.zzz), true);
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
                                BlockPos blockPos=new BlockPos(this.xxx, this.yyy, this.zzz);
                                this.jobWorld.setBlockState(blockPos,this.id.getDefaultState(),3);
                            }
                        } else {
                            this.farmingChests = inventoriesFindClosest(this.theFolk.employedAt, 5);
                            BlockPos blockPos1=new BlockPos(this.xxx, this.yyy + 1, this.zzz);
                            this.jobWorld.setBlockState(blockPos1,this.id.getDefaultState(),3);
                            BlockPos blockPos2=new BlockPos(this.xxx, this.yyy + 2, this.zzz);
                            this.jobWorld.setBlockState(blockPos2,this.id.getDefaultState(),3);
                            if (this.farmingBlock.farmType == FarmType.SUGAR) {
                                this.inventoriesPut(this.farmingChests, new ItemStack(Items.reeds, 2), false);
                            } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                                this.inventoriesPut(this.farmingChests, new ItemStack(Blocks.cactus, 2), false);
                            }
                        }

                        if (this.farmingBlock.farmType != FarmType.CUSTOM) {
                        }

                        GameStates var10000 = ModSimReloaded.states;
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

    /**
     * 捡掉的庄稼
     * @param v3center
     */
    private void pickUpDroppedCrops(V3 v3center) {
        if (this.theFolk.theEntity != null) {
            //获取AABB中的实体，排除实体
            List list1 = this.jobWorld.getEntitiesWithinAABBExcludingEntity(this.theFolk.theEntity,new AxisAlignedBB(v3center.x, v3center.y, v3center.z, v3center.x + 1.0, v3center.y + 1.0, v3center.z + 1.0).expand(3.0, 2.0, 3.0));
            //Iterator iterator1 = list1.iterator();
            if (!list1.isEmpty()) {
                for (int i = 0; i < list1.size(); i++) {
                    Entity entity1 =(Entity)list1.get(i);
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = entityitem.getEntityItem();

                        try {
                            ItemFood food = (ItemFood)is.getItem();
                            if (food != null) {
                                boolean ok = this.inventoriesPut(this.farmingChests, is, false);
                                if (ok) {
                                    entityitem.setDead();
                                }
                            }
                        } catch (Exception var9) {
                        }
                    }
                }
                /*while(iterator1.hasNext()) {
                    Entity entity1 = (Entity)iterator1.next();
                    if (entity1 instanceof EntityItem) {
                        EntityItem entityitem = (EntityItem)entity1;
                        ItemStack is = entityitem.getEntityItem();

                        try {
                            ItemFood food = (ItemFood)is.getItem();
                            if (food != null) {
                                boolean ok = this.inventoriesPut(this.farmingChests, is, false);
                                if (ok) {
                                    entityitem.setDead();
                                }
                            }
                        } catch (Exception var9) {
                        }
                    }
                }*/
            }

        }
    }

    /**
     * 锄地
     */
    public void stageHoeland() {

        if (this.step == 1) {

            this.setupFarming();
            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Tilling");
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
                    //种植种子
                    this.theStage = Stage.PLANTSEEDS;
                    this.step = 1;
                    return;
                }

                this.id = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy - 1, this.zzz)).getBlock();
                this.meta = this.id.getMetaFromState(this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy - 1, this.zzz)));
                GameStates var10000;
                if (this.farmingBlock.farmType == FarmType.SUGAR) {
                    this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.preparing");
                    if (this.rowCounter % 3 != 0 && (this.rowCounter + 1) % 3 != 0) {
                        if ((this.rowCounter + 2) % 3 == 0 && this.id != Blocks.water) {
                            BlockPos blockPos2=new BlockPos(this.xxx, this.yyy - 1, this.zzz);
                            this.jobWorld.setBlockState(blockPos2,Blocks.water.getDefaultState(),3);
                            hasTilled = true;
                            var10000 = ModSimReloaded.states;
                            var10000.credits -= 0.01F;
                        }
                    } else if (this.id != Blocks.dirt && this.id != Blocks.grass) {
                        BlockPos blockPos2=new BlockPos(this.xxx, this.yyy - 1, this.zzz);
                        this.jobWorld.setBlockState(blockPos2,Blocks.dirt.getDefaultState(),3);
                        this.jobWorld.playSound((double)this.xxx, (double)(this.yyy - 1), (double)this.zzz, Blocks.grass.stepSound.getStepSound(), 1.0F, 1.0F, false);
                        hasTilled = true;
                        var10000 = ModSimReloaded.states;
                        var10000.credits -= 0.01F;
                    }

                    ++this.rowCounter;
                    if (this.rowCounter > this.farmingBlock.getSizeWidth() + 1) {
                        this.rowCounter = 0;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                    this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.the_land");
                    if ((this.xxx + this.zzz) % 2 == 0 && this.id != Blocks.sand) {
                        BlockPos blockPos2=new BlockPos(this.xxx, this.yyy - 1, this.zzz);
                        this.jobWorld.setBlockState(blockPos2,Blocks.sand.getDefaultState(),3);
                        hasTilled = true;
                        var10000 = ModSimReloaded.states;
                        var10000.credits -= 0.01F;
                    }
                } else if ((this.id == Blocks.grass || this.id == Blocks.dirt) && ((this.farmingBlock.farmType == FarmType.MELON || this.farmingBlock.farmType == FarmType.PUMPKIN) && (this.ftb % 4 == 0 || this.ftb % 4 == 1) || this.farmingBlock.farmType == FarmType.WHEAT || this.farmingBlock.farmType == FarmType.CARROT || this.farmingBlock.farmType == FarmType.POTATO || this.farmingBlock.farmType == FarmType.CUSTOM)) {
                    BlockPos blockPos2=new BlockPos(this.xxx, this.yyy - 1, this.zzz);
                    this.jobWorld.setBlockState(blockPos2,Blocks.farmland.getDefaultState(),3);
                    this.jobWorld.playSound((double)this.xxx, (double)(this.yyy - 1), (double)this.zzz, Blocks.grass.stepSound.getStepSound(), 1.0F, 1.0F, false);
                    hasTilled = true;
                    var10000 = ModSimReloaded.states;
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

    /**
     * 种植种子
     */
    public void stagePlantSeeds() {
        if (this.step == 1) {
            this.setupFarming();
            this.theFolk.stayPut = true;
            this.theFolk.action = FolkAction.ATWORK;
            this.step = 2;
            this.theFolk.isWorking = true;
        } else if (this.step == 2) {
            boolean done = false;//完成
            boolean hasSown = false;//播下

            while(true) {
                Block gid;
                Block aid;
                do {
                    do {
                        if (hasSown || done) {
                            if (done) {
                                this.theStage = Stage.HANGOUT;
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Relaxing_farm");
                                this.step = 1;
                                this.theFolk.isWorking = false;
                                this.inventoriesTransferFromFolk(this.theFolk.inventory, this.farmingChests, (ItemStack)null);
                                return;
                            }

                            return;
                        }

                        done = this.setXYZ();
                        if (done) {
                            this.theStage = Stage.HANGOUT;//闲置
                            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Relaxing");
                            this.step = 1;
                            return;
                        }
                        gid = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy - 1, this.zzz)).getBlock();
                        aid = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy, this.zzz)).getBlock();
                    } while(gid != Blocks.sand && gid != Blocks.grass && gid != Blocks.dirt && gid != Blocks.farmland);//gid不是沙子 不是草 不是泥土 不是耕地
                } while(aid != Blocks.air);// aid 不是空 栅栏

                try {
                    if (this.farmingBlock.farmType != FarmType.CUSTOM) {
                        this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Planting") + this.farmingBlock.farmType.toString() + I18n.format("container.sim.job.crop.farmer.seeds");
                    }
                } catch (Exception var10) {
                }

                ItemStack seed;
                if (this.farmingBlock.farmType == FarmType.WHEAT) {
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.wheat_seeds, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.No_more");
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }
                    BlockPos blockPos1=new BlockPos(this.xxx, this.yyy - 1, this.zzz);
                    this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                    BlockPos blockPos2=new BlockPos(this.xxx, this.yyy, this.zzz);
                    this.jobWorld.setBlockState(blockPos2,Blocks.wheat.getDefaultState(),3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.PUMPKIN) {
                    if (this.ftb % 4 == 0 || this.ftb % 4 == 1) {
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Items.pumpkin_seeds, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.pumpkin");
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }
                        BlockPos blockPos1=new BlockPos(this.xxx, this.yyy-1, this.zzz);
                        this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                        BlockPos blockPos2=new BlockPos(this.xxx, this.yyy, this.zzz);
                        this.jobWorld.setBlockState(blockPos2,Blocks.pumpkin_stem.getDefaultState(),3);

                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.MELON) {
                    if (this.ftb % 4 == 0 || this.ftb % 4 == 1) {
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Items.melon_seeds, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.melon");
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }
                        BlockPos blockPos1=new BlockPos(this.xxx, this.yyy-1, this.zzz);
                        this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                        BlockPos blockPos2=new BlockPos(this.xxx, this.yyy, this.zzz);
                        this.jobWorld.setBlockState(blockPos2,Blocks.melon_stem.getDefaultState(),3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CARROT) {
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.carrot, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.carrots");
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }
                    BlockPos blockPos1=new BlockPos(this.xxx, this.yyy-1, this.zzz);
                    this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                    BlockPos blockPos2=new BlockPos(this.xxx, this.yyy, this.zzz);
                    this.jobWorld.setBlockState(blockPos2,Blocks.carrots.getDefaultState(),3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.POTATO) {
                    if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                        seed = inventoriesGet(this.farmingChests, new ItemStack(Items.potato, 1), false, false);
                        if (seed == null) {
                            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.potatoes");
                            this.theStage = Stage.HANGOUT;
                            this.step = 1;
                            return;
                        }
                    }
                    BlockPos blockPos1=new BlockPos(this.xxx, this.yyy-1, this.zzz);
                    this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                    BlockPos blockPos2=new BlockPos(this.xxx, this.yyy, this.zzz);
                    this.jobWorld.setBlockState(blockPos2,Blocks.potatoes.getDefaultState(),3);
                    hasSown = true;
                } else if (this.farmingBlock.farmType == FarmType.SUGAR) {

                    Block cid = this.jobWorld.getBlockState(new BlockPos(this.xxx, this.yyy-1, this.zzz)).getBlock();
                    if (cid == Blocks.dirt || cid == Blocks.grass || cid == Blocks.sand) {
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Items.reeds, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.sugar");
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }
                        BlockPos blockPos1=new BlockPos(this.xxx, this.yyy, this.zzz);
                        this.jobWorld.setBlockState(blockPos1,Blocks.reeds.getDefaultState(),3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CACTUS) {
                    if ((this.xxx + this.zzz) % 2 == 0) {
                        if (GameMode.gameMode != GameMode.GAMEMODES.CREATIVE) {
                            seed = inventoriesGet(this.farmingChests, new ItemStack(Blocks.cactus, 1), false, false);
                            if (seed == null) {
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.cactus");
                                this.theStage = Stage.HANGOUT;
                                this.step = 1;
                                return;
                            }
                        }
                        BlockPos blockPos1=new BlockPos(this.xxx, this.yyy, this.zzz);
                        this.jobWorld.setBlockState(blockPos1,Blocks.cactus.getDefaultState(),3);
                        hasSown = true;
                    }
                } else if (this.farmingBlock.farmType == FarmType.CUSTOM) {
                    label171:
                    for(int ch = 0; ch < this.farmingChests.size(); ++ch) {
                        IInventory chest = (IInventory)this.farmingChests.get(ch);

                        for(int g = 0; g < chest.getSizeInventory(); ++g) {
                            ItemStack chestStack = chest.getStackInSlot(g);
                            if (chestStack != null) {
                                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Planting") + chestStack.getDisplayName();
                                seed = inventoriesGet(this.farmingChests, new ItemStack(chestStack.getItem(), 1), false, false);
                                if (seed != null) {
                                    BlockPos blockPos1=new BlockPos(this.xxx, this.yyy-1, this.zzz);
                                    this.jobWorld.setBlockState(blockPos1,Blocks.farmland.getDefaultState(),3);
                                    hasSown = seed.getItem().onItemUse(seed, this.mc.thePlayer, this.jobWorld, blockPos1, EnumFacing.UP, 0.0F, 0.0F, 0.0F);
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
                    this.jobWorld.playSound((double)this.xxx, (double)this.yyy, (double)this.zzz, Blocks.grass.stepSound.getStepSound(), 1.0F, 1.0F, false);
                    GameStates var10000 = ModSimReloaded.states;
                    var10000.credits -= 0.01F;
                    this.doneSomeWork = true;
                }
            }
        }

    }

    /**
     * 晾晒
     */
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
                if (GameMode.gameMode == GameMode.GAMEMODES.HARDCORE) {
                    this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Wow");
                } else {
                    this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Facebook");
                }
            } else if (r == 1) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Checking");
            } else if (r == 2) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Wishing");
            } else if (r == 3) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Having");
            } else if (r == 4) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Cleaning");
            } else if (r == 5) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Sharpening");
            } else if (r == 6) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Eating");
            } else if (r == 7) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Reticulating");
            } else if (r == 8) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Relaxing");
            } else if (r == 9) {
                this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Minecraft");
            }

            if (System.currentTimeMillis() - this.lastFarmCycle > 180000L) {
                this.theStage = Stage.HARVEST;
                this.step = 1;
                return;
            }
        }

    }

    /**
     * 上班
     */
    @Override
    public void onArrivedAtWork() {
        //int dist = false;
        int dist = this.theFolk.location.getDistanceTo(this.theFolk.employedAt);
        if (dist <= 1) {
            this.theFolk.action = FolkAction.ATWORK;
            this.theFolk.stayPut = true;
            this.theFolk.statusText = I18n.format("container.sim.job.crop.farmer.Arrived");
            this.theStage = Stage.ARRIVEDATFARM;
        } else {
            this.theFolk.gotoXYZ(this.theFolk.employedAt, (GotoMethod)null);
        }

    }

}

