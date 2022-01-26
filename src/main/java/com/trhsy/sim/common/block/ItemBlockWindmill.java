package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

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
    public ItemBlockWindmill(int par1, Block par2Block) {
        super(par2Block);
        this.func_77627_a(true);
    }

    public String getItemDisplayName(ItemStack is) {
        return is.func_77960_j() == 0 ? "Sim-U-Windmill" : "Sim-U-Windmill (colour)";
    }

    public String func_77667_c(ItemStack is) {
        return "item.SUKwindmill" + is.func_77960_j();
    }

    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Granulates ores and generates Buildcraft energy");
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    public int func_77647_b(int par1) {
        return par1;
    }
}