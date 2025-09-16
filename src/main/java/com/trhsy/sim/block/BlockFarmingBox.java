package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 农田箱
 * @Version: 1.0
 */
public class BlockFarmingBox extends Block {
    public BlockFarmingBox() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2F,1F));
        /*
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setUnlocalizedName("farmingBox");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
