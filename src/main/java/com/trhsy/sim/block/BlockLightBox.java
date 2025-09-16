package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 灯箱
 * @Version: 1.0
 */
public class BlockLightBox extends Block {
    public BlockLightBox(){
        super(Block.Properties.create(Material.WATER).sound(SoundType.WOOD).hardnessAndResistance(200F,3F));
        /*super(Material.WOOD,COLOR,EnumLightColour.class);
        this.setLightLevel(3F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setUnlocalizedName("lightBox");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumLightColour.WHITE));*/
    }
}
