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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobTaskChopTrees
 * @Description todo 砍树任务
 * @Author TRHSY
 * @Date 2023/4/1518:54
 **/
public class JobTaskChopTrees extends JobTask {
    //要开采的方块
    List<BlockPos> toMine = new ArrayList();
    //开始地点
    V3 startPoint;
    //半径
    int radius;
    //上次检查后的时间
    long timeSinceLastCheck = 0L;
    //上次砍树时间
    long timeSinceLastChop = 0L;
    public JobTaskChopTrees(Job j, long ms, V3 startPoint, int radius) {
        super(j, ms);
        this.startPoint = startPoint;
        this.radius = radius;
    }
    @Override
    public void onTaskBegin() {
        double startY = this.startPoint.y;

        for(double y = startY; y < this.startPoint.y + (double)this.radius; ++y) {
            for(double x = this.startPoint.x - (double)this.radius; x < this.startPoint.x + (double)this.radius; ++x) {
                for(double z = this.startPoint.z - (double)this.radius; z < this.startPoint.z + (double)this.radius; ++z) {
                    BlockPos bp = new BlockPos(x, y, z);
                    Block b = this.job.jobWorld.getBlockState(bp).getBlock();
                    if (b.isWood(this.job.jobWorld, bp) && !ModSimLoader.isBlockInBuilding(V3.fromBlockPos(bp))) {
                        if (!this.toMine.contains(bp)) {
                            this.toMine.add(bp);
                        }

                        this.checkNeighbours(bp, this.toMine);
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
                return;
            }

            this.job.folk.forceMoveToXYZNoWarp(V3.fromBlockPos((BlockPos)this.toMine.get(0)));
            if (this.toMine.size() > 0) {
                if (this.folk.entity.motionX == 0.0D && this.folk.entity.motionZ == 0.0D) {
                    new ArrayList();

                    for(double x = this.folk.entity.posX - 1.0D; x <= this.folk.entity.posX + 1.0D; ++x) {
                        for(double y = this.folk.entity.posY; y <= this.folk.entity.posY + 1.0D; ++y) {
                            for(double z = this.folk.entity.posZ - 1.0D; z <= this.folk.entity.posZ + 1.0D; ++z) {
                                BlockPos bp = new BlockPos(x, y, z);
                                if (this.job.jobWorld.getBlockState(bp).getBlock().isLeaves(this.job.jobWorld.getBlockState(bp), this.job.jobWorld, bp)) {
                                    this.job.jobWorld.setBlockToAir(bp);
                                }
                            }
                        }
                    }
                }

                if (this.job.folk.isAtLocation(V3.fromBlockPos((BlockPos)this.toMine.get(0)), 3)) {
                    if ((float)(System.currentTimeMillis() - this.timeSinceLastChop) > 1500.0F - 100.0F * this.folk.skillMining) {
                        //砍树
                        this.job.folk.setStatus(I18n.format("container.sim.CHOPPINGTREE"));
                        IBlockState woodState = this.job.jobWorld.getBlockState((BlockPos)this.toMine.get(0));
                        //将物品放到箱子
                        this.job.placeInJobChest(new ItemStack(woodState.getBlock().getItemDropped(woodState, this.rand, 0), woodState.getBlock().quantityDropped(this.rand)));
                        this.job.jobWorld.setBlockToAir((BlockPos)this.toMine.get(0));
                        ModSimLoader.addMoney(-0.02F);
                        this.toMine.remove(0);
                        NpcData var10000 = this.folk;
                        var10000.skillMining += 0.001F;
                        this.addMiningLevel();
                        this.timeSinceLastChop = System.currentTimeMillis();
                    }
                } else {
                    //找树
                    this.job.folk.setStatus(I18n.format("container.sim.GOTOTREE"));
                }
            }
        }
    }

    @Override
    public void onTaskComplete() {

    }
    /**
     * @Author fan
     * @Description //TODO 增加挖矿等级
     * @Date 19:01 2023/4/15
     * @Param []
     * @return void
     **/
    public void addMiningLevel() {
        int b4 = (int)Math.floor((double)this.folk.skillMining);
        if (this.folk.skillMining < 10.0F) {
            NpcData var10000 = this.folk;
            var10000.skillMining = (float)((double)var10000.skillMining + 0.001D / (double)b4);
        }

        int aft = (int)Math.floor((double)this.folk.skillMining);
        if (b4 != aft) {
            //张三 刚刚升级到矿工等级 1
            ModSimLoader.sendChat(this.folk.getName() + " "+ I18n.format("container.sim.job.miner.farmer.levelled") +" " + aft);
        }

    }
    /**
     * @Author fan
     * @Description //TODO 检查是否有要砍的方块
     * @Date 19:02 2023/4/15
     * @Param [bp, toMine]
     * @return void
     **/
    void checkNeighbours(BlockPos bp, List<BlockPos> toMine) {
        //东南西北
        EnumFacing[] var3 = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            EnumFacing facing = var3[var5];
            BlockPos neighbour = bp.offset(facing);
            BlockPos upNeighbour = bp.up().offset(facing);
            if (!((double)neighbour.getX() > this.startPoint.x + (double)this.radius) && !((double)neighbour.getX() < this.startPoint.x - (double)this.radius) && !((double)neighbour.getZ() > this.startPoint.z + (double)this.radius) && !((double)neighbour.getZ() < this.startPoint.z - (double)this.radius)) {
                if (this.job.jobWorld.getBlockState(neighbour).getBlock().isWood(this.job.jobWorld, neighbour) && !toMine.contains(neighbour)) {
                    toMine.add(neighbour);
                    this.checkNeighbours(neighbour, toMine);
                }

                if (this.job.jobWorld.getBlockState(upNeighbour).getBlock().isWood(this.job.jobWorld, upNeighbour) && !toMine.contains(upNeighbour)) {
                    toMine.add(upNeighbour);
                    this.checkNeighbours(upNeighbour, toMine);
                }
            }
        }

    }

    public BlockPos findClosestBlockType(BlockPos startXYZ, int searchDistance) {
        if (this.job.jobWorld.getBlockState(startXYZ).getBlock().isLeaves(this.job.jobWorld.getBlockState(startXYZ), this.job.jobWorld, startXYZ)) {
            return startXYZ;
        } else {
            for(int d = 1; d < searchDistance; ++d) {
                for(int xo = -d; xo <= d; ++xo) {
                    for(int zo = -d; zo <= d; ++zo) {
                        BlockPos s = new BlockPos(startXYZ.getX() + xo, startXYZ.getY(), startXYZ.getZ() + zo);
                        if (this.job.jobWorld.getBlockState(s).getBlock().isLeaves(this.job.jobWorld.getBlockState(startXYZ), this.job.jobWorld, startXYZ)) {
                            return s;
                        }
                    }
                }
            }

            return null;
        }
    }
}
