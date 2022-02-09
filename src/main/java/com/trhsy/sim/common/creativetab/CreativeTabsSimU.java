package com.trhsy.sim.common.creativetab;

import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.block.BlockCheeseBlock;
import com.trhsy.sim.common.block.ItemBlockLightBox;
import com.trhsy.sim.common.item.ItemGranulesCopper;
import com.trhsy.sim.common.loader.ItemLoader;
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
        super(ModSim.MODID);
    }

    @Override
    public Item getTabIconItem() {
        return new ItemGranulesCopper();
    }
}
