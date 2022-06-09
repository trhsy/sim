package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.blocks.GuiBuildingConstructor;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

/**
 * 建筑箱
 */
public class BlockConstructorBox extends Block {
    public String buildDirection = "";
    public BlockConstructorBox() {
        super(Material.wood);
        //名字
        this.setUnlocalizedName("constructorBox");
        //方块硬度
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    /**
     * 方块被激活
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            world.playSoundEffect(blockPos.getX(), blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":constructoractivated", 1.0F, 1.0F);
        }

        super.onBlockAdded(world, blockPos, iBlockState);
    }

    /**
     * 在被玩家摧毁的时候
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            world.playSoundEffect(blockPos.getX(), blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":powerdown", 1.0F, 1.0F);
        }

        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3(blockPos.getX(), blockPos.getY(),blockPos.getZ(), world.provider.getDimensionId()));
        if (theFolk != null) {
            theFolk.selfFire();
        }

        super.onBlockDestroyedByPlayer(world, blockPos, iBlockState);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":computer", 1.0F, 1.0F);
        int px = (int)Math.floor(thePlayer.posX);
        int py = (int)Math.floor(thePlayer.posY);
        int pz = (int)Math.floor(thePlayer.posZ);
        if (blockPos.getZ() == pz) {
            if (px < blockPos.getX()) {
                this.buildDirection = "-x";
            } else {
                this.buildDirection = "+x";
            }
        } else if (blockPos.getX() == px) {
            if (pz < blockPos.getZ()) {
                this.buildDirection = "-z";
            } else {
                this.buildDirection = "+z";
            }
        }

        V3 loc = new V3(blockPos.getX(),blockPos.getY(),blockPos.getZ(), thePlayer.dimension);
        Minecraft mc = Minecraft.getMinecraft();
        GuiBuildingConstructor ui = new GuiBuildingConstructor(loc, this.buildDirection, (ArrayList)null);
        mc.displayGuiScreen(ui);
        return true;
    }
}
