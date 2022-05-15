package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.ItemFood;

/**
 * @ClassName ItemFries
 * @Description todo 薯条
 * @Author Tian
 * @Date 2022/5/1417:21
 **/
public class ItemFries extends ItemFood {
    public ItemFries() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodFries");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
