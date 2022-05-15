package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.ItemFood;

/**
 * @ClassName ItemCheese
 * @Description todo 奶酪
 * @Author Tian
 * @Date 2022/5/1417:20
 **/
public class ItemCheese extends ItemFood {
    public ItemCheese() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodCheese");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
