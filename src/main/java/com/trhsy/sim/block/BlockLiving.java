package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockLiving
 * @Description: 地毯，
 * @date 2025/9/16 15:16
 */
public class BlockLiving extends Block {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private final EnumDyeColor color;
    public BlockLiving(EnumDyeColor color) {
        super(Block.Properties.create(Material.CARPET).hardnessAndResistance(2F,1F));
        /*
        * super(Material.CARPET);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLiving.WHITE));
        this.setTickRandomly(true);
//        this.setUnlocalizedName();
        this.setUnlocalizedName("livingBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);*/
        this.color = color;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }
}
