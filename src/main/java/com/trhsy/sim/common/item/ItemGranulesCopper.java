package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemGranulesCopper
 * @Description todo 铜粒儿
 * @Author Tian
 * @Date 2022/4/1521:39
 **/
public class ItemGranulesCopper extends Item {
    public ItemGranulesCopper(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("granulesCopper");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

}
