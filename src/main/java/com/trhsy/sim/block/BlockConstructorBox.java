package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: block
 * @Description: 建筑箱
 * @date 2025/9/3 22:54
 */
public class BlockConstructorBox extends Block {

    public BlockConstructorBox() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(0.5F,0.3F));
    }
}
