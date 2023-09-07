package com.trhsy.sim.entity.ai;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.ai
 * @ClassName: FolkAIOpenFenceGate
 * @Description:
 * @date 2022/10/13 10:59
 */
public class FolkAIOpenFenceGate extends FolkAIFenceGateInteract {
    /**是否关门**/
    boolean closeGate;
    /**关门参数**/
    int closeGateTemporisation;

    public FolkAIOpenFenceGate(EntityLiving entitylivingIn, boolean shouldClose) {
        super(entitylivingIn);
        this.entity = entitylivingIn;
        this.closeGate = shouldClose;
    }
    @Override
    public boolean shouldExecute() {
        return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
        return this.closeGate && this.closeGateTemporisation > 0 && super.shouldContinueExecuting();
    }
    @Override
    public void startExecuting() {
        this.closeGateTemporisation = 20;
        this.toggleGate(true);
    }
    @Override
    public void resetTask() {
        if (this.closeGate) {
            this.toggleGate(false);
        }

    }

    @Override
    public void updateTask() {
        --this.closeGateTemporisation;
        super.updateTask();
    }

    private void toggleGate(boolean open) {
        IBlockState iblockstate = this.entity.worldObj.getBlockState(this.fencePosition);
        if (iblockstate.getBlock() == this.fenceBlock && (Boolean)iblockstate.getValue(BlockFenceGate.OPEN) != open) {
            this.entity.worldObj.setBlockState(this.fencePosition, iblockstate.withProperty(BlockFenceGate.OPEN, open), 2);
        }

    }

}
