package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @ClassName BlockCompositeBrick
 * @Description todo 复合砖
 * @Author Tian
 * @Date 2022/5/48:03
 **/
public class BlockCompositeBrick extends Block {
    public BlockCompositeBrick() {
        super(Material.rock);
        this.setStepSound(Block.soundTypeStone);
        this.setHardness(8.0F);
        this.setResistance(7.0F);
        this.setUnlocalizedName("compositeBrick");
        //this.setTextureName(ModSim.MODID + ":" + "composite_brick");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
