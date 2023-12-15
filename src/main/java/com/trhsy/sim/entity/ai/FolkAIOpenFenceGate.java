package com.trhsy.sim.entity.ai;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.ai
 * @ClassName: FolkAIOpenFenceGate
 * @Description:
 * @date 2023/11/20 下午 4:54
 */
public class FolkAIOpenFenceGate extends FolkAIFenceGateInteract{
    /**是否关门**/
    boolean closeGate;
    /**关门参数**/
    int closeGateTemporisation;

    /**
     * 初始化
     * @param entitylivingIn
     * @param shouldClose
     */
    public FolkAIOpenFenceGate(EntityLiving entitylivingIn, boolean shouldClose) {
        super(entitylivingIn);
        this.entity = entitylivingIn;
        this.closeGate = shouldClose;
    }

    /**
     * 应继续执行
     * @return
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.closeGate && this.closeGateTemporisation > 0 && super.shouldContinueExecuting();
    }

    /**
     * 开始执行
     */
    @Override
    public void startExecuting() {
        this.closeGateTemporisation = 20;
        this.toggleGate(true);
    }

    /**
     * 重置任务
     */
    @Override
    public void resetTask() {
        if (this.closeGate) {
            this.toggleGate(false);
        }

    }

    /**
     * 更新任务
     */
    @Override
    public void updateTask() {
        --this.closeGateTemporisation;
        super.updateTask();
    }

    /**
     *拨动式闸门
     * @param open
     */
    private void toggleGate(boolean open) {
        IBlockState iblockstate = this.entity.world.getBlockState(this.fencePosition);
        if (iblockstate.getBlock() == this.fenceBlock && (Boolean)iblockstate.getValue(BlockFenceGate.OPEN) != open) {
            this.entity.world.setBlockState(this.fencePosition, iblockstate.withProperty(BlockFenceGate.OPEN, open), 2);
        }

    }
}
