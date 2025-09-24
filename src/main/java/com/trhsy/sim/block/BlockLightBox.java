package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * @Author: TRHSY
 * @CreateTime: 2025-09-13
 * @Description: 灯箱
 * @Version: 1.0
 */
public class BlockLightBox extends Block {

    private final EnumDyeColor color;
    public BlockLightBox(EnumDyeColor color){
        super(Block.Properties.create(Material.WATER).sound(SoundType.WOOD).hardnessAndResistance(200F,3F).lightValue(15));
        /*super(Material.WOOD,COLOR,EnumLightColour.class);
        this.setLightLevel(3F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
        this.setUnlocalizedName("lightBox");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumLightColour.WHITE));*/
        this.color = color;
    }
    public EnumDyeColor getColor() {
        return this.color;
    }
}
