package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemGranulesCopper
 * @Description todo 铁粒儿
 * @Author Tian
 * @Date 2022/4/1521:39
 **/
public class ItemGranulesIron extends Item {
    public ItemGranulesIron(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("granulesIron");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

}
