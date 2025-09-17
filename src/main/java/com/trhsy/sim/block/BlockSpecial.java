package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockSpecial
 * @Description: 特除空气方块
 * @date 2025/9/17 14:33
 */
public class BlockSpecial extends BlockAir {
    public BlockSpecial() {
        super(Block.Properties.create(Material.AIR).doesNotBlockMovement());
//        super(Material.air);
        //this.setRiseRate(5);
        /*this.setTickRandomly(true);
        this.disableStats();
        this.setHardness(0.0F);
        this.setUnlocalizedName("blockSpecial");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
