package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 流体牛奶块
 * @Version: 1.0
 */
public class BlockFlowingMilk extends Block {

    public BlockFlowingMilk() {
        super(Block.Properties.create(Material.WATER).sound(SoundType.WOOD).hardnessAndResistance(200F,3F));
        /*super(Material.WATER);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
