package com.trhsy.sim.common.creativetab;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @ClassName CreativeTabsSimU
 * @Description todo 创造模式物品栏
 * @Author Tian
 * @Date 2022/1/2920:54
 **/
public class CreativeTabsSimU extends CreativeTabs {
    public CreativeTabsSimU() {
        //返回modid
        super(ModSim.MODID);
        //设置一下创造模式物品栏的背景
        this.setBackgroundImageName("fmltutor.png");
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(BlockLoader.constructorBox);
    }

    /**
     * 用于设置是否有搜索框
     *
     * @return
     */
    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
