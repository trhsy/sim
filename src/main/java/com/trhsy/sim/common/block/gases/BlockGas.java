package com.trhsy.sim.common.block.gases;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @ClassName BlockGas
 * @Description todo 块状气体
 * @Author Tian
 * @Date 2022/4/1013:01
 **/
public class BlockGas extends Block {
    public int riseRate = 5;

    public BlockGas() {
        super(Material.snow);
    }

    public void setRiseRate(int rate) {
        this.riseRate = rate;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public boolean canStopRayTrace(int par1, boolean par2) {
        return par2 && par1 == 0;
    }
    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return false;
    }

    protected boolean enforceTicking() {
        return false;
    }

    protected int getDelayForUpdate(World world, int x, int y, int z) {
        return 1;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (j + 1 < 255) {
            world.setBlockToAir(i, j, k);
            world.setBlock(i, j + 1, k, this);
        } else {
            world.setBlockToAir(i, j, k);
        }

    }
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        par1World.scheduleBlockUpdate(par2, par3, par4, this, this.riseRate);
    }
}