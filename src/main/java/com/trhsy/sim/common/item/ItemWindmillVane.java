package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemWindmillVane
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:05
 * ========================================
 **/
public class ItemWindmillVane extends Item {
    private IIcon[] icons;

    public ItemWindmillVane(int par1) {
        this.field_77777_bU = 64;
    }

    @SideOnly(Side.CLIENT)
    public void func_94581_a(IIconRegister iconRegister) {
        this.icons = new IIcon[16];

        for(int i = 0; i <= 15; ++i) {
            this.icons[i] = iconRegister.func_94245_a("satscapesimukraft:windmillvane" + i);
        }

    }

    public IIcon func_77617_a(int meta) {
        return meta >= 0 && meta < 16 ? this.icons[meta] : this.icons[0];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int x = 0; x < 16; ++x) {
            par3List.add(new ItemStack(this, 1, x));
        }

    }

    public String func_77667_c(ItemStack is) {
        return "item.windmillvane" + is.func_77960_j();
    }

    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Craft 4 of these to make a sail");
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Windmill vane";
    }

    public int func_77647_b(int par1) {
        return par1;
    }
}
