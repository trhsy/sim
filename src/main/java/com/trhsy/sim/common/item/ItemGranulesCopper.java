package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName ItemGranulesCopper
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:02
 * ========================================
 **/
public class ItemGranulesCopper extends Item {
    private IIcon[] icons;

    public ItemGranulesCopper(int par1) {
        this.field_77777_bU = 64;
    }

    @SideOnly(Side.CLIENT)
    public void func_94581_a(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.func_94245_a("satscapesimukraft:granulesCopper");
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_77617_a(int par1) {
        return this.icons[0];
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Copper granules";
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[0];
    }
}