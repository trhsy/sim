package com.trhsy.sim.common.item;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
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
 * @ClassName ItemWindmillSails
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:05
 * ========================================
 **/
public class ItemWindmillSails extends Item {
    private IIcon[] icons;

    public ItemWindmillSails(int par1) {
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[16];

        for(int i = 0; i < 16; ++i) {
            this.icons[i] = iconRegister.registerIcon(ModSim.MODID + ":windmillsails" + i);
        }

    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta >= 0 && meta < 16 ? this.icons[meta] : this.icons[0];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        for(int x = 0; x < 16; ++x) {
            par3List.add(new ItemStack(this, 1, x));
        }

    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        return "item.windmillsails" + is.getMetadata();
    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        return "Windmill sails";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Craft a windmill base to make a windmill");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
