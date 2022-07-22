package com.trhsy.sim.common.item;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * 风车底座
 */
public class ItemWindmillBase extends Item {
    public ItemWindmillBase() {
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("windmillBase");
        //this.setTextureName(ModSim.MODID + ":windmill_base");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            //用船帆制造风车
            String windmill_base = I18n.format("container.sim.windmill_base");
            par3List.add(windmill_base);
            super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }
}
