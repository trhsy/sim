package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemGranulesCopper
 * @Description todo 金粒儿
 * @Author Tian
 * @Date 2022/4/1521:39
 **/
public class ItemGranulesGold extends Item {
    public ItemGranulesGold(){
        super();
        this.maxStackSize = 64;
        this.setUnlocalizedName("granulesGold");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

}
