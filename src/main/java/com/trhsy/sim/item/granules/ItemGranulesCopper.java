package com.trhsy.sim.item.granules;

import com.trhsy.sim.item.ItemBase;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.granules
 * @ClassName: ItemGranulesCopper
 * @Description: 铜粒儿
 * @date 2023/11/09 下午 3:31
 */
public class ItemGranulesCopper extends ItemBase {
    public ItemGranulesCopper(){
        super("granulesCopper");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            //把铜矿放到风车里，九个铜粒儿可以合成铜锭
            String windmill_base = new TextComponentTranslation("container.sim.granules_copper",new Object[0]).getUnformattedText();
            tooltip.add(windmill_base);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
