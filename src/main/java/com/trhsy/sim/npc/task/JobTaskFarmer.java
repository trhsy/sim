package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.npc.job.JobFarmer;
import com.trhsy.sim.task.JobTask;
import com.trhsy.sim.util.FarmType;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskFarmer
 * @Description:
 * @date 2023/08/07 上午 10:13
 */
public class JobTaskFarmer extends JobTask {
    //农田箱
    public FarmBox farm;
    String status;
    //收获检查
    long harvestCheck = 0L;
    //
    long qsCheck = 0L;
    long growCheck = 0L;
    //没有种子等待时间，
    private int noNeed;
    //摇臂和声音循环
    private long swingArmCheck = 0L;

    public JobTaskFarmer(Job j, long ms, String status,FarmBox farm) {
        super(j, ms);
        this.status = status;
        this.noNeed = 0;
        this.farm=farm;
    }

    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
    }

    @Override
    public void onUpdate() {
        try {

            //收获检查
            this.harvestCheck = System.currentTimeMillis();
            if (ModSimLoader.money > 0.02F) {
                //周围五格内查找箱子
                if (this.job.jobChests == null) {
                    //没有箱子
                    this.folk.setStatus(I18n.format("container.sim.WAITINGFORCHEST"));
                    return;
                }
                boolean falg = false;
                if (ModSimLoader.gamemode == 1) {
                    falg =(System.currentTimeMillis() - this.harvestCheck) > 1000.0F- 100.0F*9;
                }
                // 等级 时间计算 工作效率
                if ((float) (System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F * this.folk.skillFarming || falg) {
                    //锄地
                    if (this.job.stage == 1) {
                        this.hoe();
                        //种植
                    } else if (this.job.stage == 2) {
                        this.plant();
                        //收获
                    } else if (this.job.stage == 3) {
                        this.harvest();
                        //等待
                    } else if (this.job.stage == 4) {
                        Random ra = new Random();
                        int r = ra.nextInt(2);
                        if (r == 0) {
                            //在公众号'dasha500'找作者玩
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Facebook"));
                            if ((System.currentTimeMillis() - this.growCheck) > 1000 * 60) {
                                this.growCheck = System.currentTimeMillis();
                                grow();
                            }
                        } else if (r == 1) {
                            //照料作物
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tending") + "," + this.farm.farmType);
                            //用骨粉快速生长作物
                            if ((System.currentTimeMillis() - this.growCheck) > 1000 * 60) {
                                this.growCheck = System.currentTimeMillis();
                                grow();
                            }
                        }
                    } else if (this.job.stage == 0) {
                        this.job.stage = 3;
                    } else if (this.job.stage == -1) {
                        this.job.stage = 0;
                    }
                }

            } else {
                //没有钱付给我
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-onUpdate出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 4;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 农场等级
     * @Date 22:00 2022/12/9
     * @Param []
     **/
    public void addFarmingLevel() {
        try {
            int b4 = (int) Math.floor((double) this.folk.skillFarming);
            ModSimLoader.addMoney(-0.01F);
            if (this.folk.skillFarming < 10.0F) {
                NpcData var10000 = this.folk;
                var10000.skillFarming = (float) ((double) var10000.skillFarming + 0.001D / (double) b4);
            }

            int aft = (int) Math.floor((double) this.folk.skillFarming);
            if (b4 != aft) {
                //的农民等级刚刚达到了
                ModSimLoader.sendChat(this.folk.getName() + " " + I18n.format("container.sim.job_farmer_has") + " " + aft);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-addFarmingLevel出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 锄地
     * @Date 21:11 2022/12/8
     * @Param []
     **/
    public void hoe() {
        try {
            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
            //先获取农田箱的 类型
            FarmType farmType = this.farm.farmType;
            //胡萝卜
            if (farmType == FarmType.CARROT) {
//循环农场的宽
                hoe1();
                //西瓜
            } else if (farmType == FarmType.MELON) {
                hoe1();
                //土豆
            } else if (farmType == FarmType.POTATO) {
                hoe1();
                //南瓜
            } else if (farmType == FarmType.PUMPKIN) {
                hoe1();
                //小麦
            } else if (farmType == FarmType.WHEAT) {
                hoe1();
                //甜菜根
            } else if (farmType == FarmType.BEETROOTS) {
                hoe1();
                //甘蔗
            } else if (farmType == FarmType.SUGAR) {
                //循环农场的宽
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.worldObj.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.worldObj.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.worldObj.isAirBlock(bp)) {
                            if (x % 3 == 0 && (x + 1) % 3 == 0) {
                                //包含灌木 不是庄家
                                if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                                    this.folk.entity.worldObj.destroyBlock(bp, false);
                                    //设置为空气
//                                this.folk.entity.worldObj.setBlockToAir(bp);
                                }
                                //如果当前为空气
                                if (this.folk.entity.worldObj.isAirBlock(bp)) {
                                    //锄地
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                    //设置为耕地
                                    this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 11);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        this.swingArmCheck = System.currentTimeMillis();
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                                            mc.theWorld.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                        }
                                        //设置手持无
                                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
//                                    this.folk.entity.swing();
                                    }

                                    //提升农民等级
                                    this.addFarmingLevel();
                                    //收获检查
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                }
                            } else if ((x + 2) % 3 == 0) {
                                if (b1 != Blocks.WATER) {
                                    //锄地
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                    //设置为灯箱
                                    this.folk.entity.worldObj.setBlockState(bp.down().down(), BlockLoader.blockLightBox.getDefaultState(), 0);
                                    //水
                                    this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 3);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                                            mc.theWorld.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                        }
                                        //设置手持无
                                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
//                                    this.folk.entity.swing();
                                    }
                                    //提升农民等级
                                    this.addFarmingLevel();
                                    //收获检查
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                }
                            } else {
                                //锄地
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                //设置为耕地
                                this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 11);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //播放声音
                                    Minecraft mc = Minecraft.getMinecraft();
                                    for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                                        mc.theWorld.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                    }
                                    //设置手持无
                                    this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                    //摇摆手臂
                                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                }
                                //提升农民等级
                                this.addFarmingLevel();
                                //收获检查
                                this.harvestCheck = System.currentTimeMillis();
//                            return;
                            }
                        }
                    }
                }
                this.job.stage = 2;
                //仙人掌
            } else if (farmType == FarmType.CACTUS) {
                //循环农场的宽
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.worldObj.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //设置为空气
                            //this.folk.entity.worldObj.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.worldObj.isAirBlock(bp)) {
                            if ((x + z) % 2 == 0) {
                                if (b1 != Blocks.SAND) {
                                    //锄地
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                    //设置为耕地
                                    this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.SAND.getDefaultState(), 3);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                                            mc.theWorld.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                        }
                                        //设置手持无
                                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                    }
                                    //提升农民等级
                                    this.addFarmingLevel();
                                    //收获检查
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                }
                            }
                        }
                    }
                }
                this.job.stage = 2;
                //未知的可可豆
            } else if (farmType == FarmType.COCOA) {
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.worldObj.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.worldObj.isAirBlock(bp)) {
                            if ((x + 2) % 3 == 0) {
                                //锄地
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                //设置为耕地
                                this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 3);
                                this.folk.entity.worldObj.setBlockState(bp, Blocks.LOG.getStateFromMeta(3), 3);
                                this.folk.entity.worldObj.setBlockState(bp.up(), Blocks.LOG.getStateFromMeta(3), 3);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //播放声音
                                    Minecraft mc = Minecraft.getMinecraft();
                                    for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                                        mc.theWorld.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                    }
                                    //设置手持无
                                    this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                    //摇摆手臂
                                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                }
                                //提升农民等级
                                this.addFarmingLevel();
                                //收获检查
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                        }
                    }
                }
                this.job.stage = 2;
            } else {
                //循环农场的宽
                hoe1();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-hoe出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 4;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 除了甘蔗和仙人掌的耕地
     * @Date 21:35 2023/2/25
     * @Param []
     **/
    public void hoe1() {
        try {
            //循环农场的宽
            for (int z = 0; z < this.farm.z; ++z) {
                //循环长
                for (int x = 0; x < this.farm.x; ++x) {

                    BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                    Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                    Block b1 = this.folk.entity.worldObj.getBlockState(bp.down()).getBlock();
                    //包含灌木 不是庄家
                    if (b instanceof BlockBush && !(b instanceof BlockCrops) && b != Blocks.PUMPKIN_STEM && b != Blocks.MELON_STEM) {
                        this.folk.entity.worldObj.destroyBlock(bp, false);
                        //设置为空气
//                    this.folk.entity.worldObj.setBlockToAir(bp);
                    }
                    //如果当前为空气
                    if (this.folk.entity.worldObj.isAirBlock(bp)) {

                        if (z % 5 == 0 && x % 5 == 0) {
                            //不是水 或流动的水
                            if (this.folk.entity.worldObj.getBlockState(bp.down()).getBlock() != Blocks.WATER && this.folk.entity.worldObj.getBlockState(bp.down()).getBlock() != Blocks.FLOWING_WATER) {
                                //锄地
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                //设置为灯箱
                                this.folk.entity.worldObj.setBlockState(bp.down().down(), BlockLoader.blockLightBox.getDefaultState(), 0);
                                //水
                                this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 11);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 6) {
                                    //播放声音
                                    for (EntityPlayer entityPlayer : this.folk.entity.worldObj.playerEntities) {
                                        this.folk.entity.worldObj.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                    }
                                    //摇摆手臂
                                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                }
                                //提升农民等级
                                this.addFarmingLevel();
                                //收获检查
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                            //草地 草 泥土
                        } else if (b1 != Blocks.FARMLAND) {
                            //锄地
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                            //设置为耕地
                            this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.FARMLAND.getDefaultState(), 11);
                            if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 6) {
                                //播放声音
                                for (EntityPlayer entityPlayer : this.folk.entity.worldObj.playerEntities) {
                                    this.folk.entity.worldObj.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
                                }
                                //设置手持无
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                //摇摆手臂
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            }
                            //提升农民等级
                            this.addFarmingLevel();
                            //收获检查
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    }
                }
            }
            this.job.stage = 2;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-hoe1出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 4;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 种植
     * @Date 10:13 2022/12/10
     * @Param []
     **/
    public void plant() {
        try {
            //先获取农田箱的 类型
            FarmType farmType = this.farm.farmType;
            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Planting"));
            //胡萝卜
            if (farmType == FarmType.CARROT) {
                plant1(new ItemStack(Items.CARROT));
                //西瓜
            } else if (farmType == FarmType.MELON) {
                plant1(new ItemStack(Items.MELON_SEEDS));
                //土豆
            } else if (farmType == FarmType.POTATO) {
                plant1(new ItemStack(Items.POTATO));
                //南瓜
            } else if (farmType == FarmType.PUMPKIN) {
                plant1(new ItemStack(Items.PUMPKIN_SEEDS));
                //小麦
            } else if (farmType == FarmType.WHEAT) {
                plant1(new ItemStack(Items.WHEAT_SEEDS));
                //甜菜根
            } else if (farmType == FarmType.BEETROOTS) {
                plant1(new ItemStack(Items.BEETROOT_SEEDS));
                //甘蔗
            } else if (farmType == FarmType.SUGAR) {
                //循环农场的宽
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        boolean hasBlock = false;
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.worldObj.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.worldObj.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.worldObj.isAirBlock(bp)) {
                            // 泥土 草地 沙子
                            if (b1 == Blocks.DIRT || b1 == Blocks.GRASS || b1 == Blocks.SAND) {
                                ItemStack seed = null;
                                List<IInventory> iterator = this.job.inventoriesFindClosest(this.job.workPlace, 5);
                                for (IInventory inv : iterator) {
                                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                        ItemStack slot = inv.getStackInSlot(i);
                                        if (slot != null) {
                                            Item item = slot.getItem();
                                            String unlocalizedName = item.getUnlocalizedName();
                                            if (item instanceof IPlantable || unlocalizedName.contains("reeds")) {
                                                seed = slot;
                                                break;
                                            }
                                        }
                                    }
                                    if (seed != null) {
                                        break;
                                    }
                                }
//可种植的种子为空
                                if (seed == null) {
                                    //没有种子
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.no_seeds"));
                                    this.job.stage = 2;
                                    this.noNeed++;
                                    if (this.noNeed / 10 == 1) {
                                        this.job.stage = 4;
                                    }
                                    return;
                                }
//                            IPlantable plantable = Blocks.REEDS;
                                try {
                                    for (IInventory inv : iterator) {
                                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                            ItemStack slot = inv.getStackInSlot(i);
                                            //可种植
                                            if (slot != null && slot.isItemEqual(seed)) {
                                                hasBlock = true;
                                                inv.decrStackSize(i, 1);
                                                break;
                                            }
                                        }
                                        if (hasBlock) {
                                            break;
                                        }
                                    }

                                    //设置状态 种植 作物
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Planting") + " " + seed.getDisplayName());
                                    //种植 作物
                                    this.folk.entity.worldObj.setBlockState(bp, Blocks.REEDS.getDefaultState());
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                    }
//                        seed.shrink(1);
                                    //增加农民等级
                                    this.addFarmingLevel();
                                    //更新收获时间
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                } catch (Exception e) {
                                    StackTraceElement element = e.getStackTrace()[0];
                                    ModSimLoader.log.error("种植甘蔗发生了错误：" + e.getMessage() + "行数：" + element.getLineNumber());
                                }

                            }
                        }
                    }
                }
                this.job.stage = 4;
                //仙人掌
            } else if (farmType == FarmType.CACTUS) {
                //循环农场的宽
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        boolean hasBlock = false;
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.worldObj.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b != Blocks.CACTUS) {
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.worldObj.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.worldObj.isAirBlock(bp)) {
                            if (b1 == Blocks.SAND) {

                                ItemStack seed = null;
                                List<IInventory> iterator = this.job.inventoriesFindClosest(this.job.workPlace, 5);
                                for (IInventory inv : iterator) {
                                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                        ItemStack slot = inv.getStackInSlot(i);
                                        if (slot != null) {
                                            Item item = slot.getItem();
                                            String unlocalizedName = item.getUnlocalizedName();
                                            if (item instanceof IPlantable || unlocalizedName.contains("cactus")) {
                                                seed = slot;
                                                break;
                                            }
                                        }
                                    }
                                    if (seed != null) {
                                        break;
                                    }
                                }
                                //可种植的种子为空
                                if (seed == null) {
                                    //没有种子
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.no_seeds"));
                                    this.job.stage = 2;
                                    this.noNeed++;
                                    if (this.noNeed / 10 == 1) {
                                        this.job.stage = 4;
                                    }
                                    return;
                                }
                                try {
                                    for (IInventory inv : iterator) {
                                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                            ItemStack slot = inv.getStackInSlot(i);
                                            //可种植
                                            if (slot != null && slot.isItemEqual(seed)) {
                                                hasBlock = true;
                                                inv.decrStackSize(i, 1);
                                                break;
                                            }
                                        }
                                        if (hasBlock) {
                                            break;
                                        }
                                    }

                                    //设置状态 种植 作物
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Planting") + " " + seed.getDisplayName());
                                    //种植 作物
                                    this.folk.entity.worldObj.setBlockState(bp, Blocks.CACTUS.getDefaultState());
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                    }
//                        seed.shrink(1);
                                    //增加农民等级
                                    this.addFarmingLevel();
                                    //更新收获时间
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                } catch (Exception e) {
                                    StackTraceElement element = e.getStackTrace()[0];
                                    ModSimLoader.log.error("种植仙人掌发生了错误：" + e.getMessage() + "行数：" + element.getLineNumber());
                                }

                            }
                        }
                    }
                }
                this.job.stage = 4;
                //可可豆
            } else if (farmType == FarmType.COCOA) {
                //未知的
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        for (int y = 0; y < 2; ++y) {
                            boolean hasBlock = false;
                            BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                            bp = new BlockPos(bp.getX(), bp.getY() + y, bp.getZ());
                            //包含灌木 不是庄家
                            boolean f1 = this.folk.entity.worldObj.isAirBlock(bp);
                            //如果当前为空气
                            if (f1) {
                                ItemStack seed = null;
                                List<IInventory> iterator = this.job.inventoriesFindClosest(this.job.workPlace, 5);
                                for (IInventory inv : iterator) {
                                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                        ItemStack slot = inv.getStackInSlot(i);
                                        if (slot != null) {
                                            Item item = slot.getItem();
                                            String unlocalizedName = item.getUnlocalizedName();
                                            if (item instanceof IPlantable || unlocalizedName.contains("item.dyePowder")) {
                                                seed = slot;
                                                break;
                                            }
                                        }
                                    }
                                    if (seed != null) {
                                        break;
                                    }
                                }
                                //可种植的种子为空
                                if (seed == null) {
                                    //没有种子
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.no_seeds"));
                                    this.job.stage = 2;
                                    this.noNeed++;
                                    if (this.noNeed / 10 == 1) {
                                        this.job.stage = 4;
                                    }
                                    return;
                                }
                                try {
                                    for (IInventory inv : iterator) {
                                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                            ItemStack slot = inv.getStackInSlot(i);
                                            //可种植
                                            if (slot != null && slot.isItemEqual(seed)) {
                                                hasBlock = true;
                                                inv.decrStackSize(i, 1);
                                                break;
                                            }
                                        }
                                        if (hasBlock) {
                                            break;
                                        }
                                    }
                                    //设置状态 种植 作物
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Planting") + " " + seed.getDisplayName());
                                    EnumFacing facing = EnumFacing.NORTH;
                                    Block north = this.folk.entity.worldObj.getBlockState(bp.north()).getBlock();
                                    //东
                                    Block east = this.folk.entity.worldObj.getBlockState(bp.east()).getBlock();
                                    //南
                                    Block south = this.folk.entity.worldObj.getBlockState(bp.south()).getBlock();
                                    //西
                                    Block west = this.folk.entity.worldObj.getBlockState(bp.west()).getBlock();
                                    Block fs_bss = Blocks.LOG.getStateFromMeta(3).getBlock();
                                    if (north == fs_bss) {
                                        facing = EnumFacing.SOUTH;
                                    } else if (east == fs_bss) {
                                        facing = EnumFacing.WEST;
                                    } else if (south == fs_bss) {
                                        facing = EnumFacing.NORTH;
                                    } else if (west == fs_bss) {
                                        facing = EnumFacing.EAST;
                                    }
                                    //种植 作物
                                    IBlockState iblockstate1 = Blocks.COCOA.onBlockPlaced(this.folk.entity.worldObj, bp, facing, bp.getX(), bp.getY(), bp.getZ(), 0, this.folk.entity);
                                    this.folk.entity.worldObj.setBlockState(bp, iblockstate1, 10);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                    }
//                        seed.shrink(1);
                                    //增加农民等级
                                    this.addFarmingLevel();
                                    //更新收获时间
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                } catch (Exception e) {
                                    StackTraceElement element = e.getStackTrace()[0];
                                    ModSimLoader.log.error("种植仙人掌发生了错误：" + e.getMessage() + "行数：" + element.getLineNumber());
                                }
                            }
                        }
                    }
                }
                this.job.stage = 4;
            } else {
                plant1(null);
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-plant出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 4;
        }

    }

    public void plant1(ItemStack seeds) {
        try {
            //循环宽
            for (int z = 0; z < this.farm.z; ++z) {
                //循环长
                for (int x = 0; x < this.farm.x; ++x) {
                    boolean hasBlock = false;
                    //获得角落
                    BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
//                ItemStack seed = ItemStack.EMPTY;
                    ItemStack seed = null;
                    List<IInventory> iterator = this.job.inventoriesFindClosest(this.job.workPlace, 5);
                    for (IInventory inv : iterator) {
                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                            ItemStack slot = inv.getStackInSlot(i);
                            if (slot != null) {
                                //可种植 / 是甘蔗
                                if (seeds == null) {
                                    Item item = slot.getItem();
                                    String unlocalizedName = item.getUnlocalizedName();
                                    if (item instanceof IPlantable || unlocalizedName.contains("reeds")) {
                                        seed = slot;
                                        break;
                                    }
                                } else if (slot.isItemEqual(seeds)) {
                                    seed = slot;
                                    break;
                                }
                            }

                        }
                        if (seed != null) {
                            break;
                        }
                    }
                    //可种植的种子为空
                    if (seed == null) {
                        //没有种子
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.no_seeds"));
                        this.job.stage = 2;
                        this.noNeed++;
                        if (this.noNeed / 10 == 1) {
                            this.noNeed = 0;
                            this.job.stage = 4;
                        }
                        return;
                    }
                    try {
                        Block crop = null;
                        //种子为可种植
                        Item item = seed.getItem();
                        String uName = item.getUnlocalizedName();
                        //甘蔗 reeds
                        if (uName.contains("reeds")) {
                            crop = Blocks.REEDS;
                            //小麦 seeds
                        } else if (uName.equals("item.seeds")) {
                            crop = Blocks.WHEAT;
                            //西瓜
                        } else if (uName.equals("item.seeds_melon")) {
                            crop = Blocks.MELON_STEM;
                            //南瓜
                        } else if (uName.equals("item.seeds_pumpkin")) {
                            crop = Blocks.PUMPKIN_STEM;
                            //胡萝卜
                        } else if (uName.equals("item.carrots")) {
                            crop = Blocks.CARROTS;
                            //土豆
                        } else if (uName.equals("item.potato")) {
                            crop = Blocks.POTATOES;
                            //甜菜根
                        } else if (uName.equals("item.beetroot_seeds")) {
                            crop = Blocks.BEETROOTS;
                            //仙人掌
                        } else if (uName.equals("item.cactus")) {
                            crop = Blocks.CACTUS;
                            //可可豆
                        } else if (uName.equals("item.dye")) {
                            crop = Blocks.COCOA;
                        } else {
                            IPlantable plantable = (IPlantable) item;
                            Field cropsField = plantable.getClass().getDeclaredField("crops");
//                    //设置成可访问
                            cropsField.setAccessible(true);
//                    //获取作物方块
                            crop = (Block) cropsField.get(plantable);
                        }

//                    //获取种植下方的物品是否是 农田等可种植区域
                        IBlockState soil = this.folk.entity.worldObj.getBlockState(bp.down());
                        //Block crop=plantable.getPlant(this.folk.entity.worldObj,bp.down()).getBlock();
                        //确定此块是否可以支持传入的植物，允许其种植和生长。一些例子：芦苇检查它是否是芦苇，或者它的沙子/泥土/草和水附近的仙人掌检查它是否为仙人掌，或者如果它的沙子Nether类型检查灵魂沙子作物检查耕土洞穴检查它是否坚硬地面平原检查它的草或泥土水检查它是否静止
                        boolean fs_canSustainPlant = soil.getBlock().canSustainPlant(soil, this.folk.entity.worldObj, bp.down(), EnumFacing.UP, (IPlantable) item);
                        //北

                        /* Chunk chunk =this.folk.entity.worldObj.getChunkFromChunkCoords(bp.north().getX(),bp.west().getZ());*/
                        Block north = this.folk.entity.worldObj.getBlockState(bp.north()).getBlock();
                        boolean f1 = north instanceof BlockStem;
                        //东
                        /*Chunk chunk1 =this.folk.entity.worldObj.getChunkFromChunkCoords(bp.east().getX(),bp.west().getZ());*/
                        Block east = this.folk.entity.worldObj.getBlockState(bp.east()).getBlock();
                        boolean f2 = east instanceof BlockStem;
                        //南
                        //Chunk chunk2 =this.folk.entity.worldObj.getChunkFromChunkCoords(bp.south().getX(),bp.west().getZ());
                        Block south = this.folk.entity.worldObj.getBlockState(bp.south()).getBlock();
                        boolean f3 = south instanceof BlockStem;
                        //西
                        //Chunk chunk3 =this.folk.entity.worldObj.getChunkFromChunkCoords(bp.west().getX(),bp.west().getZ());
                        Block west = this.folk.entity.worldObj.getBlockState(bp.west()).getBlock();
                        boolean f4 = west instanceof BlockStem;
                        IBlockState iBlockState = this.folk.entity.worldObj.getBlockState(bp);
                        Block block = iBlockState.getBlock();
                        boolean f7 = block instanceof BlockStem;
                        //是否是空气方块
                        boolean f5 = this.folk.entity.worldObj.isAirBlock(bp);
                        String b_u_name = block.getUnlocalizedName();
//                    System.out.println("当前种植的作物是：" + b_u_name);
                        if (!f1 && !f2 && !f3 && fs_canSustainPlant && !f4 && f5) {
                            for (IInventory inv : iterator) {
                                for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                    ItemStack slot = inv.getStackInSlot(i);
                                    //可种植
                                    if (slot != null && slot.isItemEqual(seed)) {
                                        hasBlock = true;
                                        inv.decrStackSize(i, 1);
                                        break;
                                    }
                                }
                                if (hasBlock) {
                                    break;
                                }
                            }
                            //设置状态 种植 作物
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Planting") + " " + seed.getDisplayName());
                            //种植 作物
                            this.folk.entity.worldObj.setBlockState(bp, crop.getDefaultState());
                            if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                //摇摆手臂
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            }
//                        seed.shrink(1);
                            //增加农民等级
                            this.addFarmingLevel();
                            //更新收获时间
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    } catch (Exception var10) {
                        StackTraceElement element = var10.getStackTrace()[0];
                        ModSimLoader.log.error("种植发生了错误：" + var10.getMessage() + "行数：" + element.getLineNumber());
                    }
                }
            }
            this.job.stage = 4;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-plant1出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 4;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 使用骨粉快速生长
     * @Date 11:33 2023/2/12
     * @Param []
     **/
    public void grow() {
        try {
            //循环宽
            for (int z = 0; z < this.farm.z; ++z) {
                //循环长
                for (int x = 0; x < this.farm.x; ++x) {
                    //获得位置
                    BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));

                    IBlockState iblockstate = this.folk.entity.worldObj.getBlockState(bp);
                    //是否可以生长
                    if (iblockstate.getBlock() instanceof IGrowable) {
                        IGrowable igrowable = (IGrowable) iblockstate.getBlock();
                        if (igrowable.canGrow(this.folk.entity.worldObj, bp, iblockstate, this.folk.entity.worldObj.isRemote)) {
                            if (!this.folk.entity.worldObj.isRemote) {
                                if (igrowable.canUseBonemeal(this.folk.entity.worldObj, this.folk.entity.worldObj.rand, bp, iblockstate)) {
                                    List<IInventory> iterator = this.job.inventoriesFindClosest(this.job.workPlace, 5);
                                    boolean hasBlock = false;
                                    ItemStack dye = null;
                                    for (IInventory inv : iterator) {
                                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                            ItemStack slot = inv.getStackInSlot(i);
                                            ItemStack itemStack = new ItemStack(Items.DYE, 1, 15);
                                            //可种植
                                            if (slot != null && slot.isItemEqual(itemStack)) {
                                                hasBlock = true;
                                                dye = slot;
                                                inv.decrStackSize(i, 1);
                                                break;
                                            }
                                        }
                                        if (hasBlock) {
                                            break;
                                        }
                                    }
                                    if (dye != null) {
                                        igrowable.grow(this.folk.entity.worldObj, this.folk.entity.worldObj.rand, bp, iblockstate);
                                    } else {
                                        if ((System.currentTimeMillis() - this.qsCheck) > 3000 * 60) {
                                            this.qsCheck = System.currentTimeMillis();
                                            ModSimLoader.sendChat(this.farm.farmType + I18n.format("container.sim.job.crop.farmer.dye"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.job.stage = 3;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-grow出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 3;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 收获
     * @Date 10:44 2022/12/10
     * @Param []
     **/
    public void harvest() {

        try {

            if (this.farm != null) {
                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                //先获取农田箱的 类型
                FarmType farmType = this.farm.farmType;
                //胡萝卜
                if (farmType == FarmType.CARROT) {
                    harvest1();
                    //西瓜
                } else if (farmType == FarmType.MELON) {
                    //循环宽
                    for (int z = 0; z < this.farm.z; ++z) {
                        //循环长
                        for (int x = 0; x < this.farm.x; ++x) {
                            //声明作物
                            List<ItemStack> drops = new CopyOnWriteArrayList<>();
                            //获取当前位置的物品
                            BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                            IBlockState iBlockState = this.folk.entity.worldObj.getBlockState(bp);
                            //获得方块
                            Block b = iBlockState.getBlock();
                            //是西瓜
                            if (b == Blocks.MELON_BLOCK) {
                                //收获
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                //获得方块
                                Block bCrop = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                                //摧毁方块
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 1);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
                                drops.forEach((drop) -> {
                                    //放到工作箱
                                    this.job.placeInJobChest(drop);
                                });
                                //设置手持物
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //摇摆手臂
                                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                }
                                this.folk.entity.worldObj.destroyBlock(bp, false);
                                //设置为空
//                            this.folk.entity.worldObj.setBlockToAir(bp);
                                //增加农民等级
                                this.addFarmingLevel();
                                //设置收获时间
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }

                        }
                    }
                    this.job.stage = 1;
//            harvest1();
                    //土豆
                } else if (farmType == FarmType.POTATO) {
                    harvest1();
                    //南瓜
                } else if (farmType == FarmType.PUMPKIN) {
//            harvest1();
                    //循环宽
                    for (int z = 0; z < this.farm.z; ++z) {
                        //循环长
                        for (int x = 0; x < this.farm.x; ++x) {
                            //声明作物
                            List<ItemStack> drops = new CopyOnWriteArrayList<>();
                            //获取当前位置的物品
                            BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                            IBlockState iBlockState = this.folk.entity.worldObj.getBlockState(bp);
                            //获得方块
                            Block b = iBlockState.getBlock();
                            //是南瓜
                            if (b == Blocks.PUMPKIN) {
                                //收获
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                //获得方块
                                Block bCrop = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                                //摧毁方块
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 1);
                                drops.forEach((drop) -> {
                                    //放到工作箱
                                    this.job.placeInJobChest(drop);
                                });
                                //设置手持物
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //摇摆手臂
                                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                }
                                this.folk.entity.worldObj.destroyBlock(bp, false);
                                //设置为空
//                            this.folk.entity.worldObj.setBlockToAir(bp);
                                //增加农民等级
                                this.addFarmingLevel();
                                //设置收获时间
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }

                        }
                    }
                    this.job.stage = 1;
                    //小麦
                } else if (farmType == FarmType.WHEAT) {
                    harvest1();
                    //甜菜根
                } else if (farmType == FarmType.BEETROOTS) {
                    harvest1();
                    //甘蔗
                } else if (farmType == FarmType.SUGAR) {
                    //循环农场的宽
                    harvest1();
                    //仙人掌
                } else if (farmType == FarmType.CACTUS) {
                    harvest1();
                    //未知的 可可豆
                } else if (farmType == FarmType.COCOA) {
                    for (int z = 0; z < this.farm.z; ++z) {
                        //循环长
                        for (int x = 0; x < this.farm.x; ++x) {
                            for (int y = 0; y < 2; ++y) {
                                //声明作物
                                List<ItemStack> drops = new CopyOnWriteArrayList<>();
                                //获取当前位置的物品
                                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                                bp = new BlockPos(bp.getX(), bp.getY() + y, bp.getZ());
                                IBlockState iBlockState = this.folk.entity.worldObj.getBlockState(bp);
                                //获得方块
                                Block b = iBlockState.getBlock();
                                //是可可豆
                                if (b == Blocks.COCOA && b.getMetaFromState(iBlockState) == 9) {
                                    //收获
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                    //获得方块
                                    Block bCrop = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                                    //摧毁方块
                                    drops = bCrop.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 1);
                                    drops.forEach((drop) -> {
                                        //放到工作箱
                                        this.job.placeInJobChest(drop);
                                    });
                                    //设置手持物
                                    this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //摇摆手臂
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                    }
                                    this.folk.entity.worldObj.destroyBlock(bp, false);
                                    //设置为空
//                                this.folk.entity.worldObj.setBlockToAir(bp);
                                    //增加农民等级
                                    this.addFarmingLevel();
                                    //设置收获时间
                                    this.harvestCheck = System.currentTimeMillis();
                                    return;
                                }
                            }
                        }
                    }
                    this.job.stage = 1;
                    //未知的
                } else {
                    harvest1();
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-harvest出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    public void harvest1() {
        try {
            //循环宽
            for (int z = 0; z < this.farm.z; ++z) {
                //循环长
                for (int x = 0; x < this.farm.x; ++x) {
                    //声明作物
                    List<ItemStack> drops = new CopyOnWriteArrayList<>();
                    //获取当前位置的物品
                    BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                    IBlockState iBlockState = this.folk.entity.worldObj.getBlockState(bp);
                    //获得方块
                    Block b = iBlockState.getBlock();
                    //有根茎 不是作物 不是可种植 不是可生长
                    if (!(b instanceof BlockCrops) && !(b instanceof IPlantable) && !(b instanceof IGrowable)) {
                        //包含根茎
                        if (!(b instanceof BlockPumpkin) && !(b instanceof BlockMelon) && !(b instanceof BlockCocoa) && !(b instanceof BlockCactus) && b != Blocks.AIR) {
                            //
                            BlockCrops crop = (BlockCrops) b;
                            if (crop.isMaxAge(iBlockState)) {
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                //获得方块
                                Block bCrop = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                                //摧毁方块
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 1);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
                                drops.forEach((drop) -> {
                                    //放到工作箱
                                    this.job.placeInJobChest(drop);
                                });
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.folk.entity.worldObj.destroyBlock(bp, false);
//                            this.folk.entity.worldObj.setBlockToAir(bp);
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                        }
                        //甘蔗
                    } else if (b == Blocks.REEDS) {
                        if (this.folk.entity.worldObj.getBlockState(bp.up()).getBlock() == b) {
                            //收获
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                            //获得方块
                            Block bCrop = this.folk.entity.worldObj.getBlockState(bp.up()).getBlock();
                            //摧毁方块
                            drops = bCrop.getDrops(this.folk.entity.worldObj, bp.up(), this.folk.entity.worldObj.getBlockState(bp.up()), 1);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
                            drops.forEach((drop) -> {
                                //放到工作箱
                                this.job.placeInJobChest(drop);
                            });
                            //设置手持物
                            this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                            //摇摆手臂
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            //设置为空
                            this.folk.entity.worldObj.destroyBlock(bp.up(), false);
                            //this.folk.entity.worldObj.setBlockToAir(bp.up());
                            //增加农民等级
                            this.addFarmingLevel();
                            //设置收获时间
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    } else if (b == Blocks.CACTUS) {
                        if (this.folk.entity.worldObj.getBlockState(bp.up()).getBlock() == b) {
                            //收获
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                            //获得方块
                            Block bCrop = this.folk.entity.worldObj.getBlockState(bp.up()).getBlock();
                            //摧毁方块
                            drops = bCrop.getDrops(this.folk.entity.worldObj, bp.up(), this.folk.entity.worldObj.getBlockState(bp.up()), 1);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
                            drops.forEach((drop) -> {
                                //放到工作箱
                                this.job.placeInJobChest(drop);
                            });
                            //设置手持物
                            this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                            //摇摆手臂
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            this.folk.entity.worldObj.destroyBlock(bp.up(), false);
                            //设置为空
                            //this.folk.entity.worldObj.setBlockToAir(bp.up());
                            //增加农民等级
                            this.addFarmingLevel();
                            //设置收获时间
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    } else {
                        BlockCrops crop = (BlockCrops) b;
                        if (crop.isMaxAge(iBlockState)) {
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                            //获得方块
                            Block bCrop = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                            //摧毁方块
                            drops = bCrop.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 1);
                            drops.forEach((drop) -> {
                                //放到工作箱
                                this.job.placeInJobChest(drop);
                            });
                            this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            this.folk.entity.worldObj.destroyBlock(bp, false);
                            //this.folk.entity.worldObj.setBlockToAir(bp);
                            this.addFarmingLevel();
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    }
                }
            }
            this.job.stage = 1;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];

            ModSimLoader.log.error("JobFarmer-harvest1出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
            this.job.stage = 1;
        }
    }

    @Override
    public void onTaskComplete() {

    }
}
