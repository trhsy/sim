package com.trhsy.sim.common.item.food;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.ItemFood;

/**
 * @ClassName ItemBurger
 * @Description todo 汉堡
 * @Author Tian
 * @Date 2022/5/1417:20
 **/
public class ItemBurger extends ItemFood {
    public ItemBurger() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodBurger");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
