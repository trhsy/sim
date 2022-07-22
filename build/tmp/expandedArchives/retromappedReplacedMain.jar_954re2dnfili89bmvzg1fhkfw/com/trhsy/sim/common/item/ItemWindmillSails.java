package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

/**
 * 风车帆
 */
public class ItemWindmillSails extends Item {
    public ItemWindmillSails() {
        super();
        this.field_77777_bU = 64;
        this.func_77655_b("item_windmill_sails");
        this.func_77637_a(CreativeTabsLoader.tabSimU);

    }


    @Override
    @SideOnly(Side.CLIENT)
    public void func_150895_a(Item itemIn, CreativeTabs tab, List list) {
        try {
            for (int x = 0; x < 16; ++x) {
                list.add(new ItemStack(this, 1, x));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }
    public String getTexture(String name) {
        return "sim:" + name;
    }

    public String getTexture(String folder, String name) {
        return "sim:" + folder + "/" + name;
    }

    @Override
    public String func_77667_c(ItemStack is) {
        return this.func_77658_a() + is.func_77960_j();
    }

    @Override
    public int func_77647_b(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            String windmill = I18n.func_135052_a("container.sim.windmill");
            par3List.add(windmill);
            super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            ModSimReloaded.log.error("addInformation出错了：" + e.getMessage());
        }
    }
}
