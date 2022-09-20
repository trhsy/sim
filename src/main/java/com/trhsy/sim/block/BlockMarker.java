package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @ClassName BlockMarker
 * @Description todo
 * @Author Tian
 * @Date 2022/9/1921:17
 **/
public class BlockMarker extends BlockBase{
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.4, 0.0D, 0.4, 0.6, 0.9, 0.6);
    public BlockMarker(Material material) {
        super(material,"markerBar");
        this.setStepSound(SoundType.WOOD);
        this.setHardness(2.0F);
        this.setResistance(1);
//        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setLightLevel(0.1F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CARPET_AABB;
    }
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
