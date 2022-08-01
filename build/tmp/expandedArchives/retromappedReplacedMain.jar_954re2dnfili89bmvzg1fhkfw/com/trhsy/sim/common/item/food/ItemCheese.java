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
 * @ClassName ItemCheese
 * @Description todo 奶酪
 * @Author Tian
 * @Date 2022/5/1417:20
 **/
public class ItemCheese extends ItemFood {
    public ItemCheese() {
        super(6, 0.6F, false);
        this.func_77655_b("foodCheese");
        this.func_77627_a(true);
        this.func_77637_a(CreativeTabsLoader.tabSimU);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            String sim_folks = I18n.func_135052_a("container.sim.sim_folks");
            par3List.add(sim_folks);
            super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

}
