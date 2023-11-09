package com.trhsy.sim.block;

import com.trhsy.sim.block.enums.EnumBlock;
import com.trhsy.sim.block.enums.EnumBlockLiving;
import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockLiving
 * @Description:
 * @date 2023/11/07 下午 5:54
 */
public class BlockLiving extends EnumBlock<EnumBlockLiving> {
    public static final PropertyEnum<EnumBlockLiving> TYPE = PropertyEnum.create("type", EnumBlockLiving.class);
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    public BlockLiving() {
        super(Material.CARPET, TYPE, EnumBlockLiving.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockLiving.WHITE));
        this.setTickRandomly(true);
        this.setUnlocalizedName("livingBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CARPET_AABB;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state,IBlockAccess worldIn, BlockPos pos) {
        return (state.getValue(TYPE)).getMapColor();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Called when a neighboring block changes.
     */
    //@Override
    //public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    //    this.checkForDrop(worldIn, pos, state);
    //}

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? true : (blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side));
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumBlockLiving) state.getValue(TYPE)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks( CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < 16; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumBlockLiving.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(TYPE)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{TYPE});
    }
}

