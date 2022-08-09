package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName ItemCheeseburger
 * @Description todo 奶酪汉堡
 * @Author Tian
 * @Date 2022/5/1417:21
 **/
public class ItemCheeseburger extends ItemFood {
    public ItemCheeseburger() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodCheeseburger");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            String sim_folks = I18n.format("container.sim.sim_folks");
            par3List.add(sim_folks);
            super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}
