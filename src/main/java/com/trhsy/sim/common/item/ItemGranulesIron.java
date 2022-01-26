package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemGranulesIron
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:03
 * ========================================
 **/
public class ItemGranulesIron extends Item {
    private IIcon[] icons;

    public ItemGranulesIron(int par1) {
        this.field_77777_bU = 64;
    }

    @SideOnly(Side.CLIENT)
    public void func_94581_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:granulesIron");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_77617_a(int par1) {
        return this.icons[0];
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Iron granules";
    }

    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Place into furnace to make Iron Ingots");
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[0];
    }
}