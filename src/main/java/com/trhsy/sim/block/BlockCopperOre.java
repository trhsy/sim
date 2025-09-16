package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 铜矿
 * @Version: 1.0
 */
public class BlockCopperOre extends BlockOre {
    public BlockCopperOre() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 5.0F));
    }
}
