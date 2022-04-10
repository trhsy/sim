package com.trhsy.sim.common.block.gases;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

/**
 * @ClassName TileEntityBlockGasDispenser
 * @Description todo
 * @Author Tian
 * @Date 2022/4/1016:14
 **/
public class TileEntityBlockGasDispenser extends TileEntity {
    int tickCount = 0;
    Random rand = new Random();

    public TileEntityBlockGasDispenser() {
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public void func_145845_h() {
        ++this.tickCount;
        if (this.tickCount > ThreadLocalRandom.current().nextInt(80, 150)) {
            this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 20);
            this.tickCount = 0;
        }

    }
}