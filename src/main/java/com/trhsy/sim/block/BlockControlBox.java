package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 控制箱
 * @Version: 1.0
 */
public class BlockControlBox extends Block {
    public BlockControlBox() {
        //controlBox
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(10.0F,1.0F));

    }
}
