package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockLiving
 * @Description: 地毯，
 * @date 2025/9/16 15:16
 */
public class BlockLiving extends Block {
    public BlockLiving() {
        super(Block.Properties.create(Material.CARPET).hardnessAndResistance(2F,1F));
        /*
        * super(Material.CARPET);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLiving.WHITE));
        this.setTickRandomly(true);
//        this.setUnlocalizedName();
        this.setUnlocalizedName("livingBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
    }
}
