package com.trhsy.sim.item.food;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.food
 * @ClassName: ItemFries
 * @Description: 薯条
 * @date 2022/9/20 0020 下午 3:40
 */
public class ItemFries extends ItemFood {
    public ItemFries() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodFries");
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
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}