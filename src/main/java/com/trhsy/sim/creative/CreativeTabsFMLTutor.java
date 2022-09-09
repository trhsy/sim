package com.trhsy.sim.creative;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
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
        Item item=null;
        try {
            item=Item.getItemFromBlock(BlockLoader.blockConstructorBox);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("创造模式物品栏上显示的物品出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
