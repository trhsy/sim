package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemCopperIngot
 * @Description todo 铜锭
 * @Author Tian
 * @Date 2022/5/1622:21
 **/
public class ItemCopperIngot extends Item {

    public ItemCopperIngot(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("copperIngot");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
