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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.lang.reflect.Field;
import java.util.Iterator;
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
                    int r = ra.nextInt(10);
                    if (r == 0) {
                        //在公众号'dasha500'找作者玩
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Facebook"));
                    } else if (r == 1) {
                        //查看天气预报
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Checking"));
                    } else if (r == 2) {
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
                        //希望我在公众号'dasha500'和作者玩
//                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Minecraft"));
                        this.folk.setStatus(I18n.format("container.sim.job.crop.farmer.Tending"));
                    }

                }
            } else {
                //没有钱付给我
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
            }
        }

    }

    public void addFarmingLevel() {
        int b4 = (int) Math.floor((double) this.folk.skillFarming);
        ModSimLoader.addMoney(-0.01F);
        if (this.folk.skillFarming < 10.0F) {
            NpcData var10000 = this.folk;
            var10000.skillFarming = (float) ((double) var10000.skillFarming + 0.001D / (double) b4);
        }

        int aft = (int) Math.floor((double) this.folk.skillFarming);
        if (b4 != aft) {
            ModSimLoader.sendChat(this.folk.getName() + " has just levelled up to Farmer Level " + aft);
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
        for (int z = 0; z < this.farm.z; ++z) {
            for (int x = 0; x < this.farm.x; ++x) {
                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                Block b = this.jobWorld.getBlockState(bp).getBlock();
                IBlockState soil = this.jobWorld.getBlockState(bp.down());
                if (this.jobWorld.getBlockState(bp).getBlock() instanceof BlockBush && !(this.jobWorld.getBlockState(bp).getBlock() instanceof BlockCrops)) {
                    this.jobWorld.setBlockToAir(bp);
                }

                if (this.jobWorld.isAirBlock(bp)) {
                    if (z % 5 == 0 && x % 5 == 0) {
                        if (this.jobWorld.getBlockState(bp.down()).getBlock() != Blocks.WATER && this.jobWorld.getBlockState(bp.down()).getBlock() != Blocks.FLOWING_WATER) {
                            this.folk.setStatus("Tilling the ground");
                            this.jobWorld.setBlockState(bp.down(), Blocks.WATER.getDefaultState(), 11);
                            this.jobWorld.playSound(this.folk.entity.posX, this.folk.entity.posY, this.folk.entity.posZ, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                            this.addFarmingLevel();
                            this.harvestCheck = System.currentTimeMillis();
                            return;
                        }
                    } else if (soil.getBlock() == Blocks.GRASS_PATH || soil.getBlock() == Blocks.GRASS || soil.getBlock() == Blocks.DIRT) {
                        this.folk.setStatus("Tilling the ground");
                        this.jobWorld.setBlockState(bp.down(), Blocks.FARMLAND.getDefaultState(), 11);
                        this.jobWorld.playSound(this.folk.entity.posX, this.folk.entity.posY, this.folk.entity.posZ, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        this.addFarmingLevel();
                        this.harvestCheck = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }

    }

    public void plant() {
        for (int z = 0; z < this.farm.z; ++z) {
            for (int x = 0; x < this.farm.x; ++x) {
                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                Block b = this.jobWorld.getBlockState(bp).getBlock();
//                ItemStack seed = ItemStack.EMPTY;
                ItemStack seed = null;
                Iterator var6 = this.inventoriesFindClosest(this.workPlace, 5).iterator();

                while (var6.hasNext()) {
                    IInventory inv = (IInventory) var6.next();

                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack slot = inv.getStackInSlot(i);
                        if (slot != null && slot.getItem() instanceof IPlantable) {
                            seed = slot;
                        }
                    }
                }

                if (seed == null) {
                    this.folk.setStatus("No seeds");
                    return;
                }

                var6 = null;

                try {
                    IPlantable plantable = (IPlantable) seed.getItem();
                    Field cropsField = plantable.getClass().getDeclaredField("crops");
                    cropsField.setAccessible(true);
                    Block crop = (Block) cropsField.get(plantable);
                    IBlockState soil = this.jobWorld.getBlockState(bp.down());
                    if (!(this.jobWorld.getBlockState(bp.north()).getBlock() instanceof BlockStem) && !(this.jobWorld.getBlockState(bp.east()).getBlock() instanceof BlockStem) && !(this.jobWorld.getBlockState(bp.south()).getBlock() instanceof BlockStem) && !(this.jobWorld.getBlockState(bp.west()).getBlock() instanceof BlockStem) && soil.getBlock().canSustainPlant(soil, this.jobWorld, bp.down(), EnumFacing.UP, (IPlantable) seed.getItem()) && this.jobWorld.isAirBlock(bp)) {
                        this.folk.setStatus("Planting " + seed.getDisplayName());
                        this.jobWorld.setBlockState(bp, crop.getDefaultState());
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
//                        seed.shrink(1);
                        this.addFarmingLevel();
                        this.harvestCheck = System.currentTimeMillis();
                        return;
                    }
                } catch (Exception var10) {
                }
            }
        }

    }

    public void harvest() {
        for (int z = 0; z < this.farm.z; ++z) {
            for (int x = 0; x < this.farm.x; ++x) {
                List<ItemStack> drops = new CopyOnWriteArrayList<>();
                BlockPos bp = new BlockPos(this.farm.getCorner().offset(this.farm.facing, x).offset(this.farm.facing.rotateY(), z));
                Block b = this.jobWorld.getBlockState(bp).getBlock();
                if (b instanceof BlockStem || !(b instanceof BlockCrops) && !(b instanceof IPlantable) && !(b instanceof IGrowable)) {
                    if (b instanceof BlockStem) {
                        BlockStem stem = (BlockStem) b;
                        Field cropField = null;

                        try {
                            cropField = stem.getClass().getDeclaredField("crop");
                            cropField.setAccessible(true);
                            Block crop = (Block) cropField.get(stem);
                            Block bCrop;
                            if (this.jobWorld.getBlockState(bp.north()).getBlock() == crop) {
                                this.folk.setStatus("Harvesting");
                                bCrop = this.jobWorld.getBlockState(bp.north()).getBlock();
                                bCrop.getDrops(this.jobWorld, bp.north(), this.jobWorld.getBlockState(bp.north()), 0);
//                                bCrop.getDrops(drops, this.jobWorld, bp.north(), this.jobWorld.getBlockState(bp.north()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.jobWorld.setBlockToAir(bp.north());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }

                            if (this.jobWorld.getBlockState(bp.east()).getBlock() == crop) {
                                this.folk.setStatus("Harvesting");
                                bCrop = this.jobWorld.getBlockState(bp.east()).getBlock();
                                bCrop.getDrops(this.jobWorld, bp.east(), this.jobWorld.getBlockState(bp.east()), 0);
//                                bCrop.getDrops(drops, this.jobWorld, bp.east(), this.jobWorld.getBlockState(bp.east()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.jobWorld.setBlockToAir(bp.east());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }

                            if (this.jobWorld.getBlockState(bp.south()).getBlock() == crop) {
                                this.folk.setStatus("Harvesting");
                                bCrop = this.jobWorld.getBlockState(bp.south()).getBlock();
                                bCrop.getDrops(this.jobWorld, bp.south(), this.jobWorld.getBlockState(bp.south()), 0);
//                                bCrop.getDrops(drops, this.jobWorld, bp.south(), this.jobWorld.getBlockState(bp.south()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.jobWorld.setBlockToAir(bp.south());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }

                            if (this.jobWorld.getBlockState(bp.west()).getBlock() == crop) {
                                this.folk.setStatus("Harvesting");
                                bCrop = this.jobWorld.getBlockState(bp.west()).getBlock();
                                b.getDrops(this.jobWorld, bp.west(), this.jobWorld.getBlockState(bp.west()), 0);
//                                bCrop.getDrops(drops, this.jobWorld, bp.west(), this.jobWorld.getBlockState(bp.west()), 0);
                                drops.forEach((drop) -> {
                                    this.placeInJobChest(drop);
                                });
                                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                this.jobWorld.setBlockToAir(bp.west());
                                this.addFarmingLevel();
                                this.harvestCheck = System.currentTimeMillis();
                                return;
                            }
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    } else if (!(b instanceof BlockPumpkin) && !(b instanceof BlockMelon) && !(b instanceof BlockCocoa) && b instanceof BlockCactus) {
                    }
                } else {
                    BlockCrops crop = (BlockCrops) b;
                    if (crop.isMaxAge(this.jobWorld.getBlockState(bp))) {
                        this.folk.setStatus("Harvesting");
                        b.getDrops(this.jobWorld, bp, this.jobWorld.getBlockState(bp), 0);
//                        b.getDrops(drops, this.jobWorld, bp, this.jobWorld.getBlockState(bp), 0);
                        drops.forEach((drop) -> {
                            this.placeInJobChest(drop);
                        });
                        this.folk.entity.setActiveHand(EnumHand.MAIN_HAND);
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        this.folk.entity.swing();
                        this.jobWorld.setBlockToAir(bp);
                        this.addFarmingLevel();
                        this.harvestCheck = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }

    }

    public String toString() {
        return "Farmer";
    }
}
