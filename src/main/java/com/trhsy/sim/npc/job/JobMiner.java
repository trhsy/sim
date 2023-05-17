package com.trhsy.sim.npc.job;

import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.block.MineBox;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobMiner
 * @Description todo 矿工
 * @Author TRHSY
 * @Date 2023/4/1517:33
 **/
public class JobMiner extends Job{
    public MineBox mine;
    long mineCheck = 0L;
    int level = 0;
    Random rand;

    public JobMiner(NpcData folk, BlockPos pos, World world, MineBox mb) {
        super(folk, pos, world);
        //矿工
        this.jobName = I18n.format("container.sim.Vocation4");
        this.mine = mb;
        this.rand = new Random();
    }

    @Override
    public void onArrive() {
        this.mineCheck = System.currentTimeMillis();
        this.folk.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemLoader.tinSword));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.atWork) {
            if (ModSimLoader.money > 0.02F) {
                if (this.findJobChests(5).size() == 0) {
                    //附近没有箱子
                    this.folk.setStatus(I18n.format("container.sim.WAITINGFORCHEST"));
                    return;
                }

                if (this.stuckItem != null) {
                    //不行,箱子都满了
                    this.folk.setStatus(I18n.format("container.sim.job.dairy.farmer.chests"));
                    if (System.currentTimeMillis() - this.mineCheck > 1500L) {
                        this.mineCheck = System.currentTimeMillis();
                        if (!this.placeInJobChest(this.stuckItem)) {
                            return;
                        }

                        this.stuckItem = null;
                    }
                }

                if ((float)(System.currentTimeMillis() - this.mineCheck) > 1500.0F - 100.0F * this.folk.skillMining && this.stuckItem == null) {
                    this.mine();
                }
            } else {
                //没有钱付给我！
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
            }
        }

    }

    public void mine() {
        for(int y = 3; (double)y > 0.0D - this.mine.loc.y; --y) {
            for(int z = 0; z < this.mine.z; ++z) {
                for(int x = 0; x < this.mine.x; ++x) {
                    List<ItemStack> drops = new CopyOnWriteArrayList<>();
                    BlockPos bp = new BlockPos(this.mine.getCorner().offset(this.mine.facing, x).offset(this.mine.facing.rotateY(), z).offset(EnumFacing.DOWN, -y));
                    Block b = this.jobWorld.getBlockState(bp).getBlock();
                    if (!b.isAir(this.jobWorld.getBlockState(bp), this.jobWorld, bp) && this.jobWorld.getBlockState(bp).getBlock() != Blocks.BEDROCK && !this.jobWorld.getBlockState(bp).getMaterial().isLiquid()) {
                        //开采
                        this.folk.setStatus(I18n.format("container.sim.Mining10") +" " + b.getLocalizedName());
                        b.getDrops(this.jobWorld, bp, this.jobWorld.getBlockState(bp), 0);
                        drops.forEach((drop) -> {
                            this.placeInJobChest(drop);
                        });
                        this.jobWorld.setBlockToAir(bp);
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        this.addMiningLevel();
                        this.mineCheck = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }

    }

    public void addMiningLevel() {
        int b4 = (int)Math.floor((double)this.folk.skillMining);
        ModSimLoader.addMoney(-0.01F);
        if (this.folk.skillMining < 10.0F) {
            NpcData var10000 = this.folk;
            var10000.skillMining = (float)((double)var10000.skillMining + 0.001D / (double)b4);
        }

        int aft = (int)Math.floor((double)this.folk.skillMining);
        if (b4 != aft) {
            ModSimLoader.sendChat(this.folk.getName() + " "+I18n.format("container.sim.jobMiner1")+" " + aft);
        }

    }

    @Override
    public String toString() {
        return this.jobName;
    }
}
