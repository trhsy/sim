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
 * @ClassName: ItemGranulesGold
 * @Description: 金粒
 * @date 2023/11/09 下午 3:31
 */
public class ItemGranulesGold extends ItemBase {
    public ItemGranulesGold(){
        super("granulesGold");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            //把金矿放到风车里，九个金粒儿可以合成金锭
            String windmill_base = new TextComponentTranslation("container.sim.granules_gold",new Object[0]).getUnformattedText();
            tooltip.add(windmill_base);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
