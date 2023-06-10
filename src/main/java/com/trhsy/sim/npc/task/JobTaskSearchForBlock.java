package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;

/**
 * @ClassName JobTaskSearchForBlock
 * @Description todo 搜索方块
 * @Author TRHSY
 * @Date 2023/6/414:45
 **/
public class JobTaskSearchForBlock extends JobTask {
    //要寻找的方块
    public List<Block> blocks;
    //实施
    public boolean implementsAlso;
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

    public JobTaskSearchForBlock(Job j, long ms, List<Block> blocks, boolean implementsAlso, V3 startPoint, int radius, boolean belowGround) {
        super(j, ms);
        this.blocks = blocks;
        this.implementsAlso = implementsAlso;
        this.startPoint = startPoint;
        this.radius = radius;
        this.belowGround = belowGround;
    }
    @Override
    public void onTaskBegin() {
        findBlocks();
    }
    public void findBlocks(){
        //是否在底下，在底下就去底下
        double startY = this.belowGround ? this.startPoint.y - (double)this.radius : this.startPoint.y;
        for(double x = this.startPoint.x - (double)this.radius; x < this.startPoint.x + (double)this.radius; ++x) {
            for(double y = startY; y < this.startPoint.y + (double)this.radius; ++y) {
                for(double z = this.startPoint.z - (double)this.radius; z < this.startPoint.z + (double)this.radius; ++z) {
                    BlockPos bp = new BlockPos(x, y, z);
                    Block b = this.job.jobWorld.getBlockState(bp).getBlock();
                    boolean foundSimilar = false;
                    if (this.implementsAlso) {
                        Iterator var12 = this.blocks.iterator();
                        while(var12.hasNext()) {
                            Block bCheck = (Block)var12.next();
                            if (b.getClass().isInstance(bCheck)) {
                                foundSimilar = true;
                            }
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
                //找树
                this.job.folk.setStatus(I18n.format("container.sim.GOTOSANDBLOCK1"));
                findBlocks();
                return;
            }
            //去到要挖的材料边
            this.job.folk.forceMoveToXYZNoWarp(V3.fromBlockPos((BlockPos)this.toMine.get(0)));
            if (this.toMine.size() > 0) {
                
            }
        }
    }

    @Override
    public void onTaskComplete() {

    }
}
