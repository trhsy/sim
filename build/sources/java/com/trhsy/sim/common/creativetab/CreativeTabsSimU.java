package com.trhsy.sim.common.creativetab;

import com.trhsy.sim.common.item.ItemGranulesCopper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @ClassName CreativeTabsSimU
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2920:54
 **/
public class CreativeTabsSimU extends CreativeTabs {
    public CreativeTabsSimU() {
        //返回modid
        super("sim_u");
    }

    @Override
    public Item getTabIconItem() {
        return new ItemGranulesCopper();
    }
}
