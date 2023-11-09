package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemWindmillBase
 * @Description: 风车底座
 * @date 2023/11/08 下午 1:53
 */
public class ItemWindmillBase extends ItemBase{
    public ItemWindmillBase() {
        super("windmillBase");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            //用船帆制造风车
            String windmill_base = I18n.format("container.sim.windmill_base");
            tooltip.add(windmill_base);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}