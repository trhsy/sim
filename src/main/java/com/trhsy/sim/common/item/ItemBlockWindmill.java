package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.loader.BlockLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemBlockWindmill
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:01
 * ========================================
 **/
public class ItemBlockWindmill extends ItemBlock {
    public ItemBlockWindmill() {
        super(BlockLoader.blockWindmill);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    public String getItemDisplayName(ItemStack is) {
        return is.getMetadata() == 0 ? "Sim-U-Windmill" : "Sim-U-Windmill (colour)";
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        return "item.SUKwindmill" + is.getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Granulates ores and generates Buildcraft energy");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}