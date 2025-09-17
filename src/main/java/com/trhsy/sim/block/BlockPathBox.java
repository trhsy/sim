package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockPathBox
 * @Description: 路径箱
 * @date 2025/9/17 14:31
 */
public class BlockPathBox extends Block {

    public BlockPathBox() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(10F,1F));
        /*super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setUnlocalizedName("pathBox");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
