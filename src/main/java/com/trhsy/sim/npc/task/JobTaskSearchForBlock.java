package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobTaskSearchForBlock
 * @Description todo 搜索方块
 * @Author TRHSY
 * @Date 2023/6/414:45
 **/
public class JobTaskSearchForBlock extends JobTask {
    //要寻找的方块
    public List<Block> blocks;
    //开始位置
    public V3 startPoint;
    //半径
    public int radius;
    //是否在地下
    public boolean belowGround;
    //去开采
    List<BlockPos> toMine;
    //上次检查后的时间
    long timeSinceLastCheck = 0L;
    //上次砍树时间
    long timeSinceLastChop = 0L;

    public JobTaskSearchForBlock(Job j, long ms, List<Block> blocks, V3 startPoint, int radius, boolean belowGround) {
        super(j, ms);
        this.blocks = blocks;
        this.startPoint = startPoint;
        this.radius = radius;
        this.belowGround = belowGround;
        this.toMine = new CopyOnWriteArrayList<>();
    }

    @Override
    public void onTaskBegin() {
        findBlocks();
    }

    public void findBlocks() {
        //是否在底下，在底下就去底下
//        double startY = this.belowGround ? this.startPoint.y - (double) this.radius : this.startPoint.y;
        double startY = this.startPoint.y;
        for (double y = startY; y < this.startPoint.y + (double) this.radius; ++y) {
            for (double x = this.startPoint.x - (double) this.radius; x < this.startPoint.x + (double) this.radius; ++x) {
                for (double z = this.startPoint.z - (double) this.radius; z < this.startPoint.z + (double) this.radius; ++z) {
                    BlockPos bp = new BlockPos(x, y, z);
                    Block b = this.folk.entity.worldObj.getBlockState(bp).getBlock();
                    boolean foundSimilar = false;

                    Iterator var12 = this.blocks.iterator();
                    while (var12.hasNext()) {
                        Block bCheck = (Block) var12.next();
                        if (b.equals(bCheck)) {
                            foundSimilar = true;
                        }
                    }

                    if ((this.blocks.contains(b) || foundSimilar) && !ModSimLoader.isBlockInBuilding(V3.fromBlockPos(bp))) {
                        this.toMine.add(bp);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (System.currentTimeMillis() - this.timeSinceLastCheck > 5L) {
            this.timeSinceLastCheck = System.currentTimeMillis();
            if (this.toMine.size() < 1) {
                this.job.folk.forceMoveToXYZNoWarp(this.startPoint);
                //找物品
                this.job.folk.setStatus(I18n.format("container.sim.GOTOSANDBLOCK1"));
                findBlocks();
                return;
            }
            //去到要挖的材料边
            BlockPos p = this.toMine.get(0);
            V3 v3 = new V3(p.getX(), p.getY(), p.getZ());
            if (!this.job.folk.forceMoveToXYZ(v3)) {
                this.job.folk.forceMoveToXYZNoWarp(v3);
            }
            if (this.toMine.size() > 0) {
                if (this.folk.entity.motionX == 0.0D && this.folk.entity.motionZ == 0.0D) {
                    for (double x = this.folk.entity.posX - 1.0D; x <= this.folk.entity.posX + 1.0D; ++x) {
                        for (double y = this.folk.entity.posY; y <= this.folk.entity.posY + 1.0D; ++y) {
                            for (double z = this.folk.entity.posZ - 1.0D; z <= this.folk.entity.posZ + 1.0D; ++z) {
                                BlockPos bp = new BlockPos(x, y, z);
                                if (this.folk.entity.worldObj.getBlockState(bp).getBlock().isLeaves(this.folk.entity.worldObj.getBlockState(bp), this.folk.entity.worldObj, bp)) {
                                    this.folk.entity.worldObj.setBlockToAir(bp);
                                }
                            }
                        }
                    }

                }
                if (this.job.folk.isAtLocation(V3.fromBlockPos((BlockPos) this.toMine.get(0)), 3)) {
                    if ((float) (System.currentTimeMillis() - this.timeSinceLastChop) > 1500.0F - 100.0F * this.folk.skillMining) {
                        //采集方块
                        this.job.folk.setStatus(I18n.format("container.sim.COLLECTCLAY"));
                        IBlockState woodState = this.folk.entity.worldObj.getBlockState((BlockPos) this.toMine.get(0));
                        //将物品放到箱子
                        this.job.placeInJobChest(new ItemStack(woodState.getBlock().getItemDropped(woodState, new Random(), 0), woodState.getBlock().quantityDropped(new Random())));
                        this.folk.entity.worldObj.setBlockToAir((BlockPos) this.toMine.get(0));
                        ModSimLoader.addMoney(-0.02F);
                        this.toMine.remove(0);
                        NpcData var10000 = this.folk;
                        var10000.skillMining += 0.001F;
                        this.addMiningLevel();
                        this.timeSinceLastChop = System.currentTimeMillis();
                    }
                } else {
                    //找方块
                    this.job.folk.setStatus(I18n.format("container.sim.GOTOCLAYBLOCK"));
                }
            }
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 增加挖矿等级
     * @Date 19:01 2023/4/15
     * @Param []
     **/
    public void addMiningLevel() {
        int b4 = (int) Math.floor((double) this.folk.skillMining);
        if (this.folk.skillMining < 10.0F) {
            NpcData var10000 = this.folk;
            var10000.skillMining = (float) ((double) var10000.skillMining + 0.001D / (double) b4);
        }

        int aft = (int) Math.floor((double) this.folk.skillMining);
        if (b4 != aft) {
            //张三 刚刚升级到矿工等级 1
            ModSimLoader.sendChat(this.folk.getName() + " " + I18n.format("container.sim.job.miner.farmer.levelled") + " " + aft);
        }

    }

    @Override
    public void onTaskComplete() {

    }
}
