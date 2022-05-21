package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * 风车帆
 */
public class ItemWindmillSails extends Item {
    public ItemWindmillSails() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("windmillSails");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int x = 0; x < 16; ++x) {
            subItems.add(new ItemStack(this, 1, x));
        }
    }
    @Override
    public String getUnlocalizedName(ItemStack is) {
        return this.getUnlocalizedName() + is.getMetadata();
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String windmill = I18n.format("container.sim.windmill");
        par3List.add(windmill);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
}
