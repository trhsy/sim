package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName TileEnergyHandler
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:01
 * ========================================
 **/
public class TileEnergyHandler extends TileEntity implements IEnergyHandler {
    protected EnergyStorage storage = new EnergyStorage(32000);

    public TileEnergyHandler() {
    }

    public void func_145839_a(NBTTagCompound nbt) {
        super.func_145839_a(nbt);
        this.storage.readFromNBT(nbt);
    }

    public void func_145841_b(NBTTagCompound nbt) {
        super.func_145841_b(nbt);
        this.storage.writeToNBT(nbt);
    }

    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return this.storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return this.storage.getMaxEnergyStored();
    }
}