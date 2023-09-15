package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.block.FarmBox;
import com.trhsy.sim.npc.job.Job;
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
public class JobTaskFarmerGrow extends JobTask {
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

    public JobTaskFarmerGrow(Job j, long ms, String status, FarmBox farm) {
        super(j, ms);
        this.status = status;
        this.noNeed = 0;
        this.farm = farm;
    }

    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
        if (ModSimLoader.money > 0.02F) {
            boolean falg = false;
            if (ModSimLoader.gamemode == 1) {
                falg = (System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F * 10;
            }
            // 等级 时间计算 工作效率
            if (falg || (float) (System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F * this.folk.skillFarming) {
                Random ra = new Random();
                int r = ra.nextInt(2);
                if (r == 0) {
                    //在公众号'dasha500'找作者玩
                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Facebook"));
                    if ((System.currentTimeMillis() - this.growCheck) > 3000 * 60) {
                        this.growCheck = System.currentTimeMillis();
                        this.qsCheck = System.currentTimeMillis();
                        grow();
                    }
                } else if (r == 1) {
                    //照料作物
                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tending") + "," + this.farm.farmType);
                    //用骨粉快速生长作物
                    if ((System.currentTimeMillis() - this.growCheck) > 3000 * 60) {
                        this.growCheck = System.currentTimeMillis();
                        this.qsCheck = System.currentTimeMillis();
                        grow();
                    }
                }
            }
        }

    }

    @Override
    public void onUpdate() {
        try {
            //ModSimLoader.log.info("农民任务检查："+System.currentTimeMillis());
            //收获检查
            //this.harvestCheck = System.currentTimeMillis();
            if (ModSimLoader.money > 0.02F) {
                //周围五格内查找箱子
                if (this.job.jobChests == null) {
                    //没有箱子
                    this.folk.setStatus(I18n.format("container.sim.WAITINGFORCHEST"));
                    return;
                }
//                boolean falg = false;
//                if (ModSimLoader.gamemode == 1) {
//                    falg =(System.currentTimeMillis() - this.harvestCheck) > 1000.0F- 100.0F*10;
//                }
                // 等级 时间计算 工作效率
                if ((float) (System.currentTimeMillis() - this.harvestCheck) > 1000.0F - 100.0F * this.folk.skillFarming) {

                    Random ra = new Random();
                    int r = ra.nextInt(2);
                    if (r == 0) {
                        //在公众号'dasha500'找作者玩
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Facebook"));
                        if ((System.currentTimeMillis() - this.growCheck) > (3000 * 60)) {
                            this.growCheck = System.currentTimeMillis();
                            grow();
                        }
                    } else if (r == 1) {
                        //照料作物
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tending") + "," + this.farm.farmType);
                        //用骨粉快速生长作物
                        if ((System.currentTimeMillis() - this.growCheck) > (3000 * 60)) {
                            this.growCheck = System.currentTimeMillis();
                            grow();
                        }
                    }
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

                    IBlockState iblockstate = this.folk.entity.world.getBlockState(bp);
                    //是否可以生长
                    if (iblockstate.getBlock() instanceof IGrowable) {
                        IGrowable igrowable = (IGrowable) iblockstate.getBlock();
                        if (igrowable.canGrow(this.folk.entity.world, bp, iblockstate, this.folk.entity.world.isRemote)) {
                            if (!this.folk.entity.world.isRemote) {
                                if (igrowable.canUseBonemeal(this.folk.entity.world, this.folk.entity.world.rand, bp, iblockstate)) {
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
                                        addFarmingLevel();
                                        igrowable.grow(this.folk.entity.world, this.folk.entity.world.rand, bp, iblockstate);
                                    }else{
                                        if ((System.currentTimeMillis() - this.qsCheck) > 3000 * 60) {
                                            this.qsCheck = System.currentTimeMillis();
                                            ModSimLoader.sendChat(this.farm.farmType + I18n.format("container.sim.job.crop.farmer.dye"));
                                            this.completed = true;
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobFarmer-grow出错了:" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }


    @Override
    public void onTaskComplete() {

    }
}
