package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
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
 * @ClassName ItemWindmillBase
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:04
 * ========================================
 **/
public class ItemWindmillBase extends Item {
    private IIcon[] icons;

    public ItemWindmillBase(int par1) {
        this.maxStackSize = 64;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSimukraft.MODID + ":windmillbase");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icons[0];
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Windmill base";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Craft sails to make a windmill");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[0];
    }
}
