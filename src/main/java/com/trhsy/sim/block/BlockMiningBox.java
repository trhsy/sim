package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMiningBox
 * @Description: 采矿箱
 * @date 2025/9/16 15:23
 */
public class BlockMiningBox extends Block {
    public BlockMiningBox() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(100F,3F));
        /**
         *  super(Material.WOOD);
         *         this.setSoundType(SoundType.WOOD);
         *         this.setHardness(2.0F);
         *         this.setResistance(1);
         *         this.setUnlocalizedName("miningBox");
         *         this.setCreativeTab(CreativeTabsLoader.tabSimU);
         */
    }
}
