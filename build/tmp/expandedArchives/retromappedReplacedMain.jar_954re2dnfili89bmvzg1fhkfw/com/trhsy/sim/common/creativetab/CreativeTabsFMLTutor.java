package com.trhsy.sim.common.creativetab;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
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
    public Item func_78016_d() {
        Item item=null;
        try {
            item=Item.func_150898_a(BlockLoader.blockConstructorBox);
        } catch (Exception e) {
            ModSimReloaded.log.error("创造模式物品栏上显示的物品出错了：" + e.getMessage());
        }
        return item;
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
