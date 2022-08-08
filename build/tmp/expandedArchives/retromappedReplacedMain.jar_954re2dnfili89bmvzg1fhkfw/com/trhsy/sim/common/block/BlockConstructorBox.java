package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.core.entity.FolkData;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.client.gui.blocks.GuiBuildingConstructor;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
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

/**
 * 建筑箱
 */
public class BlockConstructorBox extends Block {
    public String buildDirection = "";
    public BlockConstructorBox() {
        super(Material.field_151575_d);
        //名字
        this.func_149663_c("constructorBox");
        //方块硬度
        this.func_149711_c(0.5F);
        this.func_149647_a(CreativeTabsLoader.tabSimU);
    }

    /**
     * 方块被激活
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void func_176213_c(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            if (!world.field_72995_K) {
                world.func_72908_a(blockPos.func_177958_n(), blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":constructoractivated", 1, 1);
            }
            super.func_176213_c(world, blockPos, iBlockState);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑箱onBlockAdded出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 在被玩家摧毁的时候
     * @param world
     * @param blockPos
     * @param iBlockState
     */
    @Override
    public void func_176206_d(World world, BlockPos blockPos, IBlockState iBlockState) {
        try {
            if (!world.field_72995_K) {
                world.func_72908_a(blockPos.func_177958_n(), blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":powerdown", 1, 1);
            }
            FolkData theFolk = FolkData.getFolkByEmployedAt(new V3(blockPos.func_177958_n(), blockPos.func_177956_o(),blockPos.func_177952_p(), world.field_73011_w.func_177502_q()));
            if (theFolk != null) {
                theFolk.selfFire();
            }
            super.func_176206_d(world, blockPos, iBlockState);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑箱onBlockDestroyedByPlayer出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":computer", 1, 1);
            int px = (int)Math.floor(thePlayer.field_70165_t);
            int py = (int)Math.floor(thePlayer.field_70163_u);
            int pz = (int)Math.floor(thePlayer.field_70161_v);
            if (blockPos.func_177952_p() == pz) {
                if (px < blockPos.func_177958_n()) {
                    this.buildDirection = "-x";
                } else {
                    this.buildDirection = "+x";
                }
            } else if (blockPos.func_177958_n() == px) {
                if (pz < blockPos.func_177952_p()) {
                    this.buildDirection = "-z";
                } else {
                    this.buildDirection = "+z";
                }
            }

            V3 loc = new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), thePlayer.field_71093_bK);
            Minecraft mc = Minecraft.func_71410_x();
            GuiBuildingConstructor ui = new GuiBuildingConstructor(loc, this.buildDirection, null);
            mc.func_147108_a(ui);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("建筑箱onBlockActivated出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }
        return true;
    }
}
