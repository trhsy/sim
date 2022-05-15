package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.ItemFood;

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
}
