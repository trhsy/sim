package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;

/**
 * @ClassName ItemGranulesCopper
 * @Description todo 锡粒儿
 * @Author Tian
 * @Date 2022/4/1521:39
 **/
public class ItemGranulesTin extends Item {
    public ItemGranulesTin(){
        super();
        this.field_77777_bU = 64;
        this.func_77655_b("granulesTin");
        this.func_77637_a(CreativeTabsLoader.tabSimU);
    }

}
