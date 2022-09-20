package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemWindmillBase
 * @Description: 风车底座
 * @date 2022/9/20 0020 下午 1:52
 */
public class ItemWindmillBase extends ItemBase{
    public ItemWindmillBase() {
        super("windmillBase");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            //用船帆制造风车
            String windmill_base = I18n.format("container.sim.windmill_base");
            par3List.add(windmill_base);
            super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
