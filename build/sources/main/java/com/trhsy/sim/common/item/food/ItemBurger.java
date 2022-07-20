package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName ItemBurger
 * @Description todo 汉堡
 * @Author Tian
 * @Date 2022/5/1417:20
 **/
public class ItemBurger extends ItemFood {
    public ItemBurger() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodBurger");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String sim_folks = I18n.format("container.sim.sim_folks");
        par3List.add(sim_folks);
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

}
