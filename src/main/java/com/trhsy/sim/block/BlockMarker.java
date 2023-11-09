package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMarker
 * @Description: 标记棒
 * @date 2023/11/08 上午 10:34
 */
public class BlockMarker extends BlockBase{
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1, 0.625D);
    public BlockMarker() {
        super(Material.WOOD,"markerBar");
        this.setSoundType(SoundType.WOOD);
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

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.8125D, 0.6875D);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return (new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)).offset(pos);
    }
    /**
     * 用于在重建块以进行渲染时确定环境光遮挡和剔除
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

    /**
     * 方块被放置
     * @param world
     * @param pos
     * @param state
     * @param placer
     * @param stack
     */
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!world.isRemote) {
            //NetWorkLoader.net.sendTo(new PacketAddNewMarker(pos), (EntityPlayerMP)placer);
        }
    }
}
