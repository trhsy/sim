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

public class ItemWindmillVane extends Item {
    public ItemWindmillVane() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("windmillVane");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        //for (int x = 0; x < 16; ++x) {
        //    subItems.add(new ItemStack(itemIn, 1, x));
        //}
        getSubBlocks(itemIn, tab, subItems);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int x = 0; x < 16; x++) {
            list.add(new ItemStack(this, 1, x));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        return this.getUnlocalizedName()+ is.getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String windmill_sail = I18n.format("container.sim.windmill_sail");
        par3List.add(windmill_sail);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
