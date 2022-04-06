package com.trhsy.sim.common.block;

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import net.minecraft.block.BlockCarpet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

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
}
