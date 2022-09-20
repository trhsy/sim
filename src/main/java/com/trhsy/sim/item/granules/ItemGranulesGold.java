package com.trhsy.sim.item.granules;

import com.trhsy.sim.item.ItemBase;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.granules
 * @ClassName: ItemGranulesGold
 * @Description: 金粒
 * @date 2022/9/20 0020 下午 1:38
 */
public class ItemGranulesGold extends ItemBase {
    public ItemGranulesGold(){
        super("granulesGold");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            //把金矿放到风车里，九个金粒儿可以合成金锭
            String windmill_base = I18n.format("container.sim.granules_gold");
            par3List.add(windmill_base);
            super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
