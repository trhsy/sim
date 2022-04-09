package com.trhsy.sim.common.block;

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import net.minecraft.block.BlockCarpet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * @ClassName BlockLivingBlock
 * @Description todo 活动区域，地毯
 * @Author Tian
 * @Date 2022/4/413:50
 **/
public class BlockLivingBlock extends BlockCarpet {
    private IIcon[] icons;
    public BlockLivingBlock() {
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("livingBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < 8; ++i) {
            list.add(new ItemStack(item, 1, i));
        }

    }
}
