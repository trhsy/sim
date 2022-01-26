package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName TileEntityWindmill
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:10
 * ========================================
 **/
public class TileEntityWindmill extends TileEntity implements IInventory, IEnergyHandler {
    public TileEntityWindmill() {
    }

    public boolean canConnectEnergy(ForgeDirection from) {
        return false;
    }

    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return 0;
    }

    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    public int getEnergyStored(ForgeDirection from) {
        return 0;
    }

    public int getMaxEnergyStored(ForgeDirection from) {
        return 0;
    }

    public int func_70302_i_() {
        return 0;
    }

    public ItemStack func_70301_a(int p_70301_1_) {
        return null;
    }

    public ItemStack func_70298_a(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    public ItemStack func_70304_b(int p_70304_1_) {
        return null;
    }

    public void func_70299_a(int p_70299_1_, ItemStack p_70299_2_) {
    }

    public String func_145825_b() {
        return null;
    }

    public boolean func_145818_k_() {
        return false;
    }

    public int func_70297_j_() {
        return 0;
    }

    public boolean func_70300_a(EntityPlayer p_70300_1_) {
        return false;
    }

    public void func_70295_k_() {
    }

    public void func_70305_f() {
    }

    public boolean func_94041_b(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }
}
