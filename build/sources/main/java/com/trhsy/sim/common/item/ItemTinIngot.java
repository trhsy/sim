package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemCopperIngot
 * @Description todo 锡锭
 * @Author Tian
 * @Date 2022/5/1622:21
 **/
public class ItemTinIngot extends Item {

    public ItemTinIngot(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("tinIngot");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
