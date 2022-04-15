package com.trhsy.sim.common.creativetab;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * 创造模式物品栏
 */
public class CreativeTabsFMLTutor extends CreativeTabs {
    public CreativeTabsFMLTutor() {
        //返回modid
        super(ModSim.MODID);
        //设置一下创造模式物品栏的背景
        //this.setBackgroundImageName("fmltutor.png");
    }

    /**
     * 创造模式物品栏上显示的物品
     * @return
     */
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
