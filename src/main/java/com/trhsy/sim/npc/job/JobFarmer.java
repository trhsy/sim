package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.block.FarmBox;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobFarmer
 * @Description todo 农民
 * @Author TRHSY
 * @Date 2022/12/622:48
 **/
public class JobFarmer extends Job {
    public FarmBox farm;
    //收获检查
    long harvestCheck = 0L;
    long qsCheck = 0L;
    long growCheck = 0L;
    public JobFarmer(NpcData folk, BlockPos pos, World world, FarmBox fb) {
        super(folk, pos, world);
        this.jobName = I18n.format("container.sim.Vocation5");
        this.farm = fb;
    }

    public void onArrive() {
        //收获检查
        this.harvestCheck = System.currentTimeMillis();
        //手持锡锄头
        this.folk.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemLoader.tinHoe));
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更新
     * @Date 21:04 2022/12/8
     * @Param []
     **/
    public void onUpdate() {
        super.onUpdate();
        if (this.atWork) {
            if (ModSimLoader.states.credits > 0.02F) {
                //周围五格内查找箱子
                if (this.findJobChests(5).size() == 0) {
                    //没有箱子
                    this.folk.setStatus(I18n.format("container.sim.WAITINGFORCHEST"));
                    return;
                }
                //改收获了 锄地
                if ((float) (System.currentTimeMillis() - this.harvestCheck) > 1500.0F - 100.0F * this.folk.skillFarming) {
                    this.hoe();
                }
                // 种植
                if ((float) (System.currentTimeMillis() - this.harvestCheck) > 1500.0F - 100.0F * this.folk.skillFarming) {
                    this.plant();
                }
                //收割
                if ((float) (System.currentTimeMillis() - this.harvestCheck) > 1500.0F - 100.0F * this.folk.skillFarming) {
                    this.harvest();
                } else {
                    Random ra = new Random();
                    int r = ra.nextInt(2);
                    if (r == 0) {
                        //在公众号'dasha500'找作者玩
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Facebook"));
                        if((System.currentTimeMillis() - this.growCheck)>1000*60){
                            this.growCheck=System.currentTimeMillis();
                            grow();
                        }
                    } else if (r == 1) {
                        //照料作物
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tending"));
                        //用骨粉快速生长作物
                        if((System.currentTimeMillis() - this.growCheck)>1000*60){
                            this.growCheck=System.currentTimeMillis();
                            grow();
                        }
                    } /*else if (r == 2) {
                        //但愿我有一辆拖拉机
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Wishing"));
                    } else if (r == 3) {
                        //休息一下
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Having"));
                    } else if (r == 4) {
                        //清理锄头上的污垢
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Cleaning"));
                    } else if (r == 5) {
                        //磨锄头
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Sharpening"));
                    } else if (r == 6) {
                        //吃我的午餐
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Eating"));
                    } else if (r == 7) {
                        //网格化我的样条曲线
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Reticulating"));
                    } else if (r == 8) {
                        //放松一下
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Relaxing"));
                    } else if (r == 9) {
                        //查看天气预报
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Checking"));
                    }*/

                }
            } else {
                //没有钱付给我
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
            }
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

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 锄地
     * @Date 21:11 2022/12/8
     * @Param []
     **/
    public void hoe() {
        //循环农场的宽
        for (int z = 0; z < this.farm.z; ++z) {
            //循环长
            for (int x = 0; x < this.farm.x; ++x) {

                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                IBlockState soil = this.folk.entity.worldObj.getBlockState(bp.down());
                //包含灌木 不是庄家
                if (this.folk.entity.worldObj.getBlockState(bp).getBlock() instanceof BlockBush && !(this.folk.entity.worldObj.getBlockState(bp).getBlock() instanceof BlockCrops)) {
                    //设置为空气
                    this.folk.entity.worldObj.setBlockToAir(bp);
                }
                //如果当前为空气
                if (this.folk.entity.worldObj.isAirBlock(bp)) {

                    if (z % 5 == 0 && x % 5 == 0) {
                        //不是水 或流动的水
                        if (this.folk.entity.worldObj.getBlockState(bp.down()).getBlock() != Blocks.WATER && this.folk.entity.worldObj.getBlockState(bp.down()).getBlock() != Blocks.FLOWING_WATER) {
                            //锄地
                            this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                            //设置为耕地
                            this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 11);
                            //播放声音
                            this.folk.entity.worldObj.playSound(this.folk.entity.posX, this.folk.entity.posY, this.folk.entity.posZ, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                            //摇摆手臂
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            //提升农民等级
                            this.addFarmingLevel();
                            //收获检查
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                        //草地 草 泥土
                    } else if (soil.getBlock() == Blocks.GRASS_PATH || soil.getBlock() == Blocks.GRASS || soil.getBlock() == Blocks.DIRT) {
                        //锄地
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tilling"));
                        //设置为耕地
                        this.folk.entity.worldObj.setBlockState(bp.down(), Blocks.FARMLAND.getDefaultState(), 11);
                        //播放声音
                        this.folk.entity.worldObj.playSound(this.folk.entity.posX, this.folk.entity.posY, this.folk.entity.posZ, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                        //设置手持无
                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                        //摇摆手臂
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
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

    /**
     * @return void
     * @Author fan
     * @Description //TODO 种植
     * @Date 10:13 2022/12/10
     * @Param []
     **/
    public void plant() {
        //循环宽
        for (int z = 0; z < this.farm.z; ++z) {
            //循环长
            for (int x = 0; x < this.farm.x; ++x) {
                boolean hasBlock = false;
                //获得角落
                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
//                ItemStack seed = ItemStack.EMPTY;
                ItemStack seed = null;
                List<IInventory> iterator = this.inventoriesFindClosest(this.workPlace, 5);
                for (IInventory inv : iterator) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        //可种植
                        if (slot != null && slot.getItem() instanceof IPlantable) {
                            seed = slot;
                        }
                    }
                }
                //可种植的种子为空
                if (seed == null) {
                    //没有种子
                    this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.no_seeds"));
                    return;
                }
                try {
                    //种子为可种植
                    IPlantable plantable = (IPlantable) seed.getItem();
                    //作物
//                    Field cropsField = plantable.getClass().getDeclaredField("crops");
//                    //设置成可访问
//                    cropsField.setAccessible(true);
//                    //获取作物方块
//                    Block crop = (Block) cropsField.get(plantable);
//                    //获取种植位置
                    IBlockState soil = this.folk.entity.worldObj.getBlockState(bp.down());
                    Block crop=plantable.getPlant(this.folk.entity.worldObj,bp.down()).getBlock();
                    //东西南北都有根茎 可持续生长
                    if (!(this.folk.entity.worldObj.getBlockState(bp.north()).getBlock() instanceof BlockStem) && !(this.folk.entity.worldObj.getBlockState(bp.east()).getBlock() instanceof BlockStem) && !(this.folk.entity.worldObj.getBlockState(bp.south()).getBlock() instanceof BlockStem) && !(this.folk.entity.worldObj.getBlockState(bp.west()).getBlock() instanceof BlockStem) && soil.getBlock().canSustainPlant(soil, this.folk.entity.worldObj, bp.down(), EnumFacing.UP, (IPlantable) seed.getItem()) && this.folk.entity.worldObj.isAirBlock(bp)) {
                        for (IInventory inv : iterator) {
                            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                ItemStack slot = inv.getStackInSlot(i);
                                //可种植
                                if (slot != null && slot.isItemEqual(seed) ) {
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
                        //摇摆手臂
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
//                        seed.shrink(1);
                        //增加农民等级
                        this.addFarmingLevel();
                        //更新收获时间
                        this.harvestCheck = System.currentTimeMillis();
                        return;
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                    StackTraceElement element=var10.getStackTrace()[0];
                    ModSimLoader.log.error("种植发生了错误：" + var10.getMessage()+"行数："+element.getLineNumber());
                }
            }
        }

    }

    public void grow() {
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
                                List<IInventory> iterator = this.inventoriesFindClosest(this.workPlace, 5);
                                boolean hasBlock=false;
                                ItemStack dye = null;
                                for (IInventory inv : iterator) {
                                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                                        ItemStack slot = inv.getStackInSlot(i);
                                        ItemStack itemStack=new ItemStack(Items.DYE,1,15);
                                        //可种植
                                        if (slot != null && slot.isItemEqual(itemStack) ) {
                                            hasBlock = true;
                                            dye=slot;
                                            inv.decrStackSize(i, 1);
                                            break;
                                        }
                                    }
                                    if (hasBlock) {
                                        break;
                                    }
                                }
                                if(dye!=null){
                                    igrowable.grow(this.folk.entity.worldObj, this.folk.entity.worldObj.rand, bp, iblockstate);
                                }else{
                                    if((System.currentTimeMillis() - this.qsCheck)>3000*60){
                                        this.qsCheck=System.currentTimeMillis();
                                        ModSimLoader.sendChat(I18n.format("container.sim.job.crop.farmer.dye"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
        //循环宽
        for (int z = 0; z < this.farm.z; ++z) {
            //循环长
            for (int x = 0; x < this.farm.x; ++x) {
                //声明作物
                List<ItemStack> drops = new CopyOnWriteArrayList<>();
                //获取当前位置的物品
                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                IBlockState iBlockState=this.folk.entity.worldObj.getBlockState(bp);
                //获得方块
                Block b = iBlockState.getBlock();
                //有根茎 不是作物 不是可种植 不是可生长
                if (b instanceof BlockStem || !(b instanceof BlockCrops) && !(b instanceof IPlantable) && !(b instanceof IGrowable)) {
                    //包含根茎
                    if (b instanceof BlockStem) {
//                        BlockStem stem = (BlockStem) b;
//                        Field cropField = null;

                        try {
//                            cropField = stem.getClass().getDeclaredField("crop");
//                            cropField.setAccessible(true);
//                            Block crop = (Block) cropField.get(stem);
                            Block bCrop;
                            //北
                            if (this.folk.entity.worldObj.getBlockState(bp.north()).getBlock() == b) {
                                //收获
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                //获得方块
                                bCrop = this.folk.entity.worldObj.getBlockState(bp.north()).getBlock();
                                //摧毁方块
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.north(), this.folk.entity.worldObj.getBlockState(bp.north()), 0);
                                drops.forEach((drop) -> {
                                    //放到工作箱
                                    this.placeInJobChest(drop);
                                });
                                //设置手持物
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                //摇摆手臂
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                //设置为空
                                this.folk.entity.worldObj.setBlockToAir(bp.north());
                                //增加农民等级
                                this.addFarmingLevel();
                                //设置收获时间
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                            //东
                            if (this.folk.entity.worldObj.getBlockState(bp.east()).getBlock() == b) {
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                bCrop = this.folk.entity.worldObj.getBlockState(bp.east()).getBlock();
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp.east(), this.folk.entity.worldObj.getBlockState(bp.east()), 0);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.east(), this.folk.entity.worldObj.getBlockState(bp.east()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.folk.entity.worldObj.setBlockToAir(bp.east());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                            //南
                            if (this.folk.entity.worldObj.getBlockState(bp.south()).getBlock() == b) {
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                bCrop = this.folk.entity.worldObj.getBlockState(bp.south()).getBlock();
                                drops = bCrop.getDrops(this.folk.entity.worldObj, bp.south(), this.folk.entity.worldObj.getBlockState(bp.south()), 0);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.south(), this.folk.entity.worldObj.getBlockState(bp.south()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.folk.entity.worldObj.setBlockToAir(bp.south());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                            //西
                            if (this.folk.entity.worldObj.getBlockState(bp.west()).getBlock() == b) {
                                this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                                bCrop = this.folk.entity.worldObj.getBlockState(bp.west()).getBlock();
                                drops = b.getDrops(this.folk.entity.worldObj, bp.west(), this.folk.entity.worldObj.getBlockState(bp.west()), 0);
//                                bCrop.getDrops(drops, this.folk.entity.worldObj, bp.west(), this.folk.entity.worldObj.getBlockState(bp.west()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.folk.entity.worldObj.setBlockToAir(bp.west());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }

                    } else if (!(b instanceof BlockPumpkin) && !(b instanceof BlockMelon) && !(b instanceof BlockCocoa) && b instanceof BlockCactus) {
                        //设想，撒骨粉
                    }
                } else {
                    BlockCrops crop = (BlockCrops) b;
                    //作物已成熟
                    if (crop.isMaxAge(this.folk.entity.worldObj.getBlockState(bp))) {
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Harvesting"));
                        drops = b.getDrops(this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 0);
//                        b.getDrops(drops, this.folk.entity.worldObj, bp, this.folk.entity.worldObj.getBlockState(bp), 0);
                        drops.forEach((drop) -> {
                            this.placeInJobChest(drop);
                        });
                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        this.folk.entity.swing();
                        this.folk.entity.worldObj.setBlockToAir(bp);
                        this.addFarmingLevel();
                        this.harvestCheck = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }

    }

    public String toString() {
        //农民
        return I18n.format("container.sim.Vocation5");
    }
}
