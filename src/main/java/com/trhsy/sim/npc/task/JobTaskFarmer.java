package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
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

    public JobTaskFarmer(Job j, long ms, String status, FarmBox farm) {
        super(j, ms);
        this.status = status;
        this.noNeed = 0;
        if(farm==null){
            this.farm = ModSimLoader.getFarm(j.workPlace);;
        }else{
            this.farm = farm;
        }

    }

    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
        //锄地
        this.hoe();
    }

    @Override
    public void onUpdate() {
        try {
            //ModSimLoader.log.info("农民任务检查onUpdate："+System.currentTimeMillis());
            //收获检查
            //this.harvestCheck = System.currentTimeMillis();

            if (ModSimLoader.money > 0.02F) {
                //ModSimLoader.log.info("农民任务检查onUpdate-money："+ModSimLoader.money);
                //周围五格内查找箱子
                if (this.job.jobChests == null) {
                    //没有箱子
                    this.folk.setStatus(I18n.format("container.sim.WAITINGFORCHEST"));
                    return;
                }
                boolean falg = false;
                if (ModSimLoader.gamemode == 1) {
                    falg =(System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F*10;
                }
                // 等级 时间计算 工作效率
                if (falg||(float) (System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F * this.folk.skillFarming) {

                    this.hoe();

                }

            } else {
                //没有钱付给我
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
            }

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-onUpdate出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
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
            //ModSimLoader.log.info("农民任务检查：" + System.currentTimeMillis());
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
                        Block b = this.folk.entity.world.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.world.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.world.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.world.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.world.isAirBlock(bp)) {
                            if (x % 3 == 0 && (x + 1) % 3 == 0) {
                                //包含灌木 不是庄家
                                if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                                    this.folk.entity.world.destroyBlock(bp, false);
                                    //设置为空气
//                                this.folk.entity.world.setBlockToAir(bp);
                                }
                                //如果当前为空气
                                if (this.folk.entity.world.isAirBlock(bp)) {
                                    //锄地
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                    //设置为耕地
                                    this.folk.entity.world.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 11);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        this.swingArmCheck = System.currentTimeMillis();
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                            mc.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                                    this.folk.entity.world.setBlockState(bp.down().down(), BlockLoader.blockLightBox.getDefaultState(), 0);
                                    //水
                                    this.folk.entity.world.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 3);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                            mc.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                                this.folk.entity.world.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 11);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //播放声音
                                    Minecraft mc = Minecraft.getMinecraft();
                                    for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                        mc.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                this.completed = true;
                //仙人掌
            } else if (farmType == FarmType.CACTUS) {
                //循环农场的宽
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.world.getBlockState(bp).getBlock();
                        Block b1 = this.folk.entity.world.getBlockState(bp.down()).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.world.destroyBlock(bp, false);
                            //设置为空气
                            //this.folk.entity.world.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.world.isAirBlock(bp)) {
                            if ((x + z) % 2 == 0) {
                                if (b1 != Blocks.SAND) {
                                    //锄地
                                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                    //设置为耕地
                                    this.folk.entity.world.setBlockState(bp.down(), Blocks.SAND.getDefaultState(), 3);
                                    if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                        //播放声音
                                        Minecraft mc = Minecraft.getMinecraft();
                                        for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                            mc.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                this.completed = true;
                //未知的可可豆
            } else if (farmType == FarmType.COCOA) {
                for (int z = 0; z < this.farm.z; ++z) {
                    //循环长
                    for (int x = 0; x < this.farm.x; ++x) {
                        BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                        Block b = this.folk.entity.world.getBlockState(bp).getBlock();
                        //包含灌木 不是庄家
                        if (b instanceof BlockBush && !(b instanceof BlockCrops)) {
                            this.folk.entity.world.destroyBlock(bp, false);
                            //设置为空气
//                        this.folk.entity.world.setBlockToAir(bp);
                        }
                        //如果当前为空气
                        if (this.folk.entity.world.isAirBlock(bp)) {
                            if ((x + 2) % 3 == 0) {
                                //锄地
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                //设置为耕地
                                this.folk.entity.world.setBlockState(bp.down(), Blocks.DIRT.getDefaultState(), 3);
                                this.folk.entity.world.setBlockState(bp, Blocks.LOG.getStateFromMeta(3), 3);
                                this.folk.entity.world.setBlockState(bp.up(), Blocks.LOG.getStateFromMeta(3), 3);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 3) {
                                    //播放声音
                                    Minecraft mc = Minecraft.getMinecraft();
                                    for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                        mc.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                this.completed = true;
            } else {
                //循环农场的宽
                hoe1();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-hoe出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
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
            //ModSimLoader.log.info("农民任务检查hoe1：" + System.currentTimeMillis());
            //循环农场的宽
            for (int z = 0; z < this.farm.z; ++z) {
                //循环长
                for (int x = 0; x < this.farm.x; ++x) {

                    BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                    Block b = this.folk.entity.world.getBlockState(bp).getBlock();
                    Block b1 = this.folk.entity.world.getBlockState(bp.down()).getBlock();
                    //包含灌木 不是庄家
                    if (b instanceof BlockBush && !(b instanceof BlockCrops) && b != Blocks.PUMPKIN_STEM && b != Blocks.MELON_STEM) {
                        this.folk.entity.world.destroyBlock(bp, false);
                        //设置为空气
//                    this.folk.entity.world.setBlockToAir(bp);
                    }
                    //如果当前为空气
                    if (this.folk.entity.world.isAirBlock(bp)) {

                        if (z % 5 == 0 && x % 5 == 0) {
                            //不是水 或流动的水
                            if (this.folk.entity.world.getBlockState(bp.down()).getBlock() != Blocks.WATER && this.folk.entity.world.getBlockState(bp.down()).getBlock() != Blocks.FLOWING_WATER) {
                                //锄地
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                                //设置为灯箱
                                this.folk.entity.world.setBlockState(bp.down().down(), BlockLoader.blockLightBox.getDefaultState(), 0);
                                //水
                                this.folk.entity.world.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 11);
                                if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 6) {
                                    //播放声音
                                    for (EntityPlayer entityPlayer : this.folk.entity.world.playerEntities) {
                                        this.folk.entity.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
                            this.folk.entity.world.setBlockState(bp.down(), Blocks.FARMLAND.getDefaultState(), 11);
                            if ((System.currentTimeMillis() - this.swingArmCheck) > 1000 * 6) {
                                //播放声音
                                for (EntityPlayer entityPlayer : this.folk.entity.world.playerEntities) {
                                    this.folk.entity.world.playSound(entityPlayer, bp.getX(), bp.getY(), bp.getZ(), SoundEvents.ITEM_HOE_TILL, SoundCategory.AMBIENT, 1.0F, 1.0F);
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
            this.completed = true;
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-hoe1出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }


    @Override
    public void onTaskComplete() {

    }
}
