package com.trhsy.sim.item.granules;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * 铜粒儿
 */
public class ItemGranulesCopper extends Item {
    public ItemGranulesCopper(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("granulesCopper");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
