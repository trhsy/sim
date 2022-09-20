package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * 复合砖
 * @author Trhsy
 */
public class BlockCompositeBrick extends BlockBase {
    public BlockCompositeBrick(Material materialIn) {
        super(materialIn,"compositeBrick");
        this.setStepSound(SoundType.STONE);
        this.setHardness(8.0F);
        this.setResistance(7.0F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}