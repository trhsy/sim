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
        this.field_77777_bU = 64;
        this.func_77655_b("item_windmill_vane");
        this.func_77637_a(CreativeTabsLoader.tabSimU);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int x = 0; x < 16; ++x) {
            subItems.add(new ItemStack(this, 1, x));
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
        return this.func_77658_a()+ is.func_77960_j();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String windmill_sail = I18n.func_135052_a("container.sim.windmill_sail");
        par3List.add(windmill_sail);
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
    @Override
    public int func_77647_b(int par1) {
        return par1;
    }
}
