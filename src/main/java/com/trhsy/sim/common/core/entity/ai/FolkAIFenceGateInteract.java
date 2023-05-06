package com.trhsy.sim.common.core.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.common.core.entity.ai
 * @ClassName: FolkAIFenceGateInteract
 * @Description:
 * @date 2023/5/6 14:14
 */
public class FolkAIFenceGateInteract extends EntityAIBase {
    /**实体**/
    protected EntityLiving entity;
    /**栅栏位置**/
    protected BlockPos fencePosition;
    /**栏杆块**/
    protected BlockFenceGate fenceBlock;
    /**已停止门交互**/
    boolean hasStoppedDoorInteraction;
    /**实体位置X**/
    float entityPositionX;
    /***实体位置z**/
    float entityPositionZ;

    public FolkAIFenceGateInteract(EntityLiving entityIn) {
        this.fencePosition = BlockPos.ORIGIN;
        this.entity = entityIn;
        if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("门接收的互动类型不支持");
        }
    }

    /***
     * 应该执行
     * @return
     */
    @Override
    public boolean shouldExecute() {
        //是水平碰撞
        if (!this.entity.isCollidedHorizontally) {
            return false;
        } else {
            PathNavigateGround pathnavigateground = (PathNavigateGround)this.entity.getNavigator();
            PathEntity pathentity = pathnavigateground.getPath();
            if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors()) {
                for(int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
                    PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
                    this.fencePosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord);
                    if (this.entity.getDistanceSq((double)this.fencePosition.getX(), this.entity.posY, (double)this.fencePosition.getZ()) <= 2.25D) {
                        this.fenceBlock = this.getBlockFence(this.fencePosition);
                        if (this.fenceBlock != null) {
                            return true;
                        }
                    }
                }

                this.fencePosition = (new BlockPos(this.entity)).up();
                this.fenceBlock = this.getBlockFence(this.fencePosition);
                return this.fenceBlock != null;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean continueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }

    @Override
    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.fencePosition.getX() + 0.5F) - this.entity.posX);
        this.entityPositionZ = (float)((double)((float)this.fencePosition.getZ() + 0.5F) - this.entity.posZ);
    }

    @Override
    public void updateTask() {
        float f = (float)((double)((float)this.fencePosition.getX() + 0.5F) - this.entity.posX);
        float f1 = (float)((double)((float)this.fencePosition.getZ() + 0.5F) - this.entity.posZ);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
        if (f2 < 0.0F) {
            this.hasStoppedDoorInteraction = true;
        }

    }
    /***
     * 获取栅栏块
     * @param pos
     * @return
     */
    private BlockFenceGate getBlockFence(BlockPos pos) {
        Block block = this.entity.worldObj.getBlockState(pos).getBlock();
        if (!(block instanceof BlockFenceGate)) {
            block = this.entity.worldObj.getBlockState(this.entity.getPosition()).getBlock();
            this.fencePosition = this.entity.getPosition();
        }

        return block instanceof BlockFenceGate ? (BlockFenceGate)block : null;
    }
}
